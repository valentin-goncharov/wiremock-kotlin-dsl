package dsl.wiremock.request.body

import com.github.tomakehurst.wiremock.client.WireMock
import dsl.wiremock.WireMockDSL

@WireMockDSL
class JsonBodyPattern: StringValueRequestBodyPattern, JsonRequestBodyPattern {

    constructor(scope: RequestBodyScope): super(scope)

    constructor(pattern: RequestBodyPattern): super(pattern)

    @WireMockDSL
    override infix fun ignore(fn: JsonIgnoreScope.()->Unit): StringValuePatternWrapper {
        val ignoreScope = JsonIgnoreScope()
        ignoreScope.apply(fn)
        modifyPattern(WireMock.equalToJson(this.currentValue, ignoreScope.arrayOrder, ignoreScope.extraElements))
        return this
    }
}