package dsl.wiremock.request

import com.github.tomakehurst.wiremock.matching.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UrlScopeTest {

    private lateinit var url: UrlScope

    @BeforeEach
    fun init() {
        url = UrlScope()
    }

    @Test
    fun `equalTo should set pattern to EqualToPattern`() {
        val testUrl = "/test/url"
        url equalTo testUrl

        Assertions.assertThat(url.pattern).isInstanceOf(UrlPattern::class.java)
        Assertions.assertThat(url.pattern.pattern)
            .isInstanceOf(EqualToPattern::class.java)
            .hasFieldOrPropertyWithValue("expectedValue", testUrl)
    }

    @Test
    fun `matches should set pattern to RegexPattern`() {
        val regexp = ".*AAA.*"

        url matches regexp

        Assertions.assertThat(url.pattern).isInstanceOf(UrlPattern::class.java)
        Assertions.assertThat(url.pattern.pattern)
            .isInstanceOf(RegexPattern::class.java)
            .hasFieldOrPropertyWithValue("expectedValue", regexp)
    }

    @Test
    fun `pathEqualTo should set pattern to EqualToPattern`() {
        val pathUrl = "/test/path"

        url pathEqualTo pathUrl

        Assertions.assertThat(url.pattern).isInstanceOf(UrlPathPattern::class.java)
        Assertions.assertThat(url.pattern.pattern)
            .isInstanceOf(EqualToPattern::class.java)
            .hasFieldOrPropertyWithValue("expectedValue", pathUrl)
    }

    @Test
    fun `pathMatches should set pattern to RegexPattern`() {
        val regexp = ".*AAA.*"

        url pathMatches regexp

        Assertions.assertThat(url.pattern).isInstanceOf(UrlPathPattern::class.java)
        Assertions.assertThat(url.pattern.pattern)
            .isInstanceOf(RegexPattern::class.java)
            .hasFieldOrPropertyWithValue("expectedValue", regexp)
    }
}