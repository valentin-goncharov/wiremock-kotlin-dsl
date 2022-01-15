package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.matching.UrlPattern
import dsl.wiremock.mapping.MappingScope

interface StubScope<T: MappingScope> {
    fun addMapping(method: (UrlPattern) -> MappingBuilder, init: T.() -> Unit)
    fun getBuilder(): MappingBuilder
}