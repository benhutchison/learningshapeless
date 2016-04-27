package learnshapeless

import shapeless._
import shapeless.test.illTyped
import syntax.singleton._
import record._


/**
  * The Generic module shows how case classes data can be decomposed into a generic form based on
  * products (ie HLists) and sums (ie Coproducts). Such a decomposition is lossy however, in that the names of the fields
  * in the case class are lost - just the data and the ordering are retained.
  *
  * Labels and Records are the obvious next step: the generic form consisting of HLists/Coproducts of labelled fields,
  * where the label is a singleton String or Symbol. This enables lossless 2-way transform of case classes into a generic form,
  * or a wire format like json.
  */
object Ch07_LabelsAndRecords extends App {

  def eg_labelledInt = "birthYear" ->> 1879

  /** Use test.showType() to get the type of above */
  def ex_typeOfLabelledInt: String = ???

  println(ex_typeOfLabelledInt)


  /** Symbols in scala are like string literals however:
    * - All symbols for the same string share the same representation instance (see also String.intern()). This saves
    * memory but also crucially enables fast equality checking between symbols based on pointer compare.
    *
    * Symbols that comply with rules for scala identifiers (eg no whitespace) can be constructed using literal syntax 'symbol
    * But any string can be represented as a symbol, using the Symbol("a string") factory
    *
    * */
  def eg_symbolLabelledInt = 'birthYear ->> 42

  println(s"Type of eg_labelledInt: ${test.showType(eg_symbolLabelledInt)}")


  /** Special type-indexed accessors are enabled when labelled values are collected in an HList */
  def eg_randomStuff =
    eg_labelledInt ::
      ('birthYear  ->> 1542) ::
      ('name     ->>  "Newton") ::
      HNil

  def eg_birthYear: Int = eg_randomStuff('birthYear)

  def ex_name: String = ???


  /** Use the keys and values methods */

  def ex_randomStuffKeys = ???

  def ex_randomStuffValues = ???

  println(s"keys: $ex_randomStuffKeys   values: $ex_randomStuffValues")

    /** Use +(Key-Value) to correct the birthYear to 1642 in eg_randomStuff. */
  def ex_fixBirthYear = ???

  /** Actually, birthYear is no longer relevant
    * Remove the birthYear entry with -(Key) */
  def ex_noBirthYear = ???

  /** Ensure that its a compile error to access the now non-existent birthYear entry */
  illTyped("""ex_noBirthYear.<your code here>""")


  /** Lets move onto LabelledGeneric, a macro powered typeclass for conversion between cases classes and records */
  import Data._

  case class Scientist(name: String, yearBorn: Int, countryBorn: Country, discovery: Discovery)

  def eg_scientistGen = LabelledGeneric[Scientist]

  def eg_einstein = Scientist("Einstein", 1881, Germany, TheoryOfRelativity)

  /** use the `to` method on eg_scientistGen */
  def ex_genericEinstein = ???
  println(s"ex_genericEinstein: $ex_genericEinstein")

  /** Oops, that birth year is wrong too. Fix it using +(KV) to update ex_genericEinstein with the correct value 1879.
    *
    * Note Shapeless uses Symbols, not Strings, to label case class fields in LabelledGeneric form. */
  def ex_fixError = ???

  /** Use `from` method to convert `ex_fixError` to back to a case class */
  def ex_reconstructedEinstein = ???


}
