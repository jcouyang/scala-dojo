package typeclass

import org.scalatest._

class `2.5.TypeEnrichment` extends FlatSpec with Matchers {

  markup {
    """
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
"""
  }

  it should "able to use `writeJson` method" in {
    CatPerson(
      Person("oyjc", "oyanglulu@gmail.com"),
      Cat("Hello Kitty", "rainbow")).writeJson shouldBe """{"person":{"name": "oyjc", "email": "oyanglulu@gmail.com"},"cat":{"name": "Hello Kitty", "food": "rainbow"}}"""
  }

  markup {
    """
But, it's not generic enough, we still need to implement `Cat.writeJson` and `Person.writeJson`.
How can we have a generic `writeJson` method which automatically works for all `JsonWrite[_]` type
"""
  }

  "Cat and Person" should "also be able to use `writeJson` without any changes" in {
    import JsonWriter.Ops
    Cat("Garfield", "chips").writeJson shouldBe """{"name": "Garfield", "food": "chips"}"""
    Person("Philip", "Fry").writeJson shouldBe """{"name": "Philip", "email": "Fry"}"""
  }
}
