package com.aquaero.realestatemanager.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.aquaero.realestatemanager.DATE_PATTERN
import com.aquaero.realestatemanager.DATE_TIME_PATTERN
import com.aquaero.realestatemanager.RATE_OF_DOLLAR_IN_EURO
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

fun convertDollarToEuro(dollars: Int): Int {
    return (dollars * RATE_OF_DOLLAR_IN_EURO).roundToInt()
}

fun convertEuroToDollar(euros: Int): Int {
    return (euros / RATE_OF_DOLLAR_IN_EURO).roundToInt()
}

@SuppressLint("NewApi")
fun convertDateMillisToString(millis: Long): String {
    val instant = Instant.ofEpochMilli(millis)
    val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
    val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
    return localDate.format(formatter)
}

@SuppressLint("NewApi")
fun convertDateStringToMillis(string: String): Long {
    val date = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
    val localDate = if (string.isNotEmpty()) LocalDate.parse(string, formatter) else date
    val instant = localDate.atStartOfDay().toInstant(ZoneOffset.UTC)
    return instant.toEpochMilli()

    /*  // Function using LocalDateTime but time part is useless and arbitrary set to 12:00
    val date = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)
    val localDateTime = if (string.isNotEmpty()) LocalDateTime.parse("$string 12:00", formatter) else date
    val instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
    return instant.toEpochMilli()
    */
}




