package monad

import cats.Functor
import cats.syntax.functor._
import org.scalatest._

class `3.1.Functor` extends AsyncFlatSpec with Matchers {

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

Now please implement `boxPrintable` using `contracmap` and the next spec shall pass.
"""
  }
  behavior of "Printable"

  it should "print anything that can be converted to string or int" in {
    Printable.format(Box("hill")) shouldBe "\"hill\""
    Printable.format(Box(true)) shouldBe "yes"
  }
}
