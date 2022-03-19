package dsl.wiremock.response

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ResponseScopeTest {

    @Test
    fun `set status should add status to builder`() {
        val responseScope = ResponseScope()
        responseScope.status = 404

        val response = responseScope.builder.build()
        assertThat(response.status).isEqualTo(404)
    }

    @Test
    fun `proxy should apply proxy scope`() {
        val responseScope = ResponseScope()

        assertThat(responseScope.proxy.isInitialized()).isFalse

        responseScope.apply {
            proxy {
                baseUrl = "https://test.example.com"
            }
        }

        assertThat(responseScope.proxy.isInitialized()).isTrue
        assertThat(responseScope.proxy.baseUrl).isEqualTo("https://test.example.com")
    }
}