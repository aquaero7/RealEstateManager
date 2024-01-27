package com.aquaero.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Type(
    @PrimaryKey(autoGenerate = false)
    val typeId: String,
)

enum class TYPE(val key: String) {
    UNASSIGNED(key = "unassigned"),
    FLAT(key = "flat"),
    HOUSE(key = "house"),
    DUPLEX(key = "duplex"),
    PENTHOUSE(key = "penthouse"),
    LOFT(key = "loft"),
    MANOR(key = "manor"),
    CASTLE(key = "castle"),
    HOSTEL(key = "hostel"),
}

val TYPE_PREPOPULATION_DATA = listOf(
    Type(TYPE.UNASSIGNED.key),
    Type(TYPE.FLAT.key),
    Type(TYPE.HOUSE.key),
    Type(TYPE.DUPLEX.key),
    Type(TYPE.PENTHOUSE.key),
    Type(TYPE.LOFT.key),
    Type(TYPE.MANOR.key),
    Type(TYPE.CASTLE.key),
    Type(TYPE.HOSTEL.key),
)