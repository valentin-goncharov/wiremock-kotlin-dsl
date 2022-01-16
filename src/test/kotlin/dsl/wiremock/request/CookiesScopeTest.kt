package dsl.wiremock.request

import com.github.tomakehurst.wiremock.matching.AbsentPattern
import com.github.tomakehurst.wiremock.matching.AnythingPattern
import org.assertj.core.api.Assertions
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

        Assertions.assertThat(cookie)
            .isInstanceOf(Cookie::class.java)
            .hasFieldOrPropertyWithValue("name", "Session")

        Assertions.assertThat(cookies.patterns).hasSize(1)

        Assertions.assertThat(cookies.patterns[0]).satisfies(Consumer {
            Assertions.assertThat(it.name).isEqualTo("Session")
            Assertions.assertThat(it.pattern).isInstanceOf(AnythingPattern::class.java)
        })
    }

    @Test
    fun `doNotContain should put cookie with AbsentPattern into patterns`() {

        cookies doNotContain "Version"

        Assertions.assertThat(cookies.patterns).hasSize(1)
        Assertions.assertThat(cookies.patterns[0]).satisfies(Consumer {
            Assertions.assertThat(it.name).isEqualTo("Version")
            Assertions.assertThat(it.pattern).isInstanceOf(AbsentPattern::class.java)
        })
    }
}