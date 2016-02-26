package learnshapeless

import org.specs2.execute.Failure
import org.specs2.execute.StandardResults._

trait Assert {

  def mustEqual[T](expected: T, actual: T) =
    if (expected == actual) success else Failure(s"expected: $expected != actual: $actual")

}
