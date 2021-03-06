package dsl.wiremock.request

import com.github.tomakehurst.wiremock.matching.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class NamedPatternExtensionsTest {

    @Test
    fun `equalTo should set pattern to EqualToPattern`() {
        val pattern = "name" equalTo "value"

        Assertions.assertThat(pattern.pattern).isInstanceOf(EqualToPattern::class.java)
        Assertions.assertThat(pattern.pattern.expected).isEqualTo("value")
    }

    @Test
    fun `matches should set pattern to RegexPattern`() {
        val pattern = "name" matches  ".*value.*"

        Assertions.assertThat(pattern.pattern).isInstanceOf(RegexPattern::class.java)
        Assertions.assertThat(pattern.pattern.expected).isEqualTo(".*value.*")
    }

    @Test
    fun `doesNotMatch should set pattern to NegativeRegexPattern`() {
        val pattern = "name" doesNotMatch ".*value.*"

        Assertions.assertThat(pattern.pattern).isInstanceOf(NegativeRegexPattern::class.java)
        Assertions.assertThat(pattern.pattern.expected).isEqualTo(".*value.*")
    }

    @Test
    fun `contains should set pattern to ContainsPattern`() {
        val pattern = "name" contains "value"

        Assertions.assertThat(pattern.pattern).isInstanceOf(ContainsPattern::class.java)
        Assertions.assertThat(pattern.pattern.expected).isEqualTo("value")
    }

    @Test
    fun `before should set pattern to BeforeDateTimePattern`() {
        val pattern = "name" before "2022-01-01 00:00:00"

        Assertions.assertThat(pattern.pattern).isInstanceOf(BeforeDateTimePattern::class.java)
        Assertions.assertThat(pattern.pattern.expected).isEqualTo("2022-01-01 00:00:00")
    }

    @Test
    fun `after should set pattern to AfterDateTimePattern`() {
        val pattern = "name" after "2022-01-01 00:00:00"

        Assertions.assertThat(pattern.pattern).isInstanceOf(AfterDateTimePattern::class.java)
        Assertions.assertThat(pattern.pattern.expected).isEqualTo("2022-01-01 00:00:00")
    }

    @Test
    fun `dateTime should set pattern to EqualToDateTimePattern`() {
        val pattern = "name" dateTime "2022-01-01 00:00:00"

        Assertions.assertThat(pattern.pattern).isInstanceOf(EqualToDateTimePattern::class.java)
        Assertions.assertThat(pattern.pattern.expected).isEqualTo("2022-01-01 00:00:00")
    }
}