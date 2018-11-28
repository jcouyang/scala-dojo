package monad

import cats._
import cats.instances.option._
import org.scalatest._
import scala.concurrent.Future

class `3.6.Semigroupal` extends AsyncFlatSpec with Matchers {

  behavior of "Semigroupal"

  it should "join 2 contexts" in {
    Semigroupal[Option].product(Some(123), Some("abc")) shouldBe Some(
      (123, "abc"))
  }

  it should "join 3 contexts" in {
    Semigroupal.tuple3(Option(1), Option(2), Option(3)) shouldBe Some((1, 2, 3))
  }

  it should "create Cat from Future value" in {
    SemiNAppli.createCatFromFuture(Future("Garfield"),
                                   Future(1978),
                                   Future("Lasagne")) map { cat =>
      cat shouldBe Cat("Garfield", 1978, "Lasagne")
    }
  }

}
