package com.aquaero.realestatemanager.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.component.map_screen.MapScreenMap
import com.aquaero.realestatemanager.viewmodel.getCurrentLocation
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType

@SuppressLint("MissingPermission")
@Composable
fun MapScreen(context: Context, properties: List<Property>) {

    // Text(text = "MapScreen")    // TODO: To be deleted after screen implementation

    var showMap by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
            isMyLocationEnabled = true,
            mapType = MapType.NORMAL,
            )
        )
    }

        getCurrentLocation(context) {
        location = it
        showMap = true
    }

    if (showMap) {
        MapScreenMap(
            properties = properties,
            latLng = location,
            mapProperties = mapProperties,
        )
    } else {
        Text(text = "Loading map...")
    }








}