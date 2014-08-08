package org.sameersingh.htmlgen

import scala.collection.Map
import org.sameersingh.scalaplot.Chart
import org.sameersingh.htmlgen.Custom.Matrix

/**
 * @author sameer
 * @since 4/27/14.
 */
class DivConverter extends Converter {

  import ConverterUtils._

  // class specifying the exact type of the variable
  val typeName = "typeName"
  // class specifying which converter we are using
  val stringClass = "asString"
  val iterableClass = "asIterable"
  val productClass = "asProduct"
  val mapClass = "asMap"
  // divide into fields and values
  val fields = "fields"
  val field = "field"
  val fieldName = "fieldName"
  val fieldValue = "fieldValue"

  override def matrix[M](m: Matrix[M], indentLevel: Int): HTML = {
    val sb = new StringBuilder
    sb.append(indent(indentLevel) + "<div class=\"matrix\">\n")
    val cells = m.data.map(_.map(m extr _)).flatten
    val min = cells.min
    val max = cells.max
    val dimC = 100.0/(m.cols)
    val dimR = 100.0/(m.rows)
    def opacity(d: Double) = (d-min)/(max-min)
    for(i <- 0 until m.rows) {
      sb.append(indent(indentLevel+1) + "<div class=\"matrixRow\" style=\"width:100%%;height:%f%%\">\n" format(dimR))
      for(j <- 0 until m.cols) {
        val c = opacity(m.cell(i,j))
        sb.append(indent(indentLevel+2) + "<div class=\"matrixCellContainer\" style=\"width:%f%%;height:100%%\">\n" format( dimC))
        sb.append(indent(indentLevel+3) + "<div class=\"matrixCell\" style=\"opacity:%f;\"></div>\n" format(c))
        sb.append(indent(indentLevel+2) + "</div>\n" format(c, dimC))
      }
      sb.append(indent(indentLevel+1) + "</div>\n")
    }
    sb.append(indent(indentLevel) + "</div>\n")
    RawHTML(sb.mkString)
  }

  override def chart(c: Chart, indentLevel: Int): HTML = {
    import org.sameersingh.scalaplot.gnuplot.GnuplotPlotter
    val file = java.io.File.createTempFile("moro-plots", System.currentTimeMillis().toString)
    file.delete()
    file.mkdir()
    println(file.getCanonicalPath)
    // new JFGraphPlotter(chart).writeToPdf(file)
    val gpl = new GnuplotPlotter(c)
    RawHTML(gpl.svg(file.getCanonicalPath + "/", "plot") + "<a href=\"%s\">Source</a>" format(file.getCanonicalPath + "/plot.gpl"))
  }

  override def iterable(i: Iterable[Any], indentLevel: Int, overriden: PartialFunction[Any, String] = Map.empty): HTML = {
    val ordered = (i.isInstanceOf[Seq[Any]])
    val listType = if (ordered) "ol" else "ul"
    RawHTML(
      wrap({
        val sb = new StringBuffer()
        sb append (wrap(i.stringPrefix, "span", typeName) + "\n")
        sb append (indent(indentLevel) + "<" + listType + " start=\"0\" class=\"" + fields + "\">\n")
        var idx = 0
        for (item <- i) {
          sb append (indent(indentLevel + 1)
            + wrap(convert(item, indentLevel + 2, overriden).source, "li", fieldValue) + "\n")
          idx += 1
        }
        sb append (indent(indentLevel) + "</" + listType + ">\n")
        sb.toString
      }, "div", iterableClass + " " + i.stringPrefix))
  }

  def product(p: Product, indentLevel: Int, overriden: PartialFunction[Any, String] = Map.empty): HTML = {
    RawHTML(
      wrap({
        val sb = new StringBuffer()
        sb append (wrap(p.productPrefix, "span", typeName) + "\n")
        val pfields = p.getClass.getDeclaredFields
        sb append (indent(indentLevel) + "<ul class=\"" + fields + "\">\n")
        for (f <- pfields; if (!f.isSynthetic)) {
          f.setAccessible(true)
          sb append (indent(indentLevel + 1)
            + "<li class=\"" + field + "\">"
            + wrap(f.getName, "span", fieldName)
            + " "
            + wrap(convert(f.get(p), indentLevel + 2, overriden).source, "span", fieldValue)
            + "</li>\n")
        }
        sb append (indent(indentLevel) + "</ul>\n")
        sb.toString
      }, "div", productClass + " " + p.productPrefix))
  }

  def map(m: scala.collection.Map[Any, Any], indentLevel: Int = 0, overriden: PartialFunction[Any, String] = Map.empty): HTML = {
    RawHTML(wrap({
      val sb = new StringBuffer()
      sb append (wrap(m.stringPrefix, "span", typeName) + "\n")
      sb append (indent(indentLevel) + "<ul class=\"" + fields + "\">\n")
      for ((k, v) <- m) {
        sb append (indent(indentLevel + 1)
          + "<li class=\"" + field + "\">"
          + wrap(convert(k, indentLevel + 2, overriden).source, "span", fieldName)
          + " " //" &#8594; "
          + wrap(convert(v, indentLevel + 2, overriden).source, "span", fieldValue)
          + "</li>\n")
      }
      sb append (indent(indentLevel) + "</ul>\n")
      sb.toString
    }, "div", mapClass + " " + m.stringPrefix))
  }

  override def string(a: String, indentLevel: Int): HTML = RawHTML(wrap(a, "span", stringClass + " " + className(a)))

  def className(a: Any) = a match {
    case p: Product => p.productPrefix
    case i: Iterable[Any] => i.stringPrefix
    case _ => a.getClass.getSimpleName
  }
}

object DivConverter extends DivConverter