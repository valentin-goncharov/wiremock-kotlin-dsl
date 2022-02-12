package dsl.wiremock.request.body

import com.github.tomakehurst.wiremock.client.WireMock
import dsl.wiremock.WireMockDSL

class JsonPathBodyPattern: BasePathBodyPattern {

    constructor(scope: RequestBodyScope): super(scope, WireMock::matchingJsonPath)

    constructor(pattern: RequestBodyPattern): super(pattern, WireMock::matchingJsonPath)

    fun matchingJsonPath(str: String): JunctionableBodyPattern {
        currentValue = str
        applyPattern(WireMock.matchingJsonPath(currentValue))
        return this
    }

    @WireMockDSL
    override infix fun equalToJson(str: String): JsonRequestBodyPattern {
        val value = str.trimIndent()
        modifyPattern(matchingPathPattern(this.currentValue, WireMock.equalToJson(value)))
        val jsonPattern = EqualToJsonBodyPattern(this, value, matchingPathPattern)
        scope.replace(this, jsonPattern)
        return jsonPattern
    }

    @WireMockDSL
    override infix fun equalToXml(str: String): XmlRequestBodyPattern {
        val value = str.trimIndent()
        modifyPattern(matchingPathPattern(this.currentValue, WireMock.equalToXml(value)))
        val xmlPattern = EqualToXmlBodyPattern(
            this,
            value,
            WireMock.equalToXml(value),
            matchingPathPattern
        )
        scope.replace(this, xmlPattern)
        return xmlPattern
    }
}