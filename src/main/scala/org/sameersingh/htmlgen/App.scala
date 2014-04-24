package org.sameersingh.htmlgen

/**
 * Set of points that form an experiment
 * @author sameer
 * @date 04/23/2014
 */
object App {

  import ListConverter.Implicits._
  //import TableConverter.Implicits._

  def print[T](t: T) {
    val html: HTML = t
    println(t.getClass.getCanonicalName + "\n" + html.source)
  }

  def main(args: Array[String]) {
    print(10)
    print(100.0)
    print("Sameer")
    print(List(1, 2, 3))
    print(List(Set(1, 4, 7, 10), Set(2, 5, 8, 11), Set(3, 6, 9, 12)))
    print(Seq(1, 2, 3))
    print(Set(1, 2, 3))
    print(Map(1 -> "a", 2 -> "b", 3 -> "c"))
    case class Company(name: String)
    case class Person(name: String, worksFor: Company)
    print(Person("Sameer", Company("UW")))
    print(Seq(Person("Sameer", Company("UW")), Person("Sebastian", Company("UCL")), Person("Vibhuti", Company("NYU"))))
  }
}
