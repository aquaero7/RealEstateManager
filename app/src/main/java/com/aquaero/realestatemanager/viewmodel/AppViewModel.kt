package com.aquaero.realestatemanager.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.lifecycle.ViewModel
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.EditDetail
import com.aquaero.realestatemanager.GeolocMap
import com.aquaero.realestatemanager.ListAndDetail
import com.aquaero.realestatemanager.Loan
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.SearchCriteria
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.LocationRepository
import com.aquaero.realestatemanager.repository.PropertyRepository
import com.aquaero.realestatemanager.utils.AppContentType
import com.google.android.gms.maps.model.LatLng


class AppViewModel(
    private val propertyRepository: PropertyRepository,
    private val agentRepository: AgentRepository,
    private val locationRepository: LocationRepository,
): ViewModel() {

    private val context: Context by lazy { ApplicationRoot.getContext() }

    @SuppressLint("NewApi")
    val fakeProperties = propertyRepository.fakeProperties

    /**
     * Init content type, according to window's width,
     * to choose dynamically, on screen state changes, whether to show
     * just a list content, or both a list and detail content
     */
    fun contentType(windowSize: WindowWidthSizeClass): AppContentType = when (windowSize) {
        WindowWidthSizeClass.Expanded -> { AppContentType.SCREEN_WITH_DETAIL }
        else -> { AppContentType.SCREEN_ONLY }
    }

    /**
     * TopBar
     */
    /* // TODO : To be deleted if moved to activity
    fun onClickMenu(currentBackStack: NavBackStackEntry?, navController: NavController) = run {
        val currentScreen = currentBackStack?.destination?.route
        val propertyId = currentBackStack?.arguments?.getString(propertyKey) ?: 0
        when(currentScreen) {
            ListAndDetail.routeWithArgs, Detail.routeWithArgs -> {
                Log.w("Click Edit from Detail", "Property $propertyId")
                navController.navigate("${EditDetail.route}/${propertyId}")
            }
            // TODO: Replace toast with specific action
            else -> Toast
                .makeText(context, "Click on ${context.getString(R.string.valid)}", Toast.LENGTH_SHORT)
                .show()
        }
    }
    */
    fun menuIcon(currentScreen: String?) = if (
        currentScreen == EditDetail.routeWithArgs ||
        currentScreen == SearchCriteria.route ||
        currentScreen == Loan.route
    ) Icons.Default.Check else Icons.Default.Edit

    fun menuIconContentDesc(currentScreen: String?) = if (
        currentScreen == EditDetail.routeWithArgs ||
        currentScreen == Loan.route
    ) R.string.cd_check else R.string.cd_edit

    fun menuEnabled(currentScreen: String?, windowSize: WindowWidthSizeClass) = (
            currentScreen != GeolocMap.route) &&
            (currentScreen != ListAndDetail.routeWithArgs ||
                    contentType(windowSize) == AppContentType.SCREEN_WITH_DETAIL)

    val onClickRadioButton = { currency: String -> Toast
        .makeText(context, "Click on $currency", Toast.LENGTH_SHORT)
        .show()
    }
    /** End TopBar */

    @SuppressLint("NewApi")
    fun propertyFromId(propertyId: Long): Property {
        return propertyRepository.propertyFromId(propertyId)
    }

    fun thumbnailUrl(property: Property): String {
        return propertyRepository.thumbnailUrl(property)
    }

    val agentSet = agentRepository.agentsSet
    val pTypeSet = propertyRepository.pTypesSet


    /**
     * Google Maps  TODO: Move to Utils ?
     */

    fun checkForPermissions(): Boolean {
        return locationRepository.checkForPermissions()
    }
    fun areLocPermsGranted(): Boolean {
        return locationRepository.areLocPermsGranted()
    }

    fun getCurrentLocation(onLocationFetched: (location: Location) -> Unit) {
        locationRepository.getCurrentLocation(onLocationFetched)
    }

    /*  // TODO : Should it be deleted because replaced with getCurrentLocation() ?
    fun getCurrentLatLng(onLocationFetched: (location: LatLng) -> Unit) {
        locationRepository.getCurrentLatLng(onLocationFetched)
    }
    */

    fun openAppSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        )
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .also { context.startActivity(it) }
    }

    /** End Google Maps */


}

/**
 * Google Maps TOP LEVEL
 */

// TODO: Move to Utils ?
@SuppressLint("NewApi")
fun getLocationFromAddress(strAddress: String?): LatLng? {
    val coder = Geocoder(ApplicationRoot.getContext())
    var latLng: LatLng? = null
    coder.getFromLocationName(strAddress!!, 5, // @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    object: Geocoder.GeocodeListener {
        override fun onGeocode(address: MutableList<Address>) {
            val location: Address = address[0]
            latLng = LatLng(location.latitude, location.longitude)
            Log.w("Geocoder.getFromLocName", latLng.toString())
            Log.w("Geocoder.getFromLocName", location.locality)
            Log.w("Geocoder.getFromLocName", location.latitude.toString())
            Log.w("Geocoder.getFromLocName", location.longitude.toString())
        }
        override fun onError(errorMessage: String?) {
            super.onError(errorMessage)
            Log.w("Geocoder.getFromLocName", errorMessage.toString())
        }
    })
    return latLng
}

/** End Google Maps TOP LEVEL */

