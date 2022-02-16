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