package dsl.wiremock.request

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.StringValuePattern
import dsl.wiremock.WireMockDSL

class HostPattern {

    lateinit var pattern: StringValuePattern

    @WireMockDSL
    infix fun equalTo(value: String): HostPattern {
        pattern = WireMock.equalTo(value)
        return this
    }

    @WireMockDSL
    infix fun matches(regex: String): HostPattern {
        pattern = WireMock.matching(regex)
        return this
    }

    @WireMockDSL
    infix fun doesNotMatch(regex: String): HostPattern {
        pattern = WireMock.notMatching(regex)
        return this
    }

    @WireMockDSL
    infix fun contains(value: String): HostPattern {
        pattern = WireMock.containing(value)
        return this
    }

    fun isInitialized(): Boolean {
        return this::pattern.isInitialized
    }
}