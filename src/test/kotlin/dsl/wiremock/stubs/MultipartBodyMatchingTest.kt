package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.junit5.WireMockTest
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

@WireMockTest(httpPort = 9090)
@ExperimentalCoroutinesApi
internal class MultipartBodyMatchingTest: BaseStubTest() {

    @Test
    fun `request body should match to multipart data`() {

        val bodyPart1 = """{"key1": "value1"}"""
        val bodyPart2 = """{"key2": "value2"}"""

        post {
            multipart {
                type = "ALL"
                headers contain "Content-Disposition"
            }
            multipart {
                type = "ANY"
                headers contain "Content-Disposition" contains "name=test1"
                body json bodyPart1
            }
            multipart {
                type = "ANY"
                headers contain "Content-Disposition" contains "name=test2"
                body json bodyPart2
            }
        } returns {
            body json """
               {
                    "status": "OK"
               } 
            """
        }

        runTest {
            val response = client.post<String>("http://localhost:9090/path/any?query=anything") {
                body = MultiPartFormDataContent(
                    formData {
                        appendInput(
                            key = "test1",
                            size = bodyPart1.length.toLong()
                        ) { buildPacket { writeFully(bodyPart1.toByteArray()) } }
                        appendInput(
                            key = "test2",
                            size = bodyPart2.length.toLong()
                        ) { buildPacket { writeFully(bodyPart2.toByteArray()) } }
                    }
                )
            }
            assertThat(response).isEqualTo("{\"status\":\"OK\"}")
        }
    }


    @Test
    fun `should fail if request body does not match to multipart data`() {
        val bodyPart1 = """{"key1": "value1"}"""
        val bodyPart2 = """{"key2": "value2"}"""

        post {
            multipart {
                type = "ALL"
                headers contain "Content-Disposition"
            }
            multipart {
                type = "ANY"
                headers contain "Content-Disposition" contains "name=test1"
                body json bodyPart2
            }
            multipart {
                type = "ANY"
                headers contain "Content-Disposition" contains "name=test2"
                body json bodyPart1
            }
        }

        assertThatThrownBy {
            runTest {
                client.post<String>("http://localhost:9090/path/any?query=anything") {
                    body = MultiPartFormDataContent(
                        formData {
                            appendInput(
                                key = "test1",
                                size = bodyPart1.length.toLong()
                            ) { buildPacket { writeFully(bodyPart1.toByteArray()) } }
                            appendInput(
                                key = "test2",
                                size = bodyPart2.length.toLong()
                            ) { buildPacket { writeFully(bodyPart2.toByteArray()) } }
                        }
                    )
                }
            }
        }.isInstanceOf(ClientRequestException::class.java)
    }
}