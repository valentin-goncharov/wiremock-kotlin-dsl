package dsl.wiremock.request.body

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.StringValuePattern
import dsl.wiremock.WireMockDSL

@WireMockDSL
class JsonBodyPattern: StringValueRequestBodyPattern, JsonRequestBodyPattern {

    constructor(scope: RequestBodyScope): super(scope)

    constructor(pattern: RequestBodyPattern): super(pattern)

    constructor(pattern: RequestBodyPattern, value: String, valuePattern: StringValuePattern ): super(pattern) {
        this.currentValue = value
        this.valuePattern = valuePattern
    }

    @WireMockDSL
    override infix fun ignore(fn: JsonIgnoreScope.()->Unit): StringValuePatternWrapper {
        val ignoreScope = JsonIgnoreScope()
        ignoreScope.apply(fn)
        modifyPattern(WireMock.equalToJson(this.currentValue, ignoreScope.arrayOrder, ignoreScope.extraElements))
        return this
    }
}