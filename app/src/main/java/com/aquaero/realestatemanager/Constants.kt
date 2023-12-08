package com.aquaero.realestatemanager

import android.Manifest
import android.location.Location
import com.aquaero.realestatemanager.model.Photo

const val MAPS_API_KEY = BuildConfig.MAPS_API_KEY
const val SM_URL = "https://maps.googleapis.com/maps/api/staticmap?"
const val SM_SIZE = "size=400x400"
const val SM_SCALE = "&scale=2"
const val SM_TYPE = "&maptype=hybrid"
const val SM_MK_COLOR1 = "&markers=color:red%7C"
const val SM_KEY = "&key=$MAPS_API_KEY"
const val DEFAULT_ZOOM = 13F
// const val SM_KEY = "&key=${BuildConfig.MAPS_API_KEY}"

// Item for lazyList if the property has no photo
val NO_PHOTO = Photo(0, "", "")

val DEFAULT_LOCATION = Location("").apply { latitude = 0.0 ; longitude = 0.0 }
val LOCATION_PERMISSIONS = listOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
)