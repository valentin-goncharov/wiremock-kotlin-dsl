package dsl.wiremock.response

import dsl.wiremock.WireMockDSL

@WireMockDSL
class ResponseScope: DelayedResponse() {
    var status = 200
        set(value) {
            field = value
            builder.withStatus(field)
        }

    val headers = ResponseHeaders(builder)

    val body = ResponseBody(builder)

    val transformers = TransformersScope()
}
