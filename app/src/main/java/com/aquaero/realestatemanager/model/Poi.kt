package com.aquaero.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Poi(
    @PrimaryKey(autoGenerate = false)
    val poiId: String,
)

/**
 * For formatted display and translation, these keys should be set in the string resources files.
 */
enum class PoiEnum(val key: String) {
    HOSPITAL(key = "hospital"),
    SCHOOL(key = "school"),
    RESTAURANT(key = "restaurant"),
    SHOP(key = "shop"),
    RAILWAY_STATION(key = "railway_station"),
    CAR_PARK(key = "car_park"),
}

val POI_PREPOPULATION_DATA = listOf(
    Poi(PoiEnum.HOSPITAL.key),
    Poi(PoiEnum.SCHOOL.key),
    Poi(PoiEnum.RESTAURANT.key),
    Poi(PoiEnum.SHOP.key),
    Poi(PoiEnum.RAILWAY_STATION.key),
    Poi(PoiEnum.CAR_PARK.key),
)
