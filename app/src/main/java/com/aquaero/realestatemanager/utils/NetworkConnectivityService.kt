package com.aquaero.realestatemanager.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

/** Sealed model for holding connectivity status details */
sealed class ConnectionState {
    data object Available: ConnectionState()
    data object Unavailable: ConnectionState()
}

/** Core Android utilities */

/**
 * Network utility to get current state of internet connection
 */
val Context.currentConnectivityState: ConnectionState
    get() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return getCurrentConnectivityState(connectivityManager)
    }

@SuppressLint("NewApi")
private fun getCurrentConnectivityState(connectivityManager: ConnectivityManager): ConnectionState {
    val activeNetwork = connectivityManager.activeNetwork
    activeNetwork ?: return ConnectionState.Unavailable

    val network = connectivityManager.getNetworkCapabilities(activeNetwork)
    return when {
        network?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> ConnectionState.Available
        network?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> ConnectionState.Available
        else -> ConnectionState.Unavailable
    }
}

/**
 * Network Utility to observe availability or unavailability of Internet connection
 */
@SuppressLint("NewApi")
fun Context.observeConnectivityAsFlow() = callbackFlow {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val callback = NetworkCallback { connectionState -> trySend(connectionState) }
    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    connectivityManager.registerNetworkCallback(networkRequest, callback)

    // Set current state
    val currentState = getCurrentConnectivityState(connectivityManager)
    trySend(currentState)

    // Remove callback when not used
    awaitClose {
        // Remove listeners
        connectivityManager.unregisterNetworkCallback(callback)
    }
}

fun NetworkCallback(callback: (ConnectionState) -> Unit): ConnectivityManager.NetworkCallback {
    return object: ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            callback(ConnectionState.Available)
        }
        override fun onLost(network: Network) {
            callback(ConnectionState.Unavailable)
        }
    }
}

/** Compose utilities (Android utilities converted for Compose) */

/**
 * Compose utilities for observing connectivity changes
 */
@Composable
fun connectivityState(): State<ConnectionState> {
    val context = LocalContext.current
    // Creates a State<ConnectionState> with current connectivity state as initial value
    return produceState(initialValue = context.currentConnectivityState) {
        // In a coroutine, can make suspend calls
        context.observeConnectivityAsFlow().collect { value = it }
    }
}