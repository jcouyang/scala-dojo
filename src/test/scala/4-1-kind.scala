package free
import monad._
import org.scalatest._

class `4-1-Kind` extends AsyncFlatSpec with Matchers {

  markup {
    """
Kind
===

Kinds are types for types. Like a function for parameters of type and return a type.
e.g. `List[_]` is a Kind, it has a `hole` represented as `_` in it, which is like paramter in function.
if you fill the hole with a type `String`, than you will get a type `List[String]`.

recall our `Printable[_]` kind defined in `3.1.Functor`, we've create implement of typeclass `Contravariant`
over kind `Printable`, which means no matter what type you fill into the hole of `Printable[_]`, it should
fullfill the constrain of typeclass `Contravariant`.

You can also define a function over Kind `F[_] -> G[_]`, which is called ~~FunctionK~~ , just like function over type.

FunctionK
------

to define a FuntionK in cats, we can simply using fish arrow `~>`

e.g. a FuntionK from `List` to `Option`

```scala
import cats.~>
val first = new (List ~> Option) {
  def apply[A](l:List[A]): Option[A] = l.headOption
}
```

Kind Projector
------
you'll notice that we've include a compiler plugin `kind-projector` in `build.sbt`

it provides us pretty syntactic suger for such case

```scala
val first = Lambda[List ~> Option](_.headOption)
```

which will expand to exactly the same code like what we defined before.
"""
  }

  behavior of "FunctionK"
  it should "create a FunctionK from `Box[_]` to `Sphere[_]`" in {
    implicit val s: Printable[Sphere[String]] =
      implicitly[Printable[Sphere[String]]]
    s.format(Sphere("hill")) shouldBe "(\"hill\")"
  }

  markup {
    """
If your kinds are happend to be Functor, than this functionK becomes ~~Natural Transformation~~

Natural Transformation
===

"""
  }
  behavior of "Natural Transformation"

  markup { """
Rank N Type
===
""" }
  behavior of "Rank N Type"
}
