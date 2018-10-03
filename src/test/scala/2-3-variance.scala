package typeclass

import org.scalatest._

class `2.3.Variance` extends FlatSpec with Matchers {

  markup {
    """
Variance
=========
You may realize that `End` does not have to be a `case class`, it doesn't have any value in it, and we
just need one instance of `End`.

Try changing it into `case object`, uncomment the following case and fix the compiler errors.
"""
  }

  behavior of "Covarian LinkedList"

  it should "have the same behaviors as LinkedList" in {
    pending
    // CoPair(1, CoPair(2, CoEnd))(0) shouldBe 1
    // CoPair(1, CoPair(2, CoEnd))(1) shouldBe 2
    // the [Exception] thrownBy CoPair(1, CoPair(2, CoEnd))(2) should have message "Out of Boundary"
  }

  markup {
    """
The way you fix the compiler error is called *Covarian*, which means if `B` is `A`'s subtype, `CoLinkedList[B]`
is then `CoLinkedList[A]`'s subtype

Here `Nothing` is subtype of any type, since `A` is covariance, `End` of type `CoLinkedList[Nothing]` become subtype of `CoLinkedList[A]`
"""
  }
}
