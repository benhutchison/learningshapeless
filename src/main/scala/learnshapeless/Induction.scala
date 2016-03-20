package learnshapeless

import shapeless._
import shapeless.ops.hlist.Drop
import shapeless.test.illTyped

class Induction {

  val list = 123 :: "one two three" :: false :: HNil

  /** Example: A Typeclass that drops N elements from the front of an HList */

  assertEquals(123, MyDrop.drop(list, 0).head)
  assertEquals(false, MyDrop.drop(list, 2).head)

  //Recursive implicit resolution will fail here because list length is only 3
  illTyped("""MyDrop.drop(list, 4)""")

  //-1 isnt convertible to a Nat (ie natural number 0, 1, 2.. etc)
  illTyped("""MyDrop.drop(list, -1)""")


  trait MyDrop[L <: HList, N <: Nat] extends DepFn1[L] {
    type Out <: HList
  }

  object MyDrop {

    //The drop method summons an implicit typeclass MyDrop, parameterized on HList type L and the drop amount N
    def drop[L <: HList](l: L, n: Nat)(implicit myDrop: MyDrop[L, n.N]): myDrop.Out = myDrop(l)

    //The role of the MyDrop.Aux alias is to project `type Out` into a type parameter `Out0`
    type Aux[L <: HList, N <: Nat, Out0 <: HList] = MyDrop[L, N] {type Out = Out0}

    //This is the terminating-case that finishes the recursion.
    //It yields a MyDrop typeclass where the drop amount is `_0` ie type-level zero
    //No more dropping needs to be done, so the list thats passed in is returned unchanged
    implicit def hlistMyDropZero[L <: HList]: Aux[L, _0, L] =
      new MyDrop[L, _0] {
        type Out = L

        def apply(l: L): Out = l
      }

    //This is the recursive step. To understand how it works, study the type-signature of the type it yields
    //being `Aux[H :: T, Succ[N], dt.Out]`. There is type-level pattern matching going on here, where types H, T and N
    //are derived.
    // For example, if implicit instance MyDrop[String :: HNil, _2] is sought, then then invocation will bind:
    //  H = String, T = HNil, N = _1
    //Because this is the recursive step, each invocation must actually drop pone element of the list at the value level
    //This is visible in the result of `apply` as an invoke on the next typeclass `dt`, passing `l.tail`
    implicit def hlistMyDropRecurse[H, T <: HList, N <: Nat]
    (implicit dt: MyDrop[T, N]): Aux[H :: T, Succ[N], dt.Out] =
      new MyDrop[H :: T, Succ[N]] {
        type Out = dt.Out

        def apply(l: H :: T): Out = dt(l.tail)
      }
  }

  /**
    * Exercise: Drawing on the example above, write an implementation of take(n) over an hlist using the skeleton below
    * You'll need to
    * - define an Aux type alias, including type params
    * - Define type params, implicit params and implement the apply method for both `hlistMyTakeZero` and `hlistMyTakeRecurse`
    *
    * Consider what aspects will be the same, and what will be different, relative to drop(n)
    */

//Uncomment these to test when your implementation is compiling/working
//  assertEquals(123, MyTake.take(list, 1).head)
//  assertEquals(123 :: "one two three" :: HNil, MyTake.take(list, 2))

  illTyped("""list.take(4)""")
  illTyped("""list.take(-1)""")

  trait MyTake[L <: HList, N <: Nat] extends DepFn1[L] with Serializable {
    type Out <: HList
  }

  object MyTake {

    def take[L <: HList, N <: Nat](l: L, n: Nat)(implicit myTake: MyTake[L, N]): myTake.Out = myTake(l)

    type Aux = Nothing

    implicit def hlistMyTakeZero = ???

    implicit def hlistMyTakeRecurse = ???
  }


}