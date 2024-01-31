package com.aquaero.realestatemanager.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.aquaero.realestatemanager.NO_ITEM_ID
import com.aquaero.realestatemanager.NULL_ITEM_ID

@Entity(foreignKeys = [
    ForeignKey(entity = Property::class, parentColumns = ["propertyId"], childColumns = ["propertyId"],
        onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE,
    ),
])
data class Photo(
    @PrimaryKey(autoGenerate = true)
    val photoId: Long = 0,
    val uri: String,
    val label: String?,
    @ColumnInfo(index = true)
    val propertyId: Long,
)

val NO_PHOTO = Photo(photoId = NULL_ITEM_ID, uri = "", label = "", propertyId = NULL_ITEM_ID)
val PHOTO_PREPOPULATION_DATA = listOf(NO_PHOTO)
