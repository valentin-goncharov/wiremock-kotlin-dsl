package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.junit5.WireMockTest
import io.ktor.client.request.*
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.util.UUID

@WireMockTest(httpPort = 9090)
internal class CookiesMatchingTest: BaseStubTest() {

    @Test
    fun `request should not contain cookie Session`() {
        put {
            cookies contain "Session"
            body string "put body"
        } returns {
            body string "put OK"
        }

        runTest {

            val response = client.put<String>("http://localhost:9090/path/any") {
                cookie("Session", UUID.randomUUID().toString())
                body = "put body"
            }
            Assertions.assertThat(response).isEqualTo("put OK")
        }
    }
}