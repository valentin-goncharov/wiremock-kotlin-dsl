package dsl.wiremock.request.body.multipart

import com.github.tomakehurst.wiremock.client.WireMock.aMultipart
import com.github.tomakehurst.wiremock.matching.MultipartValuePattern.MatchingType
import com.github.tomakehurst.wiremock.matching.MultipartValuePatternBuilder
import dsl.wiremock.WireMockDSL
import dsl.wiremock.request.Header
import dsl.wiremock.request.HeadersScope
import dsl.wiremock.request.body.RequestBodyPattern
import dsl.wiremock.request.body.RequestBodyScope

@WireMockDSL
class MultipartRequestBodyScope {

    val patterns = mutableListOf<MultipartValuePatternBuilder>()

    @WireMockDSL
    infix fun with(fn: MultipartRequestBodyPattern.() -> Unit) = add(MultipartRequestBodyPattern().apply(fn))

    fun add (pattern: MultipartRequestBodyPattern) {
        val multipartPatternBuilder  = aMultipart().
            withName(pattern.name)
            .matchingType(MatchingType.valueOf(pattern.type))
            .withHeaders(pattern.headers.patterns)
            .withRequestBodyPatterns(pattern.body.patterns)

        patterns.add(multipartPatternBuilder)
    }
}

class MultipartRequestBodyPattern {

    var name: String? = null
    var type: String = "ANY"
    val headers = HeadersScope()
    val body = RequestBodyScope()
}

fun MultipartValuePatternBuilder.withHeaders(headers: List<Header>): MultipartValuePatternBuilder {
    headers.forEach { withHeader(it.name, it.pattern) }
    return this
}

fun MultipartValuePatternBuilder.withRequestBodyPatterns(
    bodyPatterns: List<RequestBodyPattern>
): MultipartValuePatternBuilder {
    bodyPatterns.forEach { withBody(it.getPattern()) }
    return this
}
