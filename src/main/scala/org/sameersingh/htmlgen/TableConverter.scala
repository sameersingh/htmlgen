package org.sameersingh.htmlgen

/**
 * @author sameer
 * @since 4/24/14.
 */
class TableConverter extends Converter {

  import ConverterUtils._

  def string(str: String, indentLevel: Int = 0): HTML = RawHTML(indent(indentLevel) + "<code>" + str + "</code>\n")

  def map(m: scala.collection.Map[Any, Any], indentLevel: Int = 0): HTML = {
    val sb = new StringBuffer()
    sb append (indent(indentLevel) + "<table>\n")
    sb append (wrap("<td colspan=\"2\" class=\"property\">" + m.stringPrefix + "</td>", "tr") + "\n")
    for ((k, v) <- m) {
      sb append (indent(indentLevel + 1)
        + "<tr>"
        + wrap(convert(k, indentLevel + 2).source, "td", propertyClass)
        + wrap(convert(v, indentLevel + 2).source, "td")
        + "</tr>\n")
    }
    sb append (indent(indentLevel) + "</table>\n")
    RawHTML(sb.toString)
  }

  def iterable(i: Iterable[Any], indentLevel: Int): HTML = {
    val ordered = (i.isInstanceOf[Seq[Any]])
    val cols = if (ordered) 2 else 1
    val sb = new StringBuffer()
    sb append (indent(indentLevel) + "<table>\n")
    sb append (wrap("<td colspan=\"" + cols + "\" class=\"property\">" + i.stringPrefix + "</td>", "tr") + "\n")
    var idx = 0
    for (item <- i) {
      sb append (indent(indentLevel + 1)
        + "<tr>"
        + (if (ordered) wrap(convert("[" + idx + "]", indentLevel + 2).source, "td", propertyClass) else "")
        + wrap(convert(item, indentLevel + 2).source, "td")
        + "</tr>\n")
      idx += 1
    }
    sb append (indent(indentLevel) + "</table>\n")
    RawHTML(sb.toString)
  }

  def product(p: Product, indentLevel: Int): HTML = {
    val sb = new StringBuffer()
    val fields = p.getClass.getDeclaredFields
    sb append (indent(indentLevel) + "<table>\n")
    sb append (wrap("<td colspan=\"2\" class=\"property\">" + p.productPrefix + "</td>", "tr") + "\n")
    for (f <- fields) {
      if(!f.isSynthetic) {
        f.setAccessible(true)
        sb append (indent(indentLevel + 1)
          + "<tr>"
          + wrap(f.getName, "td", propertyClass)
          + wrap(convert(f.get(p), indentLevel + 2).source + indent(indentLevel + 1), "td")
          + "</tr>\n")
      }
    }
    sb append (indent(indentLevel) + "</table>\n")
    RawHTML(sb.toString)
  }
}

object TableConverter extends TableConverter