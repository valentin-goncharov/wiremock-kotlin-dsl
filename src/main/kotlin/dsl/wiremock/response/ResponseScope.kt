package dsl.wiremock.response

import dsl.wiremock.WireMockDSL

@WireMockDSL
class ResponseScope: DelayedResponse() {

    val headers = ResponseHeaders(builder)

    var status = 200
        set(value) {
            field = value
            builder.withStatus(field)
        }

    val body = ResponseBody(builder)
}

