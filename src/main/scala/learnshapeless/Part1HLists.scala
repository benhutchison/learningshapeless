package learnshapeless

import shapeless.{HList, Poly1, HNil}

object Part1HLists extends App {

  sealed trait Country
  case object Germany extends Country
  case object Australia extends Country
  case object England extends Country

  sealed trait Discovery
  case object TheoryOfRelativity extends Discovery
  case object Calculus extends Discovery


  /* Example 1: An HList consisting of "Einstein", the Int value 1879 and the Country Germany */
  def eg1_einstein = "Einstein" :: 1879 :: Germany :: HNil

  /* Exercise 1: An HList consisting of "Newton", the Int value 1642 and the Country England */
  def ex1_newton = ???

  /* Exercise 2: Replace the type ascription `Any` with the explicit type of `ex1_newton`*/
  def ex2: Any = ex1_newton

  import shapeless.syntax.std.tuple._
  def eg3_4_5: (String, String, Int, Country, Discovery) = ("Albert" +: eg1_einstein).tupled :+ (TheoryOfRelativity)

  /* Exercise 3: Prepend the String "Isaac" to `ex1` */
  def ex3 = ???

  /* Convert `ex1` into a tuple */
  def ex4: (String, Int, Country) = ???

  /* Using operations available via `import syntax.std.tuple._`, append `Calculus` to `ex4`  */
  def ex5 = ???

  def eg_6: HList = eg3_4_5.productElements

  /* convert ex5 into an HList */
  def ex6 = ???


  /* Example 2: Mapping over an HList. Each type `T` in the list should be handled with an `at[T]` expression.
  * A */
  trait DefaultIdentityMapping extends Poly1 {
    implicit def default[T] = at[T](x => x)
  }
  object ExamplePoly extends DefaultIdentityMapping {
    implicit def yearsSinceBirth = at[Int](2016 - _)
    implicit def isAustralian[C <: Country] = at[C](_ == Australia)
  }

  def eg2 = eg1_einstein.map(ExamplePoly)

  assertEquals("Einstein" :: 137 :: false :: HNil, eg2)



}
