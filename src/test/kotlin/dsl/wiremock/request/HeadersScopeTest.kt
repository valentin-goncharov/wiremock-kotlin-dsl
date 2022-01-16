package dsl.wiremock.request

import com.github.tomakehurst.wiremock.matching.AbsentPattern
import com.github.tomakehurst.wiremock.matching.EqualToPattern
import com.github.tomakehurst.wiremock.matching.RegexPattern
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.function.Consumer

internal class HeadersScopeTest {
    private lateinit var headers: HeadersScope

    @BeforeEach
    fun init() {
        headers = HeadersScope()
    }

    @Test
    fun `contain should create new header and put them into patterns`() {

        val header = headers contain "Accept"
        header equalTo "text/plain"

        headers contain "Accept" matches "application/.+"

        assertThat(header)
            .isInstanceOf(Header::class.java)
            .hasFieldOrPropertyWithValue("name", "Accept")

        assertThat(headers.patterns).hasSize(2)

        assertThat(headers.patterns[0]).satisfies(Consumer {
            assertThat(it.name).isEqualTo("Accept")
            assertThat(it.pattern).isInstanceOf(EqualToPattern::class.java)
        })

        assertThat(headers.patterns[1]).satisfies(Consumer {
            assertThat(it.name).isEqualTo("Accept")
            assertThat(it.pattern).isInstanceOf(RegexPattern::class.java)
        })
    }

    @Test
    fun `doNotContain should put header with AbsentPattern into patterns`() {

        headers contain "Accept" equalTo "text/plain"
        headers contain "Accept" equalTo "application/json"

        headers doNotContain "Accept"

        assertThat(headers.patterns).hasSize(1)
        assertThat(headers.patterns[0]).satisfies(Consumer {
            assertThat(it.name).isEqualTo("Accept")
            assertThat(it.pattern).isInstanceOf(AbsentPattern::class.java)
        })
    }
}