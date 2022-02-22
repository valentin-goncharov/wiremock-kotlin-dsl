package dsl.wiremock.stubs

import io.ktor.client.*
import io.ktor.client.engine.java.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

open class BaseStubTest {

    lateinit var client: HttpClient

    @BeforeEach
    fun init() {
        client = HttpClient(Java)
    }

    @AfterEach
    fun close() {
        client.close()
    }
}