package typeclass

// hint: why it has to be `sealed`?
sealed trait TrafficLight {
  def next = {
    this match {
      case Red => Green
      case Green => Yellow
      case Yellow => Red
    }
  }
}

final case object Red extends TrafficLight
final case object Green extends TrafficLight
final case object Yellow extends TrafficLight



sealed trait LinkedList[A] {
  def apply(index: Int): A =
    this match {
      case Pair(head, tail) =>
        if(index == 0)
          head
        else
          tail(index - 1)
      case End() =>
        throw new Exception("Out of Boundary")
    }
}
final case class Pair[A](head: A, tail: LinkedList[A]) extends LinkedList[A]
final case class End[A]() extends LinkedList[A]


sealed trait CoLinkedList[+A] {
  def apply(index: Int): A =
    this match {
      case CoPair(head, tail) =>
        if(index == 0)
          head
        else
          tail(index - 1)
      case CoEnd =>
        throw new Exception("Out of Boundary")
    }
}
final case class CoPair[A](head: A, tail: CoLinkedList[A]) extends CoLinkedList[A]
final case object CoEnd extends CoLinkedList[Nothing]


trait JsonWriter[A] {
  def write(in: A): String
}

object JsonWriter {
  def apply[A](implicit writer:JsonWriter[A]) = writer
}

final case class Person(name: String, email: String)

object Person {
  implicit val jsonWriterForPerson = new JsonWriter[Person] {
    def write(p: Person) =
      s"""{"name": "${p.name}", "email": "${p.email}"}"""
  }
  implicit val sortablePerson = Ordering.fromLessThan[Person]((x, y) =>
    x.name < x.name
  )
}

final case class Cat(name: String, food: String)

object Cat {
  implicit val jsonWriterForCat = new JsonWriter[Cat] {
    def write(p: Cat) =
      s"""{"name": "${p.name}", "food": "${p.food}"}"""
  }
}
