package learnshapeless

import shapeless._

import Data._
import shapeless.tag.@@

/** General Guidelines
  *
  * Definitons named `eg` are examples to learn from. Examples are numbered by the exercises they related to or inform.
  *
  * Exercises beginning with `ex` are for you to complete. Each exercise has a comment above describing the task.
  *
  * You can verify whether your exercise solution is correct by running the Part1HListTest in src/test/scala.
  *
  * Note: the file is runnable, so you can drop in println statements to look at the values of expressions
  *
  * */
object Coproducts extends App {

  type Name = String @@ NameTag
  trait NameTag
  def name(n: String) = tag[NameTag](n)

  type Born = Int @@ BornTag
  trait BornTag
  def born(yearAD: Int) = tag[BornTag](yearAD)


  def eg_florey = name("Florey") :: born(1898) :: australia :: HNil

  def eg_gauss = name("Gauss") :: born(1777) :: germany :: HNil

  def eg_einstein = name("Einstein") :: born(1879) :: germany :: HNil

  def eg_hahn = name("Hahn") :: born(1879) :: germany :: HNil

  def eg_all_scientists = Vector(eg_florey, eg_gauss, eg_einstein, eg_hahn)

  type Clue = Name :+: Born :+: Country :+: CNil

  def eg_clueCountry = Coproduct[Clue](australia)

  def eg_invalidClue = Coproduct[Clue](born(2020))

  /** create a clue about the identify of a famous scientist, being the name of `eg_florey` */
  def ex_nameClue = ???

  /** create a clue about the identify of a famous scientist, being the birth year `1879` */
  def ex_bornClue = ???

  def eg_allClues = Vector(eg_clueCountry, eg_invalidClue, ex_nameClue, ex_bornClue)

  /* Some basic Coproduct operations */
  def eg_select: Option[Country] = eg_clueCountry.select[Country]

  /* select the name of `countryClue` */
  def ex_selectName: Option[Name] = ???

  /* drop two elements of  `countryClue` */
  def ex_drop2: Country :+: CNil = ???



  /* An invalid clue is one that doesn't match any scientist.*/
  object eg_isValidClue extends Poly1 {
    implicit def nameIsEasy = at[Name](n => eg_all_scientists.exists(_.select[Name] == n))
    implicit def born = at[Born](b => eg_all_scientists.exists(_.select[Born] == b))
    implicit def country = at[Country](c => eg_all_scientists.exists(_.select[Country] == c))
  }

  /* Use `eg_isValidClue` to filter down to just invalid clues in `eg_allClues` */
  def eg_invalidClues = eg_allClues.map(_.map(eg_isValidClue))

  /* Write your own Poly1 that determines if aa clue is "good". "Good" clues uniquely identify a scientist,
   * whereas non-good clues are ambiguous. */
  def ex_isGoodClue = ???

  /* Use ex_isGoodClue to filter down `eg_allClues` to just the "good" ones */
  def ex_goodClues = ???

}
