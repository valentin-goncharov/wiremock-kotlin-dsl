package dsl.wiremock.request

import dsl.wiremock.WireMockDSL

@WireMockDSL
class CookiesScope: NamedPatternScope()

typealias Cookie = NamedPattern
