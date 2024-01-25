package com.aquaero.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import java.text.Normalizer

@Entity
data class Address(
    @PrimaryKey(autoGenerate = true)
    val addressId: Long,
    val streetNumber: String,
    val streetName: String,
    val addInfo: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val country: String,
    val latLng: LatLng,
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

val ADDRESS_PREPOPULATION_DATA = listOf(
    Address(
        addressId = -1,
        streetNumber = "3",                    //"n1111111",
        streetName = "avenue de Brehat",       //"s1111111",
        addInfo = "",                          //"i1111111",
        city = "Villebon-sur-Yvette",          //"v1111111",
        state = "",                            //"d1111111",
        zipCode = "91140",                     //"z1111111",
        country = "FR",                        //"c1111111",
        latLng = LatLng(48.6860854, 2.2201107)
    ),
    Address(
        addressId = -2,
        streetNumber = "35",                   //"n2222222",
        streetName = "route de Paris",         //"s2222222",
        addInfo = "ZAC Les 4 Chênes",          //"i2222222",
        city = "Pontault-Combault",            //"v2222222",
        state = "",                            //"d2222222",
        zipCode = "77340",                     //"z2222222",
        country = "FR",                        //"c2222222",
        latLng = LatLng(48.7765790,2.5906768)
    ),
    Address(
        addressId = -3,
        streetNumber = "500",                  //"n3333333",
        streetName = "Brookhaven Ave",         //"s3333333",
        addInfo = "",                          //"i3333333",
        city = "Atlanta",                      //"v3333333",
        state = "GA",                          //"d3333333",
        zipCode = "30319",                     //"z3333333",
        country = "US",                        //"c3333333",
        latLng = LatLng(33.8725435,-84.3370041)
    ),
)




