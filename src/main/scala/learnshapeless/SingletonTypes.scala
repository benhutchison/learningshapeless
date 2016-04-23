package learnshapeless

import shapeless._
import syntax.singleton._

import scala.reflect.runtime.universe.TypeTag
import Helper._

object Ch05_SingletonTypes extends App {

  /** Singleton Types are types that describe a single value.
    *
    * What makes them useful is that, if you have the type, you
    * can recover the corresponding value using a Witness typeclass, since there is exactly one such value.
    *
    * You can think of them as allowing the lifting of Values into Types */


  def eg_42 = 42.narrow
  assertEquals("TypeTag[Int(42)]", typeToString(eg_42))

  def eg_42Int = 42
  assertEquals("TypeTag[Int]", typeToString(eg_42Int))

  def typeToString[T](t: T)(implicit tt: TypeTag[T]) = tt.toString

  def eg_42_isAnInt: Int = eg_42


  /** Singletons typing is available for the variety of literals in Scala  */
  val eg_42_Double = 42.0.narrow
  val eg_Symbol = 'symbol.narrow
  val eg_String = "string".narrow
  val eg_boolean = false.narrow

  /** note that these are ordinary values, just the inferred types have changed */
  assertEquals(false, eg_boolean)

  /** `narrow` is powered by a macro that resolves the singleton type, builds a Witness wrapping the operand, and returns the witness value.*/
  def forMoreInfoSee = new SingletonTypeMacros(???).mkSingletonOps(???)

  /** Witnesses */

  /** We can thread the value corresponding to a singleton type around via the Witness typeclass
    * A Witness is a type `T` (any type, not just singletons), and one value of that type (field `value`). */
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
  println(s"eg_hlst1(1): ${eg_hlst1(1)}")
  println(s" eg_hlst2(1): ${eg_hlst2(1)}")

}
object Helper {
  def assertHaveEqualTypes[A, B](a: A, b: B)(implicit ev: A =:= B): Unit = ()

}