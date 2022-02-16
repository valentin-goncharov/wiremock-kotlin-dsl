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

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.common.Json
import dsl.wiremock.WireMockDSL

@WireMockDSL
class ResponseBody (private val builder: ResponseDefinitionBuilder) {

    private val stringHandler: (body: String) -> ResponseDefinitionBuilder = builder::withBody
    private val fileHandler: (path: String) -> ResponseDefinitionBuilder = builder::withBodyFile
    private val jsonHandler: (json: JsonNode) -> ResponseDefinitionBuilder = builder::withJsonBody
    private val base64Handler: (body: String) -> ResponseDefinitionBuilder = builder::withBase64Body

    @WireMockDSL
    infix fun entity(obj: Any) {
        stringHandler(Json.write(obj))
    }

    @WireMockDSL
    infix fun json(node: JsonNode) {
        jsonHandler(node)
    }

    @WireMockDSL
    infix fun json(content: String) {
        val mapper = ObjectMapper()
        jsonHandler(mapper.readTree(content.trimIndent()))
    }

    @WireMockDSL
    infix fun string(content: String) {
        stringHandler(content)
    }

    @WireMockDSL
    infix fun file(path: String) {
        fileHandler(path)
    }

    @WireMockDSL
    infix fun base64(content: String) {
        base64Handler(content)
    }
}