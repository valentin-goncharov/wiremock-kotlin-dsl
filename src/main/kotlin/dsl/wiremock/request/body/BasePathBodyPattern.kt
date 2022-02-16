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
import com.github.tomakehurst.wiremock.matching.StringValuePattern
import dsl.wiremock.WireMockDSL

abstract class BasePathBodyPattern : StringValueRequestBodyPattern, PathValuePattern {

        protected val matchingPathPattern: (String, StringValuePattern) -> StringValuePattern

        constructor(
                scope: RequestBodyScope,
                matchingPathPattern: (String, StringValuePattern) -> StringValuePattern
        ): super(scope) {
                this.matchingPathPattern = matchingPathPattern
        }

        constructor(
                pattern: RequestBodyPattern,
                matchingPathPattern: (String, StringValuePattern) -> StringValuePattern
        ): super(pattern) {
                this.matchingPathPattern = matchingPathPattern
        }


        @WireMockDSL
        override infix fun equalTo(str: String): JunctionableBodyPattern {
                modifyPattern(matchingPathPattern(this.currentValue, WireMock.equalTo(str)))
                return this
        }

        @WireMockDSL
        override infix fun contains(str: String): JunctionableBodyPattern {
                modifyPattern(matchingPathPattern(this.currentValue, WireMock.containing(str)))
                return this
        }

        @WireMockDSL
        override infix fun matches(str: String): JunctionableBodyPattern {
                modifyPattern(matchingPathPattern(this.currentValue, WireMock.matching(str)))
                return this
        }

        @WireMockDSL
        override infix fun doesNotMatch(str: String): JunctionableBodyPattern {
                modifyPattern(matchingPathPattern(this.currentValue, WireMock.notMatching(str)))
                return this
        }

}