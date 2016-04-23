package learnshapeless

import shapeless.test.illTyped

object Ch11_TypeEquality extends App {

  /** A useful Shapeless utility is the illTyped("""<expression>""") macro, which fails at compile time
    * if `<expression>` compiles/type-checks, but succeeds otherwise.
    *
    * We'll use it several times in this workshop to show that expressions ought not compile,
    * without actually breaking compilation of the project (eg so tests can still run)*/

  illTyped("""val l: List[Int] = 42""")

  illTyped("""illTyped("val l: List[Int] = List(42)")""") //! note double negatives !


  /** This section introduces scala's Generalized Type Constraints typeclasses `=:=` and `<:<`.
    *
    * `=:=` is used for proving that 2 values have the same static type
    * `<:<` is used for proving that a value conforms to (ie is subtype of) a type
    *
    * These come from the standard library, not Shapeless
    */

  /** =:= Equals */

  def eg_IntIntAreEqual = implicitly[Int =:= Int]
  def eg_IntStringNotEqual = illTyped("""implicitly[Int =:= String]""")

  /** Exercise: extend this method to accept a =:= implicit parameter so that call to it wont compile unless the param types are equal.*/
  def ex_assertHaveEqualTypes[A, B](a: A, b: B): Unit = ???

  val eg_n1 = 5
  val eg_n2 = -42
  val eg_n3: Any = eg_n2

  /** Exercise: use the methods you created above on some test values.
    * Remeber you can use illTyped to wrap an expression that ought not to compile */
  def ex_assert_n1n2_equal = ???
  def ex_assert_n1n2_equal_wontCompile = ???


  /**  <:<  Conforms   */

  def eg_IntConformsToInt = implicitly[Int <:< Int]
  def eg_IntConformsToAny = implicitly[Int <:< Any]

  def eg_CovarainceFunctionOutput = implicitly[(Int => Int) <:< (Int => Any)]
  def eg_ContravarainceFunctionInput = implicitly[(Any => Int) <:< (Int => Int)]

  def ex_assertConformsToTypeOf[A, Super](value: A) = ???

  /** use cases examples from the standard library */

  def eg_optionOfOptionInt = Option(Option(42))
  /** Try out the standard libraru flatten method. Not how it uses a <:< parameter */
  def ex_flatten: Option[Int] = ???

  def eg_optionOfInt = Option(42)
  /* show that `eg_optionOfInt.flatten` wont compile using the `illTyped` macro */
  def ex_flatten2 = ???

  def eg_SomePairs = Vector((1, "one"),  (2, "two"))
  /** Exercise: use the toMap method to turn eg_SomePairs into Map. Note the role of <:< */
  def ex_toMap = ???

  def eg_SomeInts = Vector(1, 2)
  /* show that eg_SomeInts.toMap wont compile using the illTyped macro */
  def ex_toMap2 = ???

}
