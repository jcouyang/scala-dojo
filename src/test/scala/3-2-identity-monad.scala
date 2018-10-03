package monad

import cats.{Monad, Id}
import cats.instances.future._
import org.scalatest._
import scala.concurrent.Future

class `3.2.IdentityMonad` extends AsyncFlatSpec with Matchers {

  markup {
    """
Identity Monad
=========
Monad is also a Functor, but with two more method `pure` and `flatMap`.
The simplest Monad is Id Monad, which has type:
```
type Id[A] = A
```

so basically if you do:
```
val a = Monad[Id].pure(3)
// a: cats.Id[Int] = 3

val b = Monad[Id].flatMap(a)(_ + 1)
// b: cats.Id[Int] = 4
```
type of `a` is equal to `Int` as well

It seems simple but it's actually very powerful and usaful because a is now a Monad,
which means you can `map` or `flatMap` it just like any Monad.

```
for {
  x <- a
  y <- b
} yield x + y
```

Now let's implement a generic `sumSquare` function to see how useful is `Id`
"""
  }

  behavior of "Id Monad"

  it should "able to calculate sum square of Future values" in {
    IdMonad.sumSquare(Future(3), Future(4)) map { result =>
      result shouldBe 25
    }
  }
  it should "able to use Id monad to lower the type level and verify the calculation logic much more easier" in {
    IdMonad.sumSquare(Monad[Id].pure(3), Monad[Id].pure(4)) shouldBe 25
  }
}
