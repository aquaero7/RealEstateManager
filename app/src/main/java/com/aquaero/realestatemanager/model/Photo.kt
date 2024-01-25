package com.aquaero.realestatemanager.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(entity = Property::class, parentColumns = ["propertyId"], childColumns = ["propertyId"]),
])
data class Photo(
    @PrimaryKey(autoGenerate = true)
    val photoId: Long = 0,
    val uri: Uri,
    val label: String,
    val propertyId: Long,
)

val NO_PHOTO = Photo(photoId = 0, uri = Uri.EMPTY, label = "", propertyId = 0)
val PHOTO_PREPOPULATION_DATA = listOf(NO_PHOTO)
