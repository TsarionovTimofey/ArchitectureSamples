package com.common.auth

interface TokenProvider {
    fun token(): String?
}
