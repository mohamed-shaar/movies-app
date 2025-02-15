package com.company.moviesapp.data.local.datasource

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenProvider @Inject constructor() {

    private var token: String? = null

    fun setToken(token: String) {
        this.token = token
    }

    fun getToken(): String? {
        return token
    }
}