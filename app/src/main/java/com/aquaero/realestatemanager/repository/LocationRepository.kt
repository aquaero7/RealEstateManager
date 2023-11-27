package com.aquaero.realestatemanager.repository

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
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