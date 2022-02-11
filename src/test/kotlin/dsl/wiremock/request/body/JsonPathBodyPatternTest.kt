package dsl.wiremock.request.body

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.containing
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.equalToXml
import com.github.tomakehurst.wiremock.client.WireMock.matching
import com.github.tomakehurst.wiremock.client.WireMock.notMatching
import com.github.tomakehurst.wiremock.matching.MatchesJsonPathPattern
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.function.Consumer

internal class JsonPathBodyPatternTest {

    private val body = RequestBodyScope()

    @Test
    fun `matchingJsonPath should has the same expected as WireMock pattern`() {

        val jsonPath = "$..name"

        val pattern = body jsonPath jsonPath

        assertThat(pattern.matchingJsonPath(jsonPath)).isSameAs(pattern)
        assertThat(pattern.getPattern())
            .isInstanceOf(MatchesJsonPathPattern::class.java)
            .satisfies( Consumer {
                val jsonPathPattern = it as MatchesJsonPathPattern
                assertThat(jsonPathPattern.matchesJsonPath).isEqualTo(jsonPath)
            })

        val vmPattern = WireMock.matchingJsonPath(jsonPath)

        assertThat(pattern.getPattern().expected).isEqualTo(vmPattern.expected)
    }

    @Test
    fun `equalToJson should create pattern equals to WireMock pattern`() {

        val jsonPath = "$..name"
        val jsonBody = """{"key":"value"}"""

        val pattern = body jsonPath jsonPath equalToJson jsonBody

        val vmPattern = WireMock.matchingJsonPath(jsonPath, equalToJson(jsonBody))

        assertThat(pattern.getPattern()).isEqualTo(vmPattern)
    }

    @Test
    fun `equalToXml should create pattern equals to WireMock pattern`() {

        val jsonPath = "$..name"
        val xmlBody = """<xml>value</xml>"""

        val pattern = body jsonPath jsonPath equalToXml xmlBody

        val vmPattern = WireMock.matchingJsonPath(jsonPath, equalToXml(xmlBody))

        assertThat(pattern.getPattern()).isEqualTo(vmPattern)
    }

    @Test
    fun `equalTo should create pattern equals to WireMock pattern`() {

        val jsonPath = "$..name"
        val string = "string"

        val pattern = body jsonPath jsonPath equalTo string

        val vmPattern = WireMock.matchingJsonPath(jsonPath, equalTo(string))

        assertThat(pattern.getPattern()).isEqualTo(vmPattern)
    }

    @Test
    fun `contains should create pattern equals to WireMock pattern`() {

        val jsonPath = "$..name"
        val string = "string"

        val pattern = body jsonPath jsonPath contains string

        val vmPattern = WireMock.matchingJsonPath(jsonPath, containing(string))

        assertThat(pattern.getPattern()).isEqualTo(vmPattern)
    }

    @Test
    fun `matches should create pattern equals to WireMock pattern`() {

        val jsonPath = "$..name"
        val regex = ".*aaa.*"

        val pattern = body jsonPath jsonPath matches regex

        val vmPattern = WireMock.matchingJsonPath(jsonPath, matching(regex))

        assertThat(pattern.getPattern()).isEqualTo(vmPattern)
    }

    @Test
    fun `doesNotMatch should create pattern equals to WireMock pattern`() {

        val jsonPath = "$..name"
        val regex = ".*aaa.*"

        val pattern = body jsonPath jsonPath doesNotMatch regex

        val vmPattern = WireMock.matchingJsonPath(jsonPath, notMatching(regex))

        assertThat(pattern.getPattern()).isEqualTo(vmPattern)
    }
}