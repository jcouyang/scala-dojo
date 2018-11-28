package monad

import org.scalatest._

class `3.4.State` extends AsyncFlatSpec with Matchers {

  behavior of "State"

  it should "eval Int" in {
    States.evalOne("42").runA(Nil).value shouldBe 42
  }

  it should "eval and calculate" in {
    import States._
    val calculator = for {
      _ <- evalOne("1")
      _ <- evalOne("2")
      ans <- evalOne("+")
    } yield ans
    calculator.runA(Nil).value shouldBe 3
  }

  it should "eval a list of expr" in {
    import States._
    val calculator = evalAll(List("1", "2", "+", "3", "*"))
    calculator.runA(Nil).value shouldBe 9
  }

  it should "be pure and composable" in {
    import States._
    val calculator = for {
      _ <- evalAll(List("1", "2", "+"))
      _ <- evalAll(List("3", "4", "+"))
      ans <- evalOne("*")
    } yield ans
    calculator.runA(Nil).value shouldBe 21
  }

}
