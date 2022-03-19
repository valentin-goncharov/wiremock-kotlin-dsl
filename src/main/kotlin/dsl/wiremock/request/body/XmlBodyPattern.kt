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
        return exemptComparison(ComparisonType.valueOf(comparison))
    }

    @WireMockDSL
    override fun exemptComparison(comparisonType: ComparisonType): XmlRequestBodyPattern {
        comparisons.add(comparisonType)
        modifyPattern((this.valuePattern as EqualToXmlPattern).exemptingComparisons(comparisonType))
        return this
    }
}