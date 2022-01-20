package dsl.wiremock.request

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.common.DateTimeTruncation
import com.github.tomakehurst.wiremock.matching.*
import dsl.wiremock.WireMockDSL
import java.lang.Exception

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
open class NamedPattern(val name: String, pattern: StringValuePattern = ANY) {

    var pattern: StringValuePattern = pattern
        protected set

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
    infix fun before(value: String): DateTimeNamedPattern {
        val dateTimePattern = DateTimeNamedPattern(name, WireMock.before(value))
        this.pattern = dateTimePattern.pattern
        return dateTimePattern
    }

    @WireMockDSL
    infix fun after(value: String): DateTimeNamedPattern {
        val dateTimePattern = DateTimeNamedPattern(name, WireMock.after(value))
        this.pattern = dateTimePattern.pattern
        return dateTimePattern
    }

    @WireMockDSL
    infix fun dateTime(value: String): DateTimeNamedPattern {
        val dateTimePattern = DateTimeNamedPattern(name, WireMock.equalToDateTime(value))
        this.pattern = dateTimePattern.pattern
        return dateTimePattern
    }
}

class DateTimeNamedPattern(name:String, pattern: StringValuePattern): NamedPattern(name, pattern) {

    @WireMockDSL
    infix fun actualFormat(value: String): DateTimeNamedPattern {
        val datetimePattern: AbstractDateTimePattern = (pattern as AbstractDateTimePattern).actualFormat(value)
        pattern = datetimePattern
        return this
    }

    @WireMockDSL
    infix fun truncateExpected(value: String): DateTimeNamedPattern {
        val datetimePattern: AbstractDateTimePattern = (pattern as AbstractDateTimePattern)
            .truncateExpected(value.toDateTimeTruncation())
        pattern = datetimePattern
        return this
    }

    @WireMockDSL
    infix fun truncateActual(value: String): DateTimeNamedPattern {
        val datetimePattern: AbstractDateTimePattern = (pattern as AbstractDateTimePattern)
            .truncateActual( value.toDateTimeTruncation())
        pattern = datetimePattern
        return this
    }

    private fun String.toDateTimeTruncation(): DateTimeTruncation {
        try{
            return DateTimeTruncation.fromString(this)
        } catch (e: Exception) {
            throw IllegalArgumentException("Unknown truncation expression")
        }
    }
}