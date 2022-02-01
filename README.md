# DSL Kotlin for WireMock

This is library provides kotlin dsl for [Wiremock](http://wiremock.org/) stubbing.

[![Licence](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/valentin-goncharov/wiremock-kotlin-dsl/blob/master/LICENSE)

## Idea
The main idea is to combine the convenience of the declarative style of statistical JSON and the power of the imperative style of Kotlin code when creating WireMock stubs

Typical code with stubs looks like
```java
fun stubTest() {
    stubFor(get(urlPathMatching("/test/.*"))
      .willReturn(aResponse()
      .withStatus(200)
      .withHeader("Content-Type", "application/json")
      .withBody("{\"name\":\"testing-library\",\"value\":\"WireMock\"}")));
}
```

This is equivalent to JSON:
```json
{
  "request" : {
    "urlPathPattern" : "/test/.*",
    "method" : "GET"
  },
  "response" : {
    "status" : 200,
    "headers": {
      "Content-Type": "application/json"
    },
    "jsonBody": {"name":"testing-library","value":"WireMock"} 
  }
}
```

So with DSL this stub will look the next
```kotlin
fun stubTest() {
    get {
        url pathMatches "/test/.*"
    } returns {
            status = 200
            headers contain "Content-Type" equalTo "application/json"
            body json """{"name":"testing-library","value":"WireMock"}"""
    }
}
```
