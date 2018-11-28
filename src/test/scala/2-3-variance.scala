package typeclass

import org.scalatest._

class `2.3.Variance` extends FlatSpec with Matchers {

  behavior of "Covarian LinkedList"

  it should "have the same behaviors as LinkedList" in {
    pending
    // CoPair(1, CoPair(2, CoEnd))(0) shouldBe 1
    // CoPair(1, CoPair(2, CoEnd))(1) shouldBe 2
    // the [Exception] thrownBy CoPair(1, CoPair(2, CoEnd))(2) should have message "Out of Boundary"
  }

}
