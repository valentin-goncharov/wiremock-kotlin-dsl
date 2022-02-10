package dsl.wiremock.mapping

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.EqualToJsonPattern
import com.github.tomakehurst.wiremock.matching.RegexPattern
import com.github.tomakehurst.wiremock.matching.UrlPattern
import dsl.wiremock.request.Cookie
import dsl.wiremock.request.Header
import dsl.wiremock.request.Parameter
import dsl.wiremock.request.body.StringValueRequestBodyPattern
import dsl.wiremock.request.body.JsonBodyPattern
import dsl.wiremock.request.body.RequestBodyScope
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.function.Consumer

internal class MappingBuilderExtensionTest {

    lateinit var builder: MappingBuilder

    val scope = RequestBodyScope()

    @BeforeEach
    fun init() {
        builder = WireMock.any(UrlPattern.ANY)
        val responseBuilder = ResponseDefinitionBuilder().withStatus(200)
        builder.willReturn(responseBuilder)
    }

    @Test
    fun `withHeaders should add headers patterns to the mapping builder`() {

        val headerOne = Header(name = "Accept", pattern = WireMock.equalTo("plain/text"))
        val headerTwo = Header(
            name = "Authorization",
            pattern = WireMock.equalTo("Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
        )

        builder.withHeaders(listOf(headerOne, headerTwo))

        val stub = builder.build()

        assertThat(stub.request.headers).containsKey("Accept")
        assertThat(stub.request.headers["Accept"]).satisfies(Consumer {
            assertThat(it?.valuePattern?.value).isEqualTo("plain/text")
        })

        assertThat(stub.request.headers).containsKey("Authorization")
        assertThat(stub.request.headers["Authorization"]).satisfies(Consumer {
            assertThat(it?.valuePattern?.value).isEqualTo("Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
        })
    }

    @Test
    fun `withCookies should add cookies patterns to the mapping builder`() {

        val cookieOne = Cookie(name = "Session", pattern = WireMock.equalTo("SkjdhasdHH"))
        val cookieTwo = Cookie(name = "Version", pattern = WireMock.equalTo("1.0"))

        builder.withCookies(listOf(cookieOne, cookieTwo))

        val stub = builder.build()

        assertThat(stub.request.cookies).containsKey("Session")
        assertThat(stub.request.cookies["Session"]).satisfies(Consumer {
            assertThat(it?.value).isEqualTo("SkjdhasdHH")
        })

        assertThat(stub.request.cookies).containsKey("Version")
        assertThat(stub.request.cookies["Version"]).satisfies(Consumer {
            assertThat(it?.value).isEqualTo("1.0")
        })
    }

    @Test
    fun `withQueryParams should add query parameters patterns to the mapping builder`() {

        val paramOne = Parameter(name = "stage", pattern = WireMock.equalTo("dev"))
        val paramTwo = Parameter(
            name = "startDate",
            pattern = WireMock.equalToDateTime("2022-01-16 22:12:00")
        )

        builder.withQueryParams(listOf(paramOne, paramTwo))

        val stub = builder.build()

        assertThat(stub.request.queryParameters).containsKey("stage")
        assertThat(stub.request.queryParameters["stage"]).satisfies(Consumer {
            assertThat(it?.valuePattern?.value).isEqualTo("dev")
        })

        assertThat(stub.request.queryParameters).containsKey("startDate")
        assertThat(stub.request.queryParameters["startDate"]).satisfies(Consumer {
            assertThat(it?.valuePattern?.value).isEqualTo("2022-01-16 22:12:00")
        })
    }

    @Test
    fun `withRequestBodyPatterns should add body patterns to the mapping builder`() {

        val patternOne = JsonBodyPattern(scope).equalToJson("""{"key": "value"}""")
        val patternTwo = StringValueRequestBodyPattern(scope).matches(".*value.*")

        builder.withRequestBodyPatterns(listOf(patternOne, patternTwo))

        val stub = builder.build()

        assertThat(stub.request.bodyPatterns).hasSize(2)
        assertThat(stub.request.bodyPatterns[0]).satisfies(Consumer {
            assertThat(it).isInstanceOf(EqualToJsonPattern::class.java)
            assertThat(it.value).isEqualTo("""{"key": "value"}""")
        })

        assertThat(stub.request.bodyPatterns[1]).satisfies(Consumer {
            assertThat(it).isInstanceOf(RegexPattern::class.java)
            assertThat(it.value).isEqualTo(".*value.*")
        })
    }
}