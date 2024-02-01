package com.aquaero.realestatemanager.utils

import android.annotation.SuppressLint
import com.aquaero.realestatemanager.DATE_PATTERN
import com.aquaero.realestatemanager.RATE_OF_DOLLAR_IN_EURO
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

fun convertDollarToEuro(dollars: Int?): Int? {
    return dollars?.let { (it * RATE_OF_DOLLAR_IN_EURO).roundToInt() }
}

fun convertEuroToDollar(euros: Int?): Int? {
    return euros?.let { (it / RATE_OF_DOLLAR_IN_EURO).roundToInt() }
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

fun textWithEllipsis(fullText: String, maxLength: Int, maxLines: Int): String {
    var text = fullText
    if (text.length > maxLength && maxLines == 1) {
        text = text.dropLast(text.length - maxLength + 3).plus("…")
    }
    return text
}

fun ellipsis(): String {
    // Ellipsis: Unicode = &#8230;  //  Hex = u2026 ou %2026
    val hexCode = "u2026"
    val hexVal = Integer.parseInt(hexCode.drop(1), 16)
    val ellipsisChar = hexVal.toChar()
    println("Result for unicode $hexCode is $ellipsisChar")
    return ellipsisChar.toString()
}




