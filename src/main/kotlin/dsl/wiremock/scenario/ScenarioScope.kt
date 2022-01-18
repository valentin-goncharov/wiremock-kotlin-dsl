package dsl.wiremock.stubs.scenario

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.UrlPattern
import dsl.wiremock.*
import dsl.wiremock.scenario.ScenarioStubScope
import dsl.wiremock.stubs.StubScope

@WireMockDSL
class ScenarioScope(val server: WireMockServer? = null) {
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
        server: WireMockServer? = null
    ): StubScope<ScenarioRequestScope> {
        val scope = ScenarioStubScope(this, server)
        scope.addMapping(method, init)
        return scope
    }
}