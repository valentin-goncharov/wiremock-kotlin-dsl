package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.Stubbing
import com.github.tomakehurst.wiremock.matching.UrlPattern
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import dsl.wiremock.extension.PostSevereActionScope
import dsl.wiremock.mapping.*
import dsl.wiremock.metadata.MetadataEntry
import dsl.wiremock.metadata.MetadataScope
import dsl.wiremock.request.RequestScope
import dsl.wiremock.response.FaultResponseScope
import dsl.wiremock.response.ResponseScope
import dsl.wiremock.response.withProxy
import dsl.wiremock.response.withTransformers
import java.util.*

class PlainStubScope(private val server: Stubbing? = null): StubScope<RequestScope> {

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

        if (mapping.host.isInitialized()) {
            builder.withHost(mapping.host.pattern)
        }

        mapping.port?.let {
            builder.withPort(it)
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

    override infix fun returns(fn: ResponseScope.() -> Unit): StubScope<RequestScope> {
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

    override infix fun fails(fn: FaultResponseScope.() -> Unit): StubScope<RequestScope> {
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

    override fun metadata(fn: MetadataEntry.() -> Unit): StubScope<RequestScope> {
        val metadata = MetadataScope()
        metadata.apply(fn)

        if(metadata.isInitialized()) {
            builder.withMetadata(metadata.build())
        }

        buildStub()
        return this
    }

    override fun postSevereAction(fn: PostSevereActionScope.() -> Unit): StubScope<RequestScope> {
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