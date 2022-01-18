package dsl.wiremock.scenario

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.ScenarioMappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.UrlPattern
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import dsl.wiremock.mapping.withCookies
import dsl.wiremock.mapping.withHeaders
import dsl.wiremock.mapping.withQueryParams
import dsl.wiremock.mapping.withRequestBodyPatterns
import dsl.wiremock.response.FaultResponseScope
import dsl.wiremock.response.ResponseScope
import dsl.wiremock.stubs.StubScope
import dsl.wiremock.stubs.scenario.ScenarioRequestScope
import dsl.wiremock.stubs.scenario.ScenarioScope

class ScenarioStubScope(
    private val scenario: ScenarioScope,
    private val server: WireMockServer? = null
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

        val scenarioMappingBuilder = method(mapping.url.pattern) as ScenarioMappingBuilder
        scenarioMappingBuilder.inScenario(scenario.name)

        mapping.require?.let {
            scenarioMappingBuilder.whenScenarioStateIs(it)
        }

        mapping.state?.let {
            scenarioMappingBuilder.willSetStateTo(it)
        }

        mapping.priority?.let {
            scenarioMappingBuilder.atPriority(it)
        }

        mapping.name?.let {
            scenarioMappingBuilder.withName(it)
        }

        builder = scenarioMappingBuilder
            .withHeaders(mapping.headers.patterns)
            .withCookies(mapping.cookies.patterns)
            .withQueryParams(mapping.queryParameters.patterns)
            .withRequestBodyPatterns(mapping.body.patterns)

        buildStub()
    }

    override infix fun returns(fn: ResponseScope.() -> Unit) {
        builder.willReturn(ResponseScope().apply(fn).builder)
        buildStub()
    }

    override infix fun fails(fn: FaultResponseScope.() -> Unit) {
        builder.willReturn(FaultResponseScope().apply(fn).builder)
        buildStub()
    }

    private fun buildStub() {
        if (this::stub.isInitialized) {
            WireMock.removeStub(stub)
        }
        stub = server?.stubFor(this.builder) ?: WireMock.stubFor(this.builder)
    }
}