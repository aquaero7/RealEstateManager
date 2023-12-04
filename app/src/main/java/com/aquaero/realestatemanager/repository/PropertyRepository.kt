package com.aquaero.realestatemanager.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.SM_KEY
import com.aquaero.realestatemanager.SM_MK_COLOR1
import com.aquaero.realestatemanager.SM_SCALE
import com.aquaero.realestatemanager.SM_SIZE
import com.aquaero.realestatemanager.SM_TYPE
import com.aquaero.realestatemanager.SM_URL
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.utils.convertEuroToDollar
import java.time.LocalDate

class PropertyRepository() {

    private val context: Context by lazy { ApplicationRoot.getContext() }

    // For fake data only
    private val agentRepository = AgentRepository()
    private val addressRepository = AddressRepository()
    private val photoRepository = PhotoRepository()
    private val fakePhotos = photoRepository.fakePhotos

    /**
     * Temp data used as a cache for property creation ou update
     */

    private var descriptionValue by mutableStateOf("")
    private var priceValue by mutableIntStateOf(0)
    private var surfaceValue by mutableIntStateOf(0)
    private var typeValue by mutableStateOf("")


    fun propertyFromId(propertyId: Long): Property {
        return fakeProperties.find { it.pId == propertyId }!!
    }

    fun thumbnailUrl(property: Property): String {
        val smMkAddress1 = property.pAddress.toUrl()
        return SM_URL + SM_SIZE + SM_SCALE + SM_TYPE + SM_MK_COLOR1 + smMkAddress1 + SM_KEY
    }


    // ...

    fun updateProperty(propertyId: Comparable<*>) {
        // TODO: To be deleted when specific action is implemented
        Toast
            .makeText(
                context,
                "Click on ${context.getString(R.string.valid)}, for id $propertyId, from EditScreen",
                Toast.LENGTH_SHORT
            )
            .show()

        // TODO: Update database

        // After update
        Toast
            .makeText(
                context,
                context.getString(
                    R.string.recorded
                ),
                Toast.LENGTH_SHORT
            )
            .show()
    }


    /**
     * Temp data used as a cache for property creation ou update
     */

    fun onDescriptionValueChanged(value: String) {
        descriptionValue = value

        Log.w("PropertyRepository", "New value is: $value")
        Toast.makeText(context, "New value is: $value", Toast.LENGTH_SHORT).show()
    }

    fun onPriceValueChanged(value: String, currency: String) {
        priceValue = if (value.isNotEmpty()) {
            when (currency) {
                "€" -> convertEuroToDollar(value.toInt())
                else -> value.toInt()
            }
        } else 0

        Log.w(
            "PropertyRepository",
            "New value is: $priceValue dollars and input is $value $currency"
        )
        Toast.makeText(
            context,
            "New value is: $priceValue dollars and input is $value $currency",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun onSurfaceValueChanged(value: String) {
        surfaceValue = if (value.isNotEmpty()) value.toInt() else 0

        Log.w("PropertyRepository", "New value is: $value")
        Toast.makeText(context, "New value is: $value", Toast.LENGTH_SHORT).show()
    }

    fun onTypeValueChanged(value: String) {
        typeValue = value
        Log.w("PropertyRepository", "New value is: $value")
        Toast.makeText(context, "New value is: $value", Toast.LENGTH_SHORT).show()
    }


    //

    /**
     * Hardcoded data
     */
    val pTypesSet: MutableSet<Int?> = mutableSetOf(
        R.string.flat, R.string.house, R.string.duplex, R.string.penthouse,
        R.string.loft, R.string.manor, R.string.castle, R.string.hostel
    )

    val poiSet: MutableSet<Int?> = mutableSetOf(
        R.string.hospital, R.string.school, R.string.restaurant,
        R.string.shop, R.string.railway_station, R.string.car_park,
    )


    //
    /**
     * FAKE PROPERTIES
     */

    private val loremIpsum: String = LoremIpsum(300).values.first()

    @SuppressLint("NewApi")
    val fakeProperties = listOf(
        Property(
            0,
            "Flat",
            addressRepository.fakeAddresses[0],
            10000000,
            "d0000000\n$loremIpsum",
            1000,
            10,
            10,
            10,
            listOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            LocalDate.of(2023, 9, 10),  // LocalDate.parse("2023-09-10"),
            null,
            listOf("hospital", "restaurant", "shop", "car_park"),
            agentRepository.fakeAgents[0].agentId
        ),
        Property(
            1,
            "House",
            addressRepository.fakeAddresses[1],
            11111111,
            "d1111111\n$loremIpsum",
            1111,
            11,
            11,
            11,
            listOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            LocalDate.of(2023, 9, 11),  // LocalDate.parse("2023-09-11"),
            null,
            listOf("hospital", "restaurant", "shop", "car_park"),
            agentRepository.fakeAgents[1].agentId
        ),
        Property(
            2,
            "Duplex",
            addressRepository.fakeAddresses[2],
            12222222,
            "d2222222\n$loremIpsum",
            1222,
            12,
            12,
            12,
            listOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            LocalDate.of(2023, 9, 12),  // LocalDate.parse("2023-09-12"),
            null,
            listOf("hospital", "restaurant", "shop", "car_park"),
            agentRepository.fakeAgents[2].agentId
        ),
        Property(
            3,
            "Penthouse",
            addressRepository.fakeAddresses[0],
            13333333,
            "d3333333\n$loremIpsum",
            1333,
            13,
            13,
            13,
            listOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            LocalDate.of(2023, 9, 13),  // LocalDate.parse("2023-09-13"),
            null,
            listOf("hospital", "restaurant", "shop", "car_park"),
            agentRepository.fakeAgents[0].agentId
        ),
        Property(
            4,
            "Loft",
            addressRepository.fakeAddresses[1],
            14444444,
            "d4444444\n$loremIpsum",
            1444,
            14,
            14,
            14,
            listOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            LocalDate.of(2023, 9, 14),  // LocalDate.parse("2023-09-14"),
            null,
            listOf("hospital", "restaurant", "shop", "car_park"),
            agentRepository.fakeAgents[1].agentId
        ),
        Property(
            5,
            "Manor",
            addressRepository.fakeAddresses[2],
            15555555,
            "d5555555\n$loremIpsum",
            1555,
            15,
            15,
            15,
            listOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            LocalDate.of(2023, 9, 15),  // LocalDate.parse("2023-09-15"),
            null,
            listOf("hospital", "restaurant", "shop", "car_park"),
            agentRepository.fakeAgents[2].agentId
        ),
        Property(
            6,
            "Castle",
            addressRepository.fakeAddresses[0],
            16666666,
            "d6666666\n$loremIpsum",
            1666,
            16,
            16,
            16,
            listOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            LocalDate.of(2023, 9, 16),  // LocalDate.parse("2023-09-16"),
            null,
            listOf("hospital", "shop", "car_park"),
            agentRepository.fakeAgents[0].agentId
        ),
        Property(
            7,
            "Hostel",
            addressRepository.fakeAddresses[1],
            17777777,
            "d7777777\n$loremIpsum",
            1777,
            17,
            17,
            17,
            listOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            LocalDate.of(2023, 9, 17),  // LocalDate.parse("2023-09-17"),
            LocalDate.of(2023, 9, 30),  // LocalDate.parse("2023-09-17"),
            listOf("restaurant", "shop", "car_park"),
            agentRepository.fakeAgents[1].agentId
        ),
        Property(
            8,
            "t8888888",
            addressRepository.fakeAddresses[2],
            18888888,
            "d8888888\n$loremIpsum",
            1888,
            18,
            18,
            18,
            listOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            LocalDate.of(2023, 9, 18),  // LocalDate.parse("2023-09-18"),
            null,
            listOf("hospital", "restaurant", "shop", "car_park"),
            agentRepository.fakeAgents[2].agentId
        ),
        Property(
            9,
            "t9999999",
            addressRepository.fakeAddresses[0],
            19999999,
            "d9999999\n$loremIpsum",
            1999,
            19,
            19,
            19,
            /*
            listOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]),
            */
            null,
            LocalDate.of(2023, 9, 19),  // LocalDate.parse("2023-09-19"),
            null,
            listOf("hospital", "restaurant", "shop", "car_park"),
            agentRepository.fakeAgents[0].agentId
        )
    )
    //
}


