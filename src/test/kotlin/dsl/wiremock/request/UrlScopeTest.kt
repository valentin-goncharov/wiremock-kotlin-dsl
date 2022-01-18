package dsl.wiremock.request

import com.github.tomakehurst.wiremock.matching.*
import org.assertj.core.api.Assertions.assertThat
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

        assertThat(url.pattern).isInstanceOf(UrlPattern::class.java)
        assertThat(url.pattern.pattern)
            .isInstanceOf(EqualToPattern::class.java)
            .hasFieldOrPropertyWithValue("expectedValue", testUrl)
    }

    @Test
    fun `matches should set pattern to RegexPattern`() {
        val regexp = ".*AAA.*"

        url matches regexp

        assertThat(url.pattern).isInstanceOf(UrlPattern::class.java)
        assertThat(url.pattern.pattern)
            .isInstanceOf(RegexPattern::class.java)
            .hasFieldOrPropertyWithValue("expectedValue", regexp)
    }

    @Test
    fun `pathEqualTo should set pattern to EqualToPattern`() {
        val pathUrl = "/test/path"

        url pathEqualTo pathUrl

        assertThat(url.pattern).isInstanceOf(UrlPathPattern::class.java)
        assertThat(url.pattern.pattern)
            .isInstanceOf(EqualToPattern::class.java)
            .hasFieldOrPropertyWithValue("expectedValue", pathUrl)
    }

    @Test
    fun `pathMatches should set pattern to RegexPattern`() {
        val regexp = ".*AAA.*"

        url pathMatches regexp

        assertThat(url.pattern).isInstanceOf(UrlPathPattern::class.java)
        assertThat(url.pattern.pattern)
            .isInstanceOf(RegexPattern::class.java)
            .hasFieldOrPropertyWithValue("expectedValue", regexp)
    }
}