package com.aquaero.realestatemanager

import android.Manifest
import android.location.Location
import android.net.Uri
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.ui.theme.PurpleGrey40
import com.aquaero.realestatemanager.ui.theme.PurpleGrey80
import com.aquaero.realestatemanager.ui.theme.White

const val MAPS_API_KEY = BuildConfig.MAPS_API_KEY
const val SM_URL = "https://maps.googleapis.com/maps/api/staticmap?"
const val SM_SIZE = "size=400x400"
const val SM_SCALE = "&scale=2"
const val SM_TYPE = "&maptype=hybrid"
const val SM_MK_COLOR1 = "&markers=color:red%7C"
const val SM_KEY = "&key=$MAPS_API_KEY"
const val DEFAULT_ZOOM = 13F
const val LOC_PERMS_SETTINGS = "locPermsSettings"
const val FINE_LOC_PERMS = "fineLocPerms"
const val PHOTO_DELETION = "photoDeletion"
const val DATE_PATTERN = "yyyy-MM-dd"
const val DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm"
const val DATE_LENGTH = 10 // Equals to "####-##-##".count
const val RATE_OF_DOLLAR_IN_EURO = 0.812
// const val SM_KEY = "&key=${BuildConfig.MAPS_API_KEY}"

// Item for lazyList if the property has no photo
val NO_PHOTO = Photo(0, Uri.EMPTY, "")

val DEFAULT_LOCATION = Location("").apply { latitude = 0.0 ; longitude = 0.0 }
val LOCATION_PERMISSIONS = listOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
)

// DatePicker colors
val DP_TEXT_COLOR = White
val DP_HEADERS_COLOR = White
val DP_CONTENT_COLOR = PurpleGrey80
val DP_CONTAINER_COLOR = PurpleGrey40
