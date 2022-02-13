package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.UrlPattern
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import dsl.wiremock.mapping.*
import dsl.wiremock.request.RequestScope
import dsl.wiremock.response.FaultResponseScope
import dsl.wiremock.response.ResponseScope
import java.util.*

class PlainStubScope(private val server: WireMockServer? = null): StubScope<RequestScope> {

    private lateinit var builder: MappingBuilder

    private lateinit var stub: StubMapping

    private fun initMapping(init: RequestScope.() -> Unit): RequestScope {
        val mapping = RequestScope()
        mapping.init()
        return mapping
    }

    override fun addMapping(method: (UrlPattern) -> MappingBuilder, init: RequestScope.() -> Unit) {
        val mapping = initMapping(init)

        builder = method(mapping.url.pattern)

        if (mapping.authentication.isInitialized()) {
            builder.withBasicAuth(mapping.authentication.getUsername(), mapping.authentication.getPassword())
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
            .withMetadata(mapping.metadata.build())
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