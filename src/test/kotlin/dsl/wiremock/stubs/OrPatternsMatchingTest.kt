package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.junit5.WireMockTest
import io.ktor.client.features.*
import io.ktor.client.request.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@WireMockTest(httpPort = 9090)
@ExperimentalCoroutinesApi
internal class OrPatternsMatchingTest: BaseStubTest() {

    @ParameterizedTest
    @ValueSource(strings = ["anyRandomString", "randomString"])
    fun `request should match any of two header patterns`(bodyString: String) {

        put {
            headers contain "X-Test-Present" matches "[a-zA-Z]+" or "X-Test-Present" contains "random"
        } returns {
            body string "OK"
        }

        runTest {
            val response = client.put<String>("http://localhost:9090/path/any?query=anything?query=anything") {
                headers {
                    append("X-Test-Present", bodyString)
                }
            }
            assertThat(response).isEqualTo("OK")
        }
    }

    @Test
    fun `should fail if does not match none of header patterns`() {

        get {
            headers contain "X-Test-Present" matches "[a-zA-Z]+" or "X-Test-Present" contains "random"
        }

        assertThatThrownBy {
            runTest {
                client.get<String>("http://localhost:9090/path/any?query=anything") {
                    headers {
                        append("X-Test-Present", "any Random String")
                    }
                }
            }
        }.isInstanceOf(ClientRequestException::class.java)
    }

    @ParameterizedTest
    @ValueSource(strings = ["""{"key":"value"}""", """<test><key>value</key></test>"""])
    fun `request should match any of body patterns`(bodyString: String) {

        val jsonBody = """{"key":"value"}"""
        val xmlBody ="""<test><key>value</key></test>"""

        post {
            body json jsonBody or body xml xmlBody
        } returns {
            body string "OK"
        }

        runTest {
            val response = client.post<String>("http://localhost:9090/path/any?query=anything") {
                body = bodyString
            }
            assertThat(response).isEqualTo("OK")
        }
    }

    @Test
    fun `should fail if does not match none of body patterns`() {

        val jsonBody = """{"key":"value"}"""
        val xmlBody ="""<test><key>value</key></test>"""

        post {
            body json jsonBody or body xml xmlBody
        }

        assertThatThrownBy {
            runTest {
                client.post<String>("http://localhost:9090/path/any?query=anything") {
                    body = "test"
                }
            }
        }.isInstanceOf(ClientRequestException::class.java)
    }
}