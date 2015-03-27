package org.sameersingh.htmlgen

import org.nd4j.linalg.api.ndarray.{BaseNDArray, INDArray}
import scala.collection.JavaConversions._

/**
 * @author sameer
 * @since 3/24/15.
 */
object TSNE {
  val maxIter: Int = 1000
  val realMin: Double = 1e-12
  val initialMomentum: Double = 0.5
  val finalMomentum: Double = 0.8
  val minGain: Double = 1e-2
  val momentum: Double = initialMomentum
  val switchMomentumIteration: Int = 100
  val normalize: Boolean = false
  val usePca: Boolean = false
  val stopLyingIteration: Int = 250
  val tolerance: Double = 1e-5
  val learningRate: Double = 500
  val useAdaGrad: Boolean = true
  val perplexity: Double = 30

  def project(v: Custom.Vectors, dim: Int = 2): Custom.Vectors = {
    val tsne = new org.deeplearning4j.plot.Tsne(maxIter, realMin, initialMomentum,
      finalMomentum, momentum, switchMomentumIteration, normalize, usePca, stopLyingIteration,
      tolerance, learningRate, useAdaGrad, perplexity, minGain)
    val mat = TSNE.vectToMat(v)
    val res = tsne.calculate(mat, dim, perplexity)
    val rv = TSNE.matToVect(res, v)
    removeTemp()
    rv.norm
  }

  def vectToMat(v: Custom.Vectors): INDArray = {
    val arr = v.norm.points.map(_._3.toArray).toArray
    val m = new BaseNDArray(arr) {}
    m
  }

  def matToVect(mat: INDArray, v: Custom.Vectors): Custom.Vectors = {
    assert(mat.isMatrix, "TSNE only supports dimensions of 2 right now.")
    Custom.Vectors(v.points.zipWithIndex.map(pi => {
      val vect1 = mat.getRow(pi._2)
      val seq = (0 until vect1.length()).map(i => vect1.getDouble(i))
      (pi._1._1, pi._1._2, seq)
    }))
  }

  def norm(v: Custom.Vectors): Custom.Vectors = {
    val maxs = (0 until v.points.head._3.length).map(i => v.points.map(_._3(i)).max)
    val mins = (0 until v.points.head._3.length).map(i => v.points.map(_._3(i)).min)
    Custom.Vectors(v.points.map(p => (p._1, p._2, p._3.zipWithIndex.map(pi => {
      val min = mins(pi._2)
      val max = maxs(pi._2)
      val x = pi._1
      ((x - min) / (max - min))
    }))))
  }

  def removeTemp(): Unit = {
    val files = new java.io.File(".").listFiles().filter(f => f.isFile && f.getName.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"))
    for(f <- files) {
      f.deleteOnExit()
    }
  }

  def main(args: Array[String]): Unit = {
    val o = Custom.Vectors(Seq((0, "", Seq(1.0, 1.0)), (0, "", Seq(2.0, 2.0)), (0, "", Seq(3.0, 3.0))))
    println(o)
    println(TSNE.project(o, 2))
  }
}