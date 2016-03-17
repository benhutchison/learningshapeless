package learnshapeless

object Data {

  def australia: Country = Australia
  def germany: Country = Germany

  sealed trait Country
  case object Germany extends Country
  case object Australia extends Country
  case object England extends Country


  sealed trait State
  case object Victoria extends State
  case object Queensland extends State

  sealed trait Discovery
  case object TheoryOfRelativity extends Discovery
  case object Calculus extends Discovery

  val scientistsFirstNames = Map(
    "Einstein" -> "Albert",
    "Newton" -> "Isaac"
  )

}
