package typeclass

import org.scalatest._

class TypeclassSpec extends FlatSpec with Matchers {

  behavior of "TrafficLight"

  it should "follow the rules" in {
    Red.next shouldBe Green
    Green.next shouldBe Yellow
    Yellow.next shouldBe Red
  }

  behavior of "LinkedList"

  it should "be able to get first" in {
    Pair(1, Pair(2, End()))(0) shouldBe 1
  }

  it should "be able to tail second" in {
    Pair(1, Pair(2, End()))(1) shouldBe 2
  }

  it should "throw error Out of Boundary if try to get 3rd" in {
    the [Exception] thrownBy Pair(1, Pair(2, End()))(2) should have message "Out of Boundary"
  }

  behavior of "Covarian LinkedList"

  it should "be able to get head" in {
    CoPair(1, CoPair(2, CoEnd))(0) shouldBe 1
  }

  behavior of "Person"

  it should "able to convert to JSON" in {
    JsonWriter[Person].write(Person("o", "oyanglulu@gmail.com")) shouldBe """{"name": "o", "email": "oyanglulu@gmail.com"}"""
  }

  it should "able to sort by name" in {
    List(Person("o", "j"), Person("j", "o")).sorted shouldEqual List(Person("o", "j"), Person("j", "o"))
  }

  behavior of "Cat"
  it should "able to convert to JSON" in {
    JsonWriter[Cat].write(Cat("garfield", "chips")) shouldBe """{"name": "garfield", "food": "chips"}"""
  }
}
