package learnshapeless

import learnshapeless.Data._
import shapeless._

class TaggedTypesTest extends org.specs2.mutable.Specification with Assert {

  import TaggedTypes._

  eg {mustEqual(ex_name, shapeless.tag[NameTag]("Katherine"))}

  eg {mustEqual(ex_name, shapeless.tag[NameTag]("Katherine"))}

}
