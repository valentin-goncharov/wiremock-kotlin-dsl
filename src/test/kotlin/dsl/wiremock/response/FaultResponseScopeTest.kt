package dsl.wiremock.response

import com.github.tomakehurst.wiremock.http.Fault
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class FaultResponseScopeTest {

    @Test
    fun `set type should add fault type to builder`() {
        val faultScope = FaultResponseScope()
        faultScope.type = FaultType.MALFORMED_RESPONSE_CHUNK

        val response = faultScope.builder.build()
        assertThat(response.fault).isEqualTo(Fault.MALFORMED_RESPONSE_CHUNK)
    }
}