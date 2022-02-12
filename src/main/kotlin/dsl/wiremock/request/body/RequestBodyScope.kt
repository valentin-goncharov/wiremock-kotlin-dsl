package dsl.wiremock.request.body

import dsl.wiremock.WireMockDSL

@WireMockDSL
class RequestBodyScope {
    val patterns = mutableListOf<RequestBodyPattern>()

    @WireMockDSL
    infix fun json(str: String): JsonRequestBodyPattern {

        val pattern: JsonBodyPattern = createPattern()

        pattern.equalToJson(str)
        patterns += pattern
        return pattern
    }

    @WireMockDSL
    infix fun xml(str: String): XmlRequestBodyPattern {

        val pattern: XmlBodyPattern = createPattern()

        pattern.equalToXml(str)
        patterns += pattern
        return pattern
    }

    @WireMockDSL
    infix fun string(str: String): JunctionableBodyPattern {

        val pattern: StringValueRequestBodyPattern = createPattern()

        pattern.equalTo(str)
        patterns += pattern
        return pattern
    }

    @WireMockDSL
    infix fun contains(str: String): JunctionableBodyPattern {

        val pattern: StringValueRequestBodyPattern = createPattern()

        pattern.contains(str)
        patterns += pattern
        return pattern
    }

    @WireMockDSL
    infix fun matches(str: String): JunctionableBodyPattern {

        val pattern: StringValueRequestBodyPattern = createPattern()

        pattern.matches(str)
        patterns += pattern
        return pattern
    }

    @WireMockDSL
    infix fun doesNotMatch(str: String): JunctionableBodyPattern {
        val pattern: StringValueRequestBodyPattern = createPattern()

        pattern.doesNotMatch(str)
        patterns += pattern
        return pattern
    }

    @WireMockDSL
    infix fun jsonPath(str: String): JsonPathBodyPattern {
        val pattern: JsonPathBodyPattern = createPattern()

        pattern.matchingJsonPath(str)
        patterns += pattern
        return pattern
    }

    @WireMockDSL
    infix fun xmlPath(str: String): XPathBodyPattern {
        val pattern: XPathBodyPattern = createPattern()

        pattern.matchingXPath(str)
        patterns += pattern
        return pattern
    }

    fun remove(pattern: RequestBodyPattern) {
        patterns.remove(pattern)
    }

    fun replace(old: RequestBodyPattern, pattern: RequestBodyPattern) {
        patterns.remove(old)
        patterns.add(pattern)
    }

    private inline fun <reified T: RequestBodyPattern> createPattern(): T {
        return patterns.lastOrNull()?.let {
            if (it.isJunction()) {
                patterns.remove(it)
                T::class.constructors.last().call(it)
            } else {
                T::class.constructors.first().call(this)
            }
        } ?: T::class.constructors.first().call(this)
    }
}