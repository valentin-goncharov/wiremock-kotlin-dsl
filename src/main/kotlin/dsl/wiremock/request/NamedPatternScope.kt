package dsl.wiremock.request

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.common.DateTimeTruncation
import com.github.tomakehurst.wiremock.matching.*
import dsl.wiremock.WireMockDSL
import java.lang.Exception

private val ANY = AnythingPattern()

private const val ERROR_MESSAGE = "Names should be equal"

@WireMockDSL
sealed class NamedPatternScope {
    val patterns = mutableListOf<NamedPattern>()

    @WireMockDSL
    infix fun contain(key: String): NamedPattern {
        val pattern = NamedPattern(this, key)
        patterns.add(pattern)
        return pattern
    }

    @WireMockDSL
    infix fun doNotContain(key: String) : NamedPattern {
        patterns.removeAll { it.name == key }
        val pattern = NamedPattern(this, key, AbsentPattern.ABSENT )
        patterns.add(pattern)
        return pattern
    }

    fun replace(oldPattern: NamedPattern, newPattern: NamedPattern) {
        patterns.remove(oldPattern)
        patterns.add(newPattern)
    }
}

@WireMockDSL
open class NamedPattern(
    
    private val scope: NamedPatternScope = defaultScope,
    val name: String,
    pattern: StringValuePattern = ANY) {

    private var junctionPattern: ((Array<StringValuePattern>) -> StringValuePattern)? = null

    var pattern: StringValuePattern = pattern
        protected set

    @WireMockDSL
    infix fun equalTo(value: String): NamedPattern {
        val pattern = WireMock.equalTo(value)
        this.pattern = junctionPattern?.invoke(arrayOf(this.pattern, pattern)) ?: pattern
        junctionPattern = null
        return this
    }

    @WireMockDSL
    infix fun matches(regex: String): NamedPattern {
        val pattern = WireMock.matching(regex)
        this.pattern = junctionPattern?.invoke(arrayOf(this.pattern, pattern)) ?: pattern
        junctionPattern = null
        return this
    }

    @WireMockDSL
    infix fun doesNotMatch(regex: String): NamedPattern {
        val pattern = WireMock.notMatching(regex)
        this.pattern = junctionPattern?.invoke(arrayOf(this.pattern, pattern)) ?: pattern
        junctionPattern = null
        return this
    }

    @WireMockDSL
    infix fun contains(value: String): NamedPattern {
        val pattern = WireMock.containing(value)
        this.pattern = junctionPattern?.invoke(arrayOf(this.pattern, pattern)) ?: pattern
        junctionPattern = null
        return this
    }

    @WireMockDSL
    infix fun before(value: String): DateTimeNamedPattern {
        val pattern = WireMock.before(value)
        this.pattern = junctionPattern?.invoke(arrayOf(this.pattern, pattern)) ?: pattern
        junctionPattern = null
        val dateTimePattern = DateTimeNamedPattern(scope, name, this.pattern)
        scope.replace(this, dateTimePattern)
        return dateTimePattern
    }

    @WireMockDSL
    infix fun after(value: String): DateTimeNamedPattern {
        val pattern = WireMock.after(value)
        this.pattern = junctionPattern?.invoke(arrayOf(this.pattern, pattern)) ?: pattern
        junctionPattern = null
        val dateTimePattern = DateTimeNamedPattern(scope, name, this.pattern)
        scope.replace(this, dateTimePattern)
        return dateTimePattern
    }

    @WireMockDSL
    infix fun dateTime(value: String): DateTimeNamedPattern {
        val pattern = WireMock.equalToDateTime(value)
        this.pattern = junctionPattern?.invoke(arrayOf(this.pattern, pattern)) ?: pattern
        junctionPattern = null
        val dateTimePattern = DateTimeNamedPattern(scope, name, this.pattern)
        scope.replace(this, dateTimePattern)
        return dateTimePattern
    }

    @WireMockDSL
    infix fun or(name: String): NamedPattern {
        if (this.name != name) {
            throw IllegalArgumentException(ERROR_MESSAGE)
        }
        junctionPattern = WireMock::or
        return this
    }

    @WireMockDSL
    infix fun or(namedPattern: NamedPattern): NamedPattern {
        if (this.name != namedPattern.name) {
            throw IllegalArgumentException(ERROR_MESSAGE)
        }
        this.pattern = WireMock.or(this.pattern, namedPattern.pattern)
        junctionPattern = null
        return this
    }

    @WireMockDSL
    infix fun and(name: String): NamedPattern {
        if (this.name != name) {
            throw IllegalArgumentException(ERROR_MESSAGE)
        }
        junctionPattern = WireMock::and
        return this
    }

    @WireMockDSL
    infix fun and(namedPattern: NamedPattern): NamedPattern {
        if (this.name != namedPattern.name) {
            throw IllegalArgumentException(ERROR_MESSAGE)
        }
        this.pattern = WireMock.and(this.pattern, namedPattern.pattern)
        junctionPattern = null
        return this
    }


}

class DateTimeNamedPattern(
    scope: NamedPatternScope,
    name:String,
    pattern: StringValuePattern
): NamedPattern(scope, name, pattern) {

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

object DefaultScope: NamedPatternScope()
val defaultScope = DefaultScope

@WireMockDSL
infix fun String.equalTo(value: String): NamedPattern = NamedPattern(
    defaultScope,
    this,
    WireMock.equalTo(value)
)

@WireMockDSL
infix fun String.matches(regex: String): NamedPattern = NamedPattern(
    defaultScope,
    this,
    WireMock.matching(regex)
)

@WireMockDSL
infix fun String.doesNotMatch(regex: String): NamedPattern = NamedPattern(
    defaultScope,
    this,
    WireMock.notMatching(regex)
)

@WireMockDSL
infix fun String.contains(value: String): NamedPattern = NamedPattern(
    defaultScope,
    this,
    WireMock.containing(value)
)

@WireMockDSL
infix fun String.before(value: String): DateTimeNamedPattern = DateTimeNamedPattern(
    defaultScope,
    this,
    WireMock.before(value)
)

@WireMockDSL
infix fun String.after(value: String): DateTimeNamedPattern = DateTimeNamedPattern(
    defaultScope,
    this,
    WireMock.after(value)
)

@WireMockDSL
infix fun String.dateTime(value: String): DateTimeNamedPattern  = DateTimeNamedPattern(
    defaultScope,
    this,
    WireMock.equalToDateTime(value)
)