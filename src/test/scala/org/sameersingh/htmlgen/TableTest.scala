package org.sameersingh.htmlgen

import org.junit.Test

/**
 * @author sameer
 * @since 4/24/14.
 */
class TableTest extends Tester(TableConverter) {

  @Test
  def testAll() {
    this.writeHTML("Generated HTML Using Tables", "src/main/resources/tables.html")
  }

}
