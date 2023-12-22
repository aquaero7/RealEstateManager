package com.aquaero.realestatemanager.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.NO_PHOTO
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.SM_KEY
import com.aquaero.realestatemanager.SM_MK_COLOR1
import com.aquaero.realestatemanager.SM_SCALE
import com.aquaero.realestatemanager.SM_SIZE
import com.aquaero.realestatemanager.SM_TYPE
import com.aquaero.realestatemanager.SM_URL
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.utils.convertEuroToDollar
import kotlinx.coroutines.flow.asFlow
import java.time.LocalDate

class PropertyRepository() {

    private val context: Context by lazy { ApplicationRoot.getContext() }


    /** For fake data only */
    private val agentRepository = AgentRepository()
    private val addressRepository = AddressRepository()
    private val photoRepository = PhotoRepository()
    private val fakePhotos = photoRepository.fakePhotos
    /***/


    fun propertyFromId(propertyId: Long): Property {
        return fakeProperties.find { it.pId == propertyId }!!
    }

    fun thumbnailUrl(property: Property): String {
        val smMkAddress1 = property.pAddress.toUrl()
        return SM_URL + SM_SIZE + SM_SCALE + SM_TYPE + SM_MK_COLOR1 + smMkAddress1 + SM_KEY
    }


    /** Database update */
    fun updateProperty(propertyId: Comparable<*>) {
        // TODO: To be deleted
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
    /***/


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
    /***/


    //
    /**
     * FAKE PROPERTIES
     */

    private val loremIpsum: String = LoremIpsum(300).values.first()

    @SuppressLint("NewApi")
    val fakeProperties = listOf(
        Property(
            pId = 0,
            pType = R.string.flat,   // "Flat",
            pAddress = addressRepository.fakeAddresses[0],
            pPrice = 10000000,
            description = "d0000000\n$loremIpsum",
            surface = 1000,
            nbOfRooms = 10,
            nbOfBathrooms = 10,
            nbOfBedrooms = 10,
            photos = mutableListOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            registrationDate = LocalDate.of(2023, 9, 10),  // LocalDate.parse("2023-09-10"),
            saleDate = null,
            pPoi = mutableListOf("hospital", "restaurant", "shop", "car_park"),
            agentId = agentRepository.fakeAgents[0].agentId
        ),
        Property(
            pId = 1,
            pType = R.string.house,   // "House",
            pAddress = addressRepository.fakeAddresses[1],
            pPrice = 11111111,
            description = "d1111111\n$loremIpsum",
            surface = 1111,
            nbOfRooms = 11,
            nbOfBathrooms = 11,
            nbOfBedrooms = 11,
            photos = mutableListOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            registrationDate = LocalDate.of(2023, 9, 11),  // LocalDate.parse("2023-09-11"),
            saleDate = null,
            pPoi = mutableListOf("hospital", "restaurant", "shop", "car_park"),
            agentId = agentRepository.fakeAgents[1].agentId
        ),
        Property(
            pId = 2,
            pType = R.string.duplex,    // "Duplex",
            pAddress = addressRepository.fakeAddresses[2],
            pPrice = 12222222,
            description = "d2222222\n$loremIpsum",
            surface = 1222,
            nbOfRooms = 12,
            nbOfBathrooms = 12,
            nbOfBedrooms = 12,
            photos = mutableListOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            registrationDate = LocalDate.of(2023, 9, 12),  // LocalDate.parse("2023-09-12"),
            saleDate = null,
            pPoi = mutableListOf("hospital", "restaurant", "shop", "car_park"),
            agentId = agentRepository.fakeAgents[2].agentId
        ),
        Property(
            pId = 3,
            pType = R.string.penthouse,    // "Penthouse",
            pAddress = addressRepository.fakeAddresses[0],
            pPrice = 13333333,
            description = "d3333333\n$loremIpsum",
            surface = 1333,
            nbOfRooms = 13,
            nbOfBathrooms = 13,
            nbOfBedrooms = 13,
            photos = mutableListOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            registrationDate = LocalDate.of(2023, 9, 13),  // LocalDate.parse("2023-09-13"),
            saleDate = null,
            pPoi = mutableListOf("hospital", "restaurant", "shop", "car_park"),
            agentId = agentRepository.fakeAgents[0].agentId
        ),
        Property(
            pId = 4,
            pType = R.string.loft,    // "Loft",
            pAddress = addressRepository.fakeAddresses[1],
            pPrice = 14444444,
            description = "d4444444\n$loremIpsum",
            surface = 1444,
            nbOfRooms = 14,
            nbOfBathrooms = 14,
            nbOfBedrooms = 14,
            photos = mutableListOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            registrationDate = LocalDate.of(2023, 9, 14),  // LocalDate.parse("2023-09-14"),
            saleDate = null,
            pPoi = mutableListOf("hospital", "restaurant", "shop", "car_park"),
            agentId = agentRepository.fakeAgents[1].agentId
        ),
        Property(
            pId = 5,
            pType = R.string.manor,    // "Manor",
            pAddress = addressRepository.fakeAddresses[2],
            pPrice = 15555555,
            description = "d5555555\n$loremIpsum",
            surface = 1555,
            nbOfRooms = 15,
            nbOfBathrooms = 15,
            nbOfBedrooms = 15,
            photos = mutableListOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            registrationDate = LocalDate.of(2023, 9, 15),  // LocalDate.parse("2023-09-15"),
            saleDate = null,
            pPoi = mutableListOf("hospital", "restaurant", "shop", "car_park"),
            agentId = agentRepository.fakeAgents[2].agentId
        ),
        Property(
            pId = 6,
            pType = R.string.castle,    // "Castle",
            pAddress = addressRepository.fakeAddresses[0],
            pPrice = 16666666,
            description = "d6666666\n$loremIpsum",
            surface = 1666,
            nbOfRooms = 16,
            nbOfBathrooms = 16,
            nbOfBedrooms = 16,
            photos = mutableListOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            registrationDate = LocalDate.of(2023, 9, 16),  // LocalDate.parse("2023-09-16"),
            saleDate = null,
            pPoi = mutableListOf("hospital", "shop", "car_park"),
            agentId = agentRepository.fakeAgents[0].agentId
        ),
        Property(
            pId = 7,
            pType = R.string.hostel,    // "Hostel",
            pAddress = addressRepository.fakeAddresses[1],
            pPrice = 17777777,
            description = "d7777777\n$loremIpsum",
            surface = 1777,
            nbOfRooms = 17,
            nbOfBathrooms = 17,
            nbOfBedrooms = 17,
            photos = mutableListOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            registrationDate = LocalDate.of(2023, 9, 17),  // LocalDate.parse("2023-09-17"),
            saleDate = LocalDate.of(2023, 9, 30),  // LocalDate.parse("2023-09-17"),
            pPoi = mutableListOf("restaurant", "shop", "car_park"),
            agentId = agentRepository.fakeAgents[1].agentId
        ),
        Property(
            pId = 8,
            pType = R.string.flat,    // "t8888888",
            pAddress = addressRepository.fakeAddresses[2],
            pPrice = 18888888,
            description = "d8888888\n$loremIpsum",
            surface = 1888,
            nbOfRooms = 18,
            nbOfBathrooms = 18,
            nbOfBedrooms = 18,
            photos = mutableListOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            registrationDate = LocalDate.of(2023, 9, 18),  // LocalDate.parse("2023-09-18"),
            saleDate = null,
            pPoi = mutableListOf("hospital", "restaurant", "shop", "car_park"),
            agentId = agentRepository.fakeAgents[2].agentId
        ),
        Property(
            pId = 9,
            pType = R.string.house,   // "t9999999",
            pAddress = addressRepository.fakeAddresses[0],
            pPrice = 19999999,
            description = "d9999999\n$loremIpsum",
            surface = 1999,
            nbOfRooms = 19,
            nbOfBathrooms = 19,
            nbOfBedrooms = 19,
            // photos = mutableListOf(NO_PHOTO),
            registrationDate = LocalDate.of(2023, 9, 19),  // LocalDate.parse("2023-09-19"),
            saleDate = null,
            pPoi = mutableListOf("hospital", "restaurant", "shop", "car_park"),
            agentId = agentRepository.fakeAgents[0].agentId
        )
    )
    //
}

