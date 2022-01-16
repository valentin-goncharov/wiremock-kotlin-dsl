package dsl.wiremock.request

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.StringValuePattern
import dsl.wiremock.WireMockDSL

@WireMockDSL
class RequestBodyScope {
    val patterns = mutableListOf<RequestBodyPattern>()

    @WireMockDSL
    infix fun json(str: String): RequestBodyPattern {
        val pattern = RequestBodyPattern()
        patterns += pattern
        return pattern json str
    }

    @WireMockDSL
    infix fun xml(str: String): RequestBodyPattern {
        val pattern = RequestBodyPattern()
        patterns += pattern
        return pattern xml str
    }

    @WireMockDSL
    infix fun string(str: String): RequestBodyPattern {
        val pattern = RequestBodyPattern()
        patterns += pattern
        return pattern string str
    }

    @WireMockDSL
    infix fun matches(str: String): RequestBodyPattern {
        val pattern = RequestBodyPattern()
        patterns += pattern
        return pattern matches str
    }

    @WireMockDSL
    infix fun doesNotMatch(str: String): RequestBodyPattern {
        val pattern = RequestBodyPattern()
        patterns += pattern
        return pattern doesNotMatch str
    }

}

class RequestBodyPattern {

    lateinit var pattern: StringValuePattern
        protected set

    @WireMockDSL
    infix fun json(str: String): RequestBodyPattern {
        pattern = WireMock.equalToJson(str.trimIndent())
        return this
    }

    @WireMockDSL
    infix fun xml(str: String): RequestBodyPattern {
        pattern = WireMock.equalToXml(str.trimIndent())
        return this
    }

    @WireMockDSL
    infix fun string(str: String): RequestBodyPattern {
        pattern = WireMock.equalTo(str.trimIndent())
        return this
    }

    @WireMockDSL
    infix fun matches(str: String): RequestBodyPattern {
        pattern = WireMock.matching(str)
        return this
    }

    @WireMockDSL
    infix fun doesNotMatch(str: String): RequestBodyPattern {
        pattern = WireMock.notMatching(str)
        return this
    }
}