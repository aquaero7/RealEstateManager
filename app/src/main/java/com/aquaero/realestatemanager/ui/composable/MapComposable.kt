package com.aquaero.realestatemanager.ui.composable

import android.content.Context
import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.component.map_screen.MapScreenNoMap
import com.aquaero.realestatemanager.ui.screen.LocationPermissionsScreen
import com.aquaero.realestatemanager.ui.screen.MapScreen
import com.aquaero.realestatemanager.utils.connectivityState
import com.aquaero.realestatemanager.viewmodel.MapViewModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MapComposable(
    context: Context,
    mapViewModel: MapViewModel,
    properties: MutableList<Property>,
    addresses: MutableList<Address>,
    popBackStack: () -> Unit,
) {
    // Get network connection availability
    val connection by connectivityState()
    val internetAvailable by remember(connection) {
        mutableStateOf(mapViewModel.checkForConnection(connection = connection))
    }
    val networkAvailable by remember(internetAvailable) { mutableStateOf(internetAvailable) }
    // Get permission grants
    var locationPermissionsGranted by remember { mutableStateOf(mapViewModel.areLocPermsGranted()) }

    val startLocationUpdates: () -> Unit = { mapViewModel.startLocationUpdates() }
    val stopLocationUpdates: () -> Unit = { mapViewModel.stopLocationUpdates() }
    val getLocationUpdates: () -> StateFlow<Location?> = { mapViewModel.getLocationUpdates() }

    if (networkAvailable) {
        if (locationPermissionsGranted) {
            MapScreen(
                properties = properties,
                addresses = addresses,
                startLocationUpdates = startLocationUpdates,
                stopLocationUpdates = stopLocationUpdates,
                getLocationUpdates = getLocationUpdates,
                popBackStack = popBackStack,
            )
        } else {
            val onOpenAppSettings = { mapViewModel.openAppSettings(context = context) }
            val onPermissionsGranted = { locationPermissionsGranted = true }

            LocationPermissionsScreen(
                onOpenAppSettings = onOpenAppSettings,
                onPermissionsGranted = onPermissionsGranted,
                popBackStack = popBackStack,
            )
        }
    } else {
        // No network
        MapScreenNoMap(
            infoText = stringResource(id = R.string.network_unavailable),
            popBackStack = popBackStack,
        )
    }

}