package monad

import cats.data.Validated.{Invalid, Valid}
import org.scalatest._

class `3.7.Validated` extends AsyncFlatSpec with Matchers {

  markup {
    """
Hint: There is also a shortcut for `tupled` and `map` -- `mapN` from `cats.syntax.apply`

Validated
=========
Either is a fail fast data type, which means if something goes wrong, it will skip everything
else. But when it comes to a senario of validationg form, we would rather validate everything
and return all invalid message at one go.

"""
  }

  behavior of "create Cat from form input"
  it should "check empty input " in {
    SemiNAppli.createCatFromForm(Map()) shouldBe Invalid(
      List("name field not specified",
           "birth field not specified",
           "food field not specified"))
  }

  it should "check birth is a digit" in {
    SemiNAppli.createCatFromForm(Map("birth" -> "not a number")) shouldBe Invalid(
      List("name field not specified",
           "invalid birth For input string: \"not a number\"",
           "food field not specified"))
  }

  it should "check blank" in {
    SemiNAppli.createCatFromForm(Map("name" -> "", "birth" -> "not a number")) shouldBe Invalid(
      List("name should not be blank",
           "invalid birth For input string: \"not a number\"",
           "food field not specified"))
  }

  it should "create a Cat" in {
    SemiNAppli.createCatFromForm(Map("name" -> "Garfield",
                                     "birth" -> "1978",
                                     "food" -> "Lasagne")) shouldBe Valid(
      Cat("Garfield", 1978, "Lasagne"))
  }

  behavior of "Apply.ap"
  it should "be the same as mapN" in {
    SemiNAppli.applyCat(Map("name" -> "Garfield",
                            "birth" -> "1978",
                            "food" -> "Lasagne")) shouldBe Valid(
      Cat("Garfield", 1978, "Lasagne"))
  }
}
