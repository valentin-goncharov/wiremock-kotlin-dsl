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
}