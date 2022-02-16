package dsl.wiremock.request

import dsl.wiremock.WireMockDSL
import dsl.wiremock.authentication.AuthenticationScope
import dsl.wiremock.request.body.RequestBodyScope
import dsl.wiremock.request.body.multipart.MultipartRequestBodyPattern
import dsl.wiremock.request.body.multipart.MultipartRequestBodyScope
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
    val host = HostPattern()
    var port: Int? = null
        set(value) {
            field = value!!
        }

    val authentication = AuthenticationScope()

    val headers = HeadersScope()
    val cookies = CookiesScope()
    val queryParameters = QueryParametersScope()
    val body = RequestBodyScope()
    val multipart = MultipartRequestBodyScope()

    @WireMockDSL
    fun multipart(fn: MultipartRequestBodyPattern.() -> Unit) {
        val pattern = MultipartRequestBodyPattern().apply(fn)
        multipart.add(pattern)
    }

}