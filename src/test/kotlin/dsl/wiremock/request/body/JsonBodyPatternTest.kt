package dsl.wiremock.request.body


import com.github.tomakehurst.wiremock.matching.EqualToJsonPattern
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.function.Consumer

internal class JsonBodyPatternTest {

    private val scope = RequestBodyScope()

    @Test
    fun `ignore should modify pattern`() {

        val jsonBody = """{"key": "value"}"""

        val pattern = JsonBodyPattern(scope)
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
}