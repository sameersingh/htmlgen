package org.sameersingh.htmlgen

/**
 * @author sameer
 * @since 4/23/14.
 */
trait HTML {
  def source: String
}

case class RawHTML(source: String) extends HTML