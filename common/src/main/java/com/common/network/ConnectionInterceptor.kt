package com.common.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ConnectionInterceptor @Inject constructor(private val connectionService: ConnectionService) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!connectionService.hasConnection()) {
            throw NoInternetException()
        }

        return chain.proceed(chain.request())
    }
}
