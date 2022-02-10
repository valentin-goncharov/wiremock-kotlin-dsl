package dsl.wiremock.request.body

import com.github.tomakehurst.wiremock.client.WireMock
import dsl.wiremock.WireMockDSL

@WireMockDSL
open class StringValueRequestBodyPattern: BasicRequestBodyPattern, JunctionableBodyPattern, StringValuePatternWrapper {

    constructor(scope: RequestBodyScope): super(scope)

    constructor(pattern: RequestBodyPattern) : super(pattern as BasicRequestBodyPattern)

    override fun equalToJson(str: String): JunctionableBodyPattern {
        currentValue = str.trimIndent()
        applyPattern(WireMock.equalToJson(currentValue))
        return this as JsonRequestBodyPattern
    }

    override fun equalToXml(str: String): JunctionableBodyPattern {
        currentValue = str.trimIndent()
        applyPattern(WireMock.equalToXml(currentValue))
        return this as XmlRequestBodyPattern
    }

    override fun equalTo(str: String): JunctionableBodyPattern {
        this.currentValue = str
        applyPattern(WireMock.equalTo(currentValue))
        return this
    }

    override fun matches(str: String): JunctionableBodyPattern {
        this.currentValue = str
        applyPattern(WireMock.matching(currentValue))
        return this
    }

    override fun doesNotMatch(str: String): JunctionableBodyPattern {
        this.currentValue = str
        applyPattern(WireMock.notMatching(currentValue))
        return this
    }




}