package learnshapeless

import learningshapeless.macros.ShouldCompileAndEvalTrue
import learnshapeless.Data._
import shapeless._

class CoproductsTest extends org.specs2.mutable.Specification with Assert {

  import Coproducts._

  eg {mustEqual(ex_nameClue, Coproduct[Clue](name("Florey")))}

  eg {mustEqual(ex_bornClue, Coproduct[Clue](born(1879)))}

  eg {mustEqual(ex_selectName, Option.empty[Name])}

  eg {mustEqual(ex_drop2, Coproduct[Country :+: CNil](australia))}

  eg {ShouldCompileAndEvalTrue("""mustEqual(Vector(true, false, true, false), eg_allClues.map(_.map(ex_isGoodClue)))""")}

  eg {mustEqual(Vector(true, false, true, false), ex_goodClues)}

}
