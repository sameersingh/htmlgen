package org.sameersingh.htmlgen

/**
 * @author sameer
 * @since 4/24/14.
 */
class Tester(c: Converter) {

  import ConverterUtils._
  import c.Implicits._

  def writeHTML(title: String, filename: String) {
    val body = wrap(title, "h2") + wrap(
      "\n<hr>\n" + wrap("Basic", "h3") + wrap(basic, "div") +
        "\n<hr>\n" + wrap("Iterables", "h3") + wrap(iterables, "div") +
        "\n<hr>\n" + wrap("Products", "h3") + wrap(products, "div") +
        "\n<hr>\n" + wrap("Hybrid", "h3") + wrap(hybrid, "div")
      , "ul")
    val html = htmlWrap(body)
    writeFile(filename, html)
  }


  def print(a: Any, dataType: String): String = {
    wrap(dataType, "h4") +
      "<code style=\"background-color:#F0FFFF;\">%s</code>" format(a.toString) +
      wrap(a.source, "div")
  }

  def basic: String = {
    val sb = new StringBuffer()
    sb append print(10, "Int")
    sb append print(10.0, "Double")
    sb append print("qwerty", "String")
    sb.toString
  }

  def iterables: String = {
    val sb = new StringBuffer()
    sb append print(List(10, 20, 30, 40), "List")
    sb append print(Seq(10, 20, 30, 40), "Seq")
    sb append print(Set(10, 20, 30, 40), "Set")
    sb append print(Map("a" -> 10, "b" -> 20, "c" -> 30, "d" -> 40), "Map")
    sb.toString
  }

  case class Company(name: String, address: String)

  case class Person(first: String, last: String, email: String, age: Int, employedBy: Company)

  def products: String = {
    val sb = new StringBuffer()
    sb append print("a" -> 100, "Pair")
    sb append print(Tuple3("a", 10, 100.0), "Tuple3")
    val c = Company("Battle School", "Seattle, WA")
    val p = Person("Andrew", "Wiggin", "ender@intfleet.com", 65, c)
    sb append print(c, "Case")
    sb append print(p, "Case (Nested)")
    sb.toString
  }

  def hybrid: String = {
    val sb = new StringBuffer()
    sb append print(List(List(10, 20, 30, 40), List(1.0, 2.0, 3.0, 4.0), List("a", "b", "c", "d")), "List of lists")
    sb append print(Set(List(10, 20, 30, 40), List(1.0, 2.0, 3.0, 4.0), List("a", "b", "c", "d")), "Set of lists")
    sb append print(List(Set(10, 20, 30, 40), Set(1.0, 2.0, 3.0, 4.0), Set("a", "b", "c", "d")), "List of sets")
    sb append print(Map(List(100, 200) -> Set("a", "b"), List(10, 20) -> Set("abraca", "dabra")), "Map of lists to sets")
    val c1 = Company("Battle School", "Seattle, WA")
    val c2 = Company("Command School", "Mountain View, CA")
    val c3 = Company("Dept of Xenobiology", "Lusitania")
    val p1 = Person("Andrew", "Wiggin", "ender@intfleet.com", 10, c2)
    val p2 = Person("Hyrum", "Graff", "graff@intfleet.com", 65, c2)
    val p3 = Person("Ivanova", "von Hesse", "novinha@xenbio.com", 25, c3)
    sb append print(List(c1, c2, c3), "List of Cases")
    sb append print(Set(p1, p2, p3), "Set of Nested Cases")
    sb append print(Map(c2 -> List(p1, p2), c3 -> List(p3)), "Map of Cases to list of Nested Cases")
    sb.toString
  }
}
