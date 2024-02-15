package com.aquaero.realestatemanager

import android.Manifest
import android.location.Location
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.model.TypeEnum
import com.aquaero.realestatemanager.ui.theme.PurpleGrey40
import com.aquaero.realestatemanager.ui.theme.PurpleGrey80
import com.aquaero.realestatemanager.ui.theme.White

// AppTabRow
val TAB_HEIGHT = 64.dp
const val TAB_FADE_IN_ANIMATION_DURATION = 150
const val TAB_FADE_OUT_ANIMATION_DURATION = 100
const val TAB_FADE_IN_ANIMATION_DELAY = 100
const val INACTIVE_TAB_OPACITY = 0.60f

// Content type (Content shown depending on size and windows state of device)
enum class AppContentType {
    SCREEN_ONLY,
    SCREEN_WITH_DETAIL,
}

// Map
const val MAPS_API_KEY = BuildConfig.MAPS_API_KEY
const val DEFAULT_ZOOM = 13F
const val LOC_PERMS_SETTINGS = "locPermsSettings"
const val FINE_LOC_PERMS = "fineLocPerms"

val LOCATION_PERMISSIONS = listOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
)
val DEFAULT_LOCATION = Location("").apply { latitude = 0.0 ; longitude = 0.0 }

// Static Map
const val SM_URL = "https://maps.googleapis.com/maps/api/staticmap?"
const val SM_SIZE = "size=400x400"
const val SM_SCALE = "&scale=2"
const val SM_TYPE = "&maptype=hybrid"
const val SM_MARKER_COLOR = "&markers=color:red%7C"
const val SM_KEY = "&key=$MAPS_API_KEY"
// const val SM_KEY = "&key=${BuildConfig.MAPS_API_KEY}"

// Item for lazyList, lazyRow and EditScreen if the property has no photo
// val NO_PHOTO = Photo(0, Uri.EMPTY, "")

// DatePicker colors
val DP_TEXT_COLOR = White
val DP_HEADERS_COLOR = White
val DP_CONTENT_COLOR = PurpleGrey80
val DP_CONTAINER_COLOR = PurpleGrey40

// Miscellaneous
const val NEW_ITEM_ID = 0L
const val UNASSIGNED_ID = 1L    // For unassigned Agent
const val NO_ITEM_ID = -1L  // For empty photo
const val PHOTO_DELETION = "photoDeletion"
const val DATE_PATTERN = "yyyy-MM-dd"
const val DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm"
const val DATE_LENGTH = 10 // Equals to "####-##-##".count
const val RATE_OF_DOLLAR_IN_EURO = 0.812

// Cache values
const val CACHE_LONG_ID_VALUE = 0L
const val CACHE_EMPTY_STRING_VALUE = ""
const val CACHE_AGENT_ID_VALUE = UNASSIGNED_ID
val CACHE_TYPE_ID_VALUE = TypeEnum.UNASSIGNED.key
val CACHE_NULLABLE_VALUE = null

enum class DropdownMenuCategory {
    TYPE,
    AGENT,
}

