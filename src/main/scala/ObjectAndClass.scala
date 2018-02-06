package classandobject

// * Companion object
class Person(val firstName: String, val lastName: String) {}

object Person {
  def apply(fullName: String) = {
    val parts = fullName.split(" ")
    new Person(parts(0), parts(1))
  }
}

// * Pattern Matching
case class Cat(color: String, food: String)

object ChipShop {
  def willServe(cat: Cat) = true
}


