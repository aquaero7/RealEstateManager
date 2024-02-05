package com.aquaero.realestatemanager.ui.screen

import android.location.Location
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.DEFAULT_LOCATION
import com.aquaero.realestatemanager.DEFAULT_ZOOM
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.theme.Black
import com.aquaero.realestatemanager.ui.theme.White
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
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MapScreen(
    properties: List<Property>,
    addresses: List<Address>,
    startLocationUpdates: () -> Unit,
    stopLocationUpdates: () -> Unit,
    getLocationUpdates: () -> StateFlow<Location?>,
) {
    DisposableEffect(key1 = Unit) {
        startLocationUpdates()
        onDispose { stopLocationUpdates() }
    }

    val currentLocation = getLocationUpdates().collectAsState().value
    val latLngState by remember(currentLocation) {
        mutableStateOf(currentLocation?.let { LatLng(it.latitude, it.longitude) }
            ?: LatLng(DEFAULT_LOCATION.latitude, DEFAULT_LOCATION.longitude))
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLngState, DEFAULT_ZOOM)
    }

    var isMapLoaded by remember { mutableStateOf(false) }
    val isCurrentPositionSet by remember(currentLocation) {
        mutableStateOf(currentLocation != null && currentLocation != DEFAULT_LOCATION)
    }
    val isMapReady by remember(isMapLoaded, isCurrentPositionSet) {
        mutableStateOf(isMapLoaded && isCurrentPositionSet)
    }

    val mapProperties = MapProperties(isMyLocationEnabled = true, mapType = MapType.NORMAL)

    LaunchedEffect(key1 = latLngState) {
        val cameraPosition = CameraPosition.fromLatLngZoom(latLngState, DEFAULT_ZOOM)
        cameraPositionState.animate(
            update = CameraUpdateFactory.newCameraPosition(cameraPosition),
            durationMs = 1_000
        )
        Log.w(
            "MapScreen",
            "New location: Lat: ${latLngState.latitude}, Lng: ${latLngState.longitude}"
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            onMapLoaded = { isMapLoaded = true },
        ) {
            // Marker for current location
            Marker(
                state = MarkerState(position = latLngState),
                title = stringResource(id = R.string.location),
                snippet = stringResource(id = R.string.current_location),
            )
            // Markers for properties
            properties.forEach { property ->
                val latitude: Double? =
                    addresses.find { it.addressId == property.addressId }?.latitude
                val longitude: Double? =
                    addresses.find { it.addressId == property.addressId }?.longitude
                val city: String? = addresses.find { it.addressId == property.addressId }?.city

                if (latitude != null && longitude != null) {
                    Marker(
                        state = MarkerState(position = LatLng(latitude, longitude)),
                        title = city ?: "",
                        snippet = property.typeId,
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
                    )
                }
            }
        }

        AnimatedVisibility(
            modifier = Modifier.matchParentSize(),
            visible = !isMapReady,
            enter = EnterTransition.None,
            exit = fadeOut(),
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(color = White),
                    contentAlignment = Alignment.TopStart,
                ) {
                    Text(text = stringResource(R.string.loading_map), color = Black)
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .background(color = White, shape = CircleShape,)
                            .wrapContentSize()
                            .scale(4f),
                        strokeWidth = 2.dp,
                        color = White,
                    )
                }
            }
        }
    }

}