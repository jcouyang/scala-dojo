package typeclass

import org.scalatest._

class `2.4.TypeClasses` extends FlatSpec with Matchers {
  markup {
    """
Type classes
=========
`Person` is a simple case class, when we want to print it nicely as JSON format, what we usually does is to create a
interface e.g. `JsonWriter` with method `toJson` and implements it in `Person`. But if you think about it, if we need
`Person` to have another ability e.g. `HtmlWriter`, you need to open `Person` class and implement a new interface.

However, FP does it completely different, with type classes, we can leave `Person` completely untouched and define it's behavior
in type class, and then implicitly implement the type class for `Person` anywhere.

Now try not touching class `Person` and let it able to print as JSON and sortable by name.
"""
  }

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

  markup { """
It's easy to add the same behavior to any other type as well.
""" }

  behavior of "Cat"
  it should "able to convert to JSON" in {
    JsonWriter.write(Cat("Garfield", "coke")) shouldBe """{"name": "Garfield", "food": "coke"}"""
  }

  behavior of "CatPerson"

  it should "be very easy to convert to JSON" in {
    JsonWriter.write(CatPerson(Person("a", "b"), Cat("Garfield", "chips"))) shouldBe """{"person":{"name": "a", "email": "b"},"cat":{"name": "Garfield", "food": "chips"}}"""
  }
}
