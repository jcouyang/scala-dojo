package free
import cats.Contravariant
import monad.{Box, Printable}
import cats._
import cats.free.Free

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

  implicit val functorSphere: Functor[Sphere] = new Functor[Sphere] {
    def map[A, B](fa: Sphere[A])(f: A => B) =
      Sphere(f(fa.value))
  }
}

object Database {
  def get(key: String): String = {
    println("querying database")
    s"value for key $key"
  }

  def put(key: String, value: String) = {
    println(s"saving $key to database")
  }

  def delete(key: String) = {
    println(s"deleteing $key")
  }
}

object program {
  def apply() = {
    val oldKey = "123"
    val oldVal = Database.get(oldKey)
    val newVal = s"this is new: $oldVal"
    val newKey = oldKey.reverse
    Database.put(newKey, newVal)
    Database.delete(oldKey)
  }
}

sealed trait DbEff[A]
case class Put(key: String, value: String) extends DbEff[Unit]
case class Get(key: String) extends DbEff[String]
case class Delete(key: String) extends DbEff[Unit]

object DbEff {
  def get(key: String): Free[DbEff, String] =
    Free.liftF[DbEff, String](Get(key))
  def put(key: String, v: String): Free[DbEff, Unit] =
    Free.liftF[DbEff, Unit](Put(key, v))
  def delete(key: String): Free[DbEff, Unit] =
    Free.liftF[DbEff, Unit](Delete(key))
}

object freeProgram {
  val oldKey = "123"
  def apply() =
    for {
      oldVal <- DbEff.get(oldKey)
      newVal = s"this is new: $oldVal"
      newKey = oldKey.reverse
      _ <- DbEff.put(newKey, newVal)
      _ <- DbEff.delete(newKey)
    } yield ()
}
