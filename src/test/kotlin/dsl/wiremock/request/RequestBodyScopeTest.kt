package dsl.wiremock.request

import com.github.tomakehurst.wiremock.matching.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.function.Consumer

internal class RequestBodyScopeTest {

    private lateinit var body: RequestBodyScope

    @BeforeEach
    fun init() {
        body = RequestBodyScope()
    }


    @Test
    fun `json should add EqualToJsonPattern pattern to patterns list`() {
        val json = """
            {
                "key": "value"
            }
        """

        body json json

        assertThat(body.patterns).hasSize(1)
        assertThat(body.patterns[0].pattern)
            .satisfies( Consumer {
                val jsonPattern = it as EqualToJsonPattern
                assertThat(jsonPattern.equalToJson).isEqualTo(json.trimIndent())
            })
    }

    @Test
    fun `string should set pattern to EqualToPattern`() {
        val string = "some string"

        body string string

        assertThat(body.patterns).hasSize(1)
        assertThat(body.patterns[0].pattern)
            .isInstanceOf(EqualToPattern::class.java)
            .satisfies( Consumer {
                assertThat(it.expected).isEqualTo(string)
            })
    }

    @Test
    fun `xml should set pattern to EqualToXmlPattern`() {
        val xml = """
            <xml>
                <key>value</key>
            </xml>
        """

        body xml xml

        assertThat(body.patterns).hasSize(1)
        assertThat(body.patterns[0].pattern)
            .isInstanceOf(EqualToXmlPattern::class.java)
            .satisfies( Consumer {
                val xmlPattern = it as EqualToXmlPattern
                assertThat(xmlPattern.equalToXml).isEqualTo(xml.trimIndent())
            })
    }

    @Test
    fun `matches should set pattern to EqualToXmlPattern`() {
        val regexp = ".*AAA.*"

        body matches regexp

        assertThat(body.patterns).hasSize(1)
        assertThat(body.patterns[0].pattern)
            .isInstanceOf(RegexPattern::class.java)
            .satisfies( Consumer {
                assertThat(it.expected).isEqualTo(regexp)
            })
    }

    @Test
    fun `doesNotMatch should set pattern to EqualToXmlPattern`() {
        val regexp = ".*AAA.*"

        body doesNotMatch regexp

        assertThat(body.patterns).hasSize(1)
        assertThat(body.patterns[0].pattern)
            .isInstanceOf(NegativeRegexPattern::class.java)
            .satisfies( Consumer {
                assertThat(it.expected).isEqualTo(regexp)
            })
    }
}