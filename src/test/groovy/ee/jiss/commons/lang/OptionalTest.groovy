package ee.jiss.commons.lang

import org.junit.Test

import static ee.jiss.commons.lang.Optional.opt
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

class OptionalTest {
    @Test void "test IsPresent on null"() {
        assertFalse("Optional is not present", opt(null).isPresent())
    }

    @Test void "test IsPresent on object"() {
        assertTrue("Optional is not present", opt("").isPresent())
    }
}
