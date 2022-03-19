package dsl.wiremock.request.body.multipart

import com.github.tomakehurst.wiremock.client.WireMock.aMultipart
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.matching.AnythingPattern
import com.github.tomakehurst.wiremock.matching.MultipartValuePattern
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MultipartRequestBodyScopeTest {

    val scope = MultipartRequestBodyScope()

    @Test
    fun `add should create pattern builder equals to WireMock`() {

        val pattern = MultipartRequestBodyPattern().apply {
            name = "init"
            type = "ALL"
            headers contain "X-Test"
            body json """{"key":"value"}"""
        }

        scope.add(pattern)

        val vmMultipart = aMultipart()
            .withName("init")
            .matchingType(MultipartValuePattern.MatchingType.ALL)
            .withHeader("X-Test", AnythingPattern())
            .withBody(equalToJson("""{"key":"value"}"""))

        assertThat(scope.patterns.first().build())
            .usingRecursiveComparison()
            .isEqualTo(vmMultipart.build())
    }
}