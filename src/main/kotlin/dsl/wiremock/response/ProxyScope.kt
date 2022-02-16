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
package dsl.wiremock.response

import dsl.wiremock.WireMockDSL

@WireMockDSL
class ProxyScope {

    lateinit var baseUrl: String
    var prefixToRemove: String = ""
    val headers =  AdditionalHeadersScope()

    @WireMockDSL
    infix fun with(fn:ProxyScope.() -> Unit) {
        this.apply(fn)
    }

    fun isInitialized(): Boolean {
        return this::baseUrl.isInitialized
    }
}

class AdditionalHeadersScope {

    val headers = mutableListOf<AdditionalResponseHeader>()

    @WireMockDSL
    infix fun contain(headerName: String): AdditionalResponseHeader {
        val header = AdditionalResponseHeader(headerName)
        headers.add(header)
        return header
    }
}

class AdditionalResponseHeader(val name: String) {

    lateinit var value: String

    @WireMockDSL
    infix fun equalTo(value: String) {
        this.value = value
    }
}