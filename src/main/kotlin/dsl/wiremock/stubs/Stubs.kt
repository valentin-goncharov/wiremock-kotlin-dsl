package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.UrlPattern
import dsl.wiremock.WireMockDSL
import dsl.wiremock.mapping.MappingScope

@WireMockDSL
fun get(init: MappingScope.() -> Unit) {
    stubFor(WireMock::get, init)
}

@WireMockDSL
fun WireMockServer.get(init: MappingScope.() -> Unit) {
    stubFor(WireMock::get, init)
}

@WireMockDSL
fun post(init: MappingScope.() -> Unit) {
    stubFor(WireMock::post, init)
}

@WireMockDSL
fun WireMockServer.post(init: MappingScope.() -> Unit) {
    stubFor(WireMock::post, init)
}

@WireMockDSL
fun put(init: MappingScope.() -> Unit) {
    stubFor(WireMock::put, init)
}

@WireMockDSL
fun WireMockServer.put(init: MappingScope.() -> Unit) {
    stubFor(WireMock::put, init)
}

@WireMockDSL
fun patch(init: MappingScope.() -> Unit) {
    stubFor(WireMock::patch, init)
}

@WireMockDSL
fun WireMockServer.patch(init: MappingScope.() -> Unit) {
    stubFor(WireMock::patch, init)
}

@WireMockDSL
fun delete(init: MappingScope.() -> Unit) {
    stubFor(WireMock::delete, init)
}

@WireMockDSL
fun WireMockServer.delete(init: MappingScope.() -> Unit) {
    stubFor(WireMock::delete, init)
}

@WireMockDSL
fun head(init: MappingScope.() -> Unit) {
    stubFor(WireMock::head, init)
}

@WireMockDSL
fun WireMockServer.head(init: MappingScope.() -> Unit) {
    stubFor(WireMock::head, init)
}

@WireMockDSL
fun trace(init: MappingScope.() -> Unit) {
    stubFor(WireMock::trace, init)
}

@WireMockDSL
fun WireMockServer.trace(init: MappingScope.() -> Unit) {
    stubFor(WireMock::trace, init)
}

@WireMockDSL
fun options(init: MappingScope.() -> Unit) {
    stubFor(WireMock::options, init)
}

@WireMockDSL
fun WireMockServer.options(init: MappingScope.() -> Unit) {
    stubFor(WireMock::options, init)
}

@WireMockDSL
fun any(init: MappingScope.() -> Unit) {
    stubFor(WireMock::any, init)
}

@WireMockDSL
fun WireMockServer.any(init: MappingScope.() -> Unit) {
    stubFor(WireMock::any, init)
}

private fun stubFor(method: (UrlPattern) -> MappingBuilder, init: MappingScope.() -> Unit) {
    val scope = PlainStubScope()
    scope.addMapping(method, init)
    WireMock.stubFor(scope.getBuilder())
}
