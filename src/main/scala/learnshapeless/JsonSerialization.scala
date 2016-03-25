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

import shapeless._
import shapeless.labelled.FieldType
import spray.json.{JsonFormat, JsValue}

object JsonSerialization extends App {

  /** JsonFormat[T] is a serializer/deserializer for T, providing:
    * read(json: JsValue): T
    * write(t: T): JsValue
    *
    * The idea will be to define a JsonFormat for any HList and Coproduct.
    *
    * Since any case class can be represented by HLists and Coproducts, by recursive transforming a data graph, we can
    * serialize an abitrary strucutre into JSON */

  implicit val ex_formatHNil: JsonFormat[HNil] = ???

  implicit def ex_formatHList[Key <: Symbol, Value, Tail <: HList](
    implicit
    key: Witness.Aux[Key],
    formatValue: JsonFormat[Value],
    formatTail: JsonFormat[Tail]
  ): JsonFormat[FieldType[Key, Value] :: Tail] = new JsonFormat[FieldType[Key, Value] :: Tail] {

    def write(hlist: FieldType[Key, Value] :: Tail): JsValue = ???

    def read(json: JsValue): FieldType[Key, Value] :: Tail = {
      ???
    }


  }

}
