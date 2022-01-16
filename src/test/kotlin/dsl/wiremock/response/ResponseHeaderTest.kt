package dsl.wiremock.response

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ResponseHeaderTest {

    @Test
    fun `equalTo should add header with name and value to builder`() {
        val builder = ResponseDefinitionBuilder()

        val header = ResponseHeader("name", builder)

        header equalTo "value"

        val response = builder.build()
        assertThat(response.headers.getHeader("name").values()).contains("value")
    }
}