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

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import dsl.wiremock.WireMockDSL

@WireMockDSL
class TransformersScope {

    val transformers = mutableSetOf<String>()
    val transformerParameters = mutableMapOf<String, TransformerParameter>()

    @WireMockDSL
    infix fun with(name: String) {
        transformers.add(name)
    }

    @WireMockDSL
    infix fun withParameter(name: String): TransformerParameter {
        val param = TransformerParameter(name)
        transformerParameters[name] = param
        return param
    }
}

@WireMockDSL
class TransformerParameter(val name: String) {

    lateinit var value: Any

    @WireMockDSL
    infix fun equalTo(value: Any) {
        this.value = value
    }
}

fun ResponseDefinitionBuilder.withTransformers(transformers: TransformersScope): ResponseDefinitionBuilder {
    this.withTransformers(*transformers.transformers.toTypedArray())
    this.withTransformerParameters(transformers.transformerParameters.mapValues { it.value.value })
    return this
}