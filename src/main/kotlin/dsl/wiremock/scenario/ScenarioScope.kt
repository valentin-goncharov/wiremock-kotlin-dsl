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
package dsl.wiremock.scenario

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.Stubbing
import com.github.tomakehurst.wiremock.matching.UrlPattern
import dsl.wiremock.*
import dsl.wiremock.stubs.StubScope

@WireMockDSL
class ScenarioScope(private val server: Stubbing? = null) {
    lateinit var name: String

    @WireMockDSL
    fun get(init: ScenarioRequestScope.() -> Unit) = createRequestScope(WireMock::get, init, server)

    @WireMockDSL
    fun post(init: ScenarioRequestScope.() -> Unit) = createRequestScope(WireMock::post, init, server)

    @WireMockDSL
    fun put(init: ScenarioRequestScope.() -> Unit) = createRequestScope(WireMock::put, init, server)

    @WireMockDSL
    fun delete(init: ScenarioRequestScope.() -> Unit) = createRequestScope(WireMock::delete, init, server)

    @WireMockDSL
    fun patch(init: ScenarioRequestScope.() -> Unit) = createRequestScope(WireMock::patch, init, server)

    @WireMockDSL
    fun options(init: ScenarioRequestScope.() -> Unit) = createRequestScope(WireMock::options, init, server)

    @WireMockDSL
    fun head(init: ScenarioRequestScope.() -> Unit) = createRequestScope(WireMock::head, init, server)

    @WireMockDSL
    fun trace(init: ScenarioRequestScope.() -> Unit) = createRequestScope(WireMock::trace, init, server)

    @WireMockDSL
    fun any(init: ScenarioRequestScope.() -> Unit) = createRequestScope(WireMock::any, init, server)

    private fun createRequestScope(
        method: (UrlPattern) -> MappingBuilder,
        init: ScenarioRequestScope.() -> Unit,
        server: Stubbing? = null
    ): StubScope<ScenarioRequestScope> {
        val scope = ScenarioStubScope(this, server)
        scope.addMapping(method, init)
        return scope
    }
}