package dsl.wiremock.request

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.UrlPattern
import dsl.wiremock.WireMockDSL

@WireMockDSL
class UrlScope {

    var pattern: UrlPattern = UrlPattern.ANY
        private set

    @WireMockDSL
    infix fun equalTo(str: String) {
        pattern = if ("any" == str) UrlPattern.ANY else WireMock.urlEqualTo(str)
    }

    @WireMockDSL
    infix fun matches(str: String) {
        pattern = WireMock.urlMatching(str)
    }

    @WireMockDSL
    infix fun pathEqualTo(str: String) {
        pattern = WireMock.urlPathEqualTo(str)
    }

    @WireMockDSL
    infix fun pathMatches(str: String) {
        pattern = WireMock.urlPathMatching(str)
    }
}