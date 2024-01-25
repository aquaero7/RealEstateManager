package com.aquaero.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aquaero.realestatemanager.POI

@Entity
data class Poi(
    @PrimaryKey(autoGenerate = false)
    val poiId: String,
)

val POI_PREPOPULATION_DATA = listOf(
    Poi(POI.HOSPITAL.key),
    Poi(POI.SCHOOL.key),
    Poi(POI.RESTAURANT.key),
    Poi(POI.SHOP.key),
    Poi(POI.RAILWAY_STATION.key),
    Poi(POI.CAR_PARK.key),
)