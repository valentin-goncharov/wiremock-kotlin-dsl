package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.junit5.WireMockTest
import dsl.wiremock.response.FaultType.EMPTY_RESPONSE
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.io.IOException
import kotlin.system.measureTimeMillis
import kotlin.time.*

@WireMockTest(httpPort = 9090)
@ExperimentalCoroutinesApi
@ExperimentalTime
class StubFailResponseTest: BaseStubTest() {

    @Test
    fun `request body should be equal to string`() {
        get {
            url equalTo "/simple"
        } fails {
            delay fixed 1000
            type = EMPTY_RESPONSE
        }

        val elapsed = measureTimeMillis {
            assertThatThrownBy {
                runTest {
                    client.get<HttpStatement>("http://localhost:9090/simple").execute()
                }
            }.isInstanceOf(IOException::class.java)
        }
        assertThat(elapsed).isGreaterThanOrEqualTo(1000)
    }
}