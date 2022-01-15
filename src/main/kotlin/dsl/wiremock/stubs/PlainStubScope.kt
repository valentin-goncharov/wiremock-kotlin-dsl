package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.matching.UrlPattern
import dsl.wiremock.mapping.MappingScope

class PlainStubScope: StubScope<MappingScope> {

    private lateinit var builder: MappingBuilder

    override fun addMapping(method: (UrlPattern) -> MappingBuilder, init: MappingScope.() -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getBuilder(): MappingBuilder {
        return builder
    }
}