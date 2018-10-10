// package free
// import cats.functor.Contravariant
// import monad.{Box, Printable}
// import cats._

// case class Sphere[A](value: A)

// object Sphere {
//   implicit val sphereToBox: Sphere ~> Box =
//     Lambda[Sphere ~> Box](s => Box(s.value))

//   implicit def spherePrintable[F[_], A](
//       implicit p: Printable[F[A]],
//       fnk: (Sphere ~> F)): Printable[Sphere[A]] =
//     Contravariant[Printable].contramap(p)(fnk.apply)
// }
