package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.provider.Settings
import androidx.lifecycle.ViewModel
import com.aquaero.realestatemanager.repository.LocationRepository
import com.aquaero.realestatemanager.utils.ConnectionState
import kotlinx.coroutines.flow.StateFlow

class MapViewModel(
    private val locationRepository: LocationRepository
): ViewModel() {

    fun checkForConnection(connection: ConnectionState): Boolean {
        return locationRepository.checkForConnection(connection)
    }

    fun checkForPermissions(): Boolean {
        return locationRepository.checkForPermissions()
    }

    fun areLocPermsGranted(): Boolean {
        return locationRepository.areLocPermsGranted()
    }

    fun startLocationUpdates() {
        locationRepository.startLocationUpdates()
    }

    fun stopLocationUpdates() {
        locationRepository.stopLocationUpdates()
    }

    fun getLocationUpdates(): StateFlow<Location?> {
        return locationRepository.getLocationUpdates()
    }

    fun openAppSettings(context: Context) {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        )
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .also { context.startActivity(it) }
    }

}

