package org.sameersingh.htmlgen

import java.io.{File, PrintWriter, StringWriter}

import org.sameersingh.htmlgen.Custom.{Edge, Node, Graph}

import scala.collection.Map
import scala.util.Random

/**
 * @author sameer
 * @since 12/5/14.
 */
class D3jsConverter extends Converter {
  val random = new Random()
  override def string(a: String, indentLevel: Int): HTML = DivConverter.string(a, indentLevel)

  override def iterable(a: Iterable[Any], indentLevel: Int, overriden: PartialFunction[Any, String]): HTML = DivConverter.iterable(a, indentLevel)

  override def product(a: Product, indentLevel: Int, overriden: PartialFunction[Any, String]): HTML = DivConverter.product(a, indentLevel)

  override def map(a: Map[Any, Any], indentLevel: Int, overriden: PartialFunction[Any, String]): HTML = DivConverter.map(a, indentLevel)

  override def graph[A, B](m: Graph[A, B], indentLevel: Int): HTML = {
    val writer = new StringWriter()
    val sb = new PrintWriter(writer)
    val divId = random.nextInt(10000)
    sb.println(
      s"""
        |<div id="graphDiv$divId">
        |</div>
        |
        |<script src="d3utils.js"></script>
        |<script type=\"text/javascript\">
        |  var graph = ${JsonConverter.product(m).source};
        |  drawGraph(graph, "graphDraw$divId");
        |</script>
      """.stripMargin)
    sb.flush()
    sb.close()
    println("Writing: " + writer.toString)
    RawHTML(writer.toString)
  }
}

object D3jsConverter extends D3jsConverter {
  def main(args: Array[String]): Unit = {
    val output = new PrintWriter(new File("src/main/resources/d3js.html"))
    output.println(
      """
        |<html>
        |<head>
        |    <script type="text/javascript" src="file:///Users/sameer/Work/src/research/wolfe/wolfenstein/public/javascripts/d3.v3.min.js"></script>
        |</head>
        |
        |<body>
      """.stripMargin)
    val graph = Graph(Seq(
      Node("A", "a", group=1),
      Node("B", "a", group=2),
      Node("C", "a", group=3),
      Node("D", "a", group=4),
      Node("E", "a", group=5),
      Node("F", "f")),
      Seq(
        Edge(0,1,"a-b"),
        Edge(2,1,"a-b"),
        Edge(3,1,"a-b"),
        //Edge(4,1,"a-b"),
        Edge(5,1,"a-b"),
        Edge(0,2,"a-b"),
        Edge(0,3,"a-b")
      ))
    val html = convert(graph)
    output.println(html.source)
    output.println(
      """
        |</body>
        |</html>
      """.stripMargin)
    output.flush()
    output.close()
  }
}