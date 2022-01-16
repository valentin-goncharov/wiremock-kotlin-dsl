package dsl.wiremock.response

import com.github.tomakehurst.wiremock.http.Fault
import dsl.wiremock.WireMockDSL

@WireMockDSL
class FaultResponseScope: DelayedResponse() {
    var type: FaultType? = null
        set(value) {
            field = value
            value?.let{ builder.withFault(Fault.valueOf(it.name)) }
        }
}

@WireMockDSL
enum class FaultType {
    CONNECTION_RESET_BY_PEER,
    EMPTY_RESPONSE,
    MALFORMED_RESPONSE_CHUNK,
    RANDOM_DATA_THEN_CLOSE
}