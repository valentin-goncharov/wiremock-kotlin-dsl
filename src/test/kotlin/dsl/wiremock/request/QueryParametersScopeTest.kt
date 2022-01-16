package dsl.wiremock.request

import com.github.tomakehurst.wiremock.matching.AbsentPattern
import com.github.tomakehurst.wiremock.matching.NegativeRegexPattern
import org.assertj.core.api.Assertions
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

        Assertions.assertThat(parameter)
            .isInstanceOf(Parameter::class.java)
            .hasFieldOrPropertyWithValue("name", "scope")

        Assertions.assertThat(queryParameters.patterns).hasSize(1)

        Assertions.assertThat(queryParameters.patterns[0]).satisfies(Consumer {
            Assertions.assertThat(it.name).isEqualTo("scope")
            Assertions.assertThat(it.pattern).isInstanceOf(NegativeRegexPattern::class.java)
        })
    }

    @Test
    fun `doNotContain should put parameter with AbsentPattern into patterns`() {

        queryParameters doNotContain "scope"

        Assertions.assertThat(queryParameters.patterns).hasSize(1)
        Assertions.assertThat(queryParameters.patterns[0]).satisfies(Consumer {
            Assertions.assertThat(it.name).isEqualTo("scope")
            Assertions.assertThat(it.pattern).isInstanceOf(AbsentPattern::class.java)
        })
    }
}