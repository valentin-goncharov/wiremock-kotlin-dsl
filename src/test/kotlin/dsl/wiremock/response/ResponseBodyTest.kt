package dsl.wiremock.response

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.common.Json
import org.apache.commons.codec.binary.Base64
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ResponseBodyTest {

    lateinit var builder: ResponseDefinitionBuilder

    @BeforeEach
    fun init() {
        builder = ResponseDefinitionBuilder()
    }

    @Test
    fun `string should add string body to the builder`() {
        val responseBody = ResponseBody(builder)

        responseBody string "string body"

        val response = builder.build()

        assertThat(response.body)
            .isInstanceOf(String::class.java)
            .isEqualTo("string body")
    }

    @Test
    fun `base64 should add base64 encoded body to the builder`() {
        val responseBody = ResponseBody(builder)

        val base64String = Base64.encodeBase64String("base64 string".toByteArray())

        responseBody base64 base64String

        val response = builder.build()

        assertThat(response.base64Body)
            .isInstanceOf(String::class.java)
            .isEqualTo(base64String)
    }

    @Test
    fun `json for string should add json body to the builder`() {
        val responseBody = ResponseBody(builder)

        val jsonBody = """
            {
                "key1": "value1",
                "key2": "value2"
            }
        """

        responseBody json jsonBody

        val mapper = ObjectMapper()

        val jsonNode = mapper.readTree(jsonBody)

        val response = builder.build()

        assertThat(response.jsonBody)
            .isInstanceOf(JsonNode::class.java)
            .isEqualTo(jsonNode)
    }

    @Test
    fun `json for json node should add json body to the builder`() {
        val responseBody = ResponseBody(builder)

        val jsonBody = """
            {
                "key1": "value1",
                "key2": "value2"
            }
        """.trimIndent()

        val mapper = ObjectMapper()

        val jsonNode = mapper.readTree(jsonBody)

        responseBody json jsonNode

        val response = builder.build()

        assertThat(response.jsonBody)
            .isInstanceOf(JsonNode::class.java)
            .isEqualTo(jsonNode)
    }

    @Test
    fun `entity should add string body to the builder`() {
        val responseBody = ResponseBody(builder)

        val jsonObj = Data("key", 1)

        val bodyString = Json.write(jsonObj)

        responseBody entity jsonObj

        val response = builder.build()

        assertThat(response.body)
            .isInstanceOf(String::class.java)
            .isEqualTo(bodyString)
    }

    @Test
    fun `file should add file path body to the builder`() {
        val responseBody = ResponseBody(builder)

        val filePath = "test_response_body.txt"

        responseBody file filePath

        val response = builder.build()

        assertThat(response.bodyFileName)
            .isInstanceOf(String::class.java)
            .isEqualTo(filePath)
    }


}

internal data class Data(val key: String, val value: Int)