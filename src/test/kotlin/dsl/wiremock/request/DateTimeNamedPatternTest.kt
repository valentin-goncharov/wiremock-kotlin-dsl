package dsl.wiremock.request

import com.github.tomakehurst.wiremock.matching.AfterDateTimePattern
import com.github.tomakehurst.wiremock.matching.BeforeDateTimePattern
import com.github.tomakehurst.wiremock.matching.EqualToDateTimePattern
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.function.Consumer

internal class DateTimeNamedPatternTest {

    @Test
    fun `before should set pattern to BeforeDateTimePattern`() {
        val pattern = NamedPattern("name")

        pattern before "2022-01-01 00:00:00"

        assertThat(pattern.pattern).isInstanceOf(BeforeDateTimePattern::class.java)
        assertThat(pattern.pattern.expected).isEqualTo("2022-01-01 00:00:00")
    }

    @Test
    fun `after should set pattern to AfterDateTimePattern`() {
        val pattern = NamedPattern("name")

        pattern after "2022-01-01 00:00:00"

        assertThat(pattern.pattern).isInstanceOf(AfterDateTimePattern::class.java)
        assertThat(pattern.pattern.expected).isEqualTo("2022-01-01 00:00:00")
    }

    @Test
    fun `dateTime should set pattern to EqualToDateTimePattern`() {
        val pattern = NamedPattern("name")

        pattern dateTime  "2022-01-01 00:00:00"

        assertThat(pattern.pattern).isInstanceOf(EqualToDateTimePattern::class.java)
        assertThat(pattern.pattern.expected).isEqualTo("2022-01-01 00:00:00")
    }

    @Test
    fun `actualFormat should set actualFormat to date time pattern`() {
        val pattern = NamedPattern("name")

        pattern before "2022/01/20" actualFormat "yyyy/MM/dd"

        assertThat(pattern.pattern).isInstanceOf(BeforeDateTimePattern::class.java)
        
        assertThat(pattern.pattern).satisfies(Consumer {
            val dateTimePattern = it as BeforeDateTimePattern
            assertThat(dateTimePattern.expected).isEqualTo("2022/01/20")
            assertThat(dateTimePattern.actualFormat).isEqualTo("yyyy/MM/dd")
        })
    }

    @Test
    fun `truncateExpected should set truncateExpected to date time pattern`() {
        val pattern = NamedPattern("name")

        pattern before "2022/01/20" truncateExpected "first hour of day"

        assertThat(pattern.pattern).isInstanceOf(BeforeDateTimePattern::class.java)

        assertThat(pattern.pattern).satisfies(Consumer {
            val dateTimePattern = it as BeforeDateTimePattern
            assertThat(dateTimePattern.expected).isEqualTo("2022/01/20")
            assertThat(dateTimePattern.truncateExpected).isEqualTo("first hour of day")
        })
    }

    @Test
    fun `truncateActual should set truncateActual to date time pattern`() {
        val pattern = NamedPattern("name")

        pattern before "2022/01/20" truncateActual "first hour of day"

        assertThat(pattern.pattern).isInstanceOf(BeforeDateTimePattern::class.java)

        assertThat(pattern.pattern).satisfies(Consumer {
            val dateTimePattern = it as BeforeDateTimePattern
            assertThat(dateTimePattern.expected).isEqualTo("2022/01/20")
            assertThat(dateTimePattern.truncateActual).isEqualTo("first hour of day")
        })
    }

    @Test
    fun `truncateExpected should fail when unknown expression`() {
        val pattern = NamedPattern("name")

        assertThatThrownBy { pattern before "2022/01/20" truncateActual "second Monday of month" }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Unknown truncation expression")
    }
}