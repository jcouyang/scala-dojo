package typeclass

import org.scalatest._

class `2.4.TypeClasses` extends FlatSpec with Matchers {

  behavior of "Person"

  it should "able to convert to JSON" in {
    JsonWriter.write(Person("o", "oyanglulu@gmail.com")) shouldBe """{"name": "o", "email": "oyanglulu@gmail.com"}"""
  }

  it should "able to sort by name" in {
    List(Person("a", "d"), Person("b", "c")).sorted shouldEqual List(
      Person("a", "d"),
      Person("b", "c"))
    List(Person("b", "c"), Person("a", "d")).sorted shouldEqual List(
      Person("a", "d"),
      Person("b", "c"))
  }

  behavior of "Cat"
  it should "able to convert to JSON" in {
    JsonWriter.write(Cat("Garfield", "coke")) shouldBe """{"name": "Garfield", "food": "coke"}"""
  }

  behavior of "CatPerson"

  it should "be very easy to convert to JSON" in {
    JsonWriter.write(CatPerson(Person("a", "b"), Cat("Garfield", "chips"))) shouldBe """{"person":{"name": "a", "email": "b"},"cat":{"name": "Garfield", "food": "chips"}}"""
  }

}
