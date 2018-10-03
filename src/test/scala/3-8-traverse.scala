package monad

import org.scalatest._

class `3.8.Traverse` extends AsyncFlatSpec with Matchers {
  markup {
    """
Traverse
=========
All Traversable need to be Foldable, but Traversable provide a more convenient, more powerful pattern for iteration.
Let's say we have a list of hosts and we need to check if all of them are up and running well.
"""
  }
  val hostnames = List(
    "alpha.example.com",
    "beta.example.com",
    "gamma.demo.com"
  )

  markup {
    """
````
def getUptime(hostname: String): Future[Int] =
  Future(hostname.length * 60)
```

How would you implement a `allUptimes` method to fetch each of the server and then return a `Future[List[Int]]`?

We could fold the list from Future of course
```
def allUptimes(hosts: List[String]): Future[List[Int]] = {
    hosts.foldLeft(Future(List.empty[Int])) {
      (accum, host) =>
      val uptime = getUptime(host)
      for {
        accum  <- accum
        uptime <- uptime
      } yield accum :+ uptime
    }
  }
```

If we can traverse the list with `String => Future[Int]`, it would be much more concise and eligant.

Hint: using `Future.traverse`
"""
  }
  behavior of "Uptime robot"

  it should "check each host's uptime" in {
    UptimeRobot.allUptimes(hostnames) map { result =>
      result shouldBe List(1020, 960, 840)
    }
  }

  markup {
    """
With traverse and identity, you can easily just create another very useful function `sequence`
```
def sequence[G[_]: Applicative, B](inputs: F[G[B]]): G[F[B]]  = traverse(inputs)(identity)
```

which is very useful e.g. you want to convert a `List[Future[?]]` to `Future[List[?]]`

The Uptime Robot in previous implementation was checking uptime synchronously, let's make it
asynchronously
"""
  }

  it should "check hosts asynchronously" in {
    UptimeRobot.asyncGetUptimes(hostnames.map(UptimeRobot.getUptime)) map {
      result =>
        result shouldBe List(1020, 960, 840)
    }
  }
}
