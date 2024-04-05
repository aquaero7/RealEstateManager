package com.aquaero.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Type(
    @PrimaryKey(autoGenerate = false)
    val typeId: String,
)

/**
 * For formatted display and translation, these keys should be set in the string resources files.
 */
enum class TypeEnum(val key: String) {
//    EMPTY(key = ""),
    UNASSIGNED(key = "_unassigned_"),
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
//    Type(TypeEnum.EMPTY.key),
    Type(TypeEnum.UNASSIGNED.key),
    Type(TypeEnum.FLAT.key),
    Type(TypeEnum.HOUSE.key),
    Type(TypeEnum.DUPLEX.key),
    Type(TypeEnum.PENTHOUSE.key),
    Type(TypeEnum.LOFT.key),
    Type(TypeEnum.MANOR.key),
    Type(TypeEnum.CASTLE.key),
    Type(TypeEnum.HOSTEL.key),
).sortedBy { it.typeId }
