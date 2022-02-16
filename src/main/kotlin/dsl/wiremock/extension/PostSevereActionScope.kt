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
package dsl.wiremock.extension

import com.github.tomakehurst.wiremock.extension.Parameters
import dsl.wiremock.WireMockDSL

@WireMockDSL
class PostSevereActionScope {
    lateinit var name: String
    var parameters = ParametersScope()
}

class ParametersScope {

    lateinit var parameters: Parameters

    @WireMockDSL
    infix fun from(fn: ParametersScope.() -> Map<String, Any>) {
        val scope = ParametersScope()

        parameters = Parameters.from(scope.fn())
    }

    @WireMockDSL
    infix fun <T> of(data: T) {
        parameters = Parameters.of(data)
    }
}