package dsl.wiremock.request

import com.github.tomakehurst.wiremock.matching.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class HeaderTest {
    @Test
    fun `constructor creates AnythingPattern by default`() {
        val pattern = Header("name")
        assertThat(pattern.name).isEqualTo("name")
        assertThat(pattern.pattern).isInstanceOf(AnythingPattern::class.java)
    }

    @Test
    fun `equalTo should set pattern to EqualToPattern`() {
        val pattern = Header("name")

        pattern equalTo "value"

        assertThat(pattern.pattern).isInstanceOf(EqualToPattern::class.java)
        assertThat(pattern.pattern.expected).isEqualTo("value")
    }

    @Test
    fun `matches should set pattern to RegexPattern`() {
        val pattern = Header("name")

        pattern matches  ".*value.*"

        assertThat(pattern.pattern).isInstanceOf(RegexPattern::class.java)
        assertThat(pattern.pattern.expected).isEqualTo(".*value.*")
    }

    @Test
    fun `doesNotMatch should set pattern to NegativeRegexPattern`() {
        val pattern = Header("name")

        pattern doesNotMatch ".*value.*"

        assertThat(pattern.pattern).isInstanceOf(NegativeRegexPattern::class.java)
        assertThat(pattern.pattern.expected).isEqualTo(".*value.*")
    }

    @Test
    fun `contains should set pattern to ContainsPattern`() {
        val pattern = Header("name")

        pattern contains "value"

        assertThat(pattern.pattern).isInstanceOf(ContainsPattern::class.java)
        assertThat(pattern.pattern.expected).isEqualTo("value")
    }

    @Test
    fun `before should set pattern to BeforeDateTimePattern`() {
        val pattern = Header("name")

        pattern before "2022-01-01 00:00:00"

        assertThat(pattern.pattern).isInstanceOf(BeforeDateTimePattern::class.java)
        assertThat(pattern.pattern.expected).isEqualTo("2022-01-01 00:00:00")
    }

    @Test
    fun `after should set pattern to AfterDateTimePattern`() {
        val pattern = Header("name")

        pattern after "2022-01-01 00:00:00"

        assertThat(pattern.pattern).isInstanceOf(AfterDateTimePattern::class.java)
        assertThat(pattern.pattern.expected).isEqualTo("2022-01-01 00:00:00")
    }

    @Test
    fun `dateTime should set pattern to EqualToDateTimePattern`() {
        val pattern = Header("name")

        pattern dateTime  "2022-01-01 00:00:00"

        assertThat(pattern.pattern).isInstanceOf(EqualToDateTimePattern::class.java)
        assertThat(pattern.pattern.expected).isEqualTo("2022-01-01 00:00:00")
    }
}