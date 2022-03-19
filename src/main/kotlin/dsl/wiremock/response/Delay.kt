/*-
 * ========================LICENSE_START=================================
 * Wiremock Kotlin DSL
 * %%
 * Copyright (C) 2022 Valentin Goncharov
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
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