package com.aquaero.realestatemanager.repository

import android.annotation.SuppressLint
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.SM_KEY
import com.aquaero.realestatemanager.SM_MK_COLOR1
import com.aquaero.realestatemanager.SM_SCALE
import com.aquaero.realestatemanager.SM_SIZE
import com.aquaero.realestatemanager.SM_TYPE
import com.aquaero.realestatemanager.SM_URL
import com.aquaero.realestatemanager.model.Property
import java.time.LocalDate

class PropertyRepository() {

    private val agentRepository = AgentRepository()
    private val addressRepository = AddressRepository()
    private val photoRepository = PhotoRepository()
    private val fakePhotos = photoRepository.fakePhotos


    fun propertyFromId(propertyId: Long): Property {
        return fakeProperties.find { it.pId == propertyId }!!
    }

    fun thumbnailUrl(property: Property): String {
        val smMkAddress1 = property.pAddress.toUrl()
        return SM_URL + SM_SIZE + SM_SCALE + SM_TYPE + SM_MK_COLOR1 + smMkAddress1 + SM_KEY
    }




    // ...





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
            "t0000000",
            addressRepository.fakeAddresses[0],
            10000000,
            "d0000000\n$loremIpsum",
            1000,
            10,
            10,
            10,
            listOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]),
            LocalDate.of(2023, 9, 10),  // LocalDate.parse("2023-09-10"),
            null,
            listOf("hospital", "restaurant", "shop", "car_park"),
            agentRepository.fakeAgents[0]
        ),
        Property(
            1,
            "t1111111",
            addressRepository.fakeAddresses[1],
            11111111,
            "d1111111\n$loremIpsum",
            1111,
            11,
            11,
            11,
            listOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]),
            LocalDate.of(2023, 9, 11),  // LocalDate.parse("2023-09-11"),
            null,
            listOf("hospital", "restaurant", "shop", "car_park"),
            agentRepository.fakeAgents[1]
        ),
        Property(
            2,
            "t2222222",
            addressRepository.fakeAddresses[2],
            12222222,
            "d2222222\n$loremIpsum",
            1222,
            12,
            12,
            12,
            listOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]),
            LocalDate.of(2023, 9, 12),  // LocalDate.parse("2023-09-12"),
            null,
            listOf("hospital", "restaurant", "shop", "car_park"),
            agentRepository.fakeAgents[2]
        ),
        Property(
            3,
            "t3333333",
            addressRepository.fakeAddresses[0],
            13333333,
            "d3333333\n$loremIpsum",
            1333,
            13,
            13,
            13,
            listOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]),
            LocalDate.of(2023, 9, 13),  // LocalDate.parse("2023-09-13"),
            null,
            listOf("hospital", "restaurant", "shop", "car_park"),
            agentRepository.fakeAgents[0]
        ),
        Property(
            4,
            "t4444444",
            addressRepository.fakeAddresses[1],
            14444444,
            "d4444444\n$loremIpsum",
            1444,
            14,
            14,
            14,
            listOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]),
            LocalDate.of(2023, 9, 14),  // LocalDate.parse("2023-09-14"),
            null,
            listOf("hospital", "restaurant", "shop", "car_park"),
            agentRepository.fakeAgents[1]
        ),
        Property(
            5,
            "t5555555",
            addressRepository.fakeAddresses[2],
            15555555,
            "d5555555\n$loremIpsum",
            1555,
            15,
            15,
            15,
            listOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]),
            LocalDate.of(2023, 9, 15),  // LocalDate.parse("2023-09-15"),
            null,
            listOf("hospital", "restaurant", "shop", "car_park"),
            agentRepository.fakeAgents[2]
        ),
        Property(
            6,
            "t6666666",
            addressRepository.fakeAddresses[0],
            16666666,
            "d6666666\n$loremIpsum",
            1666,
            16,
            16,
            16,
            listOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]),
            LocalDate.of(2023, 9, 16),  // LocalDate.parse("2023-09-16"),
            null,
            listOf("hospital", "shop", "car_park"),
            agentRepository.fakeAgents[0]
        ),
        Property(
            7,
            "t7777777",
            addressRepository.fakeAddresses[1],
            17777777,
            "d7777777\n$loremIpsum",
            1777,
            17,
            17,
            17,
            listOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]),
            LocalDate.of(2023, 9, 17),  // LocalDate.parse("2023-09-17"),
            LocalDate.of(2023, 9, 30),  // LocalDate.parse("2023-09-17"),
            listOf("restaurant", "shop", "car_park"),
            agentRepository.fakeAgents[1]
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
                fakePhotos[5], fakePhotos[6], fakePhotos[7]),
            LocalDate.of(2023, 9, 18),  // LocalDate.parse("2023-09-18"),
            null,
            listOf("hospital", "restaurant", "shop", "car_park"),
            agentRepository.fakeAgents[2]
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
            listOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]),
            LocalDate.of(2023, 9, 19),  // LocalDate.parse("2023-09-19"),
            null,
            listOf("hospital", "restaurant", "shop", "car_park"),
            agentRepository.fakeAgents[0]
        )
    )
    //
}


