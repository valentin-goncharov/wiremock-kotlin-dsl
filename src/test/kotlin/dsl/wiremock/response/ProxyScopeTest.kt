package dsl.wiremock.response

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.function.Consumer

internal class ProxyScopeTest {

    @Test
    fun `with shoild apply scope`() {
        val scope = ProxyScope()

        assertThat(scope.isInitialized()).isFalse

        scope with {
            baseUrl = "http://test.example.com"
            prefixToRemove = "/test/prefix"
            headers contain "X-Test" equalTo "test"
        }

        assertThat(scope.isInitialized()).isTrue
        assertThat(scope.baseUrl).isEqualTo("http://test.example.com")
        assertThat(scope.prefixToRemove).isEqualTo("/test/prefix")
        assertThat(scope.headers.headers)
            .hasSize(1)
            .satisfies(Consumer {
                val header = it[0]
                assertThat(header.name).isEqualTo("X-Test")
                assertThat(header.value).isEqualTo("test")
            })

    }
}