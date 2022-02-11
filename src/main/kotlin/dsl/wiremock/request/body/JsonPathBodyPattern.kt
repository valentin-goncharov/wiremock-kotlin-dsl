package dsl.wiremock.request.body

import com.github.tomakehurst.wiremock.client.WireMock
import dsl.wiremock.WireMockDSL

class JsonPathBodyPattern: StringValueRequestBodyPattern {

    constructor(scope: RequestBodyScope): super(scope)

    constructor(pattern: RequestBodyPattern): super(pattern)

    fun matchingJsonPath(str: String): JunctionableBodyPattern {
        currentValue = str
        applyPattern(WireMock.matchingJsonPath(currentValue))
        return this
    }

    @WireMockDSL
    override infix fun equalToJson(str: String): JsonRequestBodyPattern {
        val value = str.trimIndent()
        modifyPattern(WireMock.matchingJsonPath(this.currentValue, WireMock.equalToJson(value)))
        val jsonPattern = EqualToJsonBodyPattern(this, value)
        scope.replace(this, jsonPattern)
        return jsonPattern
    }

    @WireMockDSL
    override infix fun equalToXml(str: String): JunctionableBodyPattern {
        modifyPattern(WireMock.matchingJsonPath(this.currentValue, WireMock.equalToXml(str.trimIndent())))
        TODO("Fix when implement XmlPathBodyPattern")
    }

    @WireMockDSL
    override infix fun equalTo(str: String): JunctionableBodyPattern {
        modifyPattern(WireMock.matchingJsonPath(this.currentValue, WireMock.equalTo(str)))
        return this
    }

    @WireMockDSL
    override infix fun contains(str: String): JunctionableBodyPattern {
        modifyPattern(WireMock.matchingJsonPath(this.currentValue, WireMock.containing(str)))
        return this
    }

    @WireMockDSL
    override infix fun matches(str: String): JunctionableBodyPattern {
        modifyPattern(WireMock.matchingJsonPath(this.currentValue, WireMock.matching(str)))
        return this
    }

    @WireMockDSL
    override infix fun doesNotMatch(str: String): JunctionableBodyPattern {
        modifyPattern(WireMock.matchingJsonPath(this.currentValue, WireMock.notMatching(str)))
        return this
    }
}

internal class EqualToJsonBodyPattern(pattern: RequestBodyPattern, value: String)
    : StringValueRequestBodyPattern(pattern), JsonRequestBodyPattern {

    private val jsonValue: String = value

    @WireMockDSL
    override infix fun ignore(fn: JsonIgnoreScope.()->Unit): StringValuePatternWrapper {
        val ignoreScope = JsonIgnoreScope()
        ignoreScope.apply(fn)
        modifyPattern(
            WireMock.matchingJsonPath(
                this.currentValue,
                WireMock.equalToJson(jsonValue, ignoreScope.arrayOrder, ignoreScope.extraElements)
            )
        )
        return this
    }
}