package org.sameersingh.htmlgen

import java.io.{File, PrintWriter, StringWriter}
import java.util.UUID

import org.sameersingh.htmlgen.Custom._
import org.sameersingh.scalaplot.Chart

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
    val divId = UUID.randomUUID()
    sb.println(
      s"""
         |<div id="graphDiv$divId">
                                   |</div>
                                   |
                                   |<script type=\"text/javascript\">
                                   | drawGraph(${JsonConverter.product(m).source}, "graphDiv$divId");
                                                                                                    |</script>
      """.stripMargin)
    sb.flush()
    sb.close()
    RawHTML(writer.toString)
  }


  override def vectors(v: Vectors, indentLevel: Int): HTML = {
    val t = if (v.points.head._3.length > 2) TSNE.project(v, 2) else v
    val writer = new StringWriter()
    val sb = new PrintWriter(writer)
    val divId = UUID.randomUUID()
    sb.println(
      s"""
         |<div id="vectorsDiv$divId">
                                     |</div>
                                     |
                                     |<script type=\"text/javascript\">
                                     | drawVectors(${JsonConverter.iterable(t.points).source}, "vectorsDiv$divId");
                                                                                                                  |</script>
      """.stripMargin)
    sb.flush()
    sb.close()
    RawHTML(writer.toString)
  }

  override def carousel[A](m: Carousel[A], indentLevel: Int): HTML = {
    val writer = new StringWriter()
    val sb = new PrintWriter(writer)
    val divId = UUID.randomUUID()
    sb.println(s"<div id='animation$divId'>")
    // add buttons
    sb.println(s"  <button type='button' id='animation${divId}prev' class='btn btn-default' style='width:49%;'>&lt; Previous</span></button>")
    sb.println(s"  <button type='button' id='animation${divId}next' class='btn btn-default' style='width:49%;'>Next &gt;</span></button>")
    for (index <- 0 until m.frames.size) {
      val frame = convert(m.frames(index)._2, indentLevel)
      val label = m.frames(index)._1
      sb.println(s"  <div id='frame$index' class='hide'>") //
      sb.println(s"    <h2>$label <small>$index</small></h2>")
      sb.println(frame.source)
      sb.println("  </div>")
    }
    sb.println("</div>")
    sb.println( s"""
                   |<script type=\"text/javascript\">
                   | animate("animation$divId", ${m.frames.length});
                                                                    |</script>
      """.stripMargin)
    sb.flush()
    sb.close()
    RawHTML(writer.toString)
  }

  override def matrix[M](m: Matrix[M], indentLevel: Int): HTML = DivConverter.matrix(m, indentLevel)

  override def chart(c: Chart, indentLevel: Int): HTML = DivConverter.chart(c, indentLevel)
}

object D3jsConverter extends D3jsConverter {
  def main(args: Array[String]): Unit = {
    val means = Seq(Seq(1.0, 0.0, 0.0), Seq(0.0, 1.0, 0.0), Seq(0.0, 0.0, 0.0), Seq(1.0, 1.0, 0.0))
    val vecsPerClust = 2
    val noise = 0.1
    val random = new util.Random(0)
    val vectors = Vectors(
      means.zipWithIndex.map(xi => (0 until vecsPerClust).map(j => (xi._2, xi._2 + "_" + j, xi._1.map(v => v + random.nextGaussian() * noise)))).flatten
    ).twoD
    val graph = Graph(Seq(
      Node("A", "a", group = 0, value = 0.2),
      Node("B", "a", group = 0, value = 0.4),
      Node("C", "a", group = 0, value = 0.6),
      Node("D", "a", group = 0, value = 0.8),
      Node("E", "a", group = 0, value = 1.0),
      Node("F", "f")),
      Seq(
        Edge(0, 1, "a-b"),
        Edge(2, 1, "a-b"),
        Edge(3, 1, "a-b"),
        //Edge(4,1,"a-b"),
        Edge(5, 1, "a-b"),
        Edge(0, 2, "a-b"),
        Edge(0, 3, "a-b")
      ))
    val animation = Carousel("vects" -> vectors)
    val output = new PrintWriter(new File("src/main/resources/d3js.html"))
    output.println(
      """
        |<html>
        |<head>
        |    <link rel="stylesheet" type="text/css" href="htmlgen.css"/>
        |    <link rel="stylesheet" type="text/css" href="file:///Users/sameer/Work/src/research/uclmr/moro/public/javascripts/bootstrap/css/bootstrap.min.css"/>
        |    <script type="text/javascript" src="file:///Users/sameer/Work/src/research/uclmr/moro/public/javascripts/d3.v3.min.js"></script>
        |    <script type="text/javascript" src="file:///Users/sameer/Work/src/research/uclmr/moro/public/javascripts/jquery-1.9.0.min.js"></script>
        |    <script type="text/javascript" src="d3utils.js"></script>
        |</head>
        |
        |<body>
      """.stripMargin)
    val html = convert(animation)
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