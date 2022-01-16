package dsl.wiremock.response

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import dsl.wiremock.WireMockDSL


@WireMockDSL
class ResponseHeaders(private val builder: ResponseDefinitionBuilder) {

    @WireMockDSL
    infix fun contain(header: String): ResponseHeader {
        return ResponseHeader(header, builder)
    }
}

class ResponseHeader(private val name: String, private val builder: ResponseDefinitionBuilder) {

    @WireMockDSL
    infix fun equalTo(value: String) {
        builder.withHeader(name, value)
    }
}