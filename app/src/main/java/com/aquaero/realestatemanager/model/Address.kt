package com.aquaero.realestatemanager.model

data class Address(
    val addressId: Long,
    val streetNumber: String?,
    val streetName: String,
    val addInfo: String?,
    val city: String,
    val state: String,
    val zipCode: String,
    val country: String,
    val lat: Double,
    val lng: Double
) {
    override fun toString(): String {
        val line1: String = if (streetNumber != null) "$streetNumber $streetName" else streetName
        val line2: String? = addInfo
        val line3: String = city
        val line4 = "$state $zipCode"
        val line5: String = country
        return if (line2 != null) "$line1\n$line2\n$line3\n$line4\n$line5" else "$line1\n$line3\n$line4\n$line5"
    }
}
