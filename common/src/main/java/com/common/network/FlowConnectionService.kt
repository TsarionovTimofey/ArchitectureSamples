package com.common.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

class FlowConnectionService @Inject constructor(
    context: Context,
    dispatcher: CoroutineDispatcher
) : ConnectionService {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @OptIn(ExperimentalCoroutinesApi::class)
    @Suppress("BlockingMethodInNonBlockingContext")
    private val connectionFlow = callbackFlow {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySendBlocking(true)
            }

            override fun onUnavailable() {
                trySendBlocking(false)
            }

            override fun onLost(network: Network) {
                trySendBlocking(false)
            }
        }
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(
            request,
            networkCallback
        )

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }.shareIn(
        scope = CoroutineScope(dispatcher),
        started = SharingStarted.WhileSubscribed(),
        replay = 1
    )

    override fun observeConnectionState(): Flow<Boolean> = connectionFlow

    override fun hasConnection(): Boolean {
        val connectedNetwork = connectivityManager.allNetworks
            .firstOrNull { it.isConnected() }
        return connectedNetwork != null
    }

    private fun Network.isConnected(): Boolean {
        val networkCapabilities = connectivityManager.getNetworkCapabilities(this)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }
}
