package learnshapeless

/** In this final module we put the LabelledGeneric machinery to work applied to JSON serialization.
  *
  * We'll use the Spray representation of JSON data, based on Fommil's earlier Shapeless workshop
  * [https://github.com/fommil/shapeless-for-mortals] but the technique works equally well with Argonaut, Play etc.
  *
  * This is a learning exercise, but there are existing production grade implementations:
  *   https://github.com/fommil/spray-json-shapeless
  *   https://github.com/alexarchambault/argonaut-shapeless
  *   https://github.com/mandubian/shapelaysson
  *
  * Which serves witness to the genericness of the Shapeless transformation
  */

import Data._

import shapeless._
import labelled._
import syntax.singleton._

import spray.json._

object Ch10_JsonSerialization extends App {

  implicit val s: JsonFormat[String] = new JsonFormat[String] {
    def write(s: String): JsValue = new JsString(s)
    def read(json: JsValue) = json match {
      case JsString(s) => s
      case other => throw new RuntimeException(s"Expecting string, found $other")
    }
  }
  implicit val i: JsonFormat[Int] = new JsonFormat[Int] {
    def write(n: Int): JsValue = new JsNumber(n)
    def read(json: JsValue) = json match {
      case JsNumber(n) => n.toInt
      case other => throw new RuntimeException(s"Expecting Int, found $other")
    }
  }

  /** JsonFormat[T] is a serializer/deserializer for T, providing:
    * read(json: JsValue): T
    * write(t: T): JsValue
    *
    * The idea will be to define a JsonFormat for any HList and Coproduct.
    *
    * Since any case class can be represented by HLists and Coproducts, by recursive transforming a data graph, we can
    * serialize an abitrary strucutre into JSON */

  implicit val ex_formatHNil: JsonFormat[HNil] = new JsonFormat[HNil] {

    def write(obj: HNil): JsValue = JsObject.empty

    def read(json: JsValue): HNil = HNil
  }

  /** Exercise: finish this JsonFormat for an HList, defined in terms of an implicit Key (ie label) witness, a JsonFormat
    * for the head value, and a JsonFormat for the tail of the list.*/
  implicit def ex_formatHList[Key <: Symbol, Value, Tail <: HList](
    implicit
    key: Witness.Aux[Key],
    formatValue: Lazy[JsonFormat[Value]],
    formatTail: Lazy[JsonFormat[Tail]]
  ): JsonFormat[FieldType[Key, Value] :: Tail] = new JsonFormat[FieldType[Key, Value] :: Tail] {

    def write(hlist: FieldType[Key, Value] :: Tail): JsValue = {
      val tailFields = formatTail.value.write(hlist.tail).asJsObject.fields
      val headField = key.value.name -> formatValue.value.write(hlist.head)
      JsObject(tailFields + headField)
    }

    def read(json: JsValue): FieldType[Key, Value] :: Tail = {
      val fields = json.asJsObject.fields
      val head = formatValue.value.read(fields(key.value.name))
      val tail = formatTail.value.read(json)
      field[Key](head) :: tail
    }

  }

  /** Check it works */

  def eg_einsteinHlist = ('name ->> "Einstein") :: ('born ->> 1879)  :: HNil

  eg_einsteinHlist.toJson


  /** Next, we'll integrate the HList serializer with LabelledGeneric, to build a serializer over case-classes. (Well,
    * a subset of case-classes until we support Coproducts below since class hierarchies wont be supported.)
    *
    * The idea will be to build a JsonFormat[T], based on an implicit LabelledGeneric[T] and JsonFormat[Repr],
    * where the output type `Repr` of LabelledGeneric guides the selection of the JsonFormat implicit.
    * */
  implicit def familyFormat[T, Repr](
    implicit
    gen: LabelledGeneric.Aux[T, Repr],
    formatGen: Lazy[JsonFormat[Repr]]
  ): JsonFormat[T] = new JsonFormat[T] {

    def read(json: JsValue): T = gen.from(formatGen.value.read(json))

    def write(t: T): JsValue = formatGen.value.write(gen.to(t))

  }

  /** This should compile and print json */
  case class ScientistShort(name: String, born: Int)
  val eg_einsteinShort = ScientistShort("Einstein", 1879)
  println(eg_einsteinShort.toJson)

  /** Since class hierarchies are represented as Coproducts we next need to define a JsonFormat for them
    *
    * Start with CNil, the empty coproduct.
    *
    * Strangely, this implicit is needed to complete the compile-time recursion, but will never actually
    * be used at runtime, since once of the other elements of the coproduct will always have matched first.
    * Therefore, the implementations can remain ???
    * */
  implicit val eg_formatCNil = new JsonFormat[CNil] {
      def read(json: JsValue): CNil = ???
      def write(t: CNil): JsValue = ???
  }

  /** Like HLists, we need a recursively defined format that tests each labelled head value in turn,
    * or else delegates to the format for the tail.
    *
    * But there's a difference. Coproducts model a value that may be one of several possible types, so there's uncertainty
    * in the static type. To serialize such a value, we'll need to include a special discriminator field into the json
    * indicating which of the potential options it actually is. To deserialize it, we'll need to read the discriminator
    * value, and test whether it is the same as the current Head we are processing.
    *
    * I follow fommils precedent of name the discriminator field "type". Since its a reserved word in Scala,
    * collision chances are reduced. */

  implicit def ex_formatCoproduct[Key <: Symbol, Value, Tail <: Coproduct](
    implicit
    key: Witness.Aux[Key],
    formatValue: Lazy[JsonFormat[Value]],
    formatTail: Lazy[JsonFormat[Tail]]
  ): JsonFormat[FieldType[Key, Value] :+: Tail] = new JsonFormat[FieldType[Key, Value] :+: Tail] {

    def write(coproduct: FieldType[Key, Value] :+: Tail): JsValue = coproduct match {
      //Inr() node in a Coproduct hold the actual value of the Coproduct
      //You need to write out a type descriptor field as well as the json representing the coproduct value
      //The type descriptor field should be called "type" and contain the name of `key`, the label of the current field.
      case Inl(found) =>
        val JsObject(fields) = formatValue.value.write(found).asJsObject
        JsObject(fields + ("type" -> JsString(key.value.name)))

      //Inr(tail) nodes represent the empty case, a type which the runtime value *isnt*. Continue onto the tail and write it out
      case Inr(tail) =>
        formatTail.value.write(tail)
    }

    def read(json: JsValue): FieldType[Key, Value] :+: Tail = {
      if (json.asJsObject.fields("type") == JsString(key.value.name )) //test if discriminator field matches current name
        //match - deserialize and return the value
        Inl(field[Key](formatValue.value.read(json)))
      else
        //test the next coproduct option
        Inr(formatTail.value.read(json))
    }

  }

  /** Test serialisation & deserialisation including class hierarchies */
  case class Scientist(name: String, born: Int, country: Country)
  val eg_einstein2 = Scientist("Einstein", 1879, Germany)

  implicit val genScientist = LabelledGeneric[Scientist]

  println(eg_einstein2.toJson)

  //test round trip
  assertEquals(eg_einstein2, eg_einstein2.toJson.convertTo[Scientist])

}
