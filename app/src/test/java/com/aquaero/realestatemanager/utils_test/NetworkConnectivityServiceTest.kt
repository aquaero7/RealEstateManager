package com.aquaero.realestatemanager.utils_test

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.aquaero.realestatemanager.utils.ConnectionState
import com.aquaero.realestatemanager.utils.currentConnectivityState
import com.aquaero.realestatemanager.utils.observeConnectivityAsFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.robolectric.RobolectricTestRunner

//@RunWith(MockitoJUnitRunner::class)
@RunWith(RobolectricTestRunner::class)
class NetworkConnectivityServiceTest {

    private lateinit var context: Context
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var network: Network
    private lateinit var networkCapabilities: NetworkCapabilities


    @Before
    fun setUp() {
        context = mock(Context::class.java)
        connectivityManager = mock(ConnectivityManager::class.java)
        network = mock(Network::class.java)
        networkCapabilities = mock(NetworkCapabilities::class.java)

        doReturn(connectivityManager).`when`(context).getSystemService(Context.CONNECTIVITY_SERVICE)
    }


    @Test
    fun getCurrentUnavailableConnectivityStateWithSuccess() {
        // Configure the mock behaviour to return an inactive network
        doReturn(null).`when`(connectivityManager).activeNetwork

        assertEquals(ConnectionState.Unavailable, context.currentConnectivityState)
    }

    @Test
    fun getCurrentAvailableConnectivityStateWithSuccess() {
        // Configure the mock behaviour to return an active network and WiFi network capabilities
        doReturn(network).`when`(connectivityManager).activeNetwork
        doReturn(networkCapabilities).`when`(connectivityManager).getNetworkCapabilities(network)
        doReturn(true).`when`(networkCapabilities).hasTransport(NetworkCapabilities.TRANSPORT_WIFI)

        assertEquals(ConnectionState.Available, context.currentConnectivityState)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun observeUnavailableConnectivityAsFlowWithSuccess() = runTest {
        // Configure the mock behaviour to return an inactive network
        doReturn(null).`when`(connectivityManager).activeNetwork

        // Simulate calling registerNetworkCallback
        doAnswer { invocation ->
            val callback = invocation.arguments[1] as ConnectivityManager.NetworkCallback
            callback.onLost(network)
            null // Returns null because the method is of type Unit (void)
        }.`when`(connectivityManager).registerNetworkCallback(
            Mockito.any(NetworkRequest::class.java),
            Mockito.any(ConnectivityManager.NetworkCallback::class.java)
        )

        // Start the connectivity observation flow
        val flow = context.observeConnectivityAsFlow().take(1)  // Take only the first emitted state

        // Collect emitted connectivity states
        val states = mutableListOf<ConnectionState>()
        launch {
            flow.collect { states.add(it) }
        }

        // Wait for all coroutines to complete
        advanceUntilIdle()

        // Check that the status emitted is “Unavailable”
        assertEquals(listOf(ConnectionState.Unavailable), states)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun observeAvailableConnectivityAsFlowWithSuccess() = runTest {
        // Configure the mock behaviour to return an active network and WiFi network capabilities
        doReturn(network).`when`(connectivityManager).activeNetwork
        doReturn(networkCapabilities).`when`(connectivityManager).getNetworkCapabilities(network)
        doReturn(true).`when`(networkCapabilities).hasTransport(NetworkCapabilities.TRANSPORT_WIFI)

        // Simulate calling registerNetworkCallback
        doAnswer { invocation ->
            val callback = invocation.arguments[1] as ConnectivityManager.NetworkCallback
            callback.onAvailable(network)
            null // Returns null because the method is of type Unit (void)
        }.`when`(connectivityManager).registerNetworkCallback(
            Mockito.any(NetworkRequest::class.java),
            Mockito.any(ConnectivityManager.NetworkCallback::class.java)
        )

        // Start the connectivity observation flow
        val flow = context.observeConnectivityAsFlow().take(1)  // Take only the first emitted state

        // Collect emitted connectivity states
        val states = mutableListOf<ConnectionState>()
        launch {
            flow.collect { states.add(it) }
        }

        // Wait for all coroutines to complete
        advanceUntilIdle()

        // Check that the status emitted is "Available"
        assertEquals(listOf(ConnectionState.Available), states)
    }

}