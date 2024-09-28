package com.aquaero.realestatemanager.repository

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import com.aquaero.realestatemanager.utils.ConnectionState
import com.aquaero.realestatemanager.utils.ForTestingOnly
import com.aquaero.realestatemanager.utils.GeocoderHelper
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

    private var locPermsGranted by Delegates.notNull<Boolean>()
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val locationUpdatesFlow = MutableStateFlow<Location?>(null)
    private var locationCallback = object: LocationCallback() {
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


    /**
     * For testing only
     * Set the repository private variable: fusedLocationProviderClient
     */
    @ForTestingOnly
    @Suppress("FunctionName")
    fun forTestingOnly_setFusedLocationProviderClient(client: FusedLocationProviderClient) {
        fusedLocationProviderClient = client
    }
    /**
     * For testing only
     * Set a locationResult to the repository private variable: locationCallback
     */
    @ForTestingOnly
    @Suppress("FunctionName")
    fun forTestingOnly_setLocationCallbackResult(locationResult: LocationResult) {
        locationCallback.onLocationResult(locationResult)
    }


    private fun fusedLocationClient(context: Context): FusedLocationProviderClient {
        return fusedLocationProviderClient ?: LocationServices.getFusedLocationProviderClient(context)
    }

    fun checkForConnection(connection: ConnectionState): Boolean {
        return connection === ConnectionState.Available
    }

    fun checkForPermissions(context: Context): Boolean {
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
    fun startLocationUpdates(context: Context) {
        val locationRequest = LocationRequest.Builder(10000)
            .setIntervalMillis(10000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        fusedLocationClient(context = context).requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        Log.w("LocationRepository", "Location updates started!")
    }

    fun stopLocationUpdates(context: Context) {
        fusedLocationClient(context = context).removeLocationUpdates(locationCallback)
        Log.w("LocationRepository", "Location updates stopped!")
    }

    fun getLocationUpdates(): StateFlow<Location?> {
        return locationUpdatesFlow
    }

    suspend fun getLocationFromAddress(
        geocoderHelper: GeocoderHelper,
        context: Context,
        strAddress: String?,
        isInternetAvailable: Boolean
    ): LatLng? = suspendCoroutine { continuation ->
        if (isInternetAvailable && strAddress != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Android 13+ - API 33+
                geocoderHelper.getLatLngFromAddressAsync(context, strAddress) { latLng ->
                    continuation.resume(latLng)
                }
            } else {
                // Android 12- - API 32-
                val latLng = geocoderHelper.getLatLngFromAddress(context, strAddress)
                continuation.resume(latLng)
                Log.w("Geocoder.getFromLocName", "LatLng: $latLng")
            }
        } else {
            continuation.resume(null)
        }
    }

    fun createAppSettingsIntent(context: Context): Intent {
        return Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

}



