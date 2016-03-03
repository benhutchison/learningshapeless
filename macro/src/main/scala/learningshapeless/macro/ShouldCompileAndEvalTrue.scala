package learningshapeless.macros

import scala.language.experimental.macros

import scala.reflect.macros.whitebox
import scala.util.control.NonFatal

/** Used to write test cases for exercises where strong types mean a failing test wont compile*/
object ShouldCompileAndEvalTrue {

  def apply[A](booleanExpression: String): Boolean = macro ShouldCompileAndEvalTrueMacros.applyImpl

}

class ShouldCompileAndEvalTrueMacros(val c: whitebox.Context) {
  import c.universe._

  def applyImpl(booleanExpression: Tree): Tree = {
    // unpack the string
    val Literal(Constant(codeStr: String)) = booleanExpression

    // try to compile it and evaluate it as a Boolean
    try {
      val dummy0 = TermName(c.freshName)
      val dummy1 = TermName(c.freshName)
      val tree = c.parse(s"{ object $dummy0 { val $dummy1: Boolean = { $codeStr } }; $dummy0.$dummy1 }")
      c.typecheck(tree)
      tree
    } catch {
      case NonFatal(e) =>
        q"""throw new RuntimeException("Expression does not compile: "+${codeStr} + " " + ${e.toString})"""
    }
  }
}