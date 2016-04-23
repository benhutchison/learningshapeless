package learnshapeless

import shapeless._

object WhatIsAux extends App {

  /** Aux is a recurring design pattern in Shapeless code takes quite a while to get comfortable with.
    *
    * It works around current limitations in the compiler; there's hope that one day it will become obselete, more info
    * [https://github.com/scala/scala/pull/5108]
    *
    * This section aims to give you some understanding of the role its playing */

  /** First, observe that MyType.Aux is just a type alias for MyType, with an internal type member converted to a type parameter. */

  val eg_genericAux: Generic.Aux[PersonDetails, String :: String :: HNil] = Generic[PersonDetails]

  /** This is a lossy type ascription */
  val eg_generic: Generic[PersonDetails] = eg_genericAux

  /** Writing the type as a "refinement" instead of using Aux*/
  val eg_generic2: Generic[PersonDetails]{type Repr = String :: String :: HNil} = eg_genericAux

  /** The refinement is isomorphic to the Generic.Aux form*/
  val eg_genericAux2: Generic.Aux[PersonDetails, String :: String :: HNil] = eg_generic2

  /** The type refinement is essential; if lost there's no going back.
    *
    * Uncomment this line and compile. Does the error make sense? */
  //val eg_genericAux2: Generic.Aux[PersonDetails, String :: String :: HNil] = eg_generic



  def eg_people = Seq(PersonDetails("Zane", "Zoolander"), PersonDetails("James", "Wong"))


  /** Converts each element to generic form then returns the head */
  def headGen[T](ts: Seq[T])(implicit gen: Generic[T]): Option[gen.Repr] = ts.map(gen.to(_)).headOption


  /** Apply `headGen` to `eg_people` */
  def ex_head = ???
  println(s"ex_head: $ex_head")


  /** Enables Ordering over an HList if the element types have an Ordering
    * Copied from Shapeless examples.*/
  import GenericOrdering._

  /** Converts each element to generic form then returns the minimum according to the specified Ordering */
  //def leastGen[T](ts: Seq[T])(implicit gen: Generic[T], o: Ordering[gen.Repr]): gen.Repr = ts.map(gen.to(_)).min

  /** Exercise: Uncomment the above line. What happens? Can you use the info about Aux above to fix it?*/

  /** When you get `leastGen` to compile, apply it to `eg_people` */
  def ex_least = ???
  println(s"ex_least: $ex_least")

}

/** Helper code to provide HList Ordering. No further exercises here */
trait LowPriorityGenericOrdering {
  // An Ordering for any type which is isomorphic to an HList, if that HList has an Ordering

  implicit def hlistIsoOrdering[A, H <: HList](implicit gen : Generic.Aux[A, H], oh : Ordering[H]) : Ordering[A] = new Ordering[A] {
    def compare(a1 : A, a2 : A) = oh.compare(gen to a1, gen to a2)
  }
}

object GenericOrdering extends LowPriorityGenericOrdering {
  implicit def hnilOrdering : Ordering[HNil] = new Ordering[HNil] {
    def compare(a : HNil, b : HNil) = 0
  }

  implicit def hlistOrdering[H, T <: HList](implicit oh : Ordering[H], ot : Ordering[T]) : Ordering[H :: T] = new Ordering[H :: T] {
    def compare(a : H :: T, b : H :: T) = {
      val i = oh.compare(a.head, b.head)
      if (i == 0) ot.compare(a.tail, b.tail)
      else i
    }
  }
}