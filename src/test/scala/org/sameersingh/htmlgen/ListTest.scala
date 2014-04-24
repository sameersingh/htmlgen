package org.sameersingh.htmlgen

import org.junit.Test

/**
 * @author sameer
 * @since 4/24/14.
 */
class ListTest extends Tester(ListConverter) {

  @Test
  def testAll() {
    this.writeHTML("Generated HTML Using Lists", "src/main/resources/lists.html")
  }

}

