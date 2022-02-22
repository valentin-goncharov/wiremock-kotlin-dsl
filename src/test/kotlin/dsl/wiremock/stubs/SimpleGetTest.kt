package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import io.ktor.client.request.*
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension


@WireMockTest
internal class SimpleGetTest: BaseStubTest() {

    @RegisterExtension
    val wireMock: WireMockExtension = WireMockExtension.newInstance()
        .options(wireMockConfig().port(9090))
        .build()

    @Test
    fun `simple get request should return string response`() {
        val str = "Simple response OK"
        wireMock.singleGetMapping(str)

        runTest {
            val response = client.get<String>("http://localhost:9090/simple")
            assertThat(response).isEqualTo(str)
        }
    }
}