package com.common.network.auth

/**
 * Provides an access token for request com.apps65.mvitemplate.authorization.
 */
interface AccessTokenProvider {

    /**
     * Returns an access token. In the event that you don't have a token return null.
     */
    fun token(): String?
}
