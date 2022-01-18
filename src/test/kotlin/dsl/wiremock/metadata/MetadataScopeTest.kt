package dsl.wiremock.metadata

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MetadataScopeTest {

    @Test
    fun `apply should add entries to builder`() {

        val scope = MetadataScope()

        scope.apply {
            "key" attr "value"
            "list" attr listOf(1, 2, 3)
        }

        val metadata = scope.build()
        assertThat(metadata).containsKey("key")
        assertThat(metadata.getValue("key")).isEqualTo("value")

        assertThat(metadata).containsKey("list")
        assertThat(metadata.getList("list"))
            .hasSize(3)
            .containsExactly(1, 2, 3)
    }
}