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




