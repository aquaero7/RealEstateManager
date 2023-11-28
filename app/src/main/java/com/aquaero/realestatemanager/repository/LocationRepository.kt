package com.aquaero.realestatemanager.repository

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.aquaero.realestatemanager.ApplicationRoot
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlin.properties.Delegates

class LocationRepository {

    private val context: Context by lazy { ApplicationRoot.getContext() }
    private var locPermsGranted by Delegates.notNull<Boolean>()

    fun checkForPermissions(): Boolean {
        locPermsGranted = !(ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED)

        return locPermsGranted
    }

    fun areLocPermsGranted(): Boolean {
        return locPermsGranted
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(onLocationFetched: (location: Location) -> Unit) {
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

    /*  // TODO : Should it be deleted because replaced with getCurrentLocation() ?
    @SuppressLint("MissingPermission")
    fun getCurrentLatLng(onLocationFetched: (location: LatLng) -> Unit) {
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

}

// TODO: Move from TOP LEVEL to class ?
@SuppressLint("NewApi")
fun getLocationFromAddress(strAddress: String?): LatLng? {
    val coder = Geocoder(ApplicationRoot.getContext())
    var latLng: LatLng? = null
    coder.getFromLocationName(strAddress!!, 5, // @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        object : Geocoder.GeocodeListener {
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