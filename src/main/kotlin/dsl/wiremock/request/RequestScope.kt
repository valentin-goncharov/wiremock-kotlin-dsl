package dsl.wiremock.request

import dsl.wiremock.WireMockDSL
import dsl.wiremock.metadata.MetadataEntry
import dsl.wiremock.metadata.MetadataScope
import java.util.*

@WireMockDSL
open class RequestScope {

    var id: String? = null
        set(value) {
            field = value!!
            UUID.fromString(field)
        }

    var name: String? = null
        set(value) {
            field = value!!
        }

    var priority: Int? = null
        set(value) {
            field = value!!
        }

    val url = UrlScope()

    val headers = HeadersScope()
    val cookies = CookiesScope()
    val queryParameters = QueryParametersScope()
    val body = RequestBodyScope()

    private val metadata = MetadataScope()

    @WireMockDSL
    fun metadata(fn: MetadataEntry.() -> Unit) {
        metadata.apply(fn)
    }
}