package free
import cats.functor.Contravariant
import monad.{Box, Printable}
import cats._

case class Sphere[A](value: A)

object Sphere {
  implicit val sphereToBox: Sphere ~> Box =
    Lambda[Sphere ~> Box](s => Box(s.value)) // ???

  implicit def spherePrintable[A](implicit p: Printable[Box[A]],
                                  fnk: (Sphere ~> Box)): Printable[Sphere[A]] =
    Contravariant[Printable].contramap(p)(fnk.apply)

  implicit def tuplePrintable[A, B](
      implicit p: Printable[Box[String]],
      fnk: (Sphere ~> Box)): Printable[(Sphere[A], Sphere[B])] = {
    val tupleOfSphereToBox = (tupleOfSphere: (Sphere[A], Sphere[B])) => {
      val box1 = fnk(tupleOfSphere._1)
      val box2 = fnk(tupleOfSphere._2)
      Box(s"(${box1.value}, ${box2.value})")
    }
    Contravariant[Printable].contramap(p)(tupleOfSphereToBox)
  }
}
