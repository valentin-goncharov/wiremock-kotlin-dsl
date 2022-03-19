package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import io.ktor.client.features.*
import io.ktor.client.request.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.time.LocalDateTime

@WireMockTest(httpPort = 9090)
@ExperimentalCoroutinesApi
internal class HeadersMatchingTest: BaseStubTest() {

    @Test
    fun `request should not contain header X-Not`() {
        any {
            headers doNotContain "X-Not"
        } returns {
            body string "X-Not is not presented"
        }

        runTest {

            val response = client.get<String>("http://localhost:9090/path/any?query=anything") {
                headers {
                    append("X-Test-Present", "anyRandomString")
                }
            }
            assertThat(response).isEqualTo("X-Not is not presented")
        }
    }

    @Test
    fun `request should contains header`() {
        get {
            headers contain "X-Test-Present"
        } returns {
            body string "X-Test-Present is presented"
        }

        runTest {
            val response = client.get<String>("http://localhost:9090/path/any?query=anything") {
                headers {
                    append("X-Test-Present", "anyRandomString")
                    append("X-Not", "randomString")
                }
            }
            assertThat(response).isEqualTo("X-Test-Present is presented")
        }
    }

    @Test
    fun `request should contains header equals to test`() {
        patch {
            headers contain "X-Test-Equal" equalTo "test"
        } returns {
            body string "X-Test-Equal equals to test"
        }

        runTest {

            val response = client.patch<String>("http://localhost:9090/path/any?query=anything") {
                headers {
                    append("X-Test-Equal", "test")
                    append("X-Not", "randomString")
                }
                body = "body string"
            }
            assertThat(response).isEqualTo("X-Test-Equal equals to test")
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["test", "test0", "est", "estimation"])
    fun `request should contains header matches to regex`(header: String) {
        headerMatchesRegex()

        runTest {

            val response = client.put<String>("http://localhost:9090/path/any?query=anything") {
                headers {
                    append("X-Test-Match", header)
                    append("X-Not","randomString")
                }
                body = "body string"
            }
            assertThat(response).isEqualTo("X-Test-Match matches to t?est.*")
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["pesto", "ttest"])
    fun `request should fail when header does not match to regex`(header: String) {
        headerMatchesRegex()

        runTest {

            try {
                client.put<String>("http://localhost:9090/path/any?query=anything") {
                    headers {
                        append("X-Test-Match", header)
                        append("X-Not","randomString")
                    }
                    body = "body string"
                }
            } catch (cause: ClientRequestException) {
                assertThat(cause.response.status.value).isEqualTo(404)
            }
        }
    }
    
    private fun headerMatchesRegex() {
        put {
            headers contain "X-Test-Match" matches "t?est.*"
        } returns {
            body string "X-Test-Match matches to t?est.*"
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["test", "test0", "est", "estimation"])
    fun `request should contains header does not matches to regex`(header: String) {
        headerDoesNotMatchRegex()

        runTest {

            val response = client.post<String>("http://localhost:9090/path/any?query=anything") {
                headers {
                    append("X-Test-Not-Match", header)
                    append("X-Not","randomString")
                }
                body = "body string"
            }
            assertThat(response).isEqualTo("X-Test-Not-Match does not match to testo+")
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["12345", "1", "2114445"])
    fun `request should fail when header matches to regex`(header: String) {
        headerDoesNotMatchRegex()

        runTest {

            try {
                client.post<String>("http://localhost:9090/path/any?query=anything") {
                    headers {
                        append("X-Test-Not-Match", header)
                        append("X-Not","randomString")
                    }
                    body = "body string"
                }
            } catch (cause: ClientRequestException) {
                assertThat(cause.response.status.value).isEqualTo(404)
            }
        }
    }
    
    private fun headerDoesNotMatchRegex() {
        post {
            headers contain "X-Test-Not-Match" doesNotMatch "[0-9]+"
        } returns {
            body string "X-Test-Not-Match does not match to testo+"
        }
    } 

    @Test
    fun `request should contains header that contains string`(wireMockRuntimeInfo: WireMockRuntimeInfo) {
        delete {
            headers contain "X-Test-Contain" contains "marker"
        } returns {
            body string "X-Test-Contain contains 'marker'"
        }

        runTest {

            val response = client.delete<String>("http://localhost:9090/path/any?query=anything") {
                headers {
                    append("X-Test-Contain", "This header contains marker string")
                    append("X-Not", "randomString")
                }
            }
            assertThat(response).isEqualTo("X-Test-Contain contains 'marker'")
        }
    }

    @ParameterizedTest
    @MethodSource("equalToDateTimes")
    fun `request should contains header that equals date`(header: String, message: String) {
        any {
            headers contain "X-Test-Date" dateTime "2022-01-01T00:00:00" truncateActual "first day of month"
        } returns {
            body string "X-Test-Date in January 2022"
        }

        any {
            headers contain "X-Test-Date" dateTime "2022-02-22T00:00:00"
        } returns {
            body string "X-Test-Date equals to 2022-02-22"
        }

        any {
            headers contain "X-Test-Date" dateTime "2022-02-22T00:00:00" actualFormat "dd.MM.yyyy"
        } returns {
            body string "X-Test-Date equals to 22.02.2022"
        }

        runTest {

            val response = client.get<String>("http://localhost:9090/path/any?query=anything") {
                headers {
                    append("X-Test-Date", header)
                    append("X-Not", "randomString")
                }
            }
            assertThat(response).isEqualTo(message)
        }
    }

    @Test
    fun `request should contains header that before date time`() {
        any {
            headers contain "X-Test-Date" before "now +10 hours" truncateExpected "first hour of day"
        } returns {
            body string "X-Test-Date before 10:00 of current date"
        }

        runTest {

            val response = client.put<String>("http://localhost:9090/path/any?query=anything") {
                headers {
                    append("X-Test-Date", LocalDateTime.now().minusDays(1).toString())
                    append("X-Not", "randomString")
                }
                body = "string body"
            }
            assertThat(response).isEqualTo("X-Test-Date before 10:00 of current date")
        }
    }

    @Test
    fun `request should contains header that after date time`() {
        any {
            priority = 3
            headers contain "X-Test-Date" after "2022-02-23T23:59:59"
        } returns {
            body string "X-Test-Date after 2022-02-23"
        }

        runTest {

            val response = client.post<String>("http://localhost:9090/path/any?query=anything") {
                headers {
                    append("X-Test-Date", "2022-02-24T00:00:00")
                    append("X-Not", "randomString")
                }
                body = "string body"
            }
            assertThat(response).isEqualTo("X-Test-Date after 2022-02-23")
        }
    }

    companion object {
        @JvmStatic
        fun equalToDateTimes() = listOf(
            Arguments.of("2022-02-22T00:00:00", "X-Test-Date equals to 2022-02-22"),
            Arguments.of("22.02.2022", "X-Test-Date equals to 22.02.2022"),
            Arguments.of("2022-01-15T12:45:47Z", "X-Test-Date in January 2022"),
        )
    }
}