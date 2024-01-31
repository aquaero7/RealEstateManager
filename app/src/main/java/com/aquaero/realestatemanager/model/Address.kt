package com.aquaero.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import java.text.Normalizer

@Entity
data class Address(
    @PrimaryKey(autoGenerate = true)
    val addressId: Long = 0,
    val streetNumber: String?,
    val streetName: String?,
    val addInfo: String?,
    val city: String?,
    val state: String?,
    val zipCode: String?,
    val country: String?,
    // val latLng: LatLng,
    val latitude: Double?,
    val longitude: Double?,
) {
    override fun toString(): String {
        return buildString {
            val line1: String = "${streetNumber.orEmpty()} ${streetName.orEmpty()}".trim()
            val line2: String = addInfo.orEmpty()
            val line3: String = city.orEmpty()
            val line4 = "${state.orEmpty()} ${zipCode.orEmpty()}".trim()
            val line5: String = country.orEmpty()

            val allLines = listOf(line1, line2, line3, line4, line5)
            for (line in allLines) {
                if (line.isNotEmpty()) appendLine(line)
            }
        }
    }

    fun toUrl(): String {
//        val part1: String = if (streetNumber.isNotEmpty()) "${streetNumber.toU()}+${streetName.toU()}" else streetName.toU()
        val part1 = "${
            streetNumber.orEmpty().toU()
        }${if (!streetNumber.isNullOrEmpty() && !streetName.isNullOrEmpty()) "+" else ""}${
            streetName.orEmpty().toU()
        }"
        val part2 = addInfo.orEmpty().toU()
        val part3 = city.orEmpty().toU()
//        val part4 = if (state.isNotEmpty()) "${state.toU()}+${zipCode.toU()}" else zipCode.toU()
        val part4 = "${
            state.orEmpty().toU()
        }${if (!state.isNullOrEmpty() && !zipCode.isNullOrEmpty()) "+" else ""}${
            zipCode.orEmpty().toU()
        }"
        val part5 = country.orEmpty().toU()
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
        addressId = 1001,
        streetNumber = "3",                    //"n1111111",
        streetName = "avenue de Brehat",       //"s1111111",
        addInfo = "",                          //"i1111111",
        city = "Villebon-sur-Yvette",          //"v1111111",
        state = "",                            //"d1111111",
        zipCode = "91140",                     //"z1111111",
        country = "FR",                        //"c1111111",
        latitude = 48.6860854,
        longitude = 2.2201107,
//        latLng = LatLng(48.6860854, 2.2201107)
    ),
    Address(
        addressId = 1002,
        streetNumber = "35",                   //"n2222222",
        streetName = "route de Paris",         //"s2222222",
        addInfo = "ZAC Les 4 Chênes",          //"i2222222",
        city = "Pontault-Combault",            //"v2222222",
        state = "",                            //"d2222222",
        zipCode = "77340",                     //"z2222222",
        country = "FR",                        //"c2222222",
        latitude = 48.7765790,
        longitude = 2.5906768,
//        latLng = LatLng(48.7765790, 2.5906768)
    ),
    Address(
        addressId = 1003,
        streetNumber = "500",                  //"n3333333",
        streetName = "Brookhaven Ave",         //"s3333333",
        addInfo = "",                          //"i3333333",
        city = "Atlanta",                      //"v3333333",
        state = "GA",                          //"d3333333",
        zipCode = "30319",                     //"z3333333",
        country = "US",                        //"c3333333",
        latitude = 33.8725435,
        longitude = -84.3370041,
//        latLng = LatLng(33.8725435, -84.3370041)
    ),
)




