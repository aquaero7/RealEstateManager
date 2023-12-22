package com.aquaero.realestatemanager.model

import com.aquaero.realestatemanager.NO_PHOTO
import com.aquaero.realestatemanager.utils.convertDollarToEuro
import java.text.NumberFormat
import java.time.LocalDate
import java.util.Locale

// @Serializable
data class Property(
    val pId: Long,
    val pType: Int,
    val pAddress: Address,
    val pPrice: Int,
    val description: String?,
    val surface: Int,
    val nbOfRooms: Int,
    val nbOfBathrooms: Int,
    val nbOfBedrooms: Int,
    val photos: MutableList<Photo> = mutableListOf(NO_PHOTO),
    val registrationDate: LocalDate,
    val saleDate: LocalDate?,
    // val statusSold: Boolean,
    val pPoi: MutableList<String>,
    val agentId: Long
) {
    fun priceInCurrency(currency: String): Int {
        return when (currency) {
            "€" -> convertDollarToEuro(pPrice)
            else -> pPrice
        }
    }

    fun priceStringInCurrency(currency: String): String {
        return when (currency) {
            "€" -> {
                val numberFormat = NumberFormat.getCurrencyInstance(Locale("fr", "FR"))
                numberFormat.maximumFractionDigits = 0
                numberFormat.format(convertDollarToEuro(pPrice))
            }
            else -> {
                val numberFormat = NumberFormat.getCurrencyInstance(Locale("en", "US"))
                numberFormat.maximumFractionDigits = 0
                numberFormat.format(pPrice)
            }
        }
    }
    
}