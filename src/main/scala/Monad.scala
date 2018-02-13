package monad

import scala.concurrent.{ Await, Future }
import cats.data.EitherT
import cats.instances.future._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object Transformer {

  lazy val powerLevels = Map(
  "Jazz"      -> 6,
  "Bumblebee" -> 8,
  "Hot Rod"   -> 10
  )

  type Response[A] = EitherT[Future, String, A]

  def getPowerLevel(autobot: String): Response[Int] = powerLevels.get(autobot) match {
    case Some(bot) => EitherT.right(Future(bot))
    case None => EitherT.left(Future(s"$autobot unreachable"))
  }

  def canSpecialMove(ally1: String, ally2: String): Response[Boolean] = for {
    level1 <- getPowerLevel(ally1)
    level2 <- getPowerLevel(ally2)
  } yield level1 + level2 > 15

  def tacticalReport(ally1: String, ally2: String): String = {
    val he = canSpecialMove(ally1, ally2).map { (can:Boolean) =>
      if(can) s"$ally1 and $ally2 are ready to roll out!"
      else s"$ally1 and $ally2 need a recharge."
    }.value

    Await.result(he, 1.second) match {
      case Left(msg) => s"Comms error: $msg"
      case Right(msg) => msg
    }
  }
}

trait Printable[A] { self =>
  def format(value: A): String
  def contramap[B](func: B => A): Printable[B] =
    new Printable[B] {
      def format(value: B): String =
        self.format(func(value))
    }
}

object Printable {
  def format[A: Printable](value:A): String = {
    implicitly[Printable[A]].format(value)
  }

  implicit val stringPrintable: Printable[String] = new Printable[String] {
    def format(value: String): String =
      "\"" + value + "\""
  }

  implicit val booleanPrintable: Printable[Boolean] = new Printable[Boolean] {
    def format(value: Boolean): String =
      if(value) "yes" else "no"
  }

  implicit def boxPrintable[A](implicit p: Printable[A]): Printable[Box[A]] =
    p.contramap((box:Box[A]) => box.value)
}

final case class Box[A](value: A)
