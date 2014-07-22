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

}
