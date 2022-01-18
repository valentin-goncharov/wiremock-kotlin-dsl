package dsl.wiremock.stubs.scenario

import dsl.wiremock.WireMockDSL
import dsl.wiremock.request.RequestScope

@WireMockDSL
class ScenarioRequestScope: RequestScope() {
    var state: String? = null
    var require: String? = null
}