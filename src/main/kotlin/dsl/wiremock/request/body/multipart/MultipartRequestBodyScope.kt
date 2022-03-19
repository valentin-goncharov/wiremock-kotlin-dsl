/*-
 * ========================LICENSE_START=================================
 * Wiremock Kotlin DSL
 * %%
 * Copyright (C) 2022 Valentin Goncharov
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
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

    fun add (pattern: MultipartRequestBodyPattern) {
        val multipartPatternBuilder  = aMultipart()
            .matchingType(MatchingType.valueOf(pattern.type))
            .withHeaders(pattern.headers.patterns)
            .withRequestBodyPatterns(pattern.body.patterns)
        pattern.name?.let { multipartPatternBuilder.withName(pattern.name) }
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
