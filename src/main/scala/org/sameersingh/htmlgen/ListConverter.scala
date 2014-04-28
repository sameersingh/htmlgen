package org.sameersingh.htmlgen

/**
 * @author sameer
 * @since 4/24/14.
 */
class ListConverter extends Converter {

  import ConverterUtils._

  def string(str: String, indentLevel: Int = 0): HTML = RawHTML(indent(indentLevel) + "<code>" + str + "</code>\n")

  def map(m: scala.collection.Map[Any, Any], indentLevel: Int = 0, overriden: PartialFunction[Any, String] = Map.empty): HTML = {
    val sb = new StringBuffer()
    sb append (wrap(m.stringPrefix, "span", propertyClass) + "\n")
    sb append (indent(indentLevel) + "<ul>\n")
    for ((k, v) <- m) {
      sb append (indent(indentLevel + 1)
        + "<li>"
        + wrap(convert(k, indentLevel + 2, overriden).source, "span", propertyClass)
        + " &#8594; "
        + wrap(convert(v, indentLevel + 2, overriden).source, "span")
        + "</li>\n")
    }
    sb append (indent(indentLevel) + "</ul>\n")
    RawHTML(sb.toString)
  }

  def iterable(i: Iterable[Any], indentLevel: Int, overriden: PartialFunction[Any, String] = Map.empty): HTML = {
    val ordered = (i.isInstanceOf[Seq[Any]])
    val listType = if (ordered) "ol" else "ul"
    val sb = new StringBuffer()
    sb append (wrap(i.stringPrefix, "span", propertyClass) + "\n")
    sb append (indent(indentLevel) + "<" + listType + " start=\"0\">\n")
    var idx = 0
    for (item <- i) {
      sb append (indent(indentLevel + 1)
        + wrap(convert(item, indentLevel + 2, overriden).source, "li"))
      idx += 1
    }
    sb append (indent(indentLevel) + "</" + listType + ">\n")
    RawHTML(sb.toString)
  }

  def product(p: Product, indentLevel: Int, overriden: PartialFunction[Any, String] = Map.empty): HTML = {
    val sb = new StringBuffer()
    sb append wrap(p.productPrefix, "span", propertyClass)
    val fields = p.getClass.getDeclaredFields
    sb append (indent(indentLevel) + "<ul>\n")
    for (f <- fields; if(!f.isSynthetic)) {
      f.setAccessible(true)
      sb append (indent(indentLevel + 1)
        + "<li>"
        + wrap(f.getName, "span", propertyClass) + ": "
        + convert(f.get(p), indentLevel + 2, overriden).source
        + "</li>\n")
    }
    sb append (indent(indentLevel) + "</ul>\n")
    RawHTML(sb.toString)
  }
}

object ListConverter extends ListConverter