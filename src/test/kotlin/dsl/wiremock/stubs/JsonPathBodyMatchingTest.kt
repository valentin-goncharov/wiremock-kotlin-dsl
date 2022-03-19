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
internal class JsonPathBodyMatchingTest: BaseStubTest() {

    @Test
    fun `request body should match to json path`() {
        val jsonBody = """
            {
                "values": [
                    {
                        "key": "value1"     
                    },
                    {
                        "key": "value2"     
                    },
                ]
            }
        """

        post {
            body jsonPath "$[?(@.values.size() == 2)]"
        } returns {
            body json """
               {
                    "status": "OK"
               } 
            """
        }

        runTest {
            val response = client.post<String>("http://localhost:9090/path/any?query=anything") {
                body = jsonBody
            }
            assertThat(response).isEqualTo("{\"status\":\"OK\"}")
        }
    }

    @Test
    fun `should fail if request body does not match to json path`() {
        val jsonBody = """
            {
                "values": [
                    {
                        "key": "value"
                    }
                ]
            }
        """

        post {
            body jsonPath "$[?(@.values.size() == 2)]"
        }

        assertThatThrownBy {
            runTest {
                client.post<String>("http://localhost:9090/path/any?query=anything") {
                    body = jsonBody
                }
            }
        }.isInstanceOf(ClientRequestException::class.java)
    }

    @Test
    fun `request body should match to json path expression`() {
        val jsonBody = """
            {
                "values": [
                    {
                        "key": "value1"     
                    },
                    {
                        "key": "value2"     
                    },
                ]
            }
        """

        post {
            body jsonPath "$.values[0].key" equalTo "value1"
        } returns {
            body json """
               {
                    "status": "OK"
               } 
            """
        }

        runTest {
            val response = client.post<String>("http://localhost:9090/path/any?query=anything") {
                body = jsonBody
            }
            assertThat(response).isEqualTo("{\"status\":\"OK\"}")
        }
    }

    @Test
    fun `should fail if request body does not match to json path expression`() {
        val jsonBody = """
            {
                "values": [
                    {
                        "key": "value"
                    }
                ]
            }
        """

        post {
            body jsonPath "$.values[0].key" equalTo "value1"
        }

        assertThatThrownBy {
            runTest {
                client.post<String>("http://localhost:9090/path/any?query=anything") {
                    body = jsonBody
                }
            }
        }.isInstanceOf(ClientRequestException::class.java)
    }

}