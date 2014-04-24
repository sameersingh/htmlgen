package org.sameersingh.htmlgen

import org.junit.Test

/**
 * @author sameer
 * @since 4/24/14.
 */
class StringTest extends Tester(StringConverter) {

  @Test
  def testAll() {
    this.writeHTML("Generated HTML Using Strings", "src/main/resources/strings.html")
  }

}

