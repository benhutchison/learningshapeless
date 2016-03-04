package learnshapeless

import learnshapeless.Data._
import shapeless._

class TaggedTypesTest extends org.specs2.mutable.Specification with Assert {

  import TaggedTypes._

  eg {mustEqual(ex_name, tag2("Katherine").@@[NameTag])}

  eg {mustEqual(ex_age, tag2(56).@@[AgeTag])}

  eg {mustEqual(ex_numberChildren, tag2(2).@@[NumChildrenTag])}

  eg {mustEqual(ex_TypedPerson, EgTypedPerson(eg_id, ex_name, ex_age, ex_numberChildren))}

}
