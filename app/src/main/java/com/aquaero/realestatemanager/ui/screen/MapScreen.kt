package com.aquaero.realestatemanager.ui.screen

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.component.map_screen.MapScreenMap
import com.aquaero.realestatemanager.utils.MyLocationSource
import com.google.android.gms.maps.model.LatLng

@Composable
fun MapScreen(
    showMap: Boolean,
    properties: List<Property>,
    addresses: List<Address>,
    locationState: State<Location>,
    locationSource: MyLocationSource,
) {
    if (showMap) {
        MapScreenMap(
            properties = properties,
            addresses = addresses,
            locationState = locationState,
            locationSource = locationSource,
        )
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = stringResource(R.string.loading_map))
        }
    }
}