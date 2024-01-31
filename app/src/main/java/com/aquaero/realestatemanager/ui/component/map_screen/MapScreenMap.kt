package com.aquaero.realestatemanager.ui.component.map_screen

import android.location.Location
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.aquaero.realestatemanager.DEFAULT_ZOOM
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.utils.MyLocationSource
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreenMap(
    properties: List<Property>,
    addresses: List<Address>,
    locationState: State<Location>,
    locationSource: MyLocationSource,
) {
    var isMapLoaded by remember { mutableStateOf(false) }
    val latLngState = LatLng(locationState.value.latitude, locationState.value.longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLngState, DEFAULT_ZOOM)
    }
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = true,
                mapType = MapType.NORMAL,
            )
        )
    }

    LaunchedEffect(key1 = latLngState) {
        locationSource.onLocationChanged(location = locationState.value)
        val cameraPosition = CameraPosition.fromLatLngZoom(latLngState, DEFAULT_ZOOM)
        cameraPositionState.animate(
            update = CameraUpdateFactory.newCameraPosition(cameraPosition),
            durationMs = 1_000
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {

        if (!isMapLoaded) {
            AnimatedVisibility(
                modifier = Modifier.matchParentSize(),
                visible = !isMapLoaded,
                enter = EnterTransition.None,
                exit = fadeOut(),
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .wrapContentSize()
                )
            }
        }

        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            onMapLoaded = { isMapLoaded = true }
        ) {
            // Marker for current location
            Marker(
                state = MarkerState(position = latLngState),
                title = stringResource(id = R.string.location),
                snippet = stringResource(id = R.string.current_location),
            )
            // Markers for properties
            properties.forEach { property ->

                val latitude: Double? = addresses.find { it.addressId == property.addressId }?.latitude
                val longitude: Double? = addresses.find { it.addressId == property.addressId }?.longitude
                val city: String? = addresses.find { it.addressId == property.addressId }?.city

                if (latitude != null && longitude != null) {
                    Marker(
                        state = MarkerState(
                            position = LatLng(latitude, longitude)
                            // position = LatLng(property.addressId.latitude, property.addressId.longitude)
                        ),
//                    title = property.addressId.city,
                        title = city ?: "",
//                    snippet = stringResource(property.typeId),
                        snippet = property.typeId,
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
                    )
                }
            }
        }
    }
}