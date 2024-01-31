package com.aquaero.realestatemanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.aquaero.realestatemanager.NO_ITEM_ID
import com.aquaero.realestatemanager.NULL_ITEM_ID
import com.aquaero.realestatemanager.utils.convertDollarToEuro
import java.text.NumberFormat
import java.util.Locale

// @Serializable
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Address::class, parentColumns = ["addressId"], childColumns = ["addressId"],
            onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = Type::class, parentColumns = ["typeId"], childColumns = ["typeId"],
            onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = Agent::class, parentColumns = ["agentId"], childColumns = ["agentId"],
            onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE,
        ),
    ]
)
data class Property(
    @PrimaryKey(autoGenerate = true)
    val propertyId: Long = 0,
    @ColumnInfo(index = true)
    val typeId: String = TypeEnum.UNASSIGNED.key,
    @ColumnInfo(index = true)
    val addressId: Long?,
    val price: Int?,
    val description: String?,
    val surface: Int?,
    val nbOfRooms: Int?,
    val nbOfBathrooms: Int?,
    val nbOfBedrooms: Int?,
    // val photos: MutableList<Photo> = mutableListOf(NO_PHOTO),
    val registrationDate: String?,
    val saleDate: String?,
    // val statusSold: Boolean,
    // val poi: MutableList<String>,
    @ColumnInfo(index = true)
    val agentId: Long = 0
) {
    fun priceInCurrency(currency: String): Int? {
        return when (currency) {
            "€" -> convertDollarToEuro(price)
            else -> price
        }
    }

    fun priceFormattedInCurrency(currency: String): String {
        return price?.let {
            when (currency) {
                "€" -> {
                    val numberFormat = NumberFormat.getCurrencyInstance(Locale("fr", "FR"))
                    numberFormat.maximumFractionDigits = 0
                    numberFormat.format(convertDollarToEuro(it))
                }

                else -> {
                    val numberFormat = NumberFormat.getCurrencyInstance(Locale("en", "US"))
                    numberFormat.maximumFractionDigits = 0
                    numberFormat.format(it)
                }
            }
        } ?: ""
    }

}

val PROPERTY_PREPOPULATION_DATA = listOf(
    Property(
        propertyId = NULL_ITEM_ID,
        typeId = TypeEnum.UNASSIGNED.key,
        addressId = null,
        price = null,
        description = null,
        surface = null,
        nbOfRooms = null,
        nbOfBathrooms = null,
        nbOfBedrooms = null,
        registrationDate = null,
        saleDate = null,
        agentId = NULL_ITEM_ID
    ),
)