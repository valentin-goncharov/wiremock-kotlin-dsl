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
        return pattern equalToJson str
    }

    @WireMockDSL
    infix fun xml(str: String): RequestBodyPattern {
        val pattern = RequestBodyPattern()
        patterns += pattern
        return pattern equalToXml str
    }

    @WireMockDSL
    infix fun string(str: String): RequestBodyPattern {
        val pattern = RequestBodyPattern()
        patterns += pattern
        return pattern equalTo str
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

    private var junctionPattern: ((Array<StringValuePattern>) -> StringValuePattern)? = null

    lateinit var pattern: StringValuePattern
        protected set

    @WireMockDSL
    infix fun equalToJson(str: String): RequestBodyPattern {
        val pattern = WireMock.equalToJson(str.trimIndent())
        this.pattern = junctionPattern?.invoke(arrayOf(this.pattern, pattern)) ?: pattern
        this.junctionPattern = null
        return this
    }

    @WireMockDSL
    infix fun equalToXml(str: String): RequestBodyPattern {
        val pattern = WireMock.equalToXml(str.trimIndent())
        this.pattern = junctionPattern?.invoke(arrayOf(this.pattern, pattern)) ?: pattern
        this.junctionPattern = null
        return this
    }

    @WireMockDSL
    infix fun equalTo(str: String): RequestBodyPattern {
        val pattern = WireMock.equalTo(str.trimIndent())
        this.pattern = junctionPattern?.invoke(arrayOf(this.pattern, pattern)) ?: pattern
        this.junctionPattern = null
        return this
    }

    @WireMockDSL
    infix fun matches(str: String): RequestBodyPattern {
        val pattern = WireMock.matching(str)
        this.pattern = junctionPattern?.invoke(arrayOf(this.pattern, pattern)) ?: pattern
        this.junctionPattern = null
        return this
    }

    @WireMockDSL
    infix fun doesNotMatch(str: String): RequestBodyPattern {
        val pattern = WireMock.notMatching(str)
        this.pattern = junctionPattern?.invoke(arrayOf(this.pattern, pattern)) ?: pattern
        this.junctionPattern = null
        return this
    }

    @WireMockDSL
    infix fun or(@Suppress("UNUSED_PARAMETER") scope: RequestBodyScope): RequestBodyPattern {
        junctionPattern = WireMock::or
        return this
    }

    @WireMockDSL
    infix fun or(pattern: RequestBodyPattern): RequestBodyPattern {
        this.pattern = WireMock.or(this.pattern, pattern.pattern)
        return this
    }

    @WireMockDSL
    infix fun and(@Suppress("UNUSED_PARAMETER") scope: RequestBodyScope): RequestBodyPattern {
        junctionPattern = WireMock::and
        return this
    }

    @WireMockDSL
    infix fun and(pattern: RequestBodyPattern): RequestBodyPattern {
        this.pattern = WireMock.and(this.pattern, pattern.pattern)
        return this
    }
}