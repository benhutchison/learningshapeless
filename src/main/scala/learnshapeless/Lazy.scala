package learnshapeless

import shapeless._
import test.illTyped

object LazyDemo extends App {

  val l: List[Int] = Cons(1, Nil)

  import Show._

  /** This exercise demos the Lazy[T] typeclass, which relaxes the conservative termination checker for implicit resolution,
    * which can often mistakenly reject valid recursive implicits. */

  /** Exercises: Answer each of the following
    * 1. What happens if show(l) is removed from the illTyped wrapper?
    * What does the error mean?
    * What specifically about this situation causes it to happen
    * */
  illTyped("""show(l)""")

  /** 2. Does the error happen when required implicits are manually/explicitly provided?
    * Make a prediction, then uncomment below and find out.
    * can you explain why/why not? */
  //show(l)(showList(showCons(showInt, showList)))

  /** 3. Fix it using typeclass Lazy[T]. Replace showCons with `showCons2` and study the differences.
    * Apply the same changes anywhere else you think are needed, so that `show(l)` compiles and works
    * Note: your changes will probably make exercise 2 above not compile; just comment it out. */

}

sealed trait List[+T]
case class Cons[T](hd: T, tl: List[T]) extends List[T]
sealed trait Nil extends List[Nothing]
case object Nil extends Nil

trait Show[T] {
  def apply(t: T): String
}

object Show {

  def show[T](t: T)(implicit s: Show[T]) = s(t)

  // Base case for Int
  implicit def showInt: Show[Int] = new Show[Int] {
    def apply(t: Int) = t.toString
  }

  // Base case for Nil
  implicit def showNil: Show[Nil] = new Show[Nil] {
    def apply(t: Nil) = "Nil"
  }

  // Case for Cons[T]: note (mutually) recursive implicit argument referencing Show[List[T]]
  implicit def showCons[T](implicit st: Show[T], sl: Show[List[T]]): Show[Cons[T]] = new Show[Cons[T]] {
    def apply(t: Cons[T]) = s"Cons(${show(t.hd)(st)}, ${show(t.tl)(sl)})"
  }

//  implicit def showCons2[T](implicit st: Show[T], sl: Lazy[Show[List[T]]]): Show[Cons[T]] = new Show[Cons[T]] {
//    def apply(t: Cons[T]) = s"Cons(${show(t.hd)(st)}, ${show(t.tl)(sl.value)})"
//  }

  // Case for List[T]: note (mutually) recursive implicit argument referencing Show[Cons[T]]
  implicit def showList[T](implicit sc: Show[Cons[T]]): Show[List[T]] = new Show[List[T]] {
    def apply(t: List[T]) = t match {
      case n: Nil => show(n)
      case c: Cons[T] => show(c)(sc)
    }
  }
}