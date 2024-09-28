package com.aquaero.realestatemanager.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import com.google.android.gms.maps.model.LatLng

class GeocoderHelper {

    // For Android versions below 13
    @Suppress("DEPRECATION")
    fun getLatLngFromAddress(context: Context, address: String): LatLng? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Use GeocodeListener for Android 13+
            null
        } else {
            val geocoder = Geocoder(context)
            val addressList = geocoder.getFromLocationName(address, 5)
            addressList?.get(0)?.let {
                Log.w("Geocoder.getFromLocName", "Locality: ${it.locality}")
                Log.w("Geocoder.getFromLocName", "Latitude: ${it.latitude}")
                Log.w("Geocoder.getFromLocName", "Longitude: ${it.longitude}")
                LatLng(it.latitude, it.longitude)
            }
        }
    }

    // For Android 13 (API 33) or above
    @SuppressLint("NewApi")
    fun getLatLngFromAddressAsync(context: Context, address: String, callback: (LatLng?) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Geocoder(context).getFromLocationName(address, 5, object : Geocoder.GeocodeListener {
                override fun onGeocode(addressList: MutableList<Address>) {
                    val latLng = addressList[0].let {
                        Log.w("Geocoder.getFromLocName", "Locality: ${it.locality}")
                        Log.w("Geocoder.getFromLocName", "Latitude: ${it.latitude}")
                        Log.w("Geocoder.getFromLocName", "Longitude: ${it.longitude}")
                        LatLng(it.latitude, it.longitude)
                    }
                    Log.w("Geocoder.getFromLocName", "LatLng: $latLng")
                    callback(latLng)
                }
                override fun onError(errorMessage: String?) {
                    super.onError(errorMessage)
                    Log.w("Geocoder.getFromLocName", errorMessage.toString())
                    callback(null)
                }
            })
        } else {
            // Use Geocoder without listener for Android 12-
            callback(null)
        }
    }

}