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
import com.github.tomakehurst.wiremock.client.ScenarioMappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.Stubbing
import com.github.tomakehurst.wiremock.matching.UrlPattern
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import dsl.wiremock.extension.PostSevereActionScope
import dsl.wiremock.mapping.*
import dsl.wiremock.metadata.MetadataEntry
import dsl.wiremock.metadata.MetadataScope
import dsl.wiremock.response.FaultResponseScope
import dsl.wiremock.response.ResponseScope
import dsl.wiremock.response.withProxy
import dsl.wiremock.response.withTransformers
import dsl.wiremock.stubs.StubScope
import java.util.*

class ScenarioStubScope(
    private val scenario: ScenarioScope,
    private val server: Stubbing? = null
): StubScope<ScenarioRequestScope> {

    private lateinit var builder: ScenarioMappingBuilder

    private lateinit var stub: StubMapping

    private fun initMapping(init: ScenarioRequestScope.() -> Unit): ScenarioRequestScope {
        val mapping = ScenarioRequestScope()
        mapping.init()
        return mapping
    }

    override fun addMapping(method: (UrlPattern) -> MappingBuilder, init: ScenarioRequestScope.() -> Unit) {
        val mapping = initMapping(init)

        builder = method(mapping.url.pattern) as ScenarioMappingBuilder

        if (mapping.authentication.isInitialized()) {
            builder.withBasicAuth(mapping.authentication.getUsername(), mapping.authentication.getPassword())
        }

        builder.inScenario(scenario.name)

        mapping.require?.let {
            builder.whenScenarioStateIs(it)
        }

        mapping.state?.let {
            builder.willSetStateTo(it)
        }

        mapping.id?.let {
            builder.withId(UUID.fromString(it))
        }

        mapping.priority?.let {
            builder.atPriority(it)
        }

        mapping.name?.let {
            builder.withName(it)
        }


        builder
            .withHeaders(mapping.headers.patterns)
            .withCookies(mapping.cookies.patterns)
            .withQueryParams(mapping.queryParameters.patterns)
            .withRequestBodyPatterns(mapping.body.patterns)
            .withMultipartRequestBodyPatterns(mapping.multipart.patterns)
            .willReturn(ResponseScope().builder)

        buildStub()
    }

    override infix fun returns(fn: ResponseScope.() -> Unit): StubScope<ScenarioRequestScope> {
        val  response = ResponseScope().apply(fn)
        val responseBuilder = if (response.proxy.isInitialized()) {
            response.builder.withProxy(response.proxy)
        } else {
            response.builder
        }
        responseBuilder.withTransformers(response.transformers)
        builder.willReturn(responseBuilder)
        buildStub()

        return this
    }

    override infix fun fails(fn: FaultResponseScope.() -> Unit): StubScope<ScenarioRequestScope> {
        val  response = FaultResponseScope().apply(fn)
        val responseBuilder = if (response.proxy.isInitialized()) {
            response.builder.withProxy(response.proxy)
        } else {
            response.builder
        }
        builder.willReturn(responseBuilder)
        buildStub()

        return this
    }

    override fun metadata(fn: MetadataEntry.() -> Unit): StubScope<ScenarioRequestScope> {
        val metadata = MetadataScope()
        metadata.apply(fn)

        if(metadata.isInitialized()) {
            builder.withMetadata(metadata.build())
        }

        buildStub()
        return this
    }

    override fun postSevereAction(fn: PostSevereActionScope.() -> Unit): StubScope<ScenarioRequestScope> {
        val action = PostSevereActionScope()
        action.apply(fn)

        builder.withPostServeAction(action.name, action.parameters.parameters)

        buildStub()
        return this
    }

    private fun buildStub() {
        if (this::stub.isInitialized) {
            server?.removeStub(stub) ?: WireMock.removeStub(stub)
        }

        stub = server?.stubFor(this.builder) ?: WireMock.stubFor(this.builder)
    }
}