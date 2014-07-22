package org.sameersingh.htmlgen

import org.sameersingh.htmlgen.Custom.Matrix

/**
 * @author sameer
 * @since 4/24/14.
 */
class ListConverter extends Converter {

  import ConverterUtils._

  def string(str: String, indentLevel: Int = 0): HTML = RawHTML(indent(indentLevel) + "<code>" + str + "</code>\n")

  override def matrix[M](m: Matrix[M], indentLevel: Int): HTML = {
    val sb = new StringBuilder
    sb.append(indent(indentLevel) + "<ul class=\"matrix\">\n")
    for(i <- 0 until m.rows) {
      sb.append(indent(indentLevel+1) + "<ul class=\"row\">\n")
      for(j <- 0 until m.cols) {
        sb.append(indent(indentLevel+2) + "<li class=\"cell\">%f</li>\n" format(m.cell(i,j)))
      }
      sb.append(indent(indentLevel+1) + "</ul>\n")
    }
    sb.append(indent(indentLevel) + "</ul>\n")
    RawHTML(sb.mkString)
  }

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