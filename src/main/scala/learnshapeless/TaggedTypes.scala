package learnshapeless

import shapeless._
import shapeless.tag.@@
import shapeless.test.illTyped

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
object TaggedTypes {

  case class EgWeaklyTypedPerson(id: String, name: String, age: Int, numberChildren: Int)

  def eg_person = EgWeaklyTypedPerson("KAT513436", "Katherine", 56, 2)

  def eg_personOops = EgWeaklyTypedPerson("Katherine", "KAT513436", 2, 56)

  trait IdTag
  type Id = String @@ IdTag

  def eg_id = tag[IdTag]("KAT513436")

  trait NameTag
  type Name = String @@ NameTag
  /** Tag the string "Katherine" as a Name*/
  def ex_name: Name = ???

  def eg_stillAString: String = ex_name

  /** Create 2 new type tags for Age and NumberChildren and tag `56` and `2` respectively */
  type Age = Nothing
  def ex_age = ???

  type NumChildren = Nothing
  def ex_numberChildren = ???

  case class EgTypedPerson(id: Id, name: Name, age: Age, numChildren: NumChildren)

  def eg_WontCompileMissingTags = illTyped("""EgTypedPerson("KAT513436", "Katherine", 56, 2)""")

  def eg_ShouldntCompileWrongData = illTyped("""EgTypedPerson(ex_name, eg_id, ex_numberChildren, ex_age)""")

  /** Create a typed person instance correctly passing tagged values `eg_id`, `ex_name`, ` ex_age`, `ex_numberChildren` */
  def ex_TypedPerson = ???

}
