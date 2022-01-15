package dsl.wiremock

import dsl.wiremock.stubs.get

class StubForTest {

    fun testStub() {
        get {
            url equalTo "http://localhost:8080/test/path"
            url matches  "http://localhost:8080/test/.*"
            url pathEqualTo  "/test/path"
            url pathMatches   "/test/.*"

            headers contain "X-Test-Equal" equalTo "test"
            headers contain "X-Test-Match" matches "t?est.*"
            headers contain "X-Test-Contains" contains "es"
            headers contain "X-Test-Not-Match" doesNotMatch "[0-9]+"
            headers contain "X-Test-Date" dateTime "2022-01-15 21:45:00"
            headers contain "X-Test-Date-After" after "2022-01-15 21:45:00"
            headers contain "X-Test-Date-Before" before "2022-01-15 21:45:00"
            headers doNotContain "X-Not"
        }
    }
}