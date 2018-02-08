package typeclass

import org.scalatest._

class TypeclassSpec extends FlatSpec with Matchers {

  markup { """
Type classes
------
Type classes is somewhat like an FP design pattern, with type classes
you can extend new functionality without touch any of your existing
code or 3rd party library.
Type classes vs OO classes

|   |Add new method         | Add new data          |
|---|------------------------|------------------------|
|OO |Change existing code   |Existing code unchanged|
|FP |Existing code unchanged|Change existing code   |

But FP has pretty elegant [way](https://oleksandrmanzyuk.wordpress.com/2014/06/18/from-object-algebras-to-finally-tagless-interpreters-2/) or [ways](http://www.cs.ru.nl/~W.Swierstra/Publications/DataTypesALaCarte.pdf) to walk around this issue.

Algebraic Data Type
=========
We all familiar with how to implement traffic light in OO with class and interface. Simply 3 classes Red Green Yellow extend a TrafficLight interface, and implement `next`
method in each of the 3 classes.

But how can we do differently with Scala `trait` and `pattern matching`?
  """ }

  behavior of "TrafficLight"

  it should "follow the rules" in {
    Red.next shouldBe Green
    Green.next shouldBe Yellow
    Yellow.next shouldBe Red
  }

  markup {"""
The type of `TrafficLight` you've just implemented is call *Algebraic Data Type(ADT)*, which
means the type is algebra of `Red + Green + Yellow`, type of algebra of `+` is called Sum or Coproduct type.
The other algebra is `x`, we've already tried `case class` in Scala, which is a Product Type.
e.g. Cat is product type because it's `String x String`. You also can simply translate `+` as `or` and `x` as `and`
then it will make more sense.

Recursive Data Type
=========
with ADT it's very easy to implement a linked list in Scala as well.
Try using sum and product type to implement a `LinkedList`. Type can be recursive as well.
"""}

  behavior of "LinkedList of 2 elements"

  it should "be able to get first" in {
    Pair(1, Pair(2, End()))(0) shouldBe 1
  }

  it should "be able to get second element" in {
    Pair(1, Pair(2, End()))(1) shouldBe 2
  }

  it should "throw error Out of Boundary exception if try to get 3rd" in {
    the [Exception] thrownBy Pair(1, Pair(2, End()))(2) should have message "Out of Boundary"
  }

  markup {"""
Variance
=========
You may realize that `End` does not have to be a `case class`, it doesn't have any value in it, and we
just need one instance of `End`.

Try changing it into `case object`, uncomment the following case and fix the compiler errors.
"""}

  behavior of "Covarian LinkedList"

  it should "have the same behaviors as LinkedList" in {
    pending
    // CoPair(1, CoPair(2, CoEnd))(0) shouldBe 1
    // CoPair(1, CoPair(2, CoEnd))(1) shouldBe 2
    // the [Exception] thrownBy CoPair(1, CoPair(2, CoEnd))(2) should have message "Out of Boundary"
  }

  markup {"""
The way you fix the compiler error is called *Covarian*, which means if `B` is `A`'s subtype, `CoLinkedList[B]`
is then `CoLinkedList[A]`'s subtype

Here `Nothing` is subtype of any type, since `A` is covariance, `End` of type `CoLinkedList[Nothing]` become subtype of `CoLinkedList[A]`

Type classes
=========
`Person` is a simple case class, when we want to print it nicely as JSON format, what we usually does is to create a
interface e.g. `JsonWriter` with method `toJson` and implements it in `Person`. But if you think about it, if we need
`Person` to have another ability e.g. `HtmlWriter`, you need to open `Person` class and implement a new interface.

However, FP does it completely different, with type classes, we can leave `Person` completely untouched and define it's behavior
in type class, and then implicitly implement the type class for `Person` anywhere.

Now try not touching class `Person` and let it able to print as JSON and sortable by name.
"""}

  behavior of "Person"

  it should "able to convert to JSON" in {
    JsonWriter.write(Person("o", "oyanglulu@gmail.com")) shouldBe """{"name": "o", "email": "oyanglulu@gmail.com"}"""
  }

  it should "able to sort by name" in {
    List(Person("a", "d"), Person("b", "c")).sorted shouldEqual List(Person("a", "d"), Person("b", "c"))
    List(Person("b", "c"), Person("a", "d")).sorted shouldEqual List(Person("a", "d"), Person("b", "c"))
  }

  markup {"""
It's easy to add the same behavior to any other type as well.
"""}

  behavior of "Cat"
  it should "able to convert to JSON" in {
    JsonWriter.write(Cat("Garfield", "coke")) shouldBe """{"name": "Garfield", "food": "coke"}"""
  }

  behavior of "CatPerson"

  it should "be very easy to convert to JSON" in {
    JsonWriter.write(CatPerson(Person("a", "b"), Cat("Garfield", "chips"))) shouldBe """{"person":{"name": "a", "email": "b"},"cat":"{"name": "Garfield", "food": "chips"}"}"""
  }

  markup {"""
Type enrichment
=========
With implicit class, you can magically add methods to any Type

For example to add a new method `numberOfVowels` to `String` type, we can simply define
a implicit class, and add the method there

```scala
implicit class ExtraStringMethods(str: String) {
  val vowels = Seq('a', 'e', 'i', 'o', 'u')

  def numberOfVowels =
    str.toList.filter(vowels contains _).length
}
```

When you do `"the quick brown fox".numberOfVowels`, Scala compiler can't find `numberOfVowels`
in `String` type, but it will try to find an implicit class which has a `numberOfVowels`,
if it can find one. Here compiler found `ExtraStringMethods`, then it will implicitly create
an instance of `ExtraStringMethods` from string "the quick brown fox", so calling `numberOfVowels`
will just work like it's builtin implemented method of `String` type.
"""}

  it should "able to use `writeJson` method" in {
    CatPerson(Person("oyjc", "oyanglulu@gmail.com"), Cat("Hello Kitty", "rainbow")).writeJson shouldBe """{"person":{"name": "oyjc", "email": "oyanglulu@gmail.com"},"cat":"{"name": "Hello Kitty", "food": "rainbow"}"}"""
  }

  markup {"""
But, it's not generic enough, we still need to implement `Cat.writeJson` and `Person.writeJson`.
How can we have a generic `writeJson` method which automatically works for all `JsonWrite[_]` type
"""}

  "Cat and Person" should "also be able to use `writeJson` without any changes" in {
    import JsonWriter.Ops
    Cat("Garfield", "chips").writeJson shouldBe """{"name": "Garfield", "food": "chips"}"""
    Person("Philip", "Fry").writeJson shouldBe """{"name": "Philip", "email": "Fry"}"""
  }
}
