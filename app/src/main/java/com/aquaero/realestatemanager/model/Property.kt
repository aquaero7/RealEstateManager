package com.aquaero.realestatemanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.aquaero.realestatemanager.CACHE_AGENT_ID_VALUE
import com.aquaero.realestatemanager.CACHE_LONG_ID_VALUE
import com.aquaero.realestatemanager.CACHE_NULLABLE_VALUE
import com.aquaero.realestatemanager.CACHE_TYPE_ID_VALUE
import com.aquaero.realestatemanager.NEW_ITEM_ID
import com.aquaero.realestatemanager.utils.convertDollarToEuro
import java.text.NumberFormat
import java.util.Locale
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField

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
    var propertyId: Long = 0,
    @ColumnInfo(index = true)
    var typeId: String = TypeEnum.UNASSIGNED.key,
    @ColumnInfo(index = true)
    var addressId: Long?,
    var price: Int?,
    var description: String?,
    var surface: Int?,
    var nbOfRooms: Int?,
    var nbOfBathrooms: Int?,
    var nbOfBedrooms: Int?,
    var registrationDate: String?,
    var saleDate: String?,
    @ColumnInfo(index = true)
    var agentId: Long = 0
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

    /**
     * Set to null the blank value of all properties in the instance,
     * except the id (identified by its index).
     */
    fun replaceBlankValuesWithNull(): Property {
        // Scan all properties of the class except id at index 0
        this::class.memberProperties.forEachIndexed { index, property ->
            if (index != 0) {
                // Get the value of each property
                val value = property.call(this)
                // Replaces blank value with null
                if ((value != null) && value.toString().isBlank()) {
                    // Makes the field accessible for modification
                    property.javaField?.isAccessible = true
                    property.javaField?.set(this, null)
                }
            }
        }
        return this
    }

    /**
     * Creates a new instance of the object without its id (identified by its index)
     * (or with its id set to 0) so that it is autogenerated by Room.
     */
    fun withoutId(): Property {
        // Get the primary constructor
        val constructor = this::class.primaryConstructor
            ?: error("Primary constructor not found for ${this::class.simpleName}")

        // Initializes the map <parameter, value>
        val argsMap = mutableMapOf<KParameter, Any?>()

        // Fills the map with the values of the parameters of the constructor, excepted the first one (propertyId)
        constructor.parameters.forEachIndexed { index, kParameter ->

            // TODO: Option 1   Choose one of them
            // Exclude the propertyId value and get the other values from the property instance
            if (index != 0) {
                val value = this::class.members.first { it.name == kParameter.name }.call(this)
                argsMap[kParameter] = value
            }

            /*
            // TODO: Option 2   Choose one of them
            // Set the propertyId value to 0 and get the other values from the property instance
            val value = if (index == 0) 0 else this::class.members.first { it.name == kParameter.name }.call(this)
            argsMap[kParameter] = value
            */

        }

        // Creates and returns the new instance created with the constructor and the values from the map
        return constructor.callBy(argsMap)
    }

}

val PROPERTY_PREPOPULATION_DATA = listOf(
    Property(
        propertyId = NEW_ITEM_ID,
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
        agentId = NEW_ITEM_ID
    ),
)

val CACHE_PROPERTY = Property(
    propertyId = CACHE_LONG_ID_VALUE,
    typeId = CACHE_TYPE_ID_VALUE,
    addressId = CACHE_NULLABLE_VALUE,
    price = CACHE_NULLABLE_VALUE,
    description = CACHE_NULLABLE_VALUE,
    surface = CACHE_NULLABLE_VALUE,
    nbOfRooms = CACHE_NULLABLE_VALUE,
    nbOfBathrooms = CACHE_NULLABLE_VALUE,
    nbOfBedrooms = CACHE_NULLABLE_VALUE,
    registrationDate = CACHE_NULLABLE_VALUE,
    saleDate = CACHE_NULLABLE_VALUE,
    agentId = CACHE_AGENT_ID_VALUE
)