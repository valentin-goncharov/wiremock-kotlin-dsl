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