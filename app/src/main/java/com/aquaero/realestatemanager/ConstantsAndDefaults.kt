package com.aquaero.realestatemanager

import android.Manifest
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.model.TypeEnum
import com.aquaero.realestatemanager.ui.theme.PurpleGrey40
import com.aquaero.realestatemanager.ui.theme.PurpleGrey80
import com.aquaero.realestatemanager.ui.theme.White

// Content types
/**
 * Content shown depending on size and window state of device.
 */
enum class AppContentType {
    LIST_OR_DETAIL,
    LIST_AND_DETAIL,
}

enum class NavSelection {
    LIST,
    DETAIL,
}


// AppDestinations
const val propertyKey = "single_property"
const val selectedKey = "property_selected"

/**
 * Navigation routes.
 */
enum class AppRoute(val value: String) {
    LIST(value = "list"),
    MAP(value = "map"),
    SEARCH(value = "search_criteria"),
    LOAN(value = "loan"),
    DETAIL(value = "detail"),
    EDIT(value = "edit_detail"),
}


// AppTabRow
val TAB_HEIGHT = 64.dp
const val TAB_FADE_IN_ANIMATION_DURATION = 150
const val TAB_FADE_OUT_ANIMATION_DURATION = 100
const val TAB_FADE_IN_ANIMATION_DELAY = 100
const val INACTIVE_TAB_OPACITY = 0.60f


// Language
enum class Region(val language: String) {
    US(language = "en"),
    UK(language = "en"),
    FR(language = "fr")
}


// Dropdown menus
/**
 * Classes whose instances are displayed in dropdown menus.
 */
enum class DropdownMenuCategory {
    TYPE,
    AGENT,
}


// Edition fields
/**
 * Edition fields.
 */
enum class EditField {
    SURFACE,
    ROOMS,
    BATHROOMS,
    BEDROOMS,
    DESCRIPTION,
    REGISTRATION_DATE,
    SALE_DATE,
    STREET_NUMBER,
    STREET_NAME,
    ADD_INFO,
    CITY,
    STATE,
    ZIP_CODE,
    COUNTRY,
    PRICE,
}

enum class NonEditField {
    ADDRESS_ID,
    PROPERTY_ID,
}


// Search fields
/**
 * Search fields.
 */
enum class SearchField {
    SURFACE,
    ROOMS,
    BATHROOMS,
    BEDROOMS,
    PRICE,
    DESCRIPTION,
    ZIP_CODE,
    CITY,
    STATE,
    COUNTRY,
    REGISTRATION_DATE,
    SALE_DATE,
    TYPE,
    AGENT,
    SALES_STATUS,
    PHOTOS_STATUS,
    POIS,
}


// Loan fields
/**
 * Loan fields.
 */
enum class LoanField {
    AMOUNT,
    TERM,
    YEARS,
    MONTHS,
    ANNUAL_INTEREST_RATE,
    ANNUAL_INSURANCE_RATE,
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
const val DEFAULT_LAT = 0.0
const val DEFAULT_LNG = 0.0


// Static Map
const val SM_URL = "https://maps.googleapis.com/maps/api/staticmap?"
const val SM_SIZE = "size=400x400"
const val SM_SCALE = "&scale=2"
const val SM_TYPE = "&maptype=hybrid"
const val SM_MARKER_COLOR = "&markers=color:red%7C"
const val SM_KEY = "&key=$MAPS_API_KEY"
// const val SM_KEY = "&key=${BuildConfig.MAPS_API_KEY}"


// LatLng
/**
 * LatLng items.
 */
enum class LatLngItem {
    LATITUDE,
    LONGITUDE,
}


// DatePicker colors
val DP_TEXT_COLOR = White
val DP_HEADERS_COLOR = White
val DP_CONTENT_COLOR = PurpleGrey80
val DP_CONTAINER_COLOR = PurpleGrey40


// Miscellaneous
val CLEAR_BUTTON_SIZE: Dp = 14.dp
const val DEFAULT_START_POSITION_DP = 0
const val SEARCH_RESULT_START_POSITION_DP = 740
const val MIN = "MIN"
const val MAX = "MAX"
const val UNASSIGNED_ID = 1L    // For unassigned Agent
const val NULL_PROPERTY_ID = 0L
const val NO_PHOTO_ID = -1L
const val PHOTO_DELETION = "photoDeletion"
const val DATE_PATTERN = "yyyy-MM-dd"
const val RATE_OF_DOLLAR_IN_EURO = 0.812
const val DEFAULT_LIST_INDEX = -1
const val DEFAULT_RADIO_INDEX = 2
const val EMPTY_STRING = ""


// Cache values
const val CACHE_LONG_ID_VALUE = 0L
const val CACHE_EMPTY_STRING_VALUE = ""
const val CACHE_AGENT_ID_VALUE = UNASSIGNED_ID
val CACHE_TYPE_ID_VALUE = TypeEnum.UNASSIGNED.key
val CACHE_NULLABLE_VALUE = null


// ContentProvider

// Path for FileProvider and  ContentProvider
object Path {
    const val FILE_PROVIDER = "fileprovider"
    const val CONTENT_PROVIDER = "contentprovider"
    const val CONTENT = "content"
    const val FOR_SALE = "forsale"
    const val SOLD = "sold"
}

object PropertyKey {
    const val PROPERTY_ID = "propertyId"
    const val TYPE_ID = "typeId"
    const val ADDRESS_ID = "addressId"
    const val PRICE = "price"
    const val DESCRIPTION = "description"
    const val SURFACE = "surface"
    const val ROOMS = "rooms"
    const val BATHROOMS = "bathrooms"
    const val BEDROOMS = "bedrooms"
    const val REG_DATE = "registrationDate"
    const val SALE_DATE = "saleDate"
    const val AGENT_ID = "agentId"
}

object AddressKey {
    const val ADDRESS_ID = "addressId"
}

object PhotoKey {
    const val PHOTO_ID = "photoId"
}

object AgentKey {
    const val AGENT_ID = "agentId"
}

object TypeKey {
    const val TYPE_ID = "typeId"
}

object PoiKey {
    const val POI_ID = "poiId"
}







