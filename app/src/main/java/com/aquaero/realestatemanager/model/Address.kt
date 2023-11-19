package com.aquaero.realestatemanager.model

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import java.text.Normalizer

data class Address(
    val addressId: Long,
    val streetNumber: String,
    val streetName: String,
    val addInfo: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val country: String,
    val location: LatLng,
) {
    override fun toString(): String {
        val line1: String =
            if (streetNumber.isNotEmpty()) "$streetNumber $streetName" else streetName
        val line2: String = addInfo
        val line3: String = city
        val line4 = if (state.isNotEmpty()) "$state $zipCode" else zipCode
        val line5: String = country
        return if (line2.isNotEmpty()) "$line1\n$line2\n$line3\n$line4\n$line5" else "$line1\n$line3\n$line4\n$line5"
    }

    fun toUrl(): String {
        val part1: String =
            if (streetNumber.isNotEmpty()) "${streetNumber.toU()}+${streetName.toU()}"
            else streetName.toU()
        val part2: String = addInfo.toU()
        val part3: String = city.toU()
        val part4 = if (state.isNotEmpty()) "${state.toU()}+${zipCode.toU()}" else zipCode.toU()
        val part5: String = country.toU()
        val staticUrl = "$part1,$part2,$part3,$part4,$part5"
        // String normalized
        val normalizedUrl = Normalizer.normalize(staticUrl, Normalizer.Form.NFD)
        // Returns string without accent (diacriticals marks)
        return Regex("\\p{InCombiningDiacriticalMarks}+").replace(normalizedUrl, "")
    }

    // Replaces spaces with '+'
    private fun String.toU(): String = replace("\\s".toRegex(), "+")

}




