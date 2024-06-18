package com.aquaero.realestatemanager.utils

import android.location.Location
import com.google.android.gms.maps.LocationSource

class MyLocationSource: LocationSource {
    var listener: LocationSource.OnLocationChangedListener? = null

    override fun activate(p0: LocationSource.OnLocationChangedListener) {
        this.listener = p0
    }

    override fun deactivate() {
        listener = null
    }

    fun onLocationChanged(location: Location) {
        listener?.onLocationChanged(location)
    }
}