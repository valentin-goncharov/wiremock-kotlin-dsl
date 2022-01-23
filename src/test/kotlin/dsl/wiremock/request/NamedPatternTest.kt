package dsl.wiremock.request

import com.github.tomakehurst.wiremock.matching.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.function.Consumer

internal class NamedPatternTest {
    @Test
    fun `constructor creates AnythingPattern by default`() {
        val pattern = NamedPattern(defaultScope,"name")
        assertThat(pattern.name).isEqualTo("name")
        assertThat(pattern.pattern).isInstanceOf(AnythingPattern::class.java)
    }

    @Test
    fun `equalTo should set pattern to EqualToPattern`() {
        val pattern = NamedPattern(defaultScope,"name")

        pattern equalTo "value"

        assertThat(pattern.pattern).isInstanceOf(EqualToPattern::class.java)
        assertThat(pattern.pattern.expected).isEqualTo("value")
    }

    @Test
    fun `matches should set pattern to RegexPattern`() {
        val pattern = NamedPattern(defaultScope,"name")

        pattern matches  ".*value.*"

        assertThat(pattern.pattern).isInstanceOf(RegexPattern::class.java)
        assertThat(pattern.pattern.expected).isEqualTo(".*value.*")
    }

    @Test
    fun `doesNotMatch should set pattern to NegativeRegexPattern`() {
        val pattern = NamedPattern(defaultScope,"name")

        pattern doesNotMatch ".*value.*"

        assertThat(pattern.pattern).isInstanceOf(NegativeRegexPattern::class.java)
        assertThat(pattern.pattern.expected).isEqualTo(".*value.*")
    }

    @Test
    fun `contains should set pattern to ContainsPattern`() {
        val pattern = NamedPattern(defaultScope,"name")

        pattern contains "value"

        assertThat(pattern.pattern).isInstanceOf(ContainsPattern::class.java)
        assertThat(pattern.pattern.expected).isEqualTo("value")
    }

    @Test
    fun `before should set pattern to BeforeDateTimePattern`() {
        val pattern = NamedPattern(defaultScope,"name")

        pattern before "2022-01-01 00:00:00"

        assertThat(pattern.pattern).isInstanceOf(BeforeDateTimePattern::class.java)
        assertThat(pattern.pattern.expected).isEqualTo("2022-01-01 00:00:00")
    }

    @Test
    fun `after should set pattern to AfterDateTimePattern`() {
        val pattern = NamedPattern(defaultScope,"name")

        pattern after "2022-01-01 00:00:00"

        assertThat(pattern.pattern).isInstanceOf(AfterDateTimePattern::class.java)
        assertThat(pattern.pattern.expected).isEqualTo("2022-01-01 00:00:00")
    }

    @Test
    fun `dateTime should set pattern to EqualToDateTimePattern`() {
        val pattern = NamedPattern(defaultScope,"name")

        pattern dateTime  "2022-01-01 00:00:00"

        assertThat(pattern.pattern).isInstanceOf(EqualToDateTimePattern::class.java)
        assertThat(pattern.pattern.expected).isEqualTo("2022-01-01 00:00:00")
    }

    @Test
    fun `or should set pattern to LogicalOr`() {
        val pattern = NamedPattern(defaultScope,"name")

        pattern equalTo "test" or "name" matches ".*es.*"

        assertThat(pattern.pattern).isInstanceOf(LogicalOr::class.java)
        assertThat(pattern.pattern).satisfies(Consumer {
            val orPattern  = it as LogicalOr
            assertThat(orPattern.expected).isEqualTo("equalTo test OR matches .*es.*")
        })
    }

    @Test
    fun `or with NamedPattern should set pattern to LogicalOr`() {
        val pattern = NamedPattern(defaultScope,"name")

        pattern equalTo "test" or ("name" matches ".*es.*" and "name" doesNotMatch ".*ff.*")

        assertThat(pattern.pattern).isInstanceOf(LogicalOr::class.java)
        assertThat(pattern.pattern).satisfies(Consumer {
            val orPattern  = it as LogicalOr
            assertThat(orPattern.expected).isEqualTo("equalTo test OR and matches .*es.* AND doesNotMatch .*ff.*")
        })
    }

    @Test
    fun `and should set pattern to LogicalAnd`() {
        val pattern = NamedPattern(defaultScope,"name")

        pattern equalTo "test" and "name" matches ".*es.*"

        assertThat(pattern.pattern).isInstanceOf(LogicalAnd::class.java)
        assertThat(pattern.pattern).satisfies(Consumer {
            val andPattern  = it as LogicalAnd
            assertThat(andPattern.expected).isEqualTo("equalTo test AND matches .*es.*")
        })
    }

    @Test
    fun `and with NamedPattern should set pattern to LogicalAnd`() {
        val pattern = NamedPattern(defaultScope,"name")

        pattern equalTo "test" and ("name" matches ".*es.*" or "name" doesNotMatch ".*ff.*")

        assertThat(pattern.pattern).isInstanceOf(LogicalAnd::class.java)
        assertThat(pattern.pattern).satisfies(Consumer {
            val orPattern  = it as LogicalAnd
            assertThat(orPattern.expected).isEqualTo("equalTo test AND or matches .*es.* OR doesNotMatch .*ff.*")
        })
    }

    @Test
    fun `or should fail when name is different`() {
        val pattern = NamedPattern(defaultScope,"name")

        assertThatThrownBy { pattern equalTo "test" or "surname" matches ".*es.*" }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Names should be equal")
    }

    @Test
    fun `and should fail when name is different`() {
        val pattern = NamedPattern(defaultScope,"name")

        assertThatThrownBy { pattern equalTo "test" and "surname" matches ".*es.*" }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Names should be equal")
    }
}