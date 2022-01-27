package dsl.wiremock

import com.github.tomakehurst.wiremock.junit5.WireMockTest
import dsl.wiremock.request.dateTime
import dsl.wiremock.response.FaultType.CONNECTION_RESET_BY_PEER
import dsl.wiremock.stubs.get
import dsl.wiremock.stubs.scenario
import org.junit.jupiter.api.Test
import java.time.LocalDate

@WireMockTest
class StubForTest {

    @Test
    fun testStub() {
        get {
            id = "8d463e72-790a-40d7-8ffa-ffca31d1bdf6"
            name = "test stub"
            priority = 1

            url equalTo "http://localhost:8080/test/path"
            url matches "http://localhost:8080/test/.*"
            url pathEqualTo "/test/path"
            url pathMatches "/test/.*"

            headers contain "X-Test-Equal" equalTo "test" or "X-Test-Equal" matches "test"
            headers contain "X-Test-Match" matches "t?est.*" and "X-Test-Match" doesNotMatch "testo.+"
            headers contain "X-Test-Contains" contains "test" or ("X-Test-Contains" dateTime "2022-01-15 21:45:00" and
                    "X-Test-Contains" equalTo "sdsd")
            headers contain "X-Test-Not-Match" doesNotMatch "[0-9]+"
            headers contain "X-Test-Date" dateTime "2022-01-15" actualFormat "yyyy-MM-dd"
            headers contain "X-Test-Date-After" after "2022-01-15 21:45:00" truncateActual "last day of month"
            headers contain "X-Test-Date-Before" before "2022-01-15 21:45:00"
            headers doNotContain "X-Not"

            cookies contain "Session"
            cookies doNotContain "Version"

            queryParameters contain "startDate" after "2022-01-16 11:45:00" actualFormat "yyyy-MM-dd HH:mm:ss"
            queryParameters doNotContain "endDate"

            body json """
               {
                "key": "value" 
               } 
            """ or (body matches ".*es.*" and body doesNotMatch "sss")

            metadata {
                "attribute" attr "value"
                "nested" metadata {
                    "list" list listOf(1, LocalDate.now(), "some string")
                }
            }
        } returns {
            status = 200
            headers contain "ETag" equalTo "56d-9989200-1132c580"

            body json """
                    {
                        "key": "value"
                    }
                """
        }
    }

    @Test
    fun testScenarioStub() {
        scenario {
            name = "Test scenario"
            get {
                id = "8d463e72-790a-40d7-8ffa-ffca31d1bdf6"
                name = "test get stub"
                priority = 1
                state = "First"


                url equalTo "http://localhost:8080/test/path"
                url matches "http://localhost:8080/test/.*"
                url pathEqualTo "/test/path"
                url pathMatches "/test/.*"

                headers contain "X-Test-Equal" equalTo "test"
                headers contain "X-Test-Match" matches "t?est.*"
                headers contain "X-Test-Contains" contains "es"
                headers contain "X-Test-Not-Match" doesNotMatch "[0-9]+"
                headers contain "X-Test-Date" dateTime "2022-01-15 21:45:00"
                headers contain "X-Test-Date-After" after "2022-01-15 21:45:00"
                headers contain "X-Test-Date-Before" before "2022-01-15 21:45:00"
                headers doNotContain "X-Not"

                cookies contain "Session"
                cookies doNotContain "Version"

                queryParameters contain "startDate" after "2022-01-16 11:45:00"
                queryParameters doNotContain "endDate"

            } returns {
                status = 200
                headers contain "ETag" equalTo "56d-9989200-1132c580"

                body json """
                        {
                            "key": "value"
                        }
                    """
            }


            post {
                id = "8d463e72-790a-40d7-8ffa-ffca31d1bdf6"
                name = "test post stub"
                priority = 1
                require = "First"
                state = "Second"


                url equalTo "http://localhost:8080/test/path"
                url pathMatches "/test/.*"

                headers contain "X-Test-Equal" equalTo "test"

                cookies contain "Session"
                cookies doNotContain "Version"

                queryParameters contain "startDate" after "2022-01-16 11:45:00"
                queryParameters doNotContain "endDate"

                body json """
                   {
                    "key": "value" 
                   } 
                """
            } fails {
                delay fixed 1000
                type = CONNECTION_RESET_BY_PEER

            }
        }
    }
}