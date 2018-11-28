package monad

import cats.Functor
import cats.syntax.functor._
import org.scalatest._

class `3.1.Functor` extends AsyncFlatSpec with Matchers {

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

  behavior of "Printable"

  it should "print anything that can be converted to string or int" in {
    Printable.format(Box("hill")) shouldBe "\"hill\""
    Printable.format(Box(true)) shouldBe "yes"
  }

}
