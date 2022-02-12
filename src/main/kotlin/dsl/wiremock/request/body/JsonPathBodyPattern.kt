package dsl.wiremock.request.body

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.EqualToXmlPattern
import dsl.wiremock.WireMockDSL
import org.xmlunit.diff.ComparisonType

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
    override infix fun equalToXml(str: String): XmlRequestBodyPattern {
        val value = str.trimIndent()
        modifyPattern(WireMock.matchingJsonPath(this.currentValue, WireMock.equalToXml(value)))
        val xmlPattern = EqualToXmlBodyPattern(this, value, WireMock.equalToXml(value))
        scope.replace(this, xmlPattern)
        return xmlPattern
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

internal class EqualToXmlBodyPattern(pattern: RequestBodyPattern, value: String, valuePattern: EqualToXmlPattern)
    : StringValueRequestBodyPattern(pattern), XmlRequestBodyPattern {

    private val comparisons = mutableSetOf<ComparisonType>()

    private val xmlValue: String = value
    private var xmlValuePattern: EqualToXmlPattern = valuePattern

    @WireMockDSL
    override fun placeholders(fn: XmlPlaceholdersScope.() -> Unit): XmlRequestBodyPattern {

        val placeholdersScope = XmlPlaceholdersScope()

        placeholdersScope.apply(fn)

        xmlValuePattern = WireMock.equalToXml(
            this.xmlValue,
            placeholdersScope.enabled,
            placeholdersScope.openingDelimiterRegex,
            placeholdersScope.closingDelimiterRegex
        )
        xmlValuePattern = if (comparisons.isNotEmpty())
            xmlValuePattern.exemptingComparisons(*comparisons.toTypedArray())
        else xmlValuePattern

        modifyPattern(WireMock.matchingJsonPath(this.currentValue, xmlValuePattern))

        return this
    }

    @WireMockDSL
    override fun exemptComparison(comparison: String): XmlRequestBodyPattern {
        return exemptComparison(ComparisonType.valueOf(comparison))
    }

    @WireMockDSL
    override fun exemptComparison(comparisonType: ComparisonType): XmlRequestBodyPattern {
        comparisons.add(comparisonType)
        xmlValuePattern = xmlValuePattern.exemptingComparisons(comparisonType)
        modifyPattern(WireMock.matchingJsonPath(this.currentValue, xmlValuePattern))
        return this
    }
}