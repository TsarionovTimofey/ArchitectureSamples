package com.common.network

import kotlinx.coroutines.flow.Flow

interface ConnectionService {
    fun observeConnectionState(): Flow<Boolean>
    fun hasConnection(): Boolean
}
