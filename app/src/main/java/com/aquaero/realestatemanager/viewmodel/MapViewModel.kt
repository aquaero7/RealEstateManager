package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import android.location.Location
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

    fun checkForPermissions(context: Context): Boolean {
        return locationRepository.checkForPermissions(context = context)
    }

    fun areLocPermsGranted(): Boolean {
        return locationRepository.areLocPermsGranted()
    }

    fun startLocationUpdates(context: Context) {
        locationRepository.startLocationUpdates(context = context)
    }

    fun stopLocationUpdates(context: Context) {
        locationRepository.stopLocationUpdates(context = context)
    }

    fun getLocationUpdates(): StateFlow<Location?> {
        return locationRepository.getLocationUpdates()
    }

    fun openAppSettings(context: Context) {
        context.startActivity(locationRepository.createAppSettingsIntent(context))
    }

}

