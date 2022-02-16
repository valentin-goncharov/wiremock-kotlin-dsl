/*-
 * ========================LICENSE_START=================================
 * Wiremock Kotlin DSL
 * %%
 * Copyright (C) 2022 Valentin Goncharov
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
package dsl.wiremock.request.body

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.EqualToXmlPattern
import com.github.tomakehurst.wiremock.matching.StringValuePattern
import dsl.wiremock.WireMockDSL
import org.xmlunit.diff.ComparisonType


internal class EqualToJsonBodyPattern(
    pattern: RequestBodyPattern,
    private val jsonValue: String,
    private val matchingPathPattern: (String, StringValuePattern) -> StringValuePattern
) : StringValueRequestBodyPattern(pattern), JsonRequestBodyPattern {

    @WireMockDSL
    override infix fun ignore(fn: JsonIgnoreScope.()->Unit): StringValuePatternWrapper {
        val ignoreScope = JsonIgnoreScope()
        ignoreScope.apply(fn)
        modifyPattern(
            matchingPathPattern(
                this.currentValue,
                WireMock.equalToJson(jsonValue, ignoreScope.arrayOrder, ignoreScope.extraElements)
            )
        )
        return this
    }
}

internal class EqualToXmlBodyPattern(
    pattern: RequestBodyPattern,
    private val xmlValue: String,
    private var xmlValuePattern: EqualToXmlPattern,
    private val matchingPathPattern: (String, StringValuePattern) -> StringValuePattern
) : StringValueRequestBodyPattern(pattern), XmlRequestBodyPattern {

    private val comparisons = mutableSetOf<ComparisonType>()

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

        modifyPattern(matchingPathPattern(this.currentValue, xmlValuePattern))

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
        modifyPattern(matchingPathPattern(this.currentValue, xmlValuePattern))
        return this
    }
}