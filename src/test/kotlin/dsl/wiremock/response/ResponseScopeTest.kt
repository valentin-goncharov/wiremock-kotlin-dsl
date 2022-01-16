package dsl.wiremock.response

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class ResponseScopeTest {

    @Test
    fun `set status should add status to builder`() {
        val responseScope = ResponseScope()
        responseScope.status = 404

        val response = responseScope.builder.build()
        Assertions.assertThat(response.status).isEqualTo(404)
    }
}