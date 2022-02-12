package dsl.wiremock.request.body

import com.github.tomakehurst.wiremock.matching.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.function.Consumer

internal class StringValueRequestBodyPatternTest {
    private lateinit var pattern: StringValueRequestBodyPattern

    private val body = RequestBodyScope()

    @BeforeEach
    fun init() {
        pattern = StringValueRequestBodyPattern(body)
        body.patterns += pattern
    }

    @Test
    fun `equalToJson should set pattern to EqualToJsonPattern and return the same object`() {

        val jsonBody = """
            {
                "key": "value"
            }
        """

        val pattern = JsonBodyPattern(body)

        assertThat(pattern.equalToJson(jsonBody)).isSameAs(pattern)
        assertThat(pattern.getPattern())
            .isInstanceOf(EqualToJsonPattern::class.java)
            .satisfies( Consumer {
                val jsonPattern = it as EqualToJsonPattern
                assertThat(jsonPattern.equalToJson).isEqualTo(jsonBody.trimIndent())
            })
    }

    @Test
    fun `equalTo should set pattern to EqualToPattern and return the same object`() {

        val stringBody = "string"

        assertThat(pattern.equalTo(stringBody)).isSameAs(pattern)
        assertThat(pattern.getPattern())
            .isInstanceOf(EqualToPattern::class.java)
            .satisfies( Consumer {
                assertThat(it.expected).isEqualTo(stringBody)
            })
    }

    @Test
    fun `contains should set pattern to ContainsPattern and return the same object`() {

        val stringBody = "string"

        assertThat(pattern.contains(stringBody)).isSameAs(pattern)
        assertThat(pattern.getPattern())
            .isInstanceOf(ContainsPattern::class.java)
            .satisfies( Consumer {
                assertThat(it.expected).isEqualTo(stringBody)
            })
    }

    @Test
    fun `equalToXml should set pattern to EqualToXmlPattern and return the same object`() {

        val xmlBody = """
            <xml>
                <key>value</key>
            </xml>
        """

        val pattern = XmlBodyPattern(body)

        assertThat(pattern.equalToXml(xmlBody)).isSameAs(pattern)
        assertThat(pattern.getPattern())
            .isInstanceOf(EqualToXmlPattern::class.java)
            .satisfies( Consumer {
                val xmlPattern = it as EqualToXmlPattern
                assertThat(xmlPattern.equalToXml).isEqualTo(xmlBody.trimIndent())
            })
    }

    @Test
    fun `matches should set pattern to RegexPattern and return the same object`() {

        val regex = ".*AAA.*"

        assertThat(pattern.matches(regex)).isSameAs(pattern)
        assertThat(pattern.getPattern())
            .isInstanceOf(RegexPattern::class.java)
            .satisfies( Consumer {
                assertThat(it.expected).isEqualTo(regex)
            })
    }

    @Test
    fun `doesNotMatch should set pattern to NegativeRegexPattern and return the same object`() {

        val regex = ".*AAA.*"

        assertThat(pattern.doesNotMatch(regex)).isSameAs(pattern)
        assertThat(pattern.getPattern())
            .isInstanceOf(NegativeRegexPattern::class.java)
            .satisfies( Consumer {
                assertThat(it.expected).isEqualTo(regex)
            })
    }

    @Test
    fun `or should set pattern to LogicalOr`() {

        val regexp = ".*AAA.*"

        val resultPattern = pattern.matches(regexp) or body doesNotMatch regexp

        assertThat(resultPattern.getPattern())
            .isInstanceOf(LogicalOr::class.java)
            .satisfies( Consumer {
                assertThat(it.expected).isEqualTo("matches .*AAA.* OR doesNotMatch .*AAA.*")
            })
    }

    @Test
    fun `or with pattern should set pattern to LogicalOr`() {
        val regexp = ".*AAA.*"

        val other = StringValueRequestBodyPattern(body).equalTo("string")

        pattern.matches(regexp) or other

        assertThat(pattern.getPattern())
            .isInstanceOf(LogicalOr::class.java)
            .satisfies( Consumer {
                assertThat(it.expected).isEqualTo("matches .*AAA.* OR equalTo string")
            })
    }

    @Test
    fun `and should set pattern to LogicalAnd`() {


        val regexp = ".*AAA.*"

        val resultPattern = pattern.matches(regexp) and body doesNotMatch regexp

        assertThat(resultPattern.getPattern())
            .isInstanceOf(LogicalAnd::class.java)
            .satisfies( Consumer {
                assertThat(it.expected).isEqualTo("matches .*AAA.* AND doesNotMatch .*AAA.*")
            })
    }

    @Test
    fun `and with pattern should set pattern to LogicalAnd`() {
        val regexp = ".*AAA.*"

        val other = StringValueRequestBodyPattern(body).equalTo("string")

        pattern.matches(regexp) and other

        assertThat(pattern.getPattern())
            .isInstanceOf(LogicalAnd::class.java)
            .satisfies( Consumer {
                assertThat(it.expected).isEqualTo("matches .*AAA.* AND equalTo string")
            })
    }
}