package dsl.wiremock.request.body

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.MatchesXPathPattern
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.function.Consumer

internal class XPathBodyPatternTest {
    private val body = RequestBodyScope()

    @Test
    fun `matchingXPath should create pattern equals to WireMock pattern`() {

        val xPath = "//key/text()"

        val pattern = body xmlPath xPath

        assertThat(pattern.matchingXPath(xPath)).isSameAs(pattern)
        assertThat(pattern.getPattern())
            .isInstanceOf(MatchesXPathPattern::class.java)
            .satisfies( Consumer {
                val jsonPathPattern = it as MatchesXPathPattern
                assertThat(jsonPathPattern.matchesXPath).isEqualTo(xPath)
            })

        val vmPattern = WireMock.matchingXPath(xPath)

        assertThat(pattern.getPattern()).isEqualTo(vmPattern)
    }

    @Test
    fun `namespace should create pattern equals to WireMock pattern`() {

        val xPath = "//test:key/more:inner/text()"

        val pattern = body xmlPath
                xPath namespace "test = http://test.example.com" namespace "more = http://more.example.com"

        val vmPattern = WireMock.matchingXPath(xPath)
            .withXPathNamespace("test", "http://test.example.com")
            .withXPathNamespace("more", "http://more.example.com")

        assertThat(pattern.getPattern()).isEqualTo(vmPattern)
    }

    @Test
    fun `equalToJson should create pattern equals to WireMock pattern`() {

        val xPath = "//key/text()"
        val jsonBody = """{"key":"value"}"""

        val pattern = body xmlPath xPath equalToJson jsonBody

        val vmPattern = WireMock.matchingXPath(xPath, WireMock.equalToJson(jsonBody))

        assertThat(pattern.getPattern()).isEqualTo(vmPattern)
    }

    @Test
    fun `equalToXml should create pattern equals to WireMock pattern`() {

        val xPath = "//key/text()"
        val xmlBody = """<xml>value</xml>"""

        val pattern = body xmlPath  xPath equalToXml xmlBody

        val vmPattern = WireMock.matchingXPath(xPath, WireMock.equalToXml(xmlBody))

        assertThat(pattern.getPattern()).isEqualTo(vmPattern)
    }

    @Test
    fun `equalTo should create pattern equals to WireMock pattern`() {

        val xPath = "//key/text()"
        val string = "string"

        val pattern = body xmlPath xPath equalTo string

        val vmPattern = WireMock.matchingXPath(xPath, WireMock.equalTo(string))

        assertThat(pattern.getPattern()).isEqualTo(vmPattern)
    }

    @Test
    fun `contains should create pattern equals to WireMock pattern`() {

        val xPath = "//key/text()"
        val string = "string"

        val pattern = body xmlPath xPath contains string

        val vmPattern = WireMock.matchingXPath(xPath, WireMock.containing(string))

        assertThat(pattern.getPattern()).isEqualTo(vmPattern)
    }

    @Test
    fun `matches should create pattern equals to WireMock pattern`() {

        val xPath = "//key/text()"
        val regex = ".*aaa.*"

        val pattern = body xmlPath xPath matches regex

        val vmPattern = WireMock.matchingXPath(xPath, WireMock.matching(regex))

        assertThat(pattern.getPattern()).isEqualTo(vmPattern)
    }

    @Test
    fun `doesNotMatch should create pattern equals to WireMock pattern`() {

        val xPath = "//key/text()"
        val regex = ".*aaa.*"

        val pattern = body xmlPath xPath doesNotMatch regex

        val vmPattern = WireMock.matchingXPath(xPath, WireMock.notMatching(regex))

        assertThat(pattern.getPattern()).isEqualTo(vmPattern)
    }
}