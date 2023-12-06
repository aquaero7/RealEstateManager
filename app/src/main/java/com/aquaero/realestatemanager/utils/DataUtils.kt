package com.aquaero.realestatemanager.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

fun convertDollarToEuro(dollars: Int): Int {
    return (dollars * 0.812).roundToInt()
}

fun convertEuroToDollar(euros: Int): Int {
    return (euros / 0.812).roundToInt()
}

fun getTodayDate(): String? {
    val dateFormat: DateFormat =
        SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
    return dateFormat.format(Date())
}

/* TODO: Function to be deleted
@SuppressLint("NewApi")
fun isInternetAvailable(context: Context?): Boolean {
    var isInternetAvailable = false
    var wifiInfo = false
    var mobileInfo = false
    if (context != null) {
        val connectivity =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivity.getNetworkCapabilities(connectivity.activeNetwork).also {
            if (it != null) {
                wifiInfo = it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                mobileInfo = it.hasTransport((NetworkCapabilities.TRANSPORT_CELLULAR))
            }
        }
        if (wifiInfo || mobileInfo) isInternetAvailable = true
    }
    Log.w("DataUtils", "Wifi: $wifiInfo / Cellular: $mobileInfo / Internet: $isInternetAvailable")
    return isInternetAvailable
}
*/



