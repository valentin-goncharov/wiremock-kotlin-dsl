package dsl.wiremock.request.body

import com.github.tomakehurst.wiremock.matching.StringValuePattern
import dsl.wiremock.WireMockDSL
import org.xmlunit.diff.ComparisonType

interface RequestBodyPattern {
    fun getPattern(): StringValuePattern
    fun isJunction(): Boolean
}

interface JunctionableBodyPattern: RequestBodyPattern {
    @WireMockDSL
    infix fun or(@Suppress("UNUSED_PARAMETER") scope: RequestBodyScope): RequestBodyScope

    @WireMockDSL
    infix fun or(pattern: JunctionableBodyPattern): JunctionableBodyPattern

    @WireMockDSL
    infix fun and(@Suppress("UNUSED_PARAMETER") scope: RequestBodyScope): RequestBodyScope

    @WireMockDSL
    infix fun and(pattern: JunctionableBodyPattern): JunctionableBodyPattern
}

class JsonIgnoreScope(var arrayOrder: Boolean = false, var extraElements: Boolean = false)

interface JsonRequestBodyPattern: JunctionableBodyPattern {
    @WireMockDSL
    infix fun ignore(fn: JsonIgnoreScope.()->Unit): JsonRequestBodyPattern
}

class XmlPlaceholdersScope(
    var enabled: Boolean = false,
    var openingDelimiterRegex: String? = null,
    var closingDelimiterRegex: String? = null
)

interface XmlRequestBodyPattern: JunctionableBodyPattern {
    @WireMockDSL
    infix fun placeholders(fn: XmlPlaceholdersScope.()->Unit): XmlRequestBodyPattern

    @WireMockDSL
    infix fun exemptComparison(comparison: String): XmlRequestBodyPattern

    @WireMockDSL
    infix fun exemptComparison(comparisonType: ComparisonType): XmlRequestBodyPattern
}

interface StringValuePatternWrapper {
    fun equalToJson(str: String): JunctionableBodyPattern

    fun equalToXml(str: String): JunctionableBodyPattern

    fun equalTo(str: String): JunctionableBodyPattern

    fun contains(str: String): JunctionableBodyPattern

    fun matches(str: String): JunctionableBodyPattern

    fun doesNotMatch(str: String): JunctionableBodyPattern
}
