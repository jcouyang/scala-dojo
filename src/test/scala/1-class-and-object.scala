package classandobject

import org.scalatest._

class `1.ClassAndObject` extends FlatSpec with Matchers {
  markup {
    """
This is just a warm up excercise, which verify your compiler, sbt and IDE is working fine.

Try implement `Person` and `ChipShop` in `ClassAndObject` and run `sbt testOnly *ClassAndObject`
"""
  }

  behavior of "Friendly Person Factory"

  it should "take a full name and able to get first and last name seprately" in {
    val me = Person("Jichao Ouyang")
    me.lastName shouldBe "Ouyang"
    me.firstName shouldBe "Jichao"
  }

  behavior of "Chip Shop"

  it should "only accept Cat when it's favourites food is Chips" in {
    ChipShop.willServe(Cat("Garfield", "Chips")) shouldBe true
    ChipShop.willServe(Cat("Azrael", "Smurfs")) shouldBe false
  }
}
