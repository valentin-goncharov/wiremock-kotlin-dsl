package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.junit5.WireMockTest
import io.ktor.client.features.*
import io.ktor.client.request.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

@WireMockTest(httpPort = 9090)
@ExperimentalCoroutinesApi
internal class AndPatternsMatchingTest:BaseStubTest() {

    @Test
    fun `request should match two header patterns simultaneously`() {

        put {
            headers contain "X-Test-Present" matches "[a-zA-Z]+" and "X-Test-Present" contains "Random"
        } returns {
            body string "OK"
        }

        runTest {
            val response = client.put<String>("http://localhost:9090/path/any?query=anything?query=anything") {
                headers {
                    append("X-Test-Present", "anyRandomString")
                }
            }
            assertThat(response).isEqualTo("OK")
        }
    }

    @Test
    fun `should fail if does not match two header patterns simultaneously`() {

        get {
            headers contain "X-Test-Present" matches "[a-zA-Z]+" and "X-Test-Present" contains "random"
        }

        assertThatThrownBy {
            runTest {
                client.get<String>("http://localhost:9090/path/any?query=anything") {
                    headers {
                        append("X-Test-Present", "anyRandomString")
                    }
                }
            }
        }.isInstanceOf(ClientRequestException::class.java)
    }

    @Test
    fun `request should match two body patterns simultaneously`() {

        val regex = "\\{.*te?st.*\\}"

        post {
            body doesNotMatch regex and body contains "simple"
        } returns {
            body string "OK"
        }

        runTest {
            val response = client.post<String>("http://localhost:9090/path/any?query=anything") {
                body = "{just simple text}"
            }
            assertThat(response).isEqualTo("OK")
        }
    }

    @Test
    fun `should fail if does not match two body patterns simultaneously`() {
        val regex = "\\{.*te?st.*\\}"

        post {
            body doesNotMatch regex and body contains "simple"
        }

        assertThatThrownBy {
            runTest {
                client.get<String>("http://localhost:9090/path/any?query=anything") {
                    body = "{just simple test}"
                }
            }
        }.isInstanceOf(ClientRequestException::class.java)
    }
}