package dsl.wiremock.request

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.AbsentPattern
import com.github.tomakehurst.wiremock.matching.AnythingPattern
import com.github.tomakehurst.wiremock.matching.StringValuePattern
import dsl.wiremock.WireMockDSL

private val ANY = AnythingPattern()

@WireMockDSL
class HeadersScope {
    val patterns = mutableListOf<Header>()

    @WireMockDSL
    infix fun contain(key: String): Header {
        val pattern = Header(key)
        patterns.add(pattern)
        return pattern
    }

    @WireMockDSL
    infix fun doNotContain(key: String) : Header {
        patterns.removeAll { it.name == key }
        val pattern = Header(key, AbsentPattern.ABSENT)
        patterns.add(pattern)
        return pattern
    }
}

@WireMockDSL
open class Header(val name: String, pattern: StringValuePattern = ANY) {

    var pattern: StringValuePattern = pattern
        private set

    @WireMockDSL
    open infix fun equalTo(value: String): Header {
        this.pattern = WireMock.equalTo(value)
        return this
    }

    @WireMockDSL
    open infix fun matches(regex: String): Header {
        this.pattern = WireMock.matching(regex)
        return this
    }

    @WireMockDSL
    open infix fun doesNotMatch(regex: String): Header {
        this.pattern = WireMock.notMatching(regex)
        return this
    }

    @WireMockDSL
    open infix fun contains(value: String): Header {
        this.pattern = WireMock.containing(value)
        return this
    }

    @WireMockDSL
    open infix fun before(value: String): Header {
        this.pattern = WireMock.before(value)
        return this
    }

    @WireMockDSL
    open infix fun after(value: String): Header {
        this.pattern = WireMock.after(value)
        return this
    }

    @WireMockDSL
    open infix fun dateTime(value: String): Header {
        this.pattern = WireMock.equalToDateTime(value)
        return this
    }
}