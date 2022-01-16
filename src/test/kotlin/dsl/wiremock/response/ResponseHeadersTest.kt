package dsl.wiremock.response

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ResponseHeadersTest {

    @Test
    fun `contain should create new header with name`() {
        val builder = ResponseDefinitionBuilder()
        val headers = ResponseHeaders(builder)

        assertThat(headers contain "key").hasFieldOrPropertyWithValue("name", "key")
    }
}