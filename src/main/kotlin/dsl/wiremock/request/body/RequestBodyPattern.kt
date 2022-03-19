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
    infix fun ignore(fn: JsonIgnoreScope.()->Unit): StringValuePatternWrapper
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

interface PathValuePattern: StringValuePatternWrapper {
    @WireMockDSL
    override infix fun equalToJson(str: String): JunctionableBodyPattern

    @WireMockDSL
    override infix fun equalToXml(str: String): JunctionableBodyPattern

    @WireMockDSL
    override infix fun equalTo(str: String): JunctionableBodyPattern

    @WireMockDSL
    override infix fun contains(str: String): JunctionableBodyPattern

    @WireMockDSL
    override infix fun matches(str: String): JunctionableBodyPattern

    @WireMockDSL
    override infix fun doesNotMatch(str: String): JunctionableBodyPattern
}

interface XPathRequestBodyPattern: PathValuePattern {
    @WireMockDSL
    infix fun namespace(namespace: String) : XPathRequestBodyPattern
}

interface StringValuePatternWrapper: JunctionableBodyPattern {
    fun equalToJson(str: String): JunctionableBodyPattern

    fun equalToXml(str: String): JunctionableBodyPattern

    fun equalTo(str: String): JunctionableBodyPattern

    fun contains(str: String): JunctionableBodyPattern

    fun matches(str: String): JunctionableBodyPattern

    fun doesNotMatch(str: String): JunctionableBodyPattern
}
