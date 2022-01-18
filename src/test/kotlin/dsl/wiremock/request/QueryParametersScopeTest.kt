package dsl.wiremock.request

import com.github.tomakehurst.wiremock.matching.AbsentPattern
import com.github.tomakehurst.wiremock.matching.NegativeRegexPattern
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.function.Consumer

internal class QueryParametersScopeTest {
    private lateinit var queryParameters: QueryParametersScope

    @BeforeEach
    fun init() {
        queryParameters = QueryParametersScope()
    }

    @Test
    fun `contain should create new parameter and put them into patterns`() {

        val parameter = queryParameters contain "scope" doesNotMatch "dev"

        assertThat(parameter)
            .isInstanceOf(Parameter::class.java)
            .hasFieldOrPropertyWithValue("name", "scope")

        assertThat(queryParameters.patterns).hasSize(1)

        assertThat(queryParameters.patterns[0]).satisfies(Consumer {
            assertThat(it.name).isEqualTo("scope")
            assertThat(it.pattern).isInstanceOf(NegativeRegexPattern::class.java)
        })
    }

    @Test
    fun `doNotContain should put parameter with AbsentPattern into patterns`() {

        queryParameters doNotContain "scope"

        assertThat(queryParameters.patterns).hasSize(1)
        assertThat(queryParameters.patterns[0]).satisfies(Consumer {
            assertThat(it.name).isEqualTo("scope")
            assertThat(it.pattern).isInstanceOf(AbsentPattern::class.java)
        })
    }
}