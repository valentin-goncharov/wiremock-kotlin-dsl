package dsl.wiremock.request

import com.github.tomakehurst.wiremock.matching.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.function.Consumer

internal class RequestBodyPatternTest {
    private lateinit var pattern: RequestBodyPattern

    @BeforeEach
    fun init() {
        pattern = RequestBodyPattern()
    }

    @Test
    fun `json should set pattern to EqualToJsonPattern and return the same object`() {

        val jsonBody = """
            {
                "key": "value"
            }
        """

        assertThat(pattern equalToJson jsonBody).isSameAs(pattern)
        assertThat(pattern.pattern)
            .isInstanceOf(EqualToJsonPattern::class.java)
            .satisfies( Consumer {
                val jsonPattern = it as EqualToJsonPattern
                assertThat(jsonPattern.equalToJson).isEqualTo(jsonBody.trimIndent())
            })
    }

    @Test
    fun `string should set pattern to EqualToPattern and return the same object`() {

        val stringBody = "string"

        assertThat(pattern equalTo stringBody).isSameAs(pattern)
        assertThat(pattern.pattern)
            .isInstanceOf(EqualToPattern::class.java)
            .satisfies( Consumer {
                assertThat(it.expected).isEqualTo(stringBody)
            })
    }

    @Test
    fun `xml should set pattern to EqualToXmlPattern and return the same object`() {

        val xmlBody = """
            <xml>
                <key>value</key>
            </xml>
        """

        assertThat(pattern equalToXml xmlBody).isSameAs(pattern)
        assertThat(pattern.pattern)
            .isInstanceOf(EqualToXmlPattern::class.java)
            .satisfies( Consumer {
                val xmlPattern = it as EqualToXmlPattern
                assertThat(xmlPattern.equalToXml).isEqualTo(xmlBody.trimIndent())
            })
    }

    @Test
    fun `matches should set pattern to RegexPattern and return the same object`() {

        val regex = ".*AAA.*"

        assertThat(pattern matches regex).isSameAs(pattern)
        assertThat(pattern.pattern)
            .isInstanceOf(RegexPattern::class.java)
            .satisfies( Consumer {
                assertThat(it.expected).isEqualTo(regex)
            })
    }

    @Test
    fun `doesNotMatch should set pattern to NegativeRegexPattern and return the same object`() {

        val regex = ".*AAA.*"

        assertThat(pattern doesNotMatch regex).isSameAs(pattern)
        assertThat(pattern.pattern)
            .isInstanceOf(NegativeRegexPattern::class.java)
            .satisfies( Consumer {
                assertThat(it.expected).isEqualTo(regex)
            })
    }

    @Test
    fun `or should set pattern to LogicalOr`() {

        val body = RequestBodyScope()

        val regexp = ".*AAA.*"

        pattern matches regexp or body doesNotMatch regexp

        assertThat(pattern.pattern)
            .isInstanceOf(LogicalOr::class.java)
            .satisfies( Consumer {
                assertThat(it.expected).isEqualTo("matches .*AAA.* OR doesNotMatch .*AAA.*")
            })
    }

    @Test
    fun `or with pattern should set pattern to LogicalOr`() {
        val regexp = ".*AAA.*"

        val other = RequestBodyPattern() equalTo "string"

        pattern matches regexp or other

        assertThat(pattern.pattern)
            .isInstanceOf(LogicalOr::class.java)
            .satisfies( Consumer {
                assertThat(it.expected).isEqualTo("matches .*AAA.* OR equalTo string")
            })
    }

    @Test
    fun `and should set pattern to LogicalAnd`() {

        val body = RequestBodyScope()

        val regexp = ".*AAA.*"

        pattern matches regexp and body doesNotMatch regexp

        assertThat(pattern.pattern)
            .isInstanceOf(LogicalAnd::class.java)
            .satisfies( Consumer {
                assertThat(it.expected).isEqualTo("matches .*AAA.* AND doesNotMatch .*AAA.*")
            })
    }

    @Test
    fun `and with pattern should set pattern to LogicalAnd`() {
        val regexp = ".*AAA.*"

        val other = RequestBodyPattern() equalTo "string"

        pattern matches regexp and other

        assertThat(pattern.pattern)
            .isInstanceOf(LogicalAnd::class.java)
            .satisfies( Consumer {
                assertThat(it.expected).isEqualTo("matches .*AAA.* AND equalTo string")
            })
    }
}