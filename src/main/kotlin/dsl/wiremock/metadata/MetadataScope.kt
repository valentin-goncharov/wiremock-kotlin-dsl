package dsl.wiremock.metadata

import com.github.tomakehurst.wiremock.common.Metadata
import dsl.wiremock.WireMockDSL

@WireMockDSL
class MetadataScope {
    private lateinit var builder: Metadata.Builder

    @WireMockDSL
    infix fun with (fn: MetadataEntry.() -> Unit) = apply(fn)


    fun apply(fn: MetadataEntry.() -> Unit) {
        val entry = MetadataEntry(Metadata.metadata())
        entry.apply(fn)
        builder = entry.metadataBuilder
    }

    fun build(): Metadata {
        return builder.build()
    }
}

@WireMockDSL
class MetadataEntry(val metadataBuilder: Metadata.Builder, val name: String = "") {

    @WireMockDSL
    infix fun String.attr(value: Any){
        with(metadataBuilder) {
            attr(this@attr, value)
        }
    }

    @WireMockDSL
    infix fun String.metadata(fn: MetadataEntry.() -> Unit){
        with(metadataBuilder) {
            val nestedScope = MetadataEntry(Metadata.metadata())
            nestedScope.apply(fn)
            attr(this@metadata, nestedScope.metadataBuilder)
        }
    }

    @WireMockDSL
    infix fun String.list(items: List<Any>){
        with(metadataBuilder) {
            list(this@list, *items.toTypedArray())
        }
    }
}