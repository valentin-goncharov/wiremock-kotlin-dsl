package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.junit5.WireMockTest
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.HttpStatusCode.Companion.Accepted
import io.ktor.http.HttpStatusCode.Companion.OK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@WireMockTest(httpPort = 9090)
@ExperimentalCoroutinesApi
class ScenarioStubTest: BaseStubTest() {

    @Test
    fun `scenario should pass through states`() {
        scenario {
            name = "Unit test scenario"

            get {
                name = "initial get"
                state = "initialized"

                url equalTo "/keys"

            } returns {
                status = 200

                body json """
                    [
                        {
                            "key1": "value1"
                        },
                        {
                            "key2": "value2"
                        }
                    ]
                    """
            }

            put {
                name = "update"
                require = "initialized"
                state = "updated"

                url equalTo "/keys"

                body json """
                    [
                        {
                            "key1": "value1"
                        },
                        {
                            "key2": "value2"
                        },
                        {
                            "key3": "value3"
                        }
                    ]
                    """
            } returns {
                status = 202
                body json """
                    [
                        {
                            "key1": "value1"
                        },
                        {
                            "key2": "value2"
                        },
                        {
                            "key3": "value3"
                        }
                    ]
                    """
            }

            get {
                name = "get updated"
                require = "updated"

                url equalTo "/keys"

            } returns {
                status = 200

                body json """
                    [
                        {
                            "key1": "value1"
                        },
                        {
                            "key2": "value2"
                        },
                        {
                            "key3": "value3"
                        }
                    ]
                    """
            }

            get {
                name = "get all values"

                url equalTo "/values"

            } returns {
                status = 200

                body json """["value1", "value2", "value3"]"""
            }

            delete {
                require = "updated"
                state = "final"

                url equalTo "/keys?key=key2"
            } returns {
                status = 200

                body json """
                    [
                        {
                            "key1": "value1"
                        },
                        {
                            "key3": "value3"
                        }
                    ]
                    """
            }

        }

        runTest {
            val response1: HttpResponse = client.get<HttpStatement>("http://localhost:9090/keys").execute()
            assertThat(response1.status).isEqualTo(OK)
            assertThat(response1.receive<String>()).isEqualTo("""[{"key1":"value1"},{"key2":"value2"}]""")

            val response2: HttpResponse = client.put<HttpStatement>("http://localhost:9090/keys") {
                body = """[{"key1":"value1"},{"key2":"value2"},{"key3":"value3"}]"""
            }.execute()
            assertThat(response2.status).isEqualTo(Accepted)
            assertThat(response2.receive<String>())
                .isEqualTo("""[{"key1":"value1"},{"key2":"value2"},{"key3":"value3"}]""")

            val response3: HttpResponse = client.get<HttpStatement>("http://localhost:9090/keys").execute()
            assertThat(response3.status).isEqualTo(OK)
            assertThat(response3.receive<String>())
                .isEqualTo("""[{"key1":"value1"},{"key2":"value2"},{"key3":"value3"}]""")

            val response31: HttpResponse = client.get<HttpStatement>("http://localhost:9090/values").execute()
            assertThat(response31.status).isEqualTo(OK)
            assertThat(response31.receive<String>())
                .isEqualTo("""["value1","value2","value3"]""")

            val response4: HttpResponse = client.delete<HttpStatement>("http://localhost:9090/keys?key=key2").execute()
            assertThat(response3.status).isEqualTo(OK)
            assertThat(response4.receive<String>()).isEqualTo("""[{"key1":"value1"},{"key3":"value3"}]""")

            val response41: HttpResponse = client.get<HttpStatement>("http://localhost:9090/values").execute()
            assertThat(response41.status).isEqualTo(OK)
            assertThat(response41.receive<String>())
                .isEqualTo("""["value1","value2","value3"]""")
        }
    }
}