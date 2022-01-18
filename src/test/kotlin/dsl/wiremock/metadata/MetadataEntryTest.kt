package dsl.wiremock.metadata

import com.github.tomakehurst.wiremock.common.Metadata
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MetadataEntryTest {

    @Test
    fun `attr should add metadata with name and value to metadata`() {

        val builder = Metadata.metadata()
        val entry = MetadataEntry(builder)
        val value = 100

        entry.apply{
            "name" attr value
        }

        val metadata = builder.build()
        assertThat(metadata.getValue("name")).isEqualTo(value)
    }

    @Test
    fun `list should add metadata with name and value to metadata`() {

        val builder = Metadata.metadata()
        val entry = MetadataEntry(builder)
        val list = listOf(1, 2, 3)

        entry.apply {
            "name" list list
        }

        val metadata = builder.build()
        assertThat(metadata.getValue("name")).isEqualTo(list)
    }

    @Test
    fun `metadata should add metadata with name and all values to metadata`() {

        val builder = Metadata.metadata()
        val entry = MetadataEntry(builder, "name")
        val list = listOf("value2")

        entry.apply {
            name metadata {
                "key1" attr "value1"
                "key2" list list
                "key3" metadata  {
                    "key4" attr "value4"
                }
            }
        }

        val metadata = builder.build()
        assertThat(metadata.getMetadata("name").getValue("key1")).isEqualTo("value1")
        assertThat(metadata.getMetadata("name").getValue("key2")).isEqualTo(list)
        assertThat(metadata.getMetadata("name").getMetadata("key3").getValue("key4")).isEqualTo("value4")
    }

}