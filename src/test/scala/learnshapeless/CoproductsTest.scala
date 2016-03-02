package learnshapeless

import learnshapeless.Data._
import shapeless._

class CoproductsTest extends org.specs2.mutable.Specification with Assert {

  import Coproducts._

  eg {mustEqual(ex_nameClue, Coproduct[Clue](name("Florey")))}

}
