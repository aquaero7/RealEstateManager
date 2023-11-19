package com.aquaero.realestatemanager.ui.component.map_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Property
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreenMap(
    properties: List<Property>,
    currentLocation: LatLng,
    mapProperties: MapProperties,
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation, 15F)
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
        ) {
            /* // TODO : Marker for current location to remove because not updated without navigation
            Marker(
                state = MarkerState(position = currentLocation),
                title = stringResource(id = R.string.location),
                snippet = stringResource(id = R.string.current_location),
            )
            */

            properties.forEach { it ->
                Marker(
                    state = MarkerState(
                        position = LatLng(it.pAddress.location.latitude, it.pAddress.location.longitude)
                    ),
                    title = it.pAddress.city,
                    snippet = it.pType,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
                )
            }
        }
    }

}