package monad

import org.scalatest._

class `3.8.Traverse` extends AsyncFlatSpec with Matchers {

  val hostnames = List(
    "alpha.example.com",
    "beta.example.com",
    "gamma.demo.com"
  )

  behavior of "Uptime robot"

  it should "check each host's uptime" in {
    UptimeRobot.allUptimes(hostnames) map { result =>
      result shouldBe List(1020, 960, 840)
    }
  }

  it should "check hosts asynchronously" in {
    UptimeRobot.asyncGetUptimes(hostnames.map(UptimeRobot.getUptime)) map {
      result =>
        result shouldBe List(1020, 960, 840)
    }
  }

}
