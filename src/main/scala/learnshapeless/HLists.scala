package learnshapeless

import shapeless._

import learnshapeless.Data._

/** HLists are linked-list data structures that include type-information about all their elements. The H stands for
  * "heterogeneous", because the elements dont share a single "homogeneous" type. HLists are like tuples in that all
  * elements contribute to the overall type, but like lists in that they can grow, shrink, concat, fold etc.
  *
  * As HLists are constructed from typed values, a corresponding linked-list of types is constructed at the type-level.
  *
  * Shapeless provides many of the typical operations of a collections API over HLists, including take, drop, map, fold
  * and indexing. To map over an HList containing several element types, Shapeless introduces polymorphic functions (eg `Poly1`)
  * which, unlike regular mono-morphic functions, are defined for multiple input/output types.
  *
  * To perform transformations over HLists while preserving type information about all the elements requires type level
  * computation. This shows up in the signatures of shapeless library methods as implicit parameters that determine the output type.
  *
  * */

object HLists extends App {

  /* Example 1: An HList consisting of "Einstein", the Int value 1879 and the Country Germany */
  def eg1_einstein: String :: Int :: Country :: HNil = "Einstein" :: 1879 :: Germany :: HNil

  /** Exercise 1: Construct an HList consisting of "Newton", the Int value 1642 and the Country England.

    Exercise 2: Replace the type ascription `Any` with the explicit type. This isn't typically necessary in application code,
    inference is preferred, but is a good learning exercise.

    Some options to see the type of `ex_newton` printed out:
    (a) start the `sbt console`, and enter `:type learnshapeless.HLists.ex_newton`
    (b) if you are using Intellij, you can focus cursor on the expression and activate TypeInfo command (Ctrl-Shift-P on my Mac)
     */
  def ex_newton = ???
  println(s"ex_newton: $ex_newton")

  def eg_prependAndAppend: String :: String :: Int :: Country :: Discovery :: HNil = "Albert" +: eg1_einstein :+ TheoryOfRelativity

  /* Exercise : Prepend the String "Isaac" to `ex_newton` and append `Calculus` */
  def ex_prependAndAppend = ???
  println(s"ex_prependAndAppend $ex_prependAndAppend")


  /** Shapeless makes the link between tuples and HLists clear by offering all HList methods over tuples with the import below*/
  import shapeless.syntax.std.tuple._

  def eg_einstein_tuple: (String, Int, Country) = eg1_einstein.tupled

  /* Convert `ex_newton` into a tuple */
  def ex_tuple: (String, Int, Country) = ???
  println(s"ex_tuple $ex_tuple")

  /* Using operations available via `import syntax.std.tuple._`, append `Calculus` to `ex_tuple`  */
  def ex_tuple_append = ???
  println(s"ex_tuple_append $ex_tuple_append")

  def eg_from_tuple = ("Einstein", 1879, Germany).productElements

  /* convert ex_tuple_append into an HList */
  def ex_from_tuple = ???
  println(s"ex_from_tuple $ex_from_tuple")


  /* Example: Mapping over an HList. Each type `T` in the list should be handled with an `at[T]` expression.
  * A */
  trait DefaultIdentityMapping extends Poly1 {
    implicit def default[T] = at[T](x => x)
  }
  object ExamplePoly extends DefaultIdentityMapping {
    implicit def yearsSinceBirth = at[Int](2016 - _)
    implicit def isAustralian[C <: Country] = at[C](_ == Australia)
  }

  def eg_poly = eg1_einstein.map(ExamplePoly)

  /* Apply a Poly1 mapping over `ex_newton` that converts the name to ALLCAPS, and leaves other fields unchanged
   * Return resulting HList */
  def ex_poly = ???
  println(s"ex_poly: $ex_poly")



  /* Note how exact type of element 2 is returned */
  def eg_index: Country = eg1_einstein(2)

  /* Exercise: what happen if you try to index an HList outside it bounds? Try accessing the 4th element of `eg1_einstein` */
  def ex_indexOutOfBounds = ???


  def eg_mapped_by_index = eg1_einstein.updateAtWith(2)(_ == Australia)

  /* Transform the surname field in `ex_newton` to the first name by looking it up in `Data.scientistsFirstNames`.
  Because `Data.scientistsFirstNames` is a `Map`, it can be passed as a function K => V
  * Use updateAtWith to identify the field by numeric index rather than y type.
  * `updateAtWith` returns the old value, and the updated HList in a tuple */
  def ex_to_firstname_by_index: (String, HList) = ???
  println(s"ex_to_firstname_by_index $ex_to_firstname_by_index")

}
