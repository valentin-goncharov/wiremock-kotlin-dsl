package dsl.wiremock.response

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import dsl.wiremock.WireMockDSL

@WireMockDSL
class TransformersScope {

    val transformers = mutableSetOf<String>()
    val transformerParameters = mutableMapOf<String, TransformerParameter>()

    @WireMockDSL
    infix fun with(name: String) {
        transformers.add(name)
    }

    @WireMockDSL
    infix fun withParameter(name: String): TransformerParameter {
        val param = TransformerParameter(name)
        transformerParameters[name] = param
        return param
    }
}

@WireMockDSL
class TransformerParameter(val name: String) {

    lateinit var value: Any

    @WireMockDSL
    infix fun equalTo(value: Any) {
        this.value = value
    }
}

fun ResponseDefinitionBuilder.withTransformers(transformers: TransformersScope): ResponseDefinitionBuilder {
    this.withTransformers(*transformers.transformers.toTypedArray())
    this.withTransformerParameters(transformers.transformerParameters.mapValues { it.value.value })
    return this
}