package dsl.wiremock.request

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.AbsentPattern
import com.github.tomakehurst.wiremock.matching.AnythingPattern
import com.github.tomakehurst.wiremock.matching.StringValuePattern
import dsl.wiremock.WireMockDSL

private val ANY = AnythingPattern()

@WireMockDSL
sealed class NamedPatternScope {
    val patterns = mutableListOf<NamedPattern>()

    @WireMockDSL
    infix fun contain(key: String): NamedPattern {
        val pattern = NamedPattern(key)
        patterns.add(pattern)
        return pattern
    }

    @WireMockDSL
    infix fun doNotContain(key: String) : NamedPattern {
        patterns.removeAll { it.name == key }
        val pattern = NamedPattern(key, AbsentPattern.ABSENT)
        patterns.add(pattern)
        return pattern
    }
}

@WireMockDSL
class NamedPattern(val name: String, pattern: StringValuePattern = ANY) {

    var pattern: StringValuePattern = pattern
        private set

    @WireMockDSL
    infix fun equalTo(value: String): NamedPattern {
        this.pattern = WireMock.equalTo(value)
        return this
    }

    @WireMockDSL
    infix fun matches(regex: String): NamedPattern {
        this.pattern = WireMock.matching(regex)
        return this
    }

    @WireMockDSL
    infix fun doesNotMatch(regex: String): NamedPattern {
        this.pattern = WireMock.notMatching(regex)
        return this
    }

    @WireMockDSL
    infix fun contains(value: String): NamedPattern {
        this.pattern = WireMock.containing(value)
        return this
    }

    @WireMockDSL
    infix fun before(value: String): NamedPattern {
        this.pattern = WireMock.before(value)
        return this
    }

    @WireMockDSL
    infix fun after(value: String): NamedPattern {
        this.pattern = WireMock.after(value)
        return this
    }

    @WireMockDSL
    infix fun dateTime(value: String): NamedPattern {
        this.pattern = WireMock.equalToDateTime(value)
        return this
    }
}