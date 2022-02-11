package dsl.wiremock.request.body

import com.github.tomakehurst.wiremock.client.WireMock
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class OrRequestBodyPatternTest {
    @Test
    fun `or with body should has the same expected as WireMock pattern`() {

        val wmPattern = WireMock.or(WireMock.matching(".*aaa.*"), WireMock.equalToJson("{\"key\":\"value\"}"))

        val body = RequestBodyScope()

        body matches ".*aaa.*" or body json """{"key":"value"}"""

        val pattern = body.patterns[0]

        Assertions.assertThat(pattern.getPattern().expected).isEqualTo(wmPattern.expected)
    }

    @Test
    fun `or with other pattern should has the same expected as WireMock pattern`() {

        val wmPattern = WireMock.matching(".*aaa.*").or(WireMock.equalToJson("{\"key\":\"value\"}"))

        val body = RequestBodyScope()

        body matches ".*aaa.*" or (body json """{"key":"value"}""")

        val pattern = body.patterns[0]

        Assertions.assertThat(pattern.getPattern().expected).isEqualTo(wmPattern.expected)
    }
}