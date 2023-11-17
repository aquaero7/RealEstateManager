package com.aquaero.realestatemanager.ui.component.map_screen

import android.content.Context
import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.House
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Property
import com.google.android.gms.maps.model.BitmapDescriptor
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
    latLng: LatLng,
    mapProperties: MapProperties,
) {
    val latLngList = remember { mutableStateListOf(latLng) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 15F)
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,

        ) {
            /*
            latLngList.toList().forEach { _ ->
                Marker(
                    state = MarkerState(position = latLng),
                    title = "Location",
                    snippet = "Current location",
                )
            }
            */
            Marker(
                state = MarkerState(position = latLng),
                title = "Location",
                snippet = "Current location",
            )

            properties.forEach { it ->
                /* TODO: To uncomment when property lat and lng are known
                Marker(
                    state = MarkerState(position = LatLng(it.pAddress.lat, it.pAddress.lng)),
                    title = it.pAddress.city,
                    snippet = it.pType,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
                )
                */
                // TODO : To be deleted when property lat and lng are known
                Marker(
                    state = MarkerState(position = LatLng(latLng.latitude + (it.pId + 1).toDouble() / 100, latLng.longitude + (it.pId + 1).toDouble() / 100)),
                    title = it.pAddress.city,
                    snippet = it.pType,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
                )
                //
            }
        }
    }

}