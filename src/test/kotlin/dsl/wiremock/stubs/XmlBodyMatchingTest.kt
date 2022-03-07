package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.junit5.WireMockTest
import io.ktor.client.features.*
import io.ktor.client.request.*
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

@WireMockTest(httpPort = 9090)
internal class XmlBodyMatchingTest: BaseStubTest() {

    @Test
    fun `request body should be equal to xml`() {
        val xmlBody = """
            <xml>test</xml>
        """

        post {
            body xml xmlBody
        } returns {
            body string "OK"
        }

        runTest {
            val response = client.post<String>("http://localhost:9090/path/any?query=anything") {
                body = "<xml>test</xml>"
            }
            assertThat(response).isEqualTo("OK")
        }
    }

    @Test
    fun `should fail if request body does not equal to xml`() {
        val xmlBody = """
            <xml>test</xml>
        """

        post {
            body xml xmlBody
        } returns {
            body string "OK"
        }

        assertThatThrownBy {
            runTest {
                client.post<String>("http://localhost:9090/path/any?query=anything") {
                    body = "<test>xml</test>"
                }
            }
        }.isInstanceOf(ClientRequestException::class.java)
    }

    @Test
    fun `request body should be equal to xml considering placeholders`() {
        val xmlBody = """
            <xml>
                <id>[[xmlunit.ignore]]</id>
                <message>test</message>
            </xml>
        """

        post {
            body xml xmlBody placeholders {
                enabled = true
                openingDelimiterRegex = "\\[\\["
                closingDelimiterRegex = "]]"
            }
        } returns {
            body string "OK"
        }

        runTest {
            val response = client.post<String>("http://localhost:9090/path/any?query=anything") {
                body = "<xml><id>1234567</id><message>test</message></xml>"
            }
            assertThat(response).isEqualTo("OK")
        }
    }

    @Test
    fun `should fail when request body is xml but placeholders disabled`() {
        val xmlBody = """
            <xml>
                <id>[[xmlunit.ignore]]</id>
                <message>test</message>
            </xml>
        """

        post {
            body xml xmlBody
        } returns {
            body string "OK"
        }

        assertThatThrownBy {
            runTest {
                client.post<String>("http://localhost:9090/path/any?query=anything") {
                    body = "<xml><id>1234567</id><message>test</message></xml>"
                }
            }
        }.isInstanceOf(ClientRequestException::class.java)
    }

    @Test
    fun `request body should be equal to xml except comparisons`() {
        val expected = """
            <stuff attr="1">
                <thing>Match this</thing>
            </stuff>
            """

        val actual = """
            <stuff attr="2">
                <thing>Match this</thing>
            </stuff>
            """

        post {
            body xml expected exemptComparison "ATTR_VALUE"
        } returns {
            body string "OK"
        }

        runTest {
            val response = client.post<String>("http://localhost:9090/path/any?query=anything") {
                body = actual
            }
            assertThat(response).isEqualTo("OK")
        }
    }

    @Test
    fun `should fail when request body is xml and comparison types are not exempted`() {
        val expected = """
            <stuff attr="1">
                <thing>Match this</thing>
            </stuff>
            """

        val actual = """
            <stuff attr="2">
                <thing>Match this</thing>
            </stuff>
            """

        post {
            body xml expected
        } returns {
            body string "OK"
        }

        assertThatThrownBy {
            runTest {
                client.post<String>("http://localhost:9090/path/any?query=anything") {
                    body = actual
                }
            }
        }.isInstanceOf(ClientRequestException::class.java)
    }

}