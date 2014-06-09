package org.sameersingh.htmlgen

import scala.collection
import org.sameersingh.scalaplot.XYChart

/**
 * @author sameer
 * @since 4/24/14.
 */

trait Converter {

  import ConverterUtils._

  def convert(a: Any, indentLevel: Int = 0, overriden: PartialFunction[Any, String] = Map.empty): HTML =
    if (maxIndent <= indentLevel) string("...")
    else if (overriden.isDefinedAt(a)) RawHTML(overriden(a))
    else a match {
      case m: scala.collection.Map[Any, Any] => map(m, indentLevel, overriden)
      case i: Iterable[Any] => iterable(i, indentLevel, overriden)
      case p: Product => product(p, indentLevel, overriden)
      case c: XYChart => chart(c, indentLevel)
      case _ => string(a.toString, indentLevel)
    }

  def chart(c: XYChart, indentLevel: Int = 0): HTML = string("Plots not supported.")

  def string(a: String, indentLevel: Int = 0): HTML

  def map(a: scala.collection.Map[Any, Any], indentLevel: Int = 0, overriden: PartialFunction[Any, String] = Map.empty): HTML

  def product(a: Product, indentLevel: Int = 0, overriden: PartialFunction[Any, String] = Map.empty): HTML

  def iterable(a: Iterable[Any], indentLevel: Int = 0, overriden: PartialFunction[Any, String] = Map.empty): HTML

  object Implicits {
    implicit def anyToConvertable(a: Any): Convertable = new Convertable(a, Converter.this)

    implicit def anyToHTML(a: Any): HTML = convert(a)
  }

}

object StringConverter extends Converter {
  override def iterable(a: Iterable[Any], indentLevel: Int, overriden: PartialFunction[Any, String] = Map.empty): HTML = RawHTML(a.toString)

  override def product(a: Product, indentLevel: Int, overriden: PartialFunction[Any, String] = Map.empty): HTML = RawHTML(a.toString)

  override def map(a: collection.Map[Any, Any], indentLevel: Int, overriden: PartialFunction[Any, String] = Map.empty): HTML = RawHTML(a.toString)

  override def string(a: String, indentLevel: Int): HTML = RawHTML(a.toString)
}

class Convertable(a: Any, c: Converter) {
  def asHTML: HTML = c.convert(a)

  def asHTML(overriden: PartialFunction[Any, String]): HTML = c.convert(a, overriden = overriden)

  def asTable: HTML = TableConverter.convert(a)

  def asTable(overriden: PartialFunction[Any, String]): HTML = TableConverter.convert(a, overriden = overriden)

  def asList: HTML = ListConverter.convert(a)

  def asList(overriden: PartialFunction[Any, String]): HTML = ListConverter.convert(a, overriden = overriden)

  def asString: HTML = StringConverter.convert(a)

  def asString(overriden: PartialFunction[Any, String]): HTML = StringConverter.convert(a, overriden = overriden)

  def asDivs: HTML = DivConverter.convert(a)

  def asDivs(overriden: PartialFunction[Any, String]): HTML = DivConverter.convert(a, overriden = overriden)
}