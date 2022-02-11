package dsl.wiremock.request.body

import dsl.wiremock.WireMockDSL

@WireMockDSL
class RequestBodyScope {
    val patterns = mutableListOf<RequestBodyPattern>()

    @WireMockDSL
    infix fun json(str: String): JsonRequestBodyPattern {

        val pattern = patterns.lastOrNull()?.let {
            if (it.isJunction()) {
                patterns.remove(it)
                JsonBodyPattern(it)
            } else {
                JsonBodyPattern(this)
            }
        } ?: JsonBodyPattern(this)

        pattern.equalToJson(str)
        patterns += pattern
        return pattern
    }

    @WireMockDSL
    infix fun xml(str: String): XmlRequestBodyPattern {
        val pattern = patterns.lastOrNull()?.let {
            if (it.isJunction()) {
                patterns.remove(it)
                XmlBodyPattern(it)
            } else {
                XmlBodyPattern(this)
            }
        } ?: XmlBodyPattern(this)

        pattern.equalToXml(str)
        patterns += pattern
        return pattern
    }

    @WireMockDSL
    infix fun string(str: String): JunctionableBodyPattern {

        val pattern = patterns.lastOrNull()?.let {
            if (it.isJunction()) {
                patterns.remove(it)
                StringValueRequestBodyPattern(it)
            } else {
                StringValueRequestBodyPattern(this)
            }
        } ?: StringValueRequestBodyPattern(this)

        pattern.equalTo(str)
        patterns += pattern
        return pattern
    }

    @WireMockDSL
    infix fun matches(str: String): JunctionableBodyPattern {

        val pattern = patterns.lastOrNull()?.let {
            if (it.isJunction()) {
                patterns.remove(it)
                StringValueRequestBodyPattern(it)
            } else {
                StringValueRequestBodyPattern(this)
            }
        } ?: StringValueRequestBodyPattern(this)

        pattern.matches(str)
        patterns += pattern
        return pattern
    }

    @WireMockDSL
    infix fun doesNotMatch(str: String): JunctionableBodyPattern {
        val pattern = patterns.lastOrNull()?.let {
            if (it.isJunction()) {
                patterns.remove(it)
                StringValueRequestBodyPattern(it)
            } else {
                StringValueRequestBodyPattern(this)
            }
        } ?: StringValueRequestBodyPattern(this)

        pattern.doesNotMatch(str)
        patterns += pattern
        return pattern
    }

    fun remove(pattern: RequestBodyPattern) {
        patterns.remove(pattern)
    }
}