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
package dsl.wiremock.mapping

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.matching.MultipartValuePatternBuilder
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

fun <T: MappingBuilder> T.withMultipartRequestBodyPatterns(bodyPatterns: List<MultipartValuePatternBuilder>): T {
    bodyPatterns.forEach { withMultipartRequestBody(it) }
    return this
}