package org.sameersingh.htmlgen

import scala.collection
import org.sameersingh.scalaplot.Chart
import org.sameersingh.htmlgen.Custom.{Vectors, Carousel, Graph, Matrix}

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
      case html: RawHTML => html
      case a: Carousel[_] => carousel(a, indentLevel)
      case c: Chart => chart(c, indentLevel)
      case mat: Matrix[_] => matrix(mat)
      case g: Graph[_,_] => graph(g)
      case v: Vectors => vectors(v)
      case m: scala.collection.Map[Any, Any] => map(m, indentLevel, overriden)
      case i: Iterable[Any] => iterable(i, indentLevel, overriden)
      case p: Product => product(p, indentLevel, overriden)
      case i: Int => int(i, indentLevel)
      case d: Double => double(d, indentLevel)
      case null => string("null", indentLevel)
      case _ => string(a.toString, indentLevel)
    }

  def int(a: Int, indentLevel: Int = 0): HTML = string(a.toString, indentLevel)

  def double(a: Double, indentLevel: Int = 0): HTML = string(a.toString, indentLevel)

  def carousel[M](anim: Carousel[M], indentLevel: Int = 0): HTML = string("Animations not supported.")

  def graph[A,B](m: Graph[A,B], indentLevel: Int = 0): HTML = string("Graph not supported.")

  def matrix[M](m: Matrix[M], indentLevel: Int = 0): HTML = string("Matrix not supported.")

  def vectors(v: Vectors, indentLevel: Int = 0): HTML = string("Vectors not supported.")

  def chart(c: Chart, indentLevel: Int = 0): HTML = string("Charts not supported.")

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
  import ConverterUtils._

  override def matrix[M](m: Matrix[M], indentLevel: Int): HTML = RawHTML(wrap(m.data.map(_.map(d => m.extr(d)).mkString("\t")).mkString("\n"), "pre"))

  override def chart(c: Chart, indentLevel: Int): HTML = super.chart(c, indentLevel)

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

  def asD3: HTML = D3jsConverter.convert(a)
}