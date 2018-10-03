package typeclass

import org.scalatest._

class `2.2.RecursiveDataType` extends FlatSpec with Matchers {
  markup {
    """
Recursive Data Type
=========
with ADT it's very easy to implement a linked list in Scala as well.
Try using sum and product type to implement a `LinkedList`. Type can be recursive as well.
"""
  }

  behavior of "LinkedList of 2 elements"

  it should "be able to get first" in {
    Pair(1, Pair(2, End()))(0) shouldBe 1
  }

  it should "be able to get second element" in {
    Pair(1, Pair(2, End()))(1) shouldBe 2
  }

  it should "throw error Out of Boundary exception if try to get 3rd" in {
    the[Exception] thrownBy Pair(1, Pair(2, End()))(2) should have message "Out of Boundary"
  }
}
