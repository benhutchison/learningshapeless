import shapeless.tag
import shapeless.tag.@@

package object learnshapeless {

  def assertEquals[T](expected: T, actual: T) = assert(expected == actual, s"Expected: $expected != Actual: $actual")

  def assertHaveEqualTypes[A, B](a: A, b: B)(implicit ev: A =:= B): Unit = ()


  def assertConformsToTypeOf[A, B](subtype: A, supertype: B)(implicit ev: A <:< B): Unit = ()

  //The `tag` operator provided by Shapeless doesn't work when the tagged value is assigned onto an explicit typed val or def.
  //https://github.com/milessabin/shapeless/issues/557
  //for this reason, Im using a variant syntax but the final effect is the same
  object tag2 {
    def apply[T](t: T) = new TaggedValue[T](t)

    class TaggedValue[T](t: T) {
      def @@[U]: T @@ U = tag[U].apply[T](t)
    }
  }

}
