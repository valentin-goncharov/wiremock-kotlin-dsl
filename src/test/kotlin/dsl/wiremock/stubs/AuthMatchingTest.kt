package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.junit5.WireMockTest
import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.statement.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


@WireMockTest(httpPort = 9090)
@ExperimentalCoroutinesApi
internal class AuthMatchingTest: BaseStubTest() {

    private lateinit var clientAuth: HttpClient
    @BeforeEach
    fun initAuth() {
        clientAuth = HttpClient(Java) {
            install(Auth) {
                basic {
                    sendWithoutRequest {
                        true
                    }
                    credentials {
                        BasicAuthCredentials(username = "wiremockdsl", password = "passw0rd")
                    }
                    realm = "Access to the '/' path"
                }
            }
        }
    }

    @Test
    fun `authentication should match basic auth header`() {
        get {
            url equalTo "any"
            priority = 1
            authentication username "wiremockdsl" password "passw0rd"
        } returns {
            body string "Authenticated"
        }

        any {
            url equalTo "any"
            priority = 10
        } returns {
            status = 401
            body string "Not Authorized"
        }

        runTest {
            try {
                val responseAuth = clientAuth.get<String>("http://localhost:9090/path/other?query=trash")
                assertThat(responseAuth).isEqualTo("Authenticated")

                client.get<String>("http://localhost:9090/path/any?query=anything")
            } catch (cause: ClientRequestException) {
                assertThat(cause.response.status.value).isEqualTo(401)
                assertThat(cause.response.readText()).isEqualTo("Not Authorized")
            }
        }
    }
}