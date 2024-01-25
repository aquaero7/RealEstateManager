package com.aquaero.realestatemanager.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "property_poi_join",
    primaryKeys = ["propertyId", "poiId"],
    foreignKeys = [
        ForeignKey(entity = Property::class, parentColumns = ["propertyId"], childColumns = ["propertyId"]),
        ForeignKey(entity = Poi::class, parentColumns = ["poiId"], childColumns = ["poiId"]),
    ],
)
data class PropertyPoiJoin(
    val propertyId: Long,
    val poiId: String,
)
