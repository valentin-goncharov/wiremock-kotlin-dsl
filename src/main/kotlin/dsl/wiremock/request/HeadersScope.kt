package dsl.wiremock.request

import dsl.wiremock.WireMockDSL

@WireMockDSL
class HeadersScope: NamedPatternScope()

typealias Header = NamedPattern