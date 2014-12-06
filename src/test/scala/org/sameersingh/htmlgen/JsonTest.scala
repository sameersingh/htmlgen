package org.sameersingh.htmlgen

import org.junit.Test

/**
  * @author sameer
 * @since 12/5/14.
 */
class JsonTest extends Tester(JsonConverter) {

  @Test
  def testAll() {
    this.writeHTML("Generated HTML Using Json", "src/main/resources/json.html")
  }

}