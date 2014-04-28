package org.sameersingh.htmlgen

import java.io.{FileOutputStream, PrintWriter}

/**
 * @author sameer
 * @since 4/23/14.
 */
object ConverterUtils {
  val propertyClass = "property"
  val maxIndent = 25

  def indent(indentLevel: Int) = if (indentLevel == 0) "" else "%" + (2 * indentLevel) + "s" format (" ")

  def wrap(string: String, tag: String, tagClass: String = ""): String = {
    val classString = if (tagClass.length > 0) " class=\"%s\"" format (tagClass) else ""
    "<%s%s>%s</%s>" format(tag, classString, string, tag)
  }

  def htmlWrap(body: String): String = {
    wrap(wrap("<link rel=\"stylesheet\" type=\"text/css\" href=\"htmlgen.css\">", "head") + wrap(body, "body"), "html")
  }

  def link(href: String, text: String) = "<a href=\"%s\">%s</a>" format(href, text)

  def writeFile(fname: String, html: String): Unit = {
    val writer = new PrintWriter(new FileOutputStream(fname))
    writer.print(html)
    writer.flush()
    writer.close()
  }
}

object Converters {
  val default: Converter = TableConverter
}