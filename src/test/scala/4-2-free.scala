package free
import org.scalatest._
import cats._
import cats.effect.IO
class `4-2-Free` extends AsyncFlatSpec with Matchers {

  behavior of "program"
  it should "hard to unit test get put delete" in {
    program() shouldBe (())
  }

  object DbEffInterp {
    val fake = Lambda[DbEff ~> IO](_ match {
      case Get("123")    => IO(s"value for key 123")
      case Put("321", v) => IO(println(s"saving 123 to database"))
      case Delete("123") => IO(println(s"deleteing 123"))
      case a             => IO(fail(s"unexpecting interaction: $a"))
    })
  }

  behavior of "free program"
  it should "run on fake interpreter to verify your program logic" in {
    (freeProgram() foldMap DbEffInterp.fake) unsafeRunSync () shouldBe (())
  }

}
