package org.sameersingh.htmlgen

/**
 * @author sameer
 * @since 4/23/14.
 */
object Converters {

  val propertyClass = "property"
  val maxIndent = 25

  def indent(indentLevel: Int) = if (indentLevel == 0) "" else "%" + (2 * indentLevel) + "s" format (" ")

  def wrap(string: String, tag: String, tagClass: String = ""): String = {
    val classString = if (tagClass.length > 0) " class=\"%s\"" format (tagClass) else ""
    "<%s%s>%s</%s>" format(tag, classString, string, tag)
  }

  def convert(a: Any, indentLevel: Int = 0): HTML = if (maxIndent <= indentLevel) RawHTML("<code>...</code>")
  else a match {
    case m: scala.collection.Map[Any, Any] => map(m, indentLevel)
    case i: Iterable[Any] => iterable(i, indentLevel)
    case p: Product => product(p, indentLevel)
    case _ => RawHTML(indent(indentLevel) + "<code>" + a.toString + "</code>\n")
  }

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
    val cols = if(ordered) 2 else 1
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
      f.setAccessible(true)
      sb append (indent(indentLevel + 1)
        + "<tr>"
        + wrap(f.getName, "td", propertyClass)
        + wrap(convert(f.get(p), indentLevel + 2).source + indent(indentLevel + 1), "td")
        + "</tr>\n")
    }
    sb append (indent(indentLevel) + "</table>\n")
    RawHTML(sb.toString)
  }
}

object Implicits {
  implicit def anyToHTML(a: Any): HTML = Converters.convert(a)
}
