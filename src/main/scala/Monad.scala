package monad

import cats.Apply
import cats.data.Validated
import cats.data.Validated.Valid
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

import cats.{Functor, Monad}
import cats.data.{EitherT, Reader, Validated, Writer}
import cats.functor.Contravariant
import cats.instances.future._
import cats.instances.list._
import cats.instances.vector._
import cats.syntax.applicative._
import cats.syntax.apply._
import cats.syntax.either._
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.writer._
import scala.util.Try
import cats.syntax.traverse._
sealed trait Tree[+A]
final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
final case class Leaf[A](value: A) extends Tree[A]

object Tree {
  implicit val treeFunctor: Functor[Tree] = new Functor[Tree] {
    def map[A, B](value: Tree[A])(f: A => B): Tree[B] = ???
  }

  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)
  def leaf[A](value: A): Tree[A] = Leaf(value)
}

trait Printable[A] { self =>
  def format(value: A): String
}

object Printable {
  implicit val printableContravariant = new Contravariant[Printable] {
    def contramap[A, B](fa: Printable[A])(func: B => A): Printable[B] = ???
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
    ???
}

final case class Box[A](value: A)

object IdMonad {
  def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] = ???
}

object Writers {

  type Logged[A] = Writer[Vector[String], A]

  def slowly[A](body: => A) =
    try body
    finally Thread.sleep(1)

  def factorial(n: Int): Logged[Int] = ???
}

object Readers {
  case class Db(
      usernames: Map[Int, String],
      passwords: Map[String, String]
  )

  type DbReader[A] = Reader[Db, A]

  def findUsername(userId: Int): DbReader[Option[String]] = ???

  def checkPassword(username: String, password: String): DbReader[Boolean] = ???

  def checkLogin(userId: Int, password: String): DbReader[Boolean] = ???
}

object States {
  import cats.data.State
  type CalcState[A] = State[List[Int], A]
  def evalOne(sym: String): CalcState[Int] = State[List[Int], Int] { state =>
    sym match {
      case "+" => ???
      case "*" => ???
      case _   => ???
    }
  }

  def evalAll(exprs: List[String]): CalcState[Int] = ???
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
      case Some(bot) => ???
      case None      => ???
    }

  def canSpecialMove(ally1: String, ally2: String): Response[Boolean] = ???

  def tacticalReport(ally1: String, ally2: String): String = ???
}

case class Cat(name: String, birth: Int, food: String)
object SemiNAppli {
  def createCatFromFuture(name: Future[String],
                          birth: Future[Int],
                          food: Future[String]): Future[Cat] = ???

  type AllErrorOr[A] = Validated[List[String], A]
  type FormData = Map[String, String]

  val notBlank = (field: String) =>
    (str: String) =>
      if (str.trim.isEmpty()) Left(List(field + " should not be blank"))
      else Right(str)

  def createCatFromForm(data: FormData): AllErrorOr[Cat] = ???
  def applyCat(data: FormData): AllErrorOr[Cat] = ???
}

object UptimeRobot {
  def getUptime(host: String) = Future(host.length * 60)

  def allUptimes(hosts: List[String]): Future[List[Int]] = ???

  def asyncGetUptimes(uptimes: List[Future[Int]]): Future[List[Int]] = ???
}
