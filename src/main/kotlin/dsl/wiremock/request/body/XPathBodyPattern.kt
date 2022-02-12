package dsl.wiremock.request.body

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.MatchesXPathPattern
import com.github.tomakehurst.wiremock.matching.StringValuePattern
import dsl.wiremock.WireMockDSL

class XPathBodyPattern: BasePathBodyPattern, XPathRequestBodyPattern {

    constructor(scope: RequestBodyScope): super(scope, WireMock::matchingXPath)

    constructor(pattern: RequestBodyPattern): super(pattern, WireMock::matchesXPathWithSubMatcher)

    private val namespaces = mutableMapOf<String, String>()

    fun matchingXPath(str: String): XPathRequestBodyPattern {
        currentValue = str
        applyPattern(WireMock.matchingXPath(currentValue))
        return this
    }

    @WireMockDSL
    override infix fun namespace(namespace: String) : XPathRequestBodyPattern {
        val (name, url) = namespace.split("=", limit = 2)
        namespaces[name.trim()] = url.trim()
        modifyPattern(WireMock.matchingXPath(currentValue))
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

    override fun modifyPattern(pattern: StringValuePattern) {
        var xmlPathPattern = pattern as MatchesXPathPattern
        namespaces.forEach { (name, uri) ->  xmlPathPattern = xmlPathPattern.withXPathNamespace(name, uri) }
        super.modifyPattern(xmlPathPattern)
    }
}