package dsl.wiremock.extension

import com.github.tomakehurst.wiremock.extension.Parameters
import dsl.wiremock.WireMockDSL

@WireMockDSL
class PostSevereActionScope {
    lateinit var name: String
    var parameters = ParametersScope()
}

class ParametersScope {

    lateinit var parameters: Parameters

    @WireMockDSL
    infix fun from(fn: ParametersScope.() -> Map<String, Any>) {
        val scope = ParametersScope()

        parameters = Parameters.from(scope.fn())
    }

    @WireMockDSL
    infix fun <T> of(data: T) {
        parameters = Parameters.of(data)
    }
}