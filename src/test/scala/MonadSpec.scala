package monad

import cats.data.Validated.{Invalid, Valid}
import cats.{Functor, Monad, Id, Cartesian}
import cats.instances.future._
import cats.syntax.functor._
import cats.instances.option._
import org.scalatest._
import scala.concurrent.Future

class MonadSpec extends AsyncFlatSpec with Matchers {

  markup {
    """
Functor
=========

Function is a Typeclass that generic abstract the `map` behavior.

If we map a function to a List, it will apply the function to each of it's element, and return a
new List. What if we need to map a function to our own custom data type, for example: Tree

"""
  }
  behavior of "Tree"

  it should "able to be map" in {
    Functor[Tree].map(Branch(Leaf(1), Leaf(2)))(_ * 2) shouldBe Branch(Leaf(2),
                                                                       Leaf(4))
    Functor[Tree].map(Branch(Leaf(1), Branch(Leaf(3), Leaf(4))))(_ * 3) shouldBe Branch(
      Leaf(3),
      Branch(Leaf(9), Leaf(12)))
  }

  it should "have implicit map method automatically" in {
    import Tree._
    branch(leaf(1), branch(leaf(3), leaf(4)))
      .map(_ * 3) shouldBe Branch(Leaf(3), Branch(Leaf(9), Leaf(12)))
  }

  markup {
    """

Contravariant Functor
---------
There's another variant of Functor with a reverse "arrow" of map, which we call it `contramap`

for a Functor, we have `map[A, B](fa: F[A])(A=>B): F[B]`, contrat map reverse the "arrow"

`contramap[A,B](fa: F[A])(B=>A): F[B]`

which means, if you have B=>A function and a F[A], you can get a F[B]

If it's hard to understand why we need a contramap, think about if type A is printable,
and you can convert B to A with function `B => A`, than you can say that B is also printable.
"""
  }
  behavior of "Printable"

  it should "print anything that can be converted to string or int" in {
    Printable.format(Box("hill")) shouldBe "\"hill\""
  }

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

  markup {
    """
Semigroupal a.k.a Cartesian
=========
Semigroupal is a type class that allows us to combine contexts with method `product`
```
trait Semigroupal[F[_]] {
  def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]
}
```
If we have a `F[A]` and `F[B]`, `product` method can combine them into `F[(A, B)]`
"""
  }

  behavior of "Semigroupal"

  it should "join 2 contexts" in {
    Cartesian[Option].product(Some(123), Some("abc")) shouldBe Some(
      (123, "abc"))
  }

  it should "join 3 contexts" in {
    Cartesian.tuple3(Option(1), Option(2), Option(3)) shouldBe Some((1, 2, 3))
  }
  markup {
    """
Semigroupal has predefined up to tuple22, the same size as tuple.

There is also a shortcut syntax for tupleN from `cats.syntax.apply`
```
import cats.syntax.apply._
(Option(1), Option(2), Option(3)).tupled
```
Try create a Cat with some Future value using `tupled` and map:
"""
  }

  it should "create Cat from Future value" in {
    SemiNAppli.createCatFromFuture(Future("Garfield"),
                                   Future(1978),
                                   Future("Lasagne")) map { cat =>
      cat shouldBe Cat("Garfield", 1978, "Lasagne")
    }
  }

  markup {
    """
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
}
