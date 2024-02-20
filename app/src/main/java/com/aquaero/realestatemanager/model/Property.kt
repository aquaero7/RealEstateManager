package com.aquaero.realestatemanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.aquaero.realestatemanager.CACHE_AGENT_ID_VALUE
import com.aquaero.realestatemanager.CACHE_LONG_ID_VALUE
import com.aquaero.realestatemanager.CACHE_NULLABLE_VALUE
import com.aquaero.realestatemanager.CACHE_TYPE_ID_VALUE
import com.aquaero.realestatemanager.utils.convertDollarToEuro
import java.text.NumberFormat
import java.util.Locale
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField

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
    /*
     * Warning!
     * Due to the use of functions belonging to this class,
     * be sure to update their code if the name of the class property 'propertyId' changes
     */
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
    /**
     * Converts a value from dollar to another currency.
     */
    fun priceInCurrency(currency: String): Int? {
        return when (currency) {
            "€" -> convertDollarToEuro(price)
            else -> price
        }
    }

    /**
     * Converts a value to string currency format.
     */
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
     * Set to null all blank values of the class properties in the instance,
     * excluding the id (identified by its name).
     */
    fun replaceBlankValuesWithNull(): Property {
        /*
         * Warning!
         * If the name of the class property 'propertyId' used in this function
         * changes in the constructor, be sure to update it
         */
        this::class.memberProperties
            // Filter properties to exclude the id and those already null
            .filter { it.name != "propertyId" && it.call(this) != null }
            .forEach { property ->
                // Check for blank value and replace with null
                if (property.call(this).toString().isBlank()) {
                    property.javaField?.isAccessible = true
                    property.javaField?.set(this, null)
                }
            }
        return this
    }

    /**
     * Creates a new instance of the object without its id (or with its id set to 0),
     * so that it will be autogenerated by Room. The id is identified by its name.
     */
    fun withoutId(): Property {
        /*
         * Warning!
         * If the name of the class property 'propertyId' used in this function
         * changes in the constructor, be sure to update it
         */

        // Get the primary constructor
        val constructor = this::class.primaryConstructor
            ?: error("Primary constructor not found for ${this::class.simpleName}")
        // Fill the map with parameter values, excluding the id
        val argsMap = constructor.parameters
            .filter { it.name != "propertyId" }
            .associateWith { parameter -> this::class.memberProperties.first { it.name == parameter.name }.call(this) }
        // Create and return the new instance
        return constructor.callBy(argsMap)
    }

}


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