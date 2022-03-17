package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.junit5.WireMockTest
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode.Companion.Created
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

@WireMockTest(httpPort = 9090)
@ExperimentalCoroutinesApi
internal class StubResponseTest: BaseStubTest() {

    @Test
    fun `request body should be equal to string`() {

        get {
            url equalTo "/simple"
        } returns {
            status = 201
            headers contain "ETag" equalTo "1234567890"
            body string "OK"
        }

        runTest {
            val response: HttpResponse = client.get<HttpStatement>("http://localhost:9090/simple").execute()
            Assertions.assertThat(response.status).isEqualTo(Created)
            Assertions.assertThat(response.headers.get("ETag")).isEqualTo("1234567890")
            Assertions.assertThat(response.receive<String>()).isEqualTo("OK")
        }
    }
}