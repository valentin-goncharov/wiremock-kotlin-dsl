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

@WireMockDSL
open class StringValueRequestBodyPattern: BasicRequestBodyPattern, JunctionableBodyPattern, StringValuePatternWrapper {

    constructor(scope: RequestBodyScope): super(scope)

    constructor(pattern: RequestBodyPattern) : super(pattern as BasicRequestBodyPattern)

    override fun equalToJson(str: String): JunctionableBodyPattern {
        currentValue = str.trimIndent()
        applyPattern(WireMock.equalToJson(currentValue))
        return this as JsonRequestBodyPattern
    }

    override fun equalToXml(str: String): JunctionableBodyPattern {
        currentValue = str.trimIndent()
        applyPattern(WireMock.equalToXml(currentValue))
        return this as XmlRequestBodyPattern
    }

    override fun equalTo(str: String): JunctionableBodyPattern {
        this.currentValue = str
        applyPattern(WireMock.equalTo(currentValue))
        return this
    }

    override fun contains(str: String): JunctionableBodyPattern {
        this.currentValue = str
        applyPattern(WireMock.containing(currentValue))
        return this
    }

    override fun matches(str: String): JunctionableBodyPattern {
        this.currentValue = str
        applyPattern(WireMock.matching(currentValue))
        return this
    }

    override fun doesNotMatch(str: String): JunctionableBodyPattern {
        this.currentValue = str
        applyPattern(WireMock.notMatching(currentValue))
        return this
    }




}