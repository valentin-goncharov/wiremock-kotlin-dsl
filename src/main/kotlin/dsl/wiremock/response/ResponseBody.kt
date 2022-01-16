package dsl.wiremock.response

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.common.Json
import dsl.wiremock.WireMockDSL

@WireMockDSL
class ResponseBody (private val builder: ResponseDefinitionBuilder) {

    private val stringHandler: (body: String) -> ResponseDefinitionBuilder = builder::withBody
    private val fileHandler: (path: String) -> ResponseDefinitionBuilder = builder::withBodyFile
    private val jsonHandler: (json: JsonNode) -> ResponseDefinitionBuilder = builder::withJsonBody
    private val base64Handler: (body: String) -> ResponseDefinitionBuilder = builder::withBase64Body

    @WireMockDSL
    infix fun entity(obj: Any) {
        stringHandler(Json.write(obj))
    }

    @WireMockDSL
    infix fun json(node: JsonNode) {
        jsonHandler(node)
    }

    @WireMockDSL
    infix fun json(content: String) {
        val mapper = ObjectMapper()
        jsonHandler(mapper.readTree(content.trimIndent()))
    }

    @WireMockDSL
    infix fun string(content: String) {
        stringHandler(content)
    }

    @WireMockDSL
    infix fun file(path: String) {
        fileHandler(path)
    }

    @WireMockDSL
    infix fun base64(content: String) {
        base64Handler(content)
    }
}