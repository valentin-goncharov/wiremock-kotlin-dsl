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
package dsl.wiremock.request

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.UrlPattern
import dsl.wiremock.WireMockDSL

@WireMockDSL
class UrlScope {

    var pattern: UrlPattern = UrlPattern.ANY
        private set

    @WireMockDSL
    infix fun equalTo(str: String) {
        pattern = if ("any" == str) UrlPattern.ANY else WireMock.urlEqualTo(str)
    }

    @WireMockDSL
    infix fun matches(str: String) {
        pattern = WireMock.urlMatching(str)
    }

    @WireMockDSL
    infix fun pathEqualTo(str: String) {
        pattern = WireMock.urlPathEqualTo(str)
    }

    @WireMockDSL
    infix fun pathMatches(str: String) {
        pattern = WireMock.urlPathMatching(str)
    }
}