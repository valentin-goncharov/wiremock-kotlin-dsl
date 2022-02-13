package dsl.wiremock.response

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import dsl.wiremock.WireMockDSL

@WireMockDSL
abstract class DelayedResponse {
    var builder = ResponseDefinitionBuilder()
        protected set

    val delay = Delay(builder)

    val proxy = ProxyScope()

    @WireMockDSL
    fun proxy(fn:ProxyScope.() -> Unit) {
        proxy.apply(fn)
    }
}

fun ResponseDefinitionBuilder.withProxy(proxy: ProxyScope): ResponseDefinitionBuilder.ProxyResponseDefinitionBuilder {

    val proxyBuilder = this.proxiedFrom(proxy.baseUrl).withProxyUrlPrefixToRemove(proxy.prefixToRemove)

    proxy.headers.headers.forEach {
        proxyBuilder.withAdditionalRequestHeader(it.name, it.value)
    }
    return proxyBuilder
}