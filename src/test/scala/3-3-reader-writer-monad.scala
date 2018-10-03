package monad

import org.scalatest._
import scala.concurrent.Future

class `3.3.ReaderWriter` extends AsyncFlatSpec with Matchers {
  markup {
    """
Writer Monad
=========

Writer monad is very useful for logging, especially in a concurrent system, for example the following
 `factorial` may executed concurrently, if it's not implemented in Writer, will be something looks like this:

```scala
def factorial(n: Int): Int = {
  val ans = slowly(if(n == 0) 1 else n * factorial(n - 1))
  println(s"fact $n $ans")
  ans
}
```

if you have 2 thread runs it, the logs of `factorial(3)` may interleave with `factorial(6)`, then it's hard to tell which log belongs to which calculation.

Please reimplement the `factorial` to return a Wrtier.

"""
  }
  behavior of "Writer"

  it should "logs are not interleave" in {
    Future.sequence(
      Vector(
        Future(Writers.factorial(3).run),
        Future(Writers.factorial(6).run)
      )) map { logs =>
      logs shouldBe Vector(
        (Vector("fact 0 1", "fact 1 1", "fact 2 2", "fact 3 6"), 6),
        (Vector("fact 0 1",
                "fact 1 1",
                "fact 2 2",
                "fact 3 6",
                "fact 4 24",
                "fact 5 120",
                "fact 6 720"),
         720)
      )
    }
  }

  markup {
    """
Reader Monad
=========

One common use for Readers is dependency injection. If we have a number of operations that all depend on some external configuration.

We can simply create a Reader[A, B] from `A => B` using Reader.apply

> Fun facts:
```scala
val catName: Reader[Cat, String] =
  Reader(cat => cat.name)
// catName: cats.data.Reader[Cat,String] = Kleisli(<function1>)
```
You may found that the value of a `Reader` is `Kleisli` instead of `Reader`, actually, `Kleisli` more generic form of `Reader`

`Reader[Cat, String]` is basically alias of `Kleisli[Id, Cat, String]`

where `Kleisli` is generic type represents function `A => F[B]`.

"""
  }
  behavior of "Reader"

  val config = Readers.Db(
    Map(1 -> "Jichao", 2 -> "Ouyang"),
    Map("Jichao" -> "p4ssw0rd", "Ouyang" -> "dr0wss4p")
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
}
