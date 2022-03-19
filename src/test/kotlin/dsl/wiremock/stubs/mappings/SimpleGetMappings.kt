package dsl.wiremock.stubs

import com.github.tomakehurst.wiremock.junit.Stubbing


fun Stubbing.singleGetMapping(response: String) {
    with(this) {
        get {
            url equalTo "/simple"
        } returns {
            status = 200
            body string response
        }
    }
}