package dsl.wiremock.mapping

import com.github.tomakehurst.wiremock.client.MappingBuilder
import dsl.wiremock.request.Cookie
import dsl.wiremock.request.Header
import dsl.wiremock.request.Parameter
import dsl.wiremock.request.body.RequestBodyPattern

fun <T: MappingBuilder> T.withHeaders(headers: List<Header>): T {
    headers.forEach { withHeader(it.name, it.pattern) }
    return this
}

fun <T: MappingBuilder> T.withCookies(cookies: List<Cookie>): T {
    cookies.forEach { withCookie(it.name, it.pattern) }
    return this
}

fun <T: MappingBuilder> T.withQueryParams(params: List<Parameter>): T {
    params.forEach { withQueryParam(it.name, it.pattern) }
    return this
}
fun <T: MappingBuilder> T.withRequestBodyPatterns(bodyPatterns: List<RequestBodyPattern>): T {
    bodyPatterns.forEach { withRequestBody(it.getPattern()) }
    return this
}