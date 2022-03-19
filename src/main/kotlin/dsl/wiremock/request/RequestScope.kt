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