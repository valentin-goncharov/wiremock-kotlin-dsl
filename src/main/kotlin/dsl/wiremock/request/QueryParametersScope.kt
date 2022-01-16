package dsl.wiremock.request

import dsl.wiremock.WireMockDSL


@WireMockDSL
class QueryParametersScope: NamedPatternScope()

typealias Parameter = NamedPattern
