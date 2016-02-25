package object learnshapeless {

  def assertEquals[T](expected: T, actual: T) = assert(expected == actual, s"Expected: $expected != Actual: $actual")

}
