package learnshapeless

import shapeless._
import shapeless.tag.@@
import shapeless.test.illTyped

/** Tagged types allow existing types to be tagged with extra discriminators, while remaining unboxed
  * instances of their original type as well. */
object TaggedTypes extends App {

  case class EgWeaklyTypedPerson(id: String, name: String, age: Int, numberChildren: Int)

  def eg_person = EgWeaklyTypedPerson("KAT513436", "Katherine", 56, 2)

  def eg_personOops = EgWeaklyTypedPerson("Katherine", "KAT513436", 2, 56)

  trait IdTag
  type Id = String @@ IdTag

  /** Tag the string "KAT513436" as an Id */
  def eg_id: Id = tag2("KAT513436").@@[IdTag]


  /** Note we are using a custom tagging method `tag2` rather than the one in Shapeless. This is because the
   `tag` operator provided by Shapeless doesn't work when the target def has an explicit type declared, ie: */
  illTyped("""def eg_id1: Id = tag2[IdTag]("KAT513436")""")

  //illTyped is a macro provided by Shapeless. It "inverts" compile errors for the code contained within it, ie:
  //If the expression would normally compile correctly , within illTyped it's a compile error
  //If the expression within illTyped would not compile, then illTyped(<expr>) will compile OK

  trait NameTag
  type Name = String @@ NameTag
  /** Tag the string "Katherine" as a Name*/
  def ex_name: Name = ???

  def eg_stillAString: String = ex_name

  /** Create 2 new type tags for Age and NumberChildren and tag2 `56` and `2` respectively */
  trait AgeTag
  type Age = Int @@ AgeTag
  def ex_age: Age = ???

  trait NumChildrenTag
  type NumChildren = Int @@ NumChildrenTag
  def ex_numberChildren: NumChildren = ???

  case class EgTypedPerson(id: Id, name: Name, age: Age, numChildren: NumChildren)

  def eg_WontCompileMissingTags = illTyped("""EgTypedPerson("KAT513436", "Katherine", 56, 2)""")

  def eg_ShouldntCompileWrongData = illTyped("""EgTypedPerson(ex_name, eg_id, ex_numberChildren, ex_age)""")

  /** Create a typed person instance correctly passing tagged values `eg_id`, `ex_name`, ` ex_age`, `ex_numberChildren` */
  def ex_TypedPerson = ???

}
