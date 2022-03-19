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
package dsl.wiremock.metadata

import com.github.tomakehurst.wiremock.common.Metadata
import dsl.wiremock.WireMockDSL

@WireMockDSL
class MetadataScope {
    private lateinit var builder: Metadata.Builder

    internal fun apply(fn: MetadataEntry.() -> Unit) {
        val entry = MetadataEntry(Metadata.metadata())
        entry.apply(fn)
        builder = entry.metadataBuilder
    }

    internal fun build(): Metadata {
        return builder.build()
    }

    internal fun isInitialized(): Boolean {
        return this::builder.isInitialized
    }
}

@WireMockDSL
class MetadataEntry(val metadataBuilder: Metadata.Builder, val name: String = "") {

    @WireMockDSL
    infix fun String.attr(value: Any){
        with(metadataBuilder) {
            attr(this@attr, value)
        }
    }

    @WireMockDSL
    infix fun String.metadata(fn: MetadataEntry.() -> Unit){
        with(metadataBuilder) {
            val nestedScope = MetadataEntry(Metadata.metadata())
            nestedScope.apply(fn)
            attr(this@metadata, nestedScope.metadataBuilder)
        }
    }

    @WireMockDSL
    infix fun String.list(items: List<Any>){
        with(metadataBuilder) {
            list(this@list, *items.toTypedArray())
        }
    }
}