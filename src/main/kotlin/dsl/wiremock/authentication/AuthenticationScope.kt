package dsl.wiremock.authentication

import dsl.wiremock.WireMockDSL

@WireMockDSL
class AuthenticationScope {

    private lateinit var _username: String
    private lateinit var _password: String

    @WireMockDSL
    infix fun username(str: String): AuthenticationScope {
        this._username = str
        return this
    }

    @WireMockDSL
    infix fun password(str: String): AuthenticationScope {
        this._password = str
        return this
    }

    fun getUsername(): String {
        return _username
    }

    fun getPassword(): String {
        return _password
    }

    fun isInitialized(): Boolean {
        return this::_username.isInitialized && this::_password.isInitialized
    }

}