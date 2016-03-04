import shapeless.tag
import shapeless.tag.@@

package object learnshapeless {

  def assertEquals[T](expected: T, actual: T) = assert(expected == actual, s"Expected: $expected != Actual: $actual")

  object tag2 {
    def apply[T](t: T) = new TaggedValue[T](t)

    class TaggedValue[T](t: T) {
      def @@[U]: T @@ U = tag[U].apply[T](t)
    }
  }

}
