package dsl.wiremock.request.body


import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.matching.EqualToJsonPattern
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.function.Consumer

internal class JsonBodyPatternTest {

    private val body = RequestBodyScope()

    @Test
    fun `ignore should modify pattern`() {

        val jsonBody = """{"key": "value"}"""

        val pattern = JsonBodyPattern(body)
        pattern.equalToJson(jsonBody)

        assertThat(pattern.getPattern())
            .isInstanceOf(EqualToJsonPattern::class.java)
            .satisfies( Consumer {
                val jsonPattern = it as EqualToJsonPattern
                assertThat(jsonPattern.equalToJson).isEqualTo(jsonBody.trimIndent())
            })

        pattern ignore {
            arrayOrder = true
            extraElements = true
        }

        assertThat(pattern.getPattern())
            .isInstanceOf(EqualToJsonPattern::class.java)
            .satisfies( Consumer {
                val jsonPattern = it as EqualToJsonPattern
                assertThat(jsonPattern.equalToJson).isEqualTo(jsonBody.trimIndent())
                assertThat(jsonPattern.isIgnoreArrayOrder).isTrue
                assertThat(jsonPattern.isIgnoreExtraElements).isTrue
            })
    }


    @Test
    fun `json with ignore should create pattern equals to WireMock pattern`() {

        val jsonBody = """{"key": "value"}"""

        val wmPattern = equalToJson(jsonBody, true, true)

        body json jsonBody ignore { arrayOrder = true; extraElements = true }

        val pattern = body.patterns[0]

        assertThat(pattern.getPattern()).isEqualTo(wmPattern)
    }

}