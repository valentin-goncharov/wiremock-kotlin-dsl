package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.junit5.WireMockTest
import io.ktor.client.request.*
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@WireMockTest(httpPort = 9090)
internal class StringBodyMatchingTest: BaseStubTest() {

    @Test
    fun `request body should be equal to string`() {
        val stringBody = "test body string"

        post {
            body string stringBody
        } returns {
            body string "body is presented and equal to '$stringBody'"
        }

        runTest {
            val response = client.post<String>("http://localhost:9090/path/any?query=anything") {
                body = stringBody
            }
            assertThat(response).isEqualTo("body is presented and equal to '$stringBody'")
        }
    }

    @Test
    fun `request body should contain to string`() {
        val testString = "test body substring"

        post {
            body contains testString
        } returns {
            body string "body contains '$testString'"
        }

        runTest {
            val response = client.post<String>("http://localhost:9090/path/any?query=anything") {
                body = "any prefix $testString any postfix"
            }
            assertThat(response).isEqualTo("body contains '$testString'")
        }
    }

    @Test
    fun `request body should matches to regex`() {
        val regex = "\\{.*te?st.*\\}"

        post {
            body matches regex
        } returns {
            body string "body matches '$regex'"
        }

        runTest {
            val response = client.post<String>("http://localhost:9090/path/any?query=anything") {
                body = "{just simple tst}"
            }
            assertThat(response).isEqualTo("body matches '$regex'")
        }
    }

    @Test
    fun `request body should not match to regex`() {
        val regex = "\\{.*te?st.*\\}"

        post {
            body doesNotMatch  regex
        } returns {
            body string "body does not matches '$regex'"
        }

        runTest {
            val response = client.post<String>("http://localhost:9090/path/any?query=anything") {
                body = "{just simple text}"
            }
            assertThat(response).isEqualTo("body does not matches '$regex'")
        }
    }
}