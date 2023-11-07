package com.aquaero.realestatemanager.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.utils.agentsToString

val pTypesSet: MutableSet<Int?> = mutableSetOf(
    R.string.flat, R.string.house, R.string.duplex, R.string.penthouse,
    R.string.loft, R.string.manor, R.string.castle, R.string.hostel
)

val poiSet: MutableSet<Int?> = mutableSetOf(
    R.string.hospital, R.string.school, R.string.restaurant,
    R.string.shop, R.string.railway_station, R.string.car_park,
)

val agentsSet: MutableSet<String?> = agentsToString()


