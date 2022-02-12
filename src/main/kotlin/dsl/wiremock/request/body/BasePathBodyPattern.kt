package dsl.wiremock.request.body

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.StringValuePattern
import dsl.wiremock.WireMockDSL

abstract class BasePathBodyPattern : StringValueRequestBodyPattern, PathValuePattern {

        protected val matchingPathPattern: (String, StringValuePattern) -> StringValuePattern

        constructor(
                scope: RequestBodyScope,
                matchingPathPattern: (String, StringValuePattern) -> StringValuePattern
        ): super(scope) {
                this.matchingPathPattern = matchingPathPattern
        }

        constructor(
                pattern: RequestBodyPattern,
                matchingPathPattern: (String, StringValuePattern) -> StringValuePattern
        ): super(pattern) {
                this.matchingPathPattern = matchingPathPattern
        }


        @WireMockDSL
        override infix fun equalTo(str: String): JunctionableBodyPattern {
                modifyPattern(matchingPathPattern(this.currentValue, WireMock.equalTo(str)))
                return this
        }

        @WireMockDSL
        override infix fun contains(str: String): JunctionableBodyPattern {
                modifyPattern(matchingPathPattern(this.currentValue, WireMock.containing(str)))
                return this
        }

        @WireMockDSL
        override infix fun matches(str: String): JunctionableBodyPattern {
                modifyPattern(matchingPathPattern(this.currentValue, WireMock.matching(str)))
                return this
        }

        @WireMockDSL
        override infix fun doesNotMatch(str: String): JunctionableBodyPattern {
                modifyPattern(matchingPathPattern(this.currentValue, WireMock.notMatching(str)))
                return this
        }

}