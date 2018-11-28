package monad

import org.scalatest._

class `3.5.MonadTransformer` extends AsyncFlatSpec with Matchers {

  it should "get Bumblebee's power lever" in {
    Transformer.getPowerLevel("Bumblebee").value map { level =>
      level shouldEqual Right(8)
    }
  }

  it should "not able to reach Smurf" in {
    Transformer.getPowerLevel("Smurf").value map { level =>
      level shouldEqual Left("Smurf unreachable")
    }
  }

  it should "get special move when Bumblebee and Hot Rod together" in {
    Transformer.canSpecialMove("Bumblebee", "Hot Rod").value map { level =>
      level shouldEqual Right(true)
    }
  }

  it should "not get special move when Bumblebee and Jazz together" in {
    Transformer.canSpecialMove("Bumblebee", "Jazz").value map { level =>
      level shouldEqual Right(false)
    }
  }

  it should "return a nice msg when Bumblebee and Hot Rod together" in {
    Transformer.tacticalReport("Bumblebee", "Hot Rod") shouldBe "Bumblebee and Hot Rod are ready to roll out!"
  }

  it should "return a nice msg when Bumblebee and Jazz together" in {
    Transformer.tacticalReport("Bumblebee", "Jazz") shouldBe "Bumblebee and Jazz need a recharge."
  }

  it should "return a error msg when Bumblebee and Smurf together" in {
    Transformer.tacticalReport("Bumblebee", "Smurf") shouldBe "Comms error: Smurf unreachable"
  }

}
