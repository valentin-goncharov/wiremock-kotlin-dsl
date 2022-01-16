package dsl.wiremock.response

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.http.FixedDelayDistribution
import com.github.tomakehurst.wiremock.http.LogNormal
import com.github.tomakehurst.wiremock.http.UniformDistribution
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.concurrent.ThreadLocalRandom
import java.util.function.Consumer
import kotlin.math.exp
import kotlin.math.roundToLong

@ExtendWith(MockKExtension::class)
internal class DelayTest {

    lateinit var builder: ResponseDefinitionBuilder

    @BeforeEach
    fun init() {
        builder = ResponseDefinitionBuilder()
    }

    @Test
    fun `fixed should add fixed delay to the builder`() {

        val delay = Delay(builder)

        delay fixed 100

        assertThat(builder.build().fixedDelayMilliseconds).isEqualTo(100)
        assertThat(builder.build().chunkedDribbleDelay).isNull()
        assertThat(builder.build().delayDistribution).isNull()
    }

    @Test
    fun `chunkedDribble should add chunked dribble delay to the builder`() {

        val delay = Delay(builder)

        delay chunkedDribble { numberOfChunks = 100; totalDuration = 100 }

        val definition = builder.build()

        assertThat(definition.chunkedDribbleDelay).satisfies(Consumer {
            assertThat(it.numberOfChunks).isEqualTo(100)
            assertThat(it.totalDuration).isEqualTo(100)
        })
        assertThat(definition.fixedDelayMilliseconds == null).isTrue
        assertThat(definition.delayDistribution).isNull()
    }

    @Test
    fun `random should add random delay with fixed distribution to the builder`() {

        val delay = Delay(builder)

        delay random 300L

        val definition = builder.build()
        assertThat(definition.delayDistribution).satisfies(Consumer {
            assertThat(it).isInstanceOf(FixedDelayDistribution::class.java)
            assertThat(it.sampleMillis()).isEqualTo(300L)
        })
        assertThat(definition.fixedDelayMilliseconds == null).isTrue
        assertThat(definition.chunkedDribbleDelay).isNull()
    }

    @Test
    fun `logNormalRandom should add random delay with log normal distribution to the builder`() {
        val delay = Delay(builder)

        val medianValue = 100.0
        val sigmaValue = 100.0

        val random: ThreadLocalRandom = mockk()
        mockkStatic(ThreadLocalRandom::current)
        every { ThreadLocalRandom.current() } returns random
        every { random.nextGaussian() } returns 1.0
        val sample = (exp(ThreadLocalRandom.current().nextGaussian() * sigmaValue) * medianValue).roundToLong()

        delay logNormalRandom {
            medianMilliseconds = medianValue
            sigma = sigmaValue
        }

        val definition = builder.build()
        assertThat(definition.delayDistribution).satisfies(Consumer {
            assertThat(it).isInstanceOf(LogNormal::class.java)
            assertThat(it.sampleMillis()).isEqualTo(sample)
        })
        assertThat(definition.fixedDelayMilliseconds == null).isTrue
        assertThat(definition.chunkedDribbleDelay).isNull()

    }

    @Test
    fun `uniformRandom should add random delay with uniform distribution to the builder`() {
        val delay = Delay(builder)

        val lower = 10
        val upper = 100

        val random: ThreadLocalRandom = mockk()
        mockkStatic(ThreadLocalRandom::current)
        every { ThreadLocalRandom.current() } returns random
        every { random.nextLong(lower.toLong(), (upper+1).toLong()) } returns 99L

        delay uniformRandom {
            lowerMilliseconds = lower
            upperMilliseconds = upper
        }

        val definition = builder.build()
        assertThat(definition.delayDistribution).satisfies(Consumer {
            assertThat(it).isInstanceOf(UniformDistribution::class.java)
            assertThat(it.sampleMillis()).isEqualTo(99L)
        })
        assertThat(definition.fixedDelayMilliseconds == null).isTrue
        assertThat(definition.chunkedDribbleDelay).isNull()
    }


}