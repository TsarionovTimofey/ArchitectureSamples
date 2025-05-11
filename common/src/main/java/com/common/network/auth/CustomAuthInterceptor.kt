package com.common.network.auth

import com.common.network.HeaderInterceptor

internal const val AUTH_HEADER_CUSTOM = "authorization"

class CustomAuthInterceptor(private val tokenProvider: () -> String?) :
    HeaderInterceptor(AUTH_HEADER_CUSTOM, { tokenProvider() })
