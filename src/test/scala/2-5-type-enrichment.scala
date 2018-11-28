package typeclass

import org.scalatest._

class `2.5.TypeEnrichment` extends FlatSpec with Matchers {

  it should "able to use `writeJson` method" in {
    CatPerson(
      Person("oyjc", "oyanglulu@gmail.com"),
      Cat("Hello Kitty", "rainbow")).writeJson shouldBe """{"person":{"name": "oyjc", "email": "oyanglulu@gmail.com"},"cat":{"name": "Hello Kitty", "food": "rainbow"}}"""
  }

  "Cat and Person" should "also be able to use `writeJson` without any changes" in {
    import JsonWriter.Ops
    Cat("Garfield", "chips").writeJson shouldBe """{"name": "Garfield", "food": "chips"}"""
    Person("Philip", "Fry").writeJson shouldBe """{"name": "Philip", "email": "Fry"}"""
  }

}
