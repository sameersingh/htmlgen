package org.sameersingh.htmlgen

import org.junit.Test

/**
 * @author sameer
 * @since 4/27/14.
 */
class DivTest extends Tester(DivConverter) {

  @Test
  def testAll() {
    this.writeHTML("Generated HTML Using Divs", "src/main/resources/divs.html")
  }

}