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
package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.matching.UrlPattern
import dsl.wiremock.WireMockDSL
import dsl.wiremock.extension.PostSevereActionScope
import dsl.wiremock.metadata.MetadataEntry
import dsl.wiremock.request.RequestScope
import dsl.wiremock.response.FaultResponseScope
import dsl.wiremock.response.ResponseScope

@WireMockDSL
interface StubScope<T: RequestScope> {
    fun addMapping(method: (UrlPattern) -> MappingBuilder, init: T.() -> Unit)

    @WireMockDSL
    infix fun returns(fn: ResponseScope.() -> Unit): StubScope<T>

    @WireMockDSL
    infix fun fails(fn: FaultResponseScope.() -> Unit): StubScope<T>

    @WireMockDSL
    infix fun metadata(fn: MetadataEntry.() -> Unit): StubScope<T>

    @WireMockDSL
    infix fun postSevereAction(fn: PostSevereActionScope.() -> Unit): StubScope<T>
}