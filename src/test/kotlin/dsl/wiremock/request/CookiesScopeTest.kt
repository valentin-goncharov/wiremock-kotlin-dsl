package dsl.wiremock.request

import com.github.tomakehurst.wiremock.matching.AbsentPattern
import com.github.tomakehurst.wiremock.matching.AnythingPattern
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.function.Consumer

internal class CookiesScopeTest {
    private lateinit var cookies: CookiesScope

    @BeforeEach
    fun init() {
        cookies = CookiesScope()
    }

    @Test
    fun `contain should create new cookie and put them into patterns`() {

        val cookie = cookies contain "Session"

        assertThat(cookie)
            .isInstanceOf(Cookie::class.java)
            .hasFieldOrPropertyWithValue("name", "Session")

        assertThat(cookies.patterns).hasSize(1)

        assertThat(cookies.patterns[0]).satisfies(Consumer {
            assertThat(it.name).isEqualTo("Session")
            assertThat(it.pattern).isInstanceOf(AnythingPattern::class.java)
        })
    }

    @Test
    fun `doNotContain should put cookie with AbsentPattern into patterns`() {

        cookies doNotContain "Version"

        assertThat(cookies.patterns).hasSize(1)
        assertThat(cookies.patterns[0]).satisfies(Consumer {
            assertThat(it.name).isEqualTo("Version")
            assertThat(it.pattern).isInstanceOf(AbsentPattern::class.java)
        })
    }
}