package classandobject

import org.scalatest._

class ClassAndObjectSpec extends FlatSpec with Matchers {

  behavior of "Friendly Person Factory"

  it should "take a full name and able to get first and last name seprately" in {
    val me = Person("Jichao Ouyang")
    me.lastName shouldBe "Ouyang"
    me.firstName shouldBe "Jichao"
  }

  behavior of "Chip Shop"

  it should "only accept Cat when it's favourites food is Chips" in {
    ChipShop.willServe(Cat("Garfield", "Chips")) shouldBe true
  }
}
