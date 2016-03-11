import shapeless.tag
import shapeless.tag.@@
import shapeless.test.illTyped

package object learnshapeless {

  def assertEquals[T](expected: T, actual: T) = assert(expected == actual, s"Expected: $expected != Actual: $actual")

  def assertHaveEqualTypes[A, B](a: A, b: B)(implicit ev: A =:= B): Unit = ()

  def assertHaveNonEqualTypes[A, B](a: A, b: B): Unit = illTyped("""assertHaveEqualTypes(a, b)""")


  def assertConformsToTypeOf[A, B](subtype: A, supertype: B)(implicit ev: A <:< B): Unit = ()

  object tag2 {
    def apply[T](t: T) = new TaggedValue[T](t)

    class TaggedValue[T](t: T) {
      def @@[U]: T @@ U = tag[U].apply[T](t)
    }
  }

}
