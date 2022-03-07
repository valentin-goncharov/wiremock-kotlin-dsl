package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.junit5.WireMockTest
import io.ktor.client.features.*
import io.ktor.client.request.*
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

@WireMockTest(httpPort = 9090)
internal class JsonBodyMatchingTest: BaseStubTest() {

    @Test
    fun `request body should be equal to json`() {
        val jsonBody = """
            {
                "key": "value"
            }
        """

        post {
            body json jsonBody
        } returns {
            body json """
               {
                    "status": "OK"
               } 
            """
        }

        runTest {
            val response = client.post<String>("http://localhost:9090/path/any?query=anything") {
                body = "{\"key\":\"value\"}"
            }
            assertThat(response).isEqualTo("{\"status\":\"OK\"}")
        }
    }

    @Test
    fun `should fail if request body does not equal to json`() {
        val jsonBody = """
            {
                "key": "value"
            }
        """

        post {
            body json jsonBody
        }

        assertThatThrownBy {
            runTest {
                client.post<String>("http://localhost:9090/path/any?query=anything") {
                    body = "{\"key1\":\"value1\"}"
                }
            }
        }.isInstanceOf(ClientRequestException::class.java)
    }

    @Test
    fun `request body should be equal to json ignore array order`() {
        val jsonBody = """
            [
                {
                    "key": "value1"
                },
                {
                    "key": "value2"
                }
            ]
        """

        post {
            body json jsonBody ignore { arrayOrder = true }
        } returns {
            body json """
               {
                    "status": "OK"
               } 
            """
        }

        runTest {
            val response = client.post<String>("http://localhost:9090/path/any?query=anything") {
                body = "[{\"key\":\"value2\"},{\"key\":\"value1\"}]"
            }
            assertThat(response).isEqualTo("{\"status\":\"OK\"}")
        }
    }

    @Test
    fun `should fail when request body is json and contains array in the different order`() {
        val jsonBody = """
            [
                {
                    "key": "value1"
                },
                {
                    "key": "value2"
                }
            ]
        """

        post {
            body json jsonBody
        }

        assertThatThrownBy {
            runTest {
                client.post<String>("http://localhost:9090/path/any?query=anything") {
                    body = "[{\"key\":\"value2\"},{\"key\":\"value1\"}]"
                }
            }
        }.isInstanceOf(ClientRequestException::class.java)
    }

    @Test
    fun `request body should be equal to json ignore extra elements`() {
        val jsonBody = """
            {
                "key1": "value1"
            }
        """

        post {
            body json jsonBody ignore { extraElements = true }
        } returns {
            body json """
               {
                    "status": "OK"
               } 
            """
        }

        runTest {
            val response = client.post<String>("http://localhost:9090/path/any?query=anything") {
                body = "{\"key1\":\"value1\",\"key2\":\"value2\"}"
            }
            assertThat(response).isEqualTo("{\"status\":\"OK\"}")
        }
    }

    @Test
    fun `should fail when request body is json and contains unmapped field`() {
        val jsonBody = """
            {
                "key": "value"
            }
        """

        post {
            body json jsonBody
        }

        assertThatThrownBy {
            runTest {
                client.post<String>("http://localhost:9090/path/any?query=anything") {
                    body = "{\"key\":\"value\",\"key2\":\"value2\"}"
                }
            }
        }.isInstanceOf(ClientRequestException::class.java)
    }
}