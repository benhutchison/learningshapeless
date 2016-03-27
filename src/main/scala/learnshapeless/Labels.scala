package learnshapeless

import java.time.{Month, LocalDate}

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
object LabelsAndRecords extends App {

  def eg_labelledInt = "meaningOfLife" ->> 42

  /** Use test.showType() to get the type of above */
  def ex_typeOfLabelledInt: String = test.showType(eg_labelledInt)

  println(ex_typeOfLabelledInt)


  /** Symbols in scala are like string literals however:
    * - All symbols for the same string share the same representation instance (see also String.intern()). This saves
    * memory but also crucially enables fast equality checking between symbols based on pointer compare.
    *
    * Symbols that comply with rules for scala identifiers (eg no whitespace) can be constructed using literal syntax 'symbol
    * But any string can be represented as a symbol, using the Symbol("a string") factory
    *
    * */
  def eg_symbolLabelledInt = 'meaningOfLife ->> 42

  println(s"Type of eg_labelledInt: ${test.showType(eg_symbolLabelledInt)}")


  /** Special string indexed accessors are enabled when labelled values are collected in an HList */
  def eg_randomStuff =
    eg_symbolLabelledInt ::
      ('moonLanding  ->> LocalDate.of(1969, Month.JULY, 20)) ::
      ('piDigits     ->>  3.14159265359) ::
      HNil

  def eg_moonLanding: LocalDate = eg_randomStuff('moonLanding)

  def ex_pi: Double = eg_randomStuff('piDigits)


  /** Use the keys and values methods */

  def ex_randomStuffKeys = eg_randomStuff.keys

  def ex_randomStuffValues = eg_randomStuff.values

  println(s"keys: $ex_randomStuffKeys   values: $ex_randomStuffValues")

  /** Dont trust the NSA man! The moon landing was actually faked in a TV studio 2 weeks earlier.
    * Use +(Key-Value) to redefine the moon landing date to July 4 1969 in eg_randomStuff. */
  def ex_randomStuffForRealz = eg_randomStuff + ('moonLanding  ->> LocalDate.of(1969, Month.JULY, 4))

  /** Actually, there was no moon landing. Its a synthetic memory implanted when little green men escaped from Roswell
    * Remove the moon landing entry with -(Key) */
  def ex_reallyRandomStuff =  eg_randomStuff - 'moonLanding

  /** Ensure that its a compile error to access the now non-existent moon landing entry */
  illTyped("""ex_reallyRandomStuff('moonLanding)""")


  /** Lets move onto LabelledGeneric, a macro powered typeclass for conversion between cases classes and records */
  import Data._

  case class Scientist(name: String, yearBorn: Int, countryBorn: Country, discovery: Discovery)

  def eg_scientistGen = LabelledGeneric[Scientist]

  def eg_einstein = Scientist("Einstein", 1881, Germany, TheoryOfRelativity)

  /** use the `to` method on eg_scientistGen */
  def ex_genericEinstein = eg_scientistGen.to(eg_einstein)
  println(s"ex_genericEinstein: $ex_genericEinstein")

  /** Oops, that birth year is wrong. Fix it using +(KV) to update ex_genericEinstein with the correct value 1879*/
  def ex_fixError = ex_genericEinstein.updated('yearBorn, 1879)

  /** Use from the convert back from generic representation to case class */
  def ex_reconstructedEinstein = eg_scientistGen.from(ex_fixError)



}
