package learnshapeless

import java.time.LocalDate

import learnshapeless.Data.Country
import shapeless._

object GenericRepresentation extends App {

  /** Generic provides a powerful set of macros that use compile-time reflection to convert between class hierachies
    * and a generic form based on HLists and Coproducts
    * */


  /** Lets start by creating a relatively complex domain model for a load agreement */

  def eg_james = PersonDetails("James", "Wong")
  def eg_office = AddressDetails("11", "High St", "Ivanhoe", Victoria, "3079")
  def eg_role = "Sales Manager"

  def ex_AuthorizedOfficer: AuthorizedOfficer = ???

  def eg_asset = Asset("2012 Toyota Camry", BigDecimal(19500.0), "MOT2", false)
  def eg_identity = CompanyCertificate("838562067")
  def eg_borrowerReprentative = PersonDetails("Soula", "Katsopoulis")
  def eg_address = AddressDetails("1/130", "Low Road", "Errinundra", Victoria, "3079")

  def ex_legal_entity: LegalEntity = ???

  def eg_product = MotorVehicleLoan(BigDecimal("16800.00"), 60)

  def ex_loanDetails: LoanDetails = ???

  def ex_contract: Contract = ???

  /** Use Generic[T] to build a typeclass to operate over the contract */
  def ex_genericContract = Generic[Contract]

  def eg_repr = ex_genericContract.to(ex_contract)

}
case class Contract(borrower: LegalEntity, loadDetails: LoanDetails)

case class LoanDetails(product: Product, officer: AuthorizedOfficer, asset: Asset)

case class AuthorizedOfficer(person: PersonDetails, role: String, officeAddress: AddressDetails)

case class PersonDetails(firstName: String, surname: String)

case class AddressDetails(number: String, street: String, suburb: String, state: State, postcode: String)

sealed trait IdentityDocument
case class Passport(number: String, dateOfIssue: LocalDate, countryOfIssue: Country) extends IdentityDocument
case class DriversLicense(number: String, expires: LocalDate, state: State) extends IdentityDocument
case class CompanyCertificate(abn: String) extends IdentityDocument

sealed trait State
case object Victoria extends State
case object Queensland extends State

sealed trait Product
case class PersonalLoan(amount: BigDecimal, rate: BigDecimal)
case class MotorVehicleLoan(amount: BigDecimal, termMonths: Int)

case class Asset(description: String, value: BigDecimal, categoryCode: String, verified: Boolean)

sealed trait LegalEntity
case class Person(details: PersonDetails, identityDocument: IdentityDocument, address: AddressDetails) extends LegalEntity
case class Company(representative: PersonDetails, identityDocument: IdentityDocument, address: AddressDetails) extends LegalEntity
