package dsl.wiremock.request.body

import com.github.tomakehurst.wiremock.client.WireMock.and
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.matching
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class AndRequestBodyPatternTest {

    @Test
    fun `and with body should has the same expected as WireMock pattern`() {

        val wmPattern = and(matching(".*aaa.*"),equalToJson("{\"key\":\"value\"}"))

        val body = RequestBodyScope()

        body matches ".*aaa.*" and body json """{"key":"value"}"""

        val pattern = body.patterns[0]

        assertThat(pattern.getPattern().expected).isEqualTo(wmPattern.expected)
    }

    @Test
    fun `and with other pattern should has the same expected as WireMock pattern`() {

        val wmPattern = matching(".*aaa.*").and(equalToJson("{\"key\":\"value\"}"))

        val body = RequestBodyScope()

        body matches ".*aaa.*" and (body json """{"key":"value"}""")

        val pattern = body.patterns[0]

        assertThat(pattern.getPattern().expected).isEqualTo(wmPattern.expected)
    }
}