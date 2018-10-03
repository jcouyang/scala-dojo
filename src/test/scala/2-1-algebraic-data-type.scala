package typeclass

import org.scalatest._

class `2.1.ADT` extends FlatSpec with Matchers {

  markup {
    """
Type classes
------
Type classes is somewhat like an FP design pattern, with type classes
you can extend new functionality without touch any of your existing
code or 3rd party library.
Type classes vs OO classes

|   |Add new method         | Add new data          |
|---|------------------------|------------------------|
|OO |Change existing code   |Existing code unchanged|
|FP |Existing code unchanged|Change existing code   |

But FP has pretty elegant [way](https://oleksandrmanzyuk.wordpress.com/2014/06/18/from-object-algebras-to-finally-tagless-interpreters-2/) or [ways](http://www.cs.ru.nl/~W.Swierstra/Publications/DataTypesALaCarte.pdf) to walk around this issue.

Algebraic Data Type
=========
We all familiar with how to implement traffic light in OO with class and interface. Simply 3 classes Red Green Yellow extend a TrafficLight interface, and implement `next`
method in each of the 3 classes.

But how can we do differently with Scala `trait` and `pattern matching`?
  """
  }

  behavior of "TrafficLight"

  it should "follow the rules" in {
    Red.next shouldBe Green
    Green.next shouldBe Yellow
    Yellow.next shouldBe Red
  }
  markup {
    """
The type of `TrafficLight` you've just implemented is call *Algebraic Data Type(ADT)*, which
means the type is algebra of `Red + Green + Yellow`, type of algebra of `+` is called Sum or Coproduct type.
The other algebra is `x`, we've already tried `case class` in Scala, which is a Product Type.
e.g. Cat is product type because it's `String x String`. You also can simply translate `+` as `or` and `x` as `and`
then it will make more sense.
"""
  }
}
