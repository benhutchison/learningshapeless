package learnshapeless

import Data._
import shapeless._

case class PersonDetails(firstName: String, surname: String)
case class Asset(description: String, value: BigDecimal, categoryCode: String, verified: Boolean)

object Ch04_GenericRepresentation extends App {

  /** Generic provides a powerful set of macros that use compile-time reflection to convert between class hierachies
    * and a generic form based on HLists (aka "products") and Coproducts (aka "sums")
    * */


  /** Lets start by creating a relatively complex domain model for a load agreement */

  def eg_james = PersonDetails("James", "Wong")

  def eg_genPersonDetails = Generic[PersonDetails]

  def eg_genericJames: String :: String :: HNil = eg_genPersonDetails.to(eg_james)

  def eg_country: Country = Australia

  def eg_genCountry = Generic[Country]

  def eg_genericCountry:  Australia.type :+: England.type :+: Germany.type :+: CNil = eg_genCountry.to(eg_country)



  /** Product exercise:
   1. Assign a Generic typeclass for Asset to `ex_genAsset`.
   2. Use it to convert `eg_asset` to generic form. Specify its explicit type.
   3. Uncomment and complete the assertion by filling the expected `head` value **/
  def eg_asset = Asset("2012 Toyota Camry", BigDecimal(19500.0), "MOT2", false)

  def ex_genAsset = ???

  def ex_genericAsset = ???

  //assertEquals(???, ex_genericAsset.head)

  /** Coproduct exercise:
    * 1. Assign a Generic typeclass for State to `ex_genState`.
    * 2. Use it to convert `eg_state` to generic form. Specify its explicit type.
    * 3. Uncomment and complete the assertion by filling the expected `head` value*/
  def eg_state: State = Victoria

  def ex_genState = ???

  def ex_genericState  = ???

  //assertEquals(None, ex_genericState.head)


}
