package monad

import org.scalatest._

class `3.5.MonadTransformer` extends AsyncFlatSpec with Matchers {
  behavior of "Transformer"

  markup {
    """
Monad Transformer
=========

```scala
type ErrorOr[A] = Either[String, A]
val ihave10:ErrorOr[Int] = Right(Some(10)
```

If I just want to map `10` in side `Right` and `Some`, I have to map a function and inside which map again

```scala
ihave10.map(r => r.map(_ + 1))
```

If you have a `OptionT`, which is the Monad Transformer for `Option`, things will be lot easier

```scala

val ihaveOneMore = OptionT[ErrorOr, Int](ihave10).map(_ + 1)
```

if you map on the `OptionT`, it will transfer the `map` to the Option type inside `ErrorOr`

Please implement `getPowerLevel` to return a EitherT, you may find
[companion object `EitherT`](https://typelevel.org/cats/api/cats/data/EitherT$.html#left[B]:cats.data.EitherT.LeftPartiallyApplied[B]) useful to create a EitherT.

"""
  }

  it should "get Bumblebee's power lever" in {
    Transformer.getPowerLevel("Bumblebee").value map { level =>
      level shouldEqual Right(8)
    }
  }

  it should "not able to reach Smurf" in {
    Transformer.getPowerLevel("Smurf").value map { level =>
      level shouldEqual Left("Smurf unreachable")
    }
  }

  markup {
    """
Two autobots can perform a special move if their combined power level is greater than 15.
Implement `canSpecialMove` method, you'll find out why we need a Monad Transformer here.
"""
  }
  it should "get special move when Bumblebee and Hot Rod together" in {
    Transformer.canSpecialMove("Bumblebee", "Hot Rod").value map { level =>
      level shouldEqual Right(true)
    }
  }

  it should "not get special move when Bumblebee and Jazz together" in {
    Transformer.canSpecialMove("Bumblebee", "Jazz").value map { level =>
      level shouldEqual Right(false)
    }
  }

  markup {
    """
Finally, write a method tacticalReport that takes two ally names and prints a message
saying whether they can perform a special move.
"""
  }
  it should "return a nice msg when Bumblebee and Hot Rod together" in {
    Transformer.tacticalReport("Bumblebee", "Hot Rod") shouldBe "Bumblebee and Hot Rod are ready to roll out!"
  }

  it should "return a nice msg when Bumblebee and Jazz together" in {
    Transformer.tacticalReport("Bumblebee", "Jazz") shouldBe "Bumblebee and Jazz need a recharge."
  }

  it should "return a error msg when Bumblebee and Smurf together" in {
    Transformer.tacticalReport("Bumblebee", "Smurf") shouldBe "Comms error: Smurf unreachable"
  }
}
