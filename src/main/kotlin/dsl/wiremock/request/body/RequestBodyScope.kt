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

import dsl.wiremock.WireMockDSL

@WireMockDSL
class RequestBodyScope {
    val patterns = mutableListOf<RequestBodyPattern>()

    @WireMockDSL
    infix fun json(str: String): JsonRequestBodyPattern {

        val pattern: JsonBodyPattern = createPattern()

        pattern.equalToJson(str)
        patterns += pattern
        return pattern
    }

    @WireMockDSL
    infix fun xml(str: String): XmlRequestBodyPattern {

        val pattern: XmlBodyPattern = createPattern()

        pattern.equalToXml(str)
        patterns += pattern
        return pattern
    }

    @WireMockDSL
    infix fun string(str: String): JunctionableBodyPattern {

        val pattern: StringValueRequestBodyPattern = createPattern()

        pattern.equalTo(str)
        patterns += pattern
        return pattern
    }

    @WireMockDSL
    infix fun contains(str: String): JunctionableBodyPattern {

        val pattern: StringValueRequestBodyPattern = createPattern()

        pattern.contains(str)
        patterns += pattern
        return pattern
    }

    @WireMockDSL
    infix fun matches(str: String): JunctionableBodyPattern {

        val pattern: StringValueRequestBodyPattern = createPattern()

        pattern.matches(str)
        patterns += pattern
        return pattern
    }

    @WireMockDSL
    infix fun doesNotMatch(str: String): JunctionableBodyPattern {
        val pattern: StringValueRequestBodyPattern = createPattern()

        pattern.doesNotMatch(str)
        patterns += pattern
        return pattern
    }

    @WireMockDSL
    infix fun jsonPath(str: String): JsonPathBodyPattern {
        val pattern: JsonPathBodyPattern = createPattern()

        pattern.matchingJsonPath(str)
        patterns += pattern
        return pattern
    }

    @WireMockDSL
    infix fun xmlPath(str: String): XPathBodyPattern {
        val pattern: XPathBodyPattern = createPattern()

        pattern.matchingXPath(str)
        patterns += pattern
        return pattern
    }

    internal fun remove(pattern: RequestBodyPattern) {
        patterns.remove(pattern)
    }

    internal fun replace(old: RequestBodyPattern, pattern: RequestBodyPattern) {
        patterns.remove(old)
        patterns.add(pattern)
    }

    private inline fun <reified T: RequestBodyPattern> createPattern(): T {
        return patterns.lastOrNull()?.let {
            if (it.isJunction()) {
                patterns.remove(it)
                T::class.constructors.last().call(it)
            } else {
                T::class.constructors.first().call(this)
            }
        } ?: T::class.constructors.first().call(this)
    }
}