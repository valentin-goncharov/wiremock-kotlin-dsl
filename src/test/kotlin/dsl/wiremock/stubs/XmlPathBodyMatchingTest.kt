package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.junit5.WireMockTest
import io.ktor.client.features.*
import io.ktor.client.request.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

@WireMockTest(httpPort = 9090)
@ExperimentalCoroutinesApi
internal class XmlPathBodyMatchingTest: BaseStubTest() {

    @Test
    fun `request body should match to xml path`() {
        val xmlBody = """
            <test>
                <key>value1</key>
                <key>value2</key>
            </test>
        """

        post {
            body xmlPath "//test[count(key) = 2]"
        } returns {
            body string "OK"
        }

        runTest {
            val response = client.post<String>("http://localhost:9090/path/any?query=anything") {
                body = xmlBody
            }
            Assertions.assertThat(response).isEqualTo("OK")
        }
    }

    @Test
    fun `should fail if request body does not match to xml path`() {
        val xmlBody = """
            <test>
                <key>value</key>
            </test>
        """

        post {
            body xmlPath "//test/abc"
        }

        assertThatThrownBy {
            runTest {
                client.post<String>("http://localhost:9090/path/any?query=anything") {
                    body = xmlBody
                }
            }
        }.isInstanceOf(ClientRequestException::class.java)
    }

    @Test
    fun `request body should match to xml path expression`() {
        val xmlBody = """
            <test>
                <key id="100">value1</key>
                <key>value2</key>
            </test>
        """

        post {
            body xmlPath "//test/key[1]/@id" matches "\\d+"
        } returns {
            body string "OK"
        }

        runTest {
            val response = client.post<String>("http://localhost:9090/path/any?query=anything") {
                body = xmlBody
            }
            Assertions.assertThat(response).isEqualTo("OK")
        }
    }

    @Test
    fun `should fail if request body does not match to xml path expression`() {
        val xmlBody = """
            <test>
                <key>value</key>
                <key attr="abc">value</key>
            </test>
        """

        post {
            body xmlPath "//test/key[2]/@attr" equalTo "123"
        }

        assertThatThrownBy {
            runTest {
                client.post<String>("http://localhost:9090/path/any?query=anything") {
                    body = xmlBody
                }
            }
        }.isInstanceOf(ClientRequestException::class.java)
    }

    @Test
    fun `request body should match to xml path with namespaces`() {
        val xmlBody = """
            <list xmlns="https://test.example.com">
                <key>value1</key>
                <key>value2</key>
            </list>
        """

        post {
            body xmlPath "//test:list/test:key[1]/text()" namespace "test = https://test.example.com" equalTo "value1"
        } returns {
            body string "OK"
        }

        runTest {
            val response = client.post<String>("http://localhost:9090/path/any?query=anything") {
                body = xmlBody
            }
            Assertions.assertThat(response).isEqualTo("OK")
        }
    }

    @Test
    fun `should fail if request body matches to xml path but with wrong namespace`() {
        val xmlBody = """
            <list xmlns="https://staff.example.com">
                <key>value1</key>
                <key>value2</key>
            </list>
        """

        post {
            body xmlPath "//test:list/test:key[1]/text()" namespace "test = https://test.example.com" equalTo "value1"
        }

        assertThatThrownBy {
            runTest {
                client.post<String>("http://localhost:9090/path/any?query=anything") {
                    body = xmlBody
                }
            }
        }.isInstanceOf(ClientRequestException::class.java)
    }

}