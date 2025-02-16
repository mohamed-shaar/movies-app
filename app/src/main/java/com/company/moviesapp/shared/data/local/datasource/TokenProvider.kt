package com.company.moviesapp.shared.data.local.datasource

import javax.inject.Inject
import javax.inject.Singleton

interface TokenProvider {
    fun setToken(token: String)
    fun getToken(): String?
}

@Singleton
class TokenProviderImpl @Inject constructor() : TokenProvider {

    private var token: String? = null

    override fun setToken(token: String) {
        this.token = token
    }

    override fun getToken(): String? {
        return token
    }
}