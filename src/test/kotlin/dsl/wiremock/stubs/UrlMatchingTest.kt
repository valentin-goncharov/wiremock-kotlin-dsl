package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import io.ktor.client.request.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@WireMockTest(httpPort = 9090)
@ExperimentalCoroutinesApi
internal class UrlMatchingTest: BaseStubTest() {

    @Test
    fun `url should match to any url string`(wmRuntimeInfo: WireMockRuntimeInfo) {
        any {
            url equalTo "any"
        } returns {
            body string "ANY"
        }

        runTest {
            val response = client.get<String>("http://localhost:9090/someUrl?withQuery=AndParam")
            assertThat(response).isEqualTo("ANY")
        }
    }

    @Test
    fun `url should be equal to path and query params`(wmRuntimeInfo: WireMockRuntimeInfo) {
        any {
            url equalTo "/path?query=param"
        } returns {
            body string "OK"
        }

        runTest {
            val response = client.get<String>("http://localhost:9090/path?query=param")
            assertThat(response).isEqualTo("OK")
        }
    }

    @Test
    fun `url should match path pattern and query params`(wmRuntimeInfo: WireMockRuntimeInfo) {
        any {
            url matches  "/path/.*\\?query=[0-9]+"
        } returns {
            body string "MATCHES ALL"
        }

        runTest {
            val response = client.get<String>("http://localhost:9090/path/to/entity?query=1981")
            assertThat(response).isEqualTo("MATCHES ALL")
        }
    }

    @Test
    fun `url path should be equal to path`(wmRuntimeInfo: WireMockRuntimeInfo) {
        any {
            url pathEqualTo "/path/subpath"
        } returns {
            body string "PATH EQUALS TO"
        }

        runTest {
            val response1 = client.get<String>("http://localhost:9090/path/subpath")
            assertThat(response1).isEqualTo("PATH EQUALS TO")

            val response2 = client.get<String>("http://localhost:9090/path/subpath?query=param")
            assertThat(response2).isEqualTo("PATH EQUALS TO")
        }
    }

    @Test
    fun `url path should match path pattern`(wmRuntimeInfo: WireMockRuntimeInfo) {
        any {
            url pathMatches "/path/a.*"
            priority = 1
        } returns {
            body string "PATH MATCHES A*"
        }

        any {
            url pathMatches "/path/.*"
            priority = 2
        } returns {
            body string "PATH MATCHES ALL"
        }


        runTest {
            val response1 = client.get<String>("http://localhost:9090/path/another?query=anything")
            assertThat(response1).isEqualTo("PATH MATCHES A*")

            val response2 = client.get<String>("http://localhost:9090/path/other?query=trash")
            assertThat(response2).isEqualTo("PATH MATCHES ALL")
        }
    }
}