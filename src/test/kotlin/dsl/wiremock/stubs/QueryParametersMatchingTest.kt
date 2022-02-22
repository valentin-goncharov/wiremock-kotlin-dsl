package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.junit5.WireMockTest
import io.ktor.client.request.*
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

@WireMockTest(httpPort = 9090)
internal class QueryParametersMatchingTest: BaseStubTest() {

    @Test
    fun `request should contains query param equals to string`() {
        get {
            url pathEqualTo "/path/any"

            queryParameters contain "query" equalTo "anything"
        } returns {
            body string "Query Param OK"
        }

        runTest {

            val response = client.get<String>("http://localhost:9090/path/any?query=anything")
            Assertions.assertThat(response).isEqualTo("Query Param OK")
        }
    }

    @Test
    fun `request should not contains query param matches to string`() {
        get {
            url pathEqualTo "/path/any"

            queryParameters doNotContain "query"
        } returns {
            body string "Query Param absent OK"
        }

        runTest {

            val response = client.get<String>("http://localhost:9090/path/any?param=anything")
            Assertions.assertThat(response).isEqualTo("Query Param absent OK")
        }
    }
}