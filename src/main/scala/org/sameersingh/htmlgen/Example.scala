package org.sameersingh.htmlgen

/**
 * @author sameer
 * @since 5/31/14.
 */
object Example {
  def main(args: Array[String]) {
    import org.sameersingh.htmlgen.HTML
    import org.sameersingh.htmlgen.DivConverter.Implicits._
    val html: HTML = Map(List(1,2) -> "string", Set(3,4) -> 1e5)
    println(html.source)
  }
}
