package com.aquaero.realestatemanager.viewModel_test

import android.content.Context
import android.content.Intent
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.repository.LocationRepository
import com.aquaero.realestatemanager.utils.ConnectionState
import com.aquaero.realestatemanager.viewmodel.MapViewModel
import kotlinx.coroutines.flow.StateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
//@RunWith(RobolectricTestRunner::class)
class MapViewModelTest {
    private lateinit var locationRepository: LocationRepository
    private lateinit var navController: NavHostController
    private lateinit var context: Context
    private lateinit var viewModel: MapViewModel


    @Before
    fun setup() {
        locationRepository = mock(LocationRepository::class.java)
        navController = mock(NavHostController::class.java)
        context = mock(Context::class.java)

        viewModel = MapViewModel(locationRepository)
    }


    @Test
    fun checkForConnectionWithSuccess() {
        val connection: ConnectionState = mock(ConnectionState::class.java)

        // Network unavailable
        doReturn(false).`when`(locationRepository).checkForConnection(connection)
        var result = viewModel.checkForConnection(connection)
        verify(locationRepository).checkForConnection(connection)
        assertFalse(result)

        // Reset mock to avoid interference between inner test parts
        reset(locationRepository)

        // Network available
        doReturn(true).`when`(locationRepository).checkForConnection(connection)
        result = viewModel.checkForConnection(connection)
        verify(locationRepository).checkForConnection(connection)
        assertTrue(result)
    }

    @Test
    fun checkForPermissionsWithSuccess() {
        // Permissions not granted
        doReturn(false).`when`(locationRepository).checkForPermissions(context)
        var result = viewModel.checkForPermissions(context)
        verify(locationRepository).checkForPermissions(context)
        assertFalse(result)

        // Reset mock to avoid interference between inner test parts
        reset(locationRepository)

        // Permissions granted
        doReturn(true).`when`(locationRepository).checkForPermissions(context)
        result = viewModel.checkForPermissions(context)
        verify(locationRepository).checkForPermissions(context)
        assertTrue(result)
    }

    @Test
    fun testAreLocPermsGranted() {
        // Permissions not granted
        doReturn(false).`when`(locationRepository).areLocPermsGranted()
        var result = viewModel.areLocPermsGranted()
        verify(locationRepository).areLocPermsGranted()
        assertFalse(result)

        // Reset mock to avoid interference between inner test parts
        reset(locationRepository)

        // Permissions not granted
        doReturn(true).`when`(locationRepository).areLocPermsGranted()
        result = viewModel.areLocPermsGranted()
        verify(locationRepository).areLocPermsGranted()
        assertTrue(result)
    }

    @Test
    fun startLocationUpdatesWithSuccess() {
        viewModel.startLocationUpdates(context)
        verify(locationRepository).startLocationUpdates(context)
    }

    @Test
    fun stopLocationUpdatesWithSuccess() {
        viewModel.stopLocationUpdates(context)
        verify(locationRepository).stopLocationUpdates(context)
    }

    @Test
    fun getLocationUpdatesWithSuccess() {
        val locationStateFlow = mock(StateFlow::class.java)

        doReturn(locationStateFlow).`when`(locationRepository).getLocationUpdates()
        val result = viewModel.getLocationUpdates()
        verify(locationRepository).getLocationUpdates()
        assertEquals(locationStateFlow, result)
    }

    @Test
    fun openAppSettingsWithSuccess() {
        val intent = mock(Intent::class.java)
        doReturn(intent).`when`(locationRepository).createAppSettingsIntent(context)

        viewModel.openAppSettings(context)
        verify(context).startActivity(intent)
    }

}