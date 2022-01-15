package dsl.wiremock.mapping

import dsl.wiremock.WireMockDSL
import dsl.wiremock.request.*
import java.util.*

open class MappingScope {

    var id: String? = null
        set(value) {
            field = value!!
            UUID.fromString(field)
        }

    var name: String? = null

    var priority: Int? = null
        set(value) {
            field = value!!
        }

    val url = UrlScope()

    val headers = HeadersScope()
    val cookies = CookiesScope()
    val queryParameters = QueryParametersScope()
    val body = RequestBodyScope()


}