package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.UrlPattern
import dsl.wiremock.WireMockDSL
import dsl.wiremock.request.RequestScope
import dsl.wiremock.stubs.scenario.ScenarioScope

@WireMockDSL
fun get(init: RequestScope.() -> Unit) = createRequestScope(WireMock::get, init)

@WireMockDSL
fun WireMockServer.get(init: RequestScope.() -> Unit) = createRequestScope(WireMock::get, init, this)

@WireMockDSL
fun post(init: RequestScope.() -> Unit) = createRequestScope(WireMock::post, init)

@WireMockDSL
fun WireMockServer.post(init: RequestScope.() -> Unit) = createRequestScope(WireMock::post, init, this)

@WireMockDSL
fun put(init: RequestScope.() -> Unit) = createRequestScope(WireMock::put, init)

@WireMockDSL
fun WireMockServer.put(init: RequestScope.() -> Unit) = createRequestScope(WireMock::put, init, this)

@WireMockDSL
fun patch(init: RequestScope.() -> Unit) = createRequestScope(WireMock::patch, init)

@WireMockDSL
fun WireMockServer.patch(init: RequestScope.() -> Unit) = createRequestScope(WireMock::patch, init, this)

@WireMockDSL
fun delete(init: RequestScope.() -> Unit) = createRequestScope(WireMock::delete, init)

@WireMockDSL
fun WireMockServer.delete(init: RequestScope.() -> Unit) = createRequestScope(WireMock::delete, init, this)

@WireMockDSL
fun head(init: RequestScope.() -> Unit) = createRequestScope(WireMock::head, init)

@WireMockDSL
fun WireMockServer.head(init: RequestScope.() -> Unit) = createRequestScope(WireMock::head, init, this)

@WireMockDSL
fun trace(init: RequestScope.() -> Unit) = createRequestScope(WireMock::trace, init)

@WireMockDSL
fun WireMockServer.trace(init: RequestScope.() -> Unit) = createRequestScope(WireMock::trace, init, this)

@WireMockDSL
fun options(init: RequestScope.() -> Unit) = createRequestScope(WireMock::options, init)

@WireMockDSL
fun WireMockServer.options(init: RequestScope.() -> Unit) = createRequestScope(WireMock::options, init, this)

@WireMockDSL
fun any(init: RequestScope.() -> Unit) = createRequestScope(WireMock::any, init)

@WireMockDSL
fun WireMockServer.any(init: RequestScope.() -> Unit) = createRequestScope(WireMock::any, init, this)

@WireMockDSL
fun scenario(init: ScenarioScope.() -> Unit) = createScenarioScope(init)

@WireMockDSL
fun WireMockServer.scenario(init: ScenarioScope.() -> Unit) = createScenarioScope(init,this)

private fun createRequestScope(
    method: (UrlPattern) -> MappingBuilder,
    init: RequestScope.() -> Unit,
    server: WireMockServer? = null
): StubScope<RequestScope> {
    val scope = PlainStubScope(server)
    scope.addMapping(method, init)
    return scope
}

private fun createScenarioScope(
    init: ScenarioScope.() -> Unit,
    server: WireMockServer? = null
): ScenarioScope {
    val scope = ScenarioScope(server)
    scope.init()
    return scope
}
