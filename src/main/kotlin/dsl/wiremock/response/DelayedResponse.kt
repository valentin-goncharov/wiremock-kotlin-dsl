package dsl.wiremock.response

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import dsl.wiremock.WireMockDSL

@WireMockDSL
abstract class DelayedResponse {
    val builder = ResponseDefinitionBuilder()

    val delay = Delay(builder)
}