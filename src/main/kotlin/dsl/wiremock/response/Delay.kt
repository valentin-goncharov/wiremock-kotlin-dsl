package dsl.wiremock.response

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.http.FixedDelayDistribution
import dsl.wiremock.WireMockDSL

@WireMockDSL
class Delay (private val builder: ResponseDefinitionBuilder) {

    @WireMockDSL
    infix fun fixed(millis: Int) {
        builder.withFixedDelay(millis)
    }

    @WireMockDSL
    infix fun chunkedDribble(fn: ChunkedDribbleDelay.() -> Unit){
        val delay = ChunkedDribbleDelay().apply(fn)

        with(delay) {
            this@Delay.builder.withChunkedDribbleDelay(numberOfChunks, totalDuration)
        }
    }

    @WireMockDSL
    infix fun random(sampleMilliseconds: Long){
        builder.withRandomDelay(FixedDelayDistribution(sampleMilliseconds))
    }

    @WireMockDSL
    infix fun logNormalRandom(fn: LogNormalRandomDelay.() -> Unit){
        val delay = LogNormalRandomDelay().apply(fn)

        with(delay) {
            this@Delay.builder.withLogNormalRandomDelay(medianMilliseconds, sigma)
        }
    }

    @WireMockDSL
    infix fun uniformRandom(fn: UniformRandomDelay.() -> Unit) {
        val delay = UniformRandomDelay().apply(fn)
        with(delay) {
            this@Delay.builder.withUniformRandomDelay(lowerMilliseconds, upperMilliseconds)
        }
    }
}

@WireMockDSL
class ChunkedDribbleDelay {
    var numberOfChunks: Int = 0
    var totalDuration: Int = 0
}

@WireMockDSL
class LogNormalRandomDelay {
    var medianMilliseconds: Double = 0.0
    var sigma: Double = 0.0
}

@WireMockDSL
class UniformRandomDelay {
    var lowerMilliseconds: Int = 0
    var upperMilliseconds: Int = 0

}