package free
import monad._
import cats._
import org.scalatest._

class `4-1-Kind` extends AsyncFlatSpec with Matchers {

  behavior of "FunctionK"
  it should "create a FunctionK from Box[_] to Sphere[_]" in {
    Printable.format(Sphere("hill")) shouldBe "\"hill\""
  }

  behavior of "Rank N Type"
  it should "able to print a tuple of Sphere" in {
    Printable.format((Sphere("hill"), Sphere(1))) shouldBe "\"(hill, 1)\""
  }

  "Natural Transformation" should "satisfy law" in {
    implicit val functorBox: Functor[Box] = new Functor[Box] {
      def map[A, B](fa: Box[A])(f: A => B) =
        Box(f(fa.value))
    }
    import cats.syntax.functor._
    Sphere.sphereToBox(Sphere(100).map(_ + 1)) shouldBe Sphere
      .sphereToBox(Sphere(100))
      .map(_ + 1)
  }

}
