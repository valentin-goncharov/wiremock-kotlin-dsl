package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.UrlPattern
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import dsl.wiremock.request.RequestScope
import dsl.wiremock.mapping.withCookies
import dsl.wiremock.mapping.withHeaders
import dsl.wiremock.mapping.withQueryParams
import dsl.wiremock.mapping.withRequestBodyPatterns
import dsl.wiremock.response.FaultResponseScope
import dsl.wiremock.response.ResponseScope

class PlainStubScope(val server: WireMockServer? = null): StubScope<RequestScope> {

    private lateinit var builder: MappingBuilder

    private lateinit var stub: StubMapping

    private fun initMapping(init: RequestScope.() -> Unit): RequestScope {
        val mapping = RequestScope()
        mapping.init()
        return mapping
    }

    override fun addMapping(method: (UrlPattern) -> MappingBuilder, init: RequestScope.() -> Unit) {
        val mapping = initMapping(init)

        val mappingBuilder = method(mapping.url.pattern)

        mapping.priority?.let {
            mappingBuilder.atPriority(it)
        }

        mapping.name?.let {
            mappingBuilder.withName(it)
        }

        builder = mappingBuilder
            .withHeaders(mapping.headers.patterns)
            .withCookies(mapping.cookies.patterns)
            .withQueryParams(mapping.queryParameters.patterns)
            .withRequestBodyPatterns(mapping.body.patterns)
            .willReturn(ResponseScope().builder)

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