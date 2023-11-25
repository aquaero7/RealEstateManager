package com.aquaero.realestatemanager.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.mutableStateListOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.EditDetail
import com.aquaero.realestatemanager.GeolocMap
import com.aquaero.realestatemanager.ListAndDetail
import com.aquaero.realestatemanager.Loan
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.SM_KEY
import com.aquaero.realestatemanager.SM_MK_COLOR1
import com.aquaero.realestatemanager.SM_SCALE
import com.aquaero.realestatemanager.SM_SIZE
import com.aquaero.realestatemanager.SM_TYPE
import com.aquaero.realestatemanager.SM_URL
import com.aquaero.realestatemanager.SearchCriteria
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.PropertyRepository
import com.aquaero.realestatemanager.utils.AppContentType
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng


class AppViewModel(
    private val propertyRepository: PropertyRepository,
    private val agentRepository: AgentRepository,
): ViewModel() {
    /*
    private val context: Context
        get() = ApplicationRoot.getContext()
    */
    private val context: Context by lazy { ApplicationRoot.getContext() }

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun propertyFromId(propertyId: Long): Property {
        return propertyRepository.propertyFromId(propertyId)
    }

    fun thumbnailUrl(property: Property): String {
        val smMkAddress1 = property.pAddress.toUrl()
        return SM_URL + SM_SIZE + SM_SCALE + SM_TYPE + SM_MK_COLOR1 + smMkAddress1 + SM_KEY
    }

    val agentSet = agentRepository.agentsSet
    val pTypeSet = propertyRepository.pTypesSet


    /**
     * Google Maps  TODO: Move to Utils ?
     */

    fun checkForPermissions(context: Context): Boolean {
        return !(ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context, onLocationFetched: (location: Location) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    onLocationFetched(location)
                }
            }
            .addOnFailureListener { exception: Exception ->
                Log.w("Location exception", exception.message.toString())
            }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLatLng(context: Context, onLocationFetched: (location: LatLng) -> Unit) {
        var latLng: LatLng
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    latLng = LatLng(latitude, longitude)
                    onLocationFetched(latLng)
                }
            }
            .addOnFailureListener { exception: Exception ->
                Log.w("Location exception", exception.message.toString())
            }
    }




    //
    val visiblePermissionDialogQueue = mutableStateListOf<String>()
    fun dismissdialog() {
        visiblePermissionDialogQueue.removeLast()
    }
    fun onPermissionResult(
        permission: String,
        isGranted: Boolean,
    ) {
        if (!isGranted) {
            visiblePermissionDialogQueue.add(0, permission)
        }
    }
    //




    /** End Google Maps */


}

/**
 * Google Maps TOP LEVEL
 */
/*

// TODO : To be deleted cause moved inside VM
@SuppressLint("MissingPermission")
fun getCurrentLocation(context: Context, onLocationFetched: (location: LatLng) -> Unit) {
    var latLng: LatLng
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation
        .addOnSuccessListener { location: Location? ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                latLng = LatLng(latitude, longitude)
                onLocationFetched(latLng)
            }
        }
        .addOnFailureListener { exception: Exception ->
            Log.w("Location exception", exception.message.toString())
        }
}
*/

// TODO: Move to Utils ?
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun getLocationFromAddress(context: Context?, strAddress: String?): LatLng? {
    val coder = Geocoder(context!!)
    var latLng: LatLng? = null
    coder.getFromLocationName(strAddress!!, 5, @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

/** End Google Maps */

