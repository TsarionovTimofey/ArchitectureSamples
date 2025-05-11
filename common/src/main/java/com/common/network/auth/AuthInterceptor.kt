package com.common.network.auth

import com.common.network.HeaderInterceptor

const val AUTH_HEADER = "Authorization"

class AuthInterceptor(private val tokenProvider: () -> String?) :
    HeaderInterceptor(AUTH_HEADER, {
        tokenProvider()
    })
