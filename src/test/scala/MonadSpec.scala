package monad

import org.scalatest._
import scala.concurrent.Future

class MonadSpec extends AsyncFlatSpec with Matchers {

  markup {"""
## Writer Monad

Writer monad is very useful for logging, espectially in a concurrent system, for example the following
 `factorial` may executed concurrently, if it's not implemented in Writer, will be something looks like this:

```scala
def factorial(n: Int): Int = {
  val ans = slowly(if(n == 0) 1 else n * factorial(n - 1))
  println(s"fact $n $ans")
  ans
}
```

if you have 2 thread runing it, the logs of `factorial(3)` may interleave with `factorial(6)`, then it's hard to tell which log belongs to which caculation.

Please reimplement the `factorial` to return a Wrtier.

"""}
  behavior of "Writer"

  it should "logs are not interleave" in {
    Future.sequence(Vector(
      Future(Writers.factorial(3).run),
      Future(Writers.factorial(6).run)
    )) map { logs =>
      logs shouldBe Vector(
        (Vector("fact 0 1", "fact 1 1", "fact 2 2", "fact 3 6"),6),
        (Vector("fact 0 1", "fact 1 1", "fact 2 2", "fact 3 6", "fact 4 24", "fact 5 120", "fact 6 720"),720))
    }
  }

  markup {"""
## Reader Monad

One common use for Readers is dependency injec on. If we have a number of opera ons that all depend on some external configura on.

"""}
  behavior of "Reader"

  val config = Readers.Db(
    Map( 1 -> "Jichao", 2 -> "Ouyang" ),
    Map( "Jichao" -> "p4ssw0rd", "Ouyang" -> "dr0wss4p")
  )
  it should "find user's name" in {
    Readers.findUsername(1).run(config) shouldBe Some("Jichao")
  }

  it should "check password" in {
    Readers.checkPassword("Jichao", "p4ssw0rd").run(config) shouldBe true
  }

  it should "check login" in {
    Readers.checkLogin(2, "dr0wss4p").run(config) shouldBe true
  }

  behavior of "Transformer"

  markup {"""
## Monad Transformer

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

"""}

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

  markup {"""
Two autobots can perform a special move if their combined power level is greater than 15.
Implement `canSpecialMove` method, you'll find out why we need a Monad Transformer here.
"""}
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

  markup {"""
Finally, write a method tacticalReport that takes two ally names and prints a message
saying whether they can perform a special move.
"""}
  it should "return a nice msg when Bumblebee and Hot Rod together" in {
    Transformer.tacticalReport("Bumblebee", "Hot Rod") shouldBe "Bumblebee and Hot Rod are ready to roll out!"
  }

  it should "return a nice msg when Bumblebee and Jazz together" in {
    Transformer.tacticalReport("Bumblebee", "Jazz") shouldBe "Bumblebee and Jazz need a recharge."
  }

  it should "return a error msg when Bumblebee and Smurf together" in {
    Transformer.tacticalReport("Bumblebee", "Smurf") shouldBe "Comms error: Smurf unreachable"
  }

  markup {"""
## Functor
### Contravariant Functor

"""}
  behavior of "Printable"

  it should "print anything that can be convert to string or int" in {
    Printable.format(Box("hill")) shouldBe "\"hill\""
  }
}
