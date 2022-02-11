package dsl.wiremock.request.body

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.StringValuePattern
import dsl.wiremock.WireMockDSL

abstract class BasicRequestBodyPattern(val scope: RequestBodyScope): RequestBodyPattern, JunctionableBodyPattern {
    private var junctionPattern: ((Array<StringValuePattern>) -> StringValuePattern)? = null

    private var lastJunctionPattern: ((Array<StringValuePattern>) -> StringValuePattern)? = null

    protected lateinit var currentValue: String
    protected lateinit var valuePattern: StringValuePattern

    private lateinit var currentPattern: StringValuePattern
    private var originalPattern: StringValuePattern? = null

    constructor(pattern: BasicRequestBodyPattern) : this(pattern.scope) {
        this.junctionPattern = pattern.junctionPattern
        this.lastJunctionPattern = pattern.lastJunctionPattern
        this.currentValue = pattern.currentValue
        this.currentPattern = pattern.currentPattern
        this.valuePattern = pattern.valuePattern
        this.originalPattern = pattern.originalPattern
    }

    override fun isJunction(): Boolean = junctionPattern != null

    override fun getPattern(): StringValuePattern {
        return currentPattern
    }

    protected fun applyPattern(pattern: StringValuePattern) {

        valuePattern = pattern

        this.currentPattern = junctionPattern?.let{
            lastJunctionPattern = junctionPattern
            it.invoke(arrayOf(originalPattern!!, pattern))
        } ?: pattern

        junctionPattern = null
    }

    protected fun modifyPattern(pattern: StringValuePattern) {
        this.valuePattern = pattern
        this.currentPattern = lastJunctionPattern?.invoke(arrayOf(this.originalPattern!!, pattern)) ?: pattern
    }

    @WireMockDSL
    override infix fun or(@Suppress("UNUSED_PARAMETER") scope: RequestBodyScope): RequestBodyScope {
        junctionPattern = WireMock::or
        originalPattern = this.valuePattern
        lastJunctionPattern = null
        return scope
    }

    @WireMockDSL
    override infix fun or(pattern: JunctionableBodyPattern): JunctionableBodyPattern {
        applyPattern(WireMock.or(this.valuePattern, pattern.getPattern()))
        scope.remove(pattern)
        junctionPattern = null
        lastJunctionPattern = null
        originalPattern = null
        return this
    }

    @WireMockDSL
    override infix fun and(@Suppress("UNUSED_PARAMETER") scope: RequestBodyScope): RequestBodyScope {
        junctionPattern = WireMock::and
        originalPattern = this.valuePattern
        lastJunctionPattern = null
        return scope
    }

    @WireMockDSL
    override infix fun and(pattern: JunctionableBodyPattern): JunctionableBodyPattern {
        applyPattern(WireMock.and(this.valuePattern, pattern.getPattern()))
        scope.remove(pattern)
        junctionPattern = null
        lastJunctionPattern = null
        originalPattern = null
        return this
    }
}