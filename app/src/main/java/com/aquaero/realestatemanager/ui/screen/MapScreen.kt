package com.aquaero.realestatemanager.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.component.map_screen.MapScreenMap
import com.aquaero.realestatemanager.ui.theme.Black
import com.aquaero.realestatemanager.ui.theme.White
import com.aquaero.realestatemanager.viewmodel.AppViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("MissingPermission")
@Composable
/*
fun MapScreen(
    appViewModel: AppViewModel,
    properties: List<Property>
) {
*/
fun MapScreen(
    currentLocation: LatLng,
    showMap: Boolean,
    properties: List<Property>
) {
    // var showMap by remember { mutableStateOf(false) }
    // var currentLocation by remember { mutableStateOf(LatLng(0.0, 0.0)) }

    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = true,
                mapType = MapType.NORMAL,
            )
        )
    }

    /*
    appViewModel.getCurrentLocation(LocalContext.current) {
        currentLocation = it
        showMap = true
    }
    */

    if (showMap) {
        MapScreenMap(
            properties = properties,
            currentLocation = currentLocation,
            mapProperties = mapProperties,
        )
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = stringResource(R.string.loading_map))
        }
    }

}