package com.aquaero.realestatemanager.utils

import android.annotation.SuppressLint
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.DATE_PATTERN
import com.aquaero.realestatemanager.RATE_OF_DOLLAR_IN_EURO
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlin.math.pow
import kotlin.math.roundToInt

fun convertDpToPxInt(dpValue: Int, density: Density): Int {
    return with(density) { dpValue.dp.toPx().toInt() }
}

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
    val localDate = if (string.isNotEmpty()) {
        try {
            LocalDate.parse(string, formatter)
        } catch (e: DateTimeParseException) {
            date
        }
    } else date

    val instant = localDate.atStartOfDay().toInstant(ZoneOffset.UTC)
    return instant.toEpochMilli()
}

fun calculateMonthlyPaymentWithInterest(amount: Int, annualInterestRate: Float, termInMonths: Int): Float {
    return if (annualInterestRate != 0F) {
        if (termInMonths > 1) {
            (amount * annualInterestRate / 100 / 12) / (
                    1 - (1 + annualInterestRate / 100 / 12).pow(-termInMonths)
                    )
        } else {
            amount.toFloat()
        }
    } else {
        amount.toFloat() / maxOf(termInMonths, 1)
    }
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

/**
 * Returns true if the number is decimal,
 * Otherwise returns false.
 */
fun isDecimal(str: String): Boolean {
    val regex = Regex("^\\d*\\.?\\d*\$")
    return regex.matches(str)
}

/**
 * Returns true if each character of the string is a digit,
 * otherwise returns false.
 */
fun String.areDigitsOnly(): Boolean {
    return this.all { it.isDigit() }
}

/**
 * Generates a negative five-digit number of type 'Long', based on epoch time.
 * The absence of collision (uniqueness) for this value is guaranteed, on the same device
 * and in a time slot greater than 24-hour (more precisely 99999s, i.e. 1d, 3h, 46min and 39s),
 * under the following three conditions:
 * - This function is not called more than once per second
 * - This function is only called from one thread at a time
 * - This function is only called for one instance object at a time
 */
val generateProvisionalId: () -> Long = {
    /*
     * The epoch time value is divided by 1000 to convert it to seconds.
     * Then, modulo 100000 is applied to this value in order to keep only the last five digits,
     * for better readability.
     */
    -( System.currentTimeMillis() / 1E3.toLong() ) % 1E5.toLong()
}





