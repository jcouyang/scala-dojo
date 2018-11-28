package monad

import org.scalatest._
import scala.concurrent.Future

class `3.3.ReaderWriter` extends AsyncFlatSpec with Matchers {

  behavior of "Writer"

  it should "logs are not interleave" in {
    Future.sequence(
      Vector(
        Future(Writers.factorial(3).run),
        Future(Writers.factorial(6).run)
      )) map { logs =>
      logs shouldBe Vector(
        (Vector("fact 0 1", "fact 1 1", "fact 2 2", "fact 3 6"), 6),
        (Vector("fact 0 1",
                "fact 1 1",
                "fact 2 2",
                "fact 3 6",
                "fact 4 24",
                "fact 5 120",
                "fact 6 720"),
         720)
      )
    }
  }

  behavior of "Reader"

  val config = Readers.Db(
    Map(1 -> "Jichao", 2 -> "Ouyang"),
    Map("Jichao" -> "p4ssw0rd", "Ouyang" -> "dr0wss4p")
  )
  it should "find user's name" in {
    Readers.findUsername(1).run(config) shouldBe Some("Jichao")
  }

  it should "check password" in {
    Readers.checkPassword("Jichao", "p4ssw0rd").run(config) shouldBe true
  }

  it should "check login" in {
    Readers.checkLogin(2, "dr0wss4p").run(config) shouldBe true
  }

}
