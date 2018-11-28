package monad

import cats.{Monad, Id}
import cats.instances.future._
import org.scalatest._
import scala.concurrent.Future

class `3.2.IdentityMonad` extends AsyncFlatSpec with Matchers {

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
