package com.aquaero.realestatemanager.model

import com.aquaero.realestatemanager.utils.convertDollarToEuro
import java.text.NumberFormat
import java.time.LocalDate
import java.util.Locale

data class Property(
    val pId: Long,
    val pType: String,
    val pAddress: Address,
    val pPrice: Int,
    val description: String?,
    val surface: Int,
    val nbOfRooms: Int,
    val nbOfBathrooms: Int,
    val nbOfBedrooms: Int,
    val photos: List<Photo>?,
    val registrationDate: LocalDate,
    val saleDate: LocalDate?,
    // val statusSold: Boolean,
    val pPoi: List<String>,
    val agentId: Long
) {
    fun priceInCurrency(currency: String): Int {
        return when (currency) {
            "€" -> convertDollarToEuro(pPrice)
            else -> pPrice
        }
    }


    fun priceStringInCurrency(currency: String): String {
        /*
        val numberFormat: NumberFormat = when (currency) {
            "€" -> NumberFormat.getCurrencyInstance(Locale("fr", "FR"))
            else -> NumberFormat.getCurrencyInstance(Locale("en", "US"))
        }
        numberFormat.maximumFractionDigits = 0
        return numberFormat.format(pPrice)
        */

        //
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
        //

    }
    
}