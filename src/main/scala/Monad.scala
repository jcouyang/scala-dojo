package monad

import cats.Functor
import cats.functor.Contravariant
import scala.concurrent.{Await, Future}
import cats.data.EitherT
import cats.data.Writer
import cats.data.Reader
import cats.instances.vector._
import cats.instances.future._
import cats.syntax.writer._
import cats.syntax.applicative._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

sealed trait Tree[+A]
final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
final case class Leaf[A](value: A) extends Tree[A]

object Tree {
  implicit val treeFunctor: Functor[Tree] = new Functor[Tree] {
    def map[A, B](value: Tree[A])(f: A => B): Tree[B] = {
      value match {
        case Branch(left, right) => Branch(map(left)(f), map(right)(f))
        case Leaf(v)             => Leaf(f(v))
      }
    }
  }

  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)
  def leaf[A](value: A): Tree[A] = Leaf(value)
}

trait Printable[A] { self =>
  def format(value: A): String
}

object Printable {
  implicit val printableContravariant = new Contravariant[Printable] {
    def contramap[A, B](fa: Printable[A])(func: B => A): Printable[B] = {
      new Printable[B] {
        def format(value: B): String = fa.format(func(value))
      }
    }
  }

  def format[A: Printable](value: A): String = {
    implicitly[Printable[A]].format(value)
  }

  implicit val stringPrintable: Printable[String] = new Printable[String] {
    def format(value: String): String =
      "\"" + value + "\""
  }

  implicit val booleanPrintable: Printable[Boolean] = new Printable[Boolean] {
    def format(value: Boolean): String =
      if (value) "yes" else "no"
  }

  implicit def boxPrintable[A](implicit p: Printable[A]): Printable[Box[A]] =
    Contravariant[Printable].contramap(p)((box: Box[A]) => box.value)
}

final case class Box[A](value: A)

object Writers {

  type Logged[A] = Writer[Vector[String], A]

  def slowly[A](body: => A) =
    try body
    finally Thread.sleep(1)

  def factorial(n: Int): Logged[Int] =
    for {
      ans <- slowly(if (n == 0) 1.pure[Logged] else factorial(n - 1).map(_ * n))
      _ <- Vector(s"fact $n $ans").tell
    } yield ans
}

object Readers {
  case class Db(
      usernames: Map[Int, String],
      passwords: Map[String, String]
  )

  type DbReader[A] = Reader[Db, A]

  def findUsername(userId: Int): DbReader[Option[String]] =
    Reader(db => db.usernames.get(userId))
  def checkPassword(username: String, password: String): DbReader[Boolean] =
    Reader(db => db.passwords(username) == password)

  def checkLogin(userId: Int, password: String): DbReader[Boolean] =
    findUsername(userId).flatMap(user =>
      user match {
        case Some(name) => checkPassword(name, password)
        case None       => Reader(_ => false)
    })
}

object States {
  import cats.data.State
  type CalcState[A] = State[List[Int], A]
  def evalOne(sym: String): CalcState[Int] = State[List[Int], Int] { state =>
    sym match {
      case "+" => {
        val ans = state.head + state.tail.head
        (ans :: state.tail.tail, ans)
      }
      case "*" => {
        val ans = state.head * state.tail.head
        (ans :: state.tail.tail, ans)
      }
      case _ => (sym.toInt :: state, sym.toInt)

    }
  }

  def evalAll(exprs: List[String]): CalcState[Int] =
    exprs.foldLeft(0.pure[CalcState]) { (res: CalcState[Int], exp: String) =>
      res.flatMap(_ => evalOne(exp))
    }
}

object Transformer {

  lazy val powerLevels = Map(
    "Jazz" -> 6,
    "Bumblebee" -> 8,
    "Hot Rod" -> 10
  )

  type Response[A] = EitherT[Future, String, A]

  def getPowerLevel(autobot: String): Response[Int] =
    powerLevels.get(autobot) match {
      case Some(bot) => EitherT.right(Future(bot))
      case None      => EitherT.left(Future(s"$autobot unreachable"))
    }

  def canSpecialMove(ally1: String, ally2: String): Response[Boolean] =
    for {
      level1 <- getPowerLevel(ally1)
      level2 <- getPowerLevel(ally2)
    } yield level1 + level2 > 15

  def tacticalReport(ally1: String, ally2: String): String = {
    val he = canSpecialMove(ally1, ally2).map { (can: Boolean) =>
      if (can) s"$ally1 and $ally2 are ready to roll out!"
      else s"$ally1 and $ally2 need a recharge."
    }.value

    Await.result(he, 1.second) match {
      case Left(msg)  => s"Comms error: $msg"
      case Right(msg) => msg
    }
  }
}
