package ee.jiss.commons.lang

import org.junit.Test

import static org.assertj.core.api.Assertions.*;
import static ee.jiss.commons.lang.ExceptionUtils.findCaused

class ExceptionUtilsTest {
    @Test void "findCaused: Find in deep"() {
        // Given
        def msg = "X"
        def exp = new RuntimeException(new RuntimeException(new IllegalStateException(msg, new RuntimeException())))

        // When
        def result = findCaused(exp, IllegalStateException)

        // Then
        assertThat(result).hasMessage(msg)
        assertThat(result.class).isEqualTo(IllegalStateException)
    }

    @Test void "findCaused: Find first"() {
        // Given
        def msg = "X"
        def exp = new IllegalStateException(msg, new RuntimeException())

        // When
        def result = findCaused(exp, IllegalStateException)

        // Then
        assertThat(result).hasMessage(msg)
        assertThat(result.class).isEqualTo(IllegalStateException)
    }

    @Test void "findCaused: Find last"() {
        // Given
        def msg = "X"
        def exp = new RuntimeException(new RuntimeException(new IllegalStateException(msg)))

        // When
        def result = findCaused(exp, IllegalStateException)

        // Then
        assertThat(result).hasMessage(msg)
        assertThat(result.class).isEqualTo(IllegalStateException)
    }

    @Test void "findCaused: Missing"() {
        // Given
        def exp = new RuntimeException(new RuntimeException(new RuntimeException()))

        // When
        def result = findCaused(exp, IllegalStateException)

        // Then
        assertThat(result).isNull()
    }
}
