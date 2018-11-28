package typeclass

import org.scalatest._

class `2.1.ADT` extends FlatSpec with Matchers {

  behavior of "TrafficLight"

  it should "follow the rules" in {
    Red.next shouldBe Green
    Green.next shouldBe Yellow
    Yellow.next shouldBe Red
  }

}
