package dsl.wiremock.request.body

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.EqualToXmlPattern
import dsl.wiremock.WireMockDSL
import org.xmlunit.diff.ComparisonType

class XmlBodyPattern: StringValueRequestBodyPattern, XmlRequestBodyPattern {

    constructor(scope: RequestBodyScope): super(scope)

    constructor(pattern: RequestBodyPattern) : super(pattern)

    private val comparisons = mutableSetOf<ComparisonType>()

    @WireMockDSL
    override fun placeholders(fn: XmlPlaceholdersScope.() -> Unit): XmlRequestBodyPattern {

        val placeholdersScope = XmlPlaceholdersScope()

        placeholdersScope.apply(fn)

        val pattern = WireMock.equalToXml(
            this.currentValue,
            placeholdersScope.enabled,
            placeholdersScope.openingDelimiterRegex,
            placeholdersScope.closingDelimiterRegex
        )

        modifyPattern(
            if (comparisons.isNotEmpty()) pattern.exemptingComparisons(*comparisons.toTypedArray()) else pattern
        )

        return this
    }

    @WireMockDSL
    override fun exemptComparison(comparison: String): XmlRequestBodyPattern {
        val comparisonType = ComparisonType.valueOf(comparison)
        comparisons.add(comparisonType)
        modifyPattern((this.valuePattern as EqualToXmlPattern).exemptingComparisons(comparisonType))
        return this
    }

    @WireMockDSL
    override fun exemptComparison(comparisonType: ComparisonType): XmlRequestBodyPattern {
        comparisons.add(comparisonType)
        modifyPattern((this.valuePattern as EqualToXmlPattern).exemptingComparisons(comparisonType))
        return this
    }
}