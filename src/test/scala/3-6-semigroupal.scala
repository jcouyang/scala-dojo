package monad

import cats._
import cats.instances.option._
import org.scalatest._
import scala.concurrent.Future

class `3.6.Semigroupal` extends AsyncFlatSpec with Matchers {
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
    Semigroupal[Option].product(Some(123), Some("abc")) shouldBe Some(
      (123, "abc"))
  }

  it should "join 3 contexts" in {
    Semigroupal.tuple3(Option(1), Option(2), Option(3)) shouldBe Some((1, 2, 3))
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
}
