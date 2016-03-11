package learnshapeless

import java.util.Date

import learnshapeless.Data._
import shapeless._
import shapeless.test.illTyped
import syntax.singleton._

import scala.reflect.runtime.universe.TypeTag
import Helper._

/** General Guidelines
  *
  * Definitons named `eg` are examples to learn from. Examples are numbered by the exercises they related to or inform.
  *
  * Exercises beginning with `ex` are for you to complete. Each exercise has a comment above describing the task.
  *
  * You can verify whether your exercise solution is correct by running the associated test in src/test/scala.
  *
  * Note: the file is runnable, so you can drop in println statements to look at the values of expressions
  *
  * */

object SingletonTypes extends App {

  /** Singleton Types are types that describe a single value.
    *
    * What makes them useful is that, if you have the type, you
    * can reconstruct the corresponding value, since there is exactly one such value.
    *
    * You can think of them as allowing the lifting of Values into Types */

  def typeToString[T](t: T)(implicit tt: TypeTag[T]) = tt.toString

  def eg_42 = 42.narrow
  assertEquals("TypeTag[Int(42)]", typeToString(eg_42))

  def eg_42Int = 42
  assertEquals("TypeTag[Int]", typeToString(eg_42Int))

  def eg_42_isAnInt: Int = eg_42

  val eg_Witness42 = Witness(42)
  def eg_42_again = eg_Witness42.value
  assertHaveEqualTypes(eg_42, eg_42_again)

  type eg_Int42 = eg_Witness42.T

  assertHaveEqualTypes(eg_42, eg_42_again)

  assertHaveNonEqualTypes(eg_42, eg_42_isAnInt)

  //note that eg_Witness42 is NOT available in implicit scope, ie its not an implicit val
  def eg_summonValueFromExactSingletonType = implicitly[Witness.Aux[eg_Witness42.T]].value
  assertEquals(42, eg_summonValueFromExactSingletonType)

  /** Singletons typing is available for the variety of literals in Scala */
  val eg_42_Double = 42.0.narrow
  val eg_Symbol = 'symbol.narrow
  val eg_String = "string".narrow
  val eg_boolean = false.narrow

  /** and regular instances of classes too */
  case class Foo(name: String)
  val myFoo = new Foo("mine")
  assertEquals("TypeTag[learnshapeless.SingletonTypes.Foo]", typeToString(myFoo))
  assertEquals("TypeTag[learnshapeless.SingletonTypes.myFoo.type]", typeToString(myFoo.narrow))

  /** note that these are ordinary values, just the inferred types have changed */
  assertEquals(false, eg_boolean)

  /** `narrow` is powered by a macro that resolves the singleton type, builds a Witness wrapping the operand, and returns the witness value.*/
  def forMoreInfoSee = new SingletonTypeMacros(???).mkSingletonOps(???)

  /** Witnesses */

  /** We can't write a function that passes a singleton Type and summons its Value
    * Have a go!? */
  illTyped("""def valueOf[T <: Singleton] = implicitly[Witness.Aux[T]].value""")

  /** But instead we can thread the corresponding value around via the Witness typeclass
    *
    * A Witness is simply a type, any type, not just singletons, and one value of that type. */
  implicit val w42 = 42.witness

  def valueOf[T: Witness.Aux] = implicitly[Witness.Aux[T]].value

  assertEquals(42, valueOf[w42.T])


  /** Use cases for singletons */

  val eg_hlst1 = "Ben" :: 42 :: HNil
  val eg_hlst2 = List() :: false :: HNil

  /** Consider a function to get the nth element of a HList. What should its return type be?
    *
    * It needs to be "computed" based on n and the lists type. To do that type level computation we need the type of n.
    * */
  assertHaveNonEqualTypes(eg_hlst1(1), eg_hlst2(1))


}
object Helper {
  def assertHaveEqualTypes[A, B](a: A, b: B)(implicit ev: A =:= B): Unit = ()
  def assertHaveNonEqualTypes[A, B](a: A, b: B): Unit = illTyped("""assertHaveEqualTypes(a, b)""")

}