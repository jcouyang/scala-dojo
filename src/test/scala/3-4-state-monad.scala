package monad

import org.scalatest._

class `3.4.State` extends AsyncFlatSpec with Matchers {

  markup {
    """
State Monad
=========
Same as a Writer Monad providing atomic logging, a State Monad will provide you thread safe atomic state operations.

Regardless it's name is `State`, it's pure functional, lazy and immutable operations.

Very similar to Reader, you can use `State.apply` to create a State Monad
```scala
State[Int, String](state => (state, "result"))
```
the differences from `Reader` is that it return a tuple which includes the new state.
"""
  }
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
