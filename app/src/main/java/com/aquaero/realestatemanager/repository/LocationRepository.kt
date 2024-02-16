package com.aquaero.realestatemanager.repository

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.properties.Delegates

class LocationRepository {

    private val context: Context by lazy { ApplicationRoot.getContext() }
    private var locPermsGranted by Delegates.notNull<Boolean>()
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }
    private val locationUpdatesFlow = MutableStateFlow<Location?>(null)
    private val locationCallback = object: LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            p0.lastLocation?.let {
                location -> locationUpdatesFlow.value = location
                Log.w(
                    "Location Repository",
                    "Location update callback: Lat: ${location.latitude}, Lng: ${location.longitude}"
                )
            }
        }
    }

    fun checkForPermissions(): Boolean {
        locPermsGranted = !(
                ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(
                            context, Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                )

        return locPermsGranted
    }

    fun areLocPermsGranted(): Boolean {
        return locPermsGranted
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(10000)
            .setIntervalMillis(10000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        Log.w("LocationRepository", "Location updates started!")
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.w("LocationRepository", "Location updates stopped!")
    }

    fun getLocationUpdates(): StateFlow<Location?> {
        return locationUpdatesFlow
    }

    @SuppressLint("NewApi")
    suspend fun getLocationFromAddress(strAddress: String?, internetAvailable: Boolean): LatLng? =
        suspendCoroutine { continuation ->
            val coder = Geocoder(com.aquaero.realestatemanager.context)
            if (internetAvailable) {
                coder.getFromLocationName(strAddress!!, 5,
                    object : Geocoder.GeocodeListener {
                        override fun onGeocode(address: MutableList<Address>) {
                            val location: Address = address[0]
                            val latLng = LatLng(location.latitude, location.longitude)
                            continuation.resume(latLng)
                            Log.w("Geocoder.getFromLocName", "LatLng: ${latLng.toString()}")
                            Log.w("Geocoder.getFromLocName", "Locality: ${location.locality}")
                            Log.w("Geocoder.getFromLocName", "Latitude: ${location.latitude}")
                            Log.w("Geocoder.getFromLocName", "Longitude: ${location.longitude}")
                        }

                        override fun onError(errorMessage: String?) {
                            super.onError(errorMessage)
                            Log.w("Geocoder.getFromLocName", errorMessage.toString())
                            continuation.resume(null)
                        }
                    })
            } else {
                continuation.resume(null)
            }
        }


}



