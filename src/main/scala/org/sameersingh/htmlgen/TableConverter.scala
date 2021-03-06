package org.sameersingh.htmlgen

import org.sameersingh.htmlgen.Custom.Matrix

/**
 * @author sameer
 * @since 4/24/14.
 */
class TableConverter extends Converter {

  import ConverterUtils._

  def string(str: String, indentLevel: Int = 0): HTML = RawHTML(indent(indentLevel) + "<code>" + str + "</code>\n")


  override def matrix[M](m: Matrix[M], indentLevel: Int): HTML = {
    val sb = new StringBuilder
    sb.append(indent(indentLevel) + "<table class=\"matrix\">\n")
    val cells = m.data.map(_.map(m extr _)).flatten
    val min = cells.min
    val max = cells.max
    val dimC = 250.0/(m.cols)
    val dimR = 250.0/(m.rows)
    def opacity(d: Double) = (d-min)/(max-min)
    sb.append(indent(indentLevel+1) + "<thead>\n")
    if(!m.colNames.isEmpty) {
      // column names
      sb.append(indent(indentLevel+1) + "<tr class=\"matrixRow\">\n")
      if(!m.rowNames.isEmpty)
        sb.append(indent(indentLevel+1) + "<th></th>\n")
      for(j <- 0 until m.cols) {
        sb.append(indent(indentLevel+2) + "<th class=\"rotate\"><div><span>%s</span></div></th>\n" format (m.colNames(j)))
      }
      sb.append(indent(indentLevel+1) + "</tr>\n")
    }
    sb.append(indent(indentLevel+1) + "</thead>\n")
    sb.append(indent(indentLevel+1) + "<tbody>\n")
    for(i <- 0 until m.rows) {
      sb.append(indent(indentLevel+1) + "<tr class=\"matrixRow\">\n") // style="width:100%%;height:%fpx" format(dimR)
      if(!m.rowNames.isEmpty)
        sb.append(indent(indentLevel+2) + "<th><div><span>%s</span></div></th>\n" format (m.rowNames(i)))
      for(j <- 0 until m.cols) {
        val o = opacity(m.cell(i,j))
        sb.append(indent(indentLevel+2) + "<td class=\"matrixCell\" style=\"opacity:%f\"/>\n" format(o)) //width:%fpx;height:100%%;

      }
      sb.append(indent(indentLevel+1) + "</tr>\n")
    }
    sb.append(indent(indentLevel+1) + "</tbody>\n")
    sb.append(indent(indentLevel) + "</table>\n")
    RawHTML(sb.mkString)
  }

  def map(m: scala.collection.Map[Any, Any], indentLevel: Int = 0, overriden: PartialFunction[Any, String] = Map.empty): HTML = {
    val sb = new StringBuffer()
    sb append (indent(indentLevel) + "<table>\n")
    sb append (wrap("<td colspan=\"2\" class=\"property\">" + m.stringPrefix + "</td>", "tr") + "\n")
    for ((k, v) <- m) {
      sb append (indent(indentLevel + 1)
        + "<tr>"
        + wrap(convert(k, indentLevel + 2, overriden).source, "td", propertyClass)
        + wrap(convert(v, indentLevel + 2, overriden).source, "td")
        + "</tr>\n")
    }
    sb append (indent(indentLevel) + "</table>\n")
    RawHTML(sb.toString)
  }

  def iterable(i: Iterable[Any], indentLevel: Int, overriden: PartialFunction[Any, String] = Map.empty): HTML = {
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
        + wrap(convert(item, indentLevel + 2, overriden).source, "td")
        + "</tr>\n")
      idx += 1
    }
    sb append (indent(indentLevel) + "</table>\n")
    RawHTML(sb.toString)
  }

  def product(p: Product, indentLevel: Int, overriden: PartialFunction[Any, String] = Map.empty): HTML = {
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
          + wrap(convert(f.get(p), indentLevel + 2, overriden).source + indent(indentLevel + 1), "td")
          + "</tr>\n")
      }
    }
    sb append (indent(indentLevel) + "</table>\n")
    RawHTML(sb.toString)
  }
}

object TableConverter extends TableConverter