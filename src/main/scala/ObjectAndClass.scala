package classandobject

// * Companion object
class Person(val firstName: String, val lastName: String) {}

object Person {
  def apply(name: String): Person = ???
}

// * Pattern Matching
case class Cat(color: String, food: String)

object ChipShop {
  def willServe(c: Cat): Boolean = ???
}
