package com.aquaero.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Poi(
    @PrimaryKey(autoGenerate = false)
    val poiId: String,
)

enum class POI(val key: String) {
    HOSPITAL(key = "hospital"),
    SCHOOL(key = "school"),
    RESTAURANT(key = "restaurant"),
    SHOP(key = "shop"),
    RAILWAY_STATION(key = "railway_station"),
    CAR_PARK(key = "car_park"),
}

val POI_PREPOPULATION_DATA = listOf(
    Poi(POI.HOSPITAL.key),
    Poi(POI.SCHOOL.key),
    Poi(POI.RESTAURANT.key),
    Poi(POI.SHOP.key),
    Poi(POI.RAILWAY_STATION.key),
    Poi(POI.CAR_PARK.key),
)