package com.aquaero.realestatemanager.ui.screen

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
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.component.map_screen.MapScreenMap
import com.aquaero.realestatemanager.utils.MyLocationSource
import com.google.android.gms.maps.model.LatLng

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MapScreen(
    showMap: Boolean,
    properties: List<Property>,
    locationState: State<LatLng>,
    locationSource: MyLocationSource,
) {
    if (showMap) {
        MapScreenMap(
            properties = properties,
            locationState = locationState,
            locationSource = locationSource,
        )
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = stringResource(R.string.loading_map))
        }
    }
}