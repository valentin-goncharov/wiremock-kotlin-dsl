package dsl.wiremock.response

import dsl.wiremock.WireMockDSL

@WireMockDSL
class ProxyScope {

    lateinit var baseUrl: String
    var prefixToRemove: String = ""
    val headers =  AdditionalHeadersScope()

    @WireMockDSL
    infix fun with(fn:ProxyScope.() -> Unit) {
        this.apply(fn)
    }

    fun isInitialized(): Boolean {
        return this::baseUrl.isInitialized
    }
}

class AdditionalHeadersScope {

    val headers = mutableListOf<AdditionalResponseHeader>()

    @WireMockDSL
    infix fun contain(headerName: String): AdditionalResponseHeader {
        val header = AdditionalResponseHeader(headerName)
        headers.add(header)
        return header
    }
}

class AdditionalResponseHeader(val name: String) {

    lateinit var value: String

    @WireMockDSL
    infix fun equalTo(value: String) {
        this.value = value
    }
}