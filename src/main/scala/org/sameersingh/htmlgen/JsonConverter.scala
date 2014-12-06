package org.sameersingh.htmlgen

import scala.collection.Map
import play.api.libs.json._

/**
 * @author sameer
 * @since 12/5/14.
 */
class JsonConverter extends Converter {

  def htmlToJson(html: HTML) = Json.parse(html.source)

  def jsonToHtml(json: JsValue) = RawHTML(Json.stringify(json))

  override def int(a: Int, indentLevel: Int): HTML = jsonToHtml(JsNumber(a))

  override def double(a: Double, indentLevel: Int): HTML = jsonToHtml(JsNumber(a))

  override def string(a: String, indentLevel: Int): HTML = RawHTML(Json.prettyPrint(Json.toJson(a)))

  override def map(a: Map[Any, Any], indentLevel: Int, overriden: PartialFunction[Any, String]): HTML = {
    val json = JsArray(a.map(p => {
      val k = htmlToJson(convert(p._1, indentLevel + 1, overriden))
      val v = htmlToJson(convert(p._2, indentLevel + 1, overriden))
      JsObject(Seq("key" -> k, "value" -> v))
    }).toSeq)
    jsonToHtml(json)
  }

  override def product(a: Product, indentLevel: Int, overriden: PartialFunction[Any, String]): HTML = {
    val pfields = a.getClass.getDeclaredFields
    val json = JsObject(pfields.filterNot(_.isSynthetic).map(f => {
      f.setAccessible(true)
      f.getName -> htmlToJson(convert(f.get(a)))
    }).toSeq)
    jsonToHtml(json)
  }

  override def iterable(a: Iterable[Any], indentLevel: Int, overriden: PartialFunction[Any, String]): HTML = {
    val json = JsArray(a.map(p => {
      val v = htmlToJson(convert(p, indentLevel + 1, overriden))
      v
    }).toSeq)
    jsonToHtml(json)
  }
}

object JsonConverter extends JsonConverter {
  def main(args: Array[String]): Unit = {

  }
}