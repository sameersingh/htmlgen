package org.sameersingh.htmlgen

/**
 * Custom classes for specific outputs
 * @author sameer
 * @since 7/22/14.
 */
object Custom {

  case class Matrix[A](data: Seq[Seq[A]])(implicit val extr: A => Double) {
    assert(data.length >= 1)
    assert(data.forall(_.length == data(0).length))
    val rows = data.length
    val cols = data(0).length

    def cell(i: Int, j: Int): Double = extr(data(i)(j))

    override def toString = s"Matrix: $rows x $cols"
  }

  case class Node[A](name: String, description: A, value: Double = 1.0, group: Int = 0)
  case class Edge[B](source: Int, target: Int, description: B, value: Double = 1.0, group: Int = 0)
  case class Graph[A,B](nodes: Seq[Node[A]], edges: Seq[Edge[B]], directed: Boolean = false)

  object Graph {
    def simple(nodes: Seq[String], edges: Seq[(Int, Int)]): Graph[String, (Int,Int)] =
      Graph(nodes.map(n => Node(n, n)), edges.map(e => Edge(e._1, e._2, e._1 -> e._2)))
    def nodeDesc[A](nodes: Seq[(String, A)], edges: Seq[(Int, Int)]): Graph[A, (Int,Int)] =
      Graph(nodes.map(na => Node(na._1, na._2)), edges.map(e => Edge(e._1, e._2, e._1 -> e._2)))
    def apply[A,B](nodes: Seq[(String, A)], edges: Seq[(Int, Int, B)]): Graph[A, B] =
      Graph(nodes.map(na => Node(na._1, na._2)), edges.map(e => Edge(e._1, e._2, e._3)))

    def fromPreds[A](nodes: Seq[A],
                     edges: (A,A)=>Boolean,
                     labels: A => String = (x:A) => x.toString,
                     description: A => Any = (x:A) => x,
                     groups: A => Int = (x:A) => 0,
                     values: A => Double = (x:A) => 1.0,
                     edgeValues: (A,A)=> Double = (a1:A,a2:A)=>1.0): Graph[Any, String] =
      Graph(nodes.map(n => Node(labels(n), description(n), values(n), groups(n))),
        (for(i <- (0 until nodes.length); j <- (0 until nodes.length); if i<j && edges(nodes(i), nodes(j))) yield Edge(i, j, "")).toSeq)
  }

  case class Carousel[A](frames: (String, A)*)
  object Carousel {
    def items[A](as: A*): Carousel[A] = Carousel(as.map("" -> _):_*)
  }
}
