package learnshapeless

import shapeless._

import Data._
import shapeless.tag.@@

/** Coproducts represent a single value that may be one of several possible types. They are "cousins" of HLists,
  * but with an OR operation applied between element types, rather than an AND. But unlike HLists, coproducts only
  * contain one value, but model uncertainty about what its exact type is.
  *
  * The Either[A, B] type in the Scala standard lib is analogous to a 2-type Coproduct.
  *
  * One key Coproducts use for in Shapeless to represent one concrete subtype of a closed inheritance hierarchy, where the possible
  * subtype options are enumerated in the coproduct type..
  *
  * */
object Coproducts extends App {

  type Name = String @@ NameTag
  trait NameTag
  def name(n: String) = tag2(n).@@[NameTag]

  type Born = Int @@ BornTag
  trait BornTag
  def born(yearAD: Int) = tag2(yearAD).@@[BornTag]


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

  def eg_allClues: Vector[Clue] = Vector(eg_clueCountry, eg_invalidClue, ex_nameClue, ex_bornClue)

  /* Some basic Coproduct operations */
  def eg_select: Option[Country] = eg_clueCountry.select[Country]

  /* select the name of `eg_clueCountry` */
  def ex_selectName: Option[Name] = ???

  /* drop two elements of  `eg_clueCountry` */
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
