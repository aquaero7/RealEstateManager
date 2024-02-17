package com.aquaero.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aquaero.realestatemanager.CACHE_LONG_ID_VALUE
import com.aquaero.realestatemanager.CACHE_NULLABLE_VALUE
import java.text.Normalizer
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField

@Entity
data class Address(
    /*
     * Warning!
     * Because of the use of functions belonging to this class,
     * 'addressId' must always be the first property in the class (index 0),
     * 'latitude' must be set at index 8, and 'longitude' must be set at index 9,
     * otherwise, the code of these functions must be updated
     */
    @PrimaryKey(autoGenerate = true)
    var addressId: Long = 0,
    var streetNumber: String?,
    var streetName: String?,
    var addInfo: String?,
    var city: String?,
    var state: String?,
    var zipCode: String?,
    var country: String?,
    var latitude: Double?,
    var longitude: Double?,
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
        val part1 = "${
            streetNumber.orEmpty().toU()
        }${if (!streetNumber.isNullOrEmpty() && !streetName.isNullOrEmpty()) "+" else ""}${
            streetName.orEmpty().toU()
        }"
        val part2 = addInfo.orEmpty().toU()
        val part3 = city.orEmpty().toU()
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

    /**
     * Returns true if the value of all class properties in the instance,
     * except the id, latitude and longitude (identified by their index) is null or blank.
     * Otherwise returns false.
     */
    fun isNullOrBlank(): Boolean {
        var areAllValuesNullOrBlank = true
        // Scan all properties of the class except id at index 0
        this::class.memberProperties.forEachIndexed { index, property ->
            if (index !in listOf(0, 8, 9)) {
                // Get the value of each property
                val value = property.call(this)
                // Checks if the value is not null or blank
                if ((value != null) && value.toString().isNotBlank()) {
                    areAllValuesNullOrBlank = false
                    // Exit the loop
                    return@forEachIndexed
                }
            }
        }
        return areAllValuesNullOrBlank
    }

    /**
     * Checks if all class properties (excepted the id at index 0) of two instances of Address have the same values.
     * Returns false if at least one class property have a different values. Otherwise, returns true.
     */
    fun hasDifferencesWith(other: Address): Boolean {
        val properties = Address::class.members
            .filterIsInstance<KProperty1<Address, *>>()
            .filterIndexed { index, _ -> index != 0 }
        for (property in properties) {
            if (property.get(this) != property.get(other)) return true
        }
        return false
    }

    /**
     * Set to null the blank value of all class properties in the instance,
     * except the id (identified by its index).
     */
    fun replaceBlankValuesWithNull(): Address {
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
     * Creates a new instance of the object without its id
     * (or with its id set to 0) so that it is autogenerated by Room.
     * The id is identified by its index.
     */
    fun withoutId(): Address {
        // Get the primary constructor
        val constructor = this::class.primaryConstructor
            ?: error("Primary constructor not found for ${this::class.simpleName}")

        // Initializes the map <parameter, value>
        val argsMap = mutableMapOf<KParameter, Any?>()

        // Fills the map with the values of the parameters of the constructor, excepted the first one (addressId)
        constructor.parameters.forEachIndexed { index, kParameter ->

            // TODO: Option 1   Choose one of them
            // Exclude the addressId value and get the other values from the address instance
            if (index != 0) {
                val value = this::class.members.first { it.name == kParameter.name }.call(this)
                argsMap[kParameter] = value
            }

            /*
            // TODO: Option 2   Choose one of them
            // Set the addressId value to 0 and get the other values from the address instance
            val value = if (index == 0) 0 else this::class.members.first { it.name == kParameter.name }.call(this)
            argsMap[kParameter] = value
            */

        }

        // Creates and returns the new instance created with the constructor and the values from the map
        return constructor.callBy(argsMap)
    }

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
    ),
)

val CACHE_ADDRESS = Address(
    addressId = CACHE_LONG_ID_VALUE,
    streetNumber = CACHE_NULLABLE_VALUE,
    streetName = CACHE_NULLABLE_VALUE,
    addInfo = CACHE_NULLABLE_VALUE,
    city = CACHE_NULLABLE_VALUE,
    state = CACHE_NULLABLE_VALUE,
    zipCode = CACHE_NULLABLE_VALUE,
    country = CACHE_NULLABLE_VALUE,
    latitude = CACHE_NULLABLE_VALUE,
    longitude = CACHE_NULLABLE_VALUE
)




