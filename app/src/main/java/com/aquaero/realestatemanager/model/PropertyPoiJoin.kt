package com.aquaero.realestatemanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "property_poi_join",
    primaryKeys = ["propertyId", "poiId"],
    foreignKeys = [
        ForeignKey(
            entity = Property::class, parentColumns = ["propertyId"], childColumns = ["propertyId"],
            onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = Poi::class, parentColumns = ["poiId"], childColumns = ["poiId"],
            onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
data class PropertyPoiJoin(
    @ColumnInfo(index = true)
    val propertyId: Long,
    @ColumnInfo(index = true)
    val poiId: String,
)
