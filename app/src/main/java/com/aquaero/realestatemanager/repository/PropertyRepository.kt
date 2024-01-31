package com.aquaero.realestatemanager.repository

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.database.AppDatabase
import com.aquaero.realestatemanager.database.dao.PropertyDao
import com.aquaero.realestatemanager.model.Property
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PropertyRepository(private val propertyDao: PropertyDao) {

    private val context: Context by lazy { ApplicationRoot.getContext() }


    /** For fake data only */
    private val agentRepository = AgentRepository(AppDatabase.getInstance().agentDao)
    private val addressRepository = AddressRepository(AppDatabase.getInstance().addressDao)
    private val photoRepository = PhotoRepository(AppDatabase.getInstance().photoDao)
    private val fakePhotos = photoRepository.fakePhotos
    /***/


    /** Room: Database CRUD **/

    suspend fun upsertPropertyInRoom(property: Property) {
        withContext(IO) {
            propertyDao.upsertProperty(property)
        }
    }

    /*suspend*/ fun getPropertyFromRoom(pId: Long): Flow<Property> {
        /*
        return withContext(IO) {
            propertyDao.getProperty(pId)
        }
        */
        return propertyDao.getProperty(pId)
    }

    /*suspend*/ fun getPropertiesFromRoom(): Flow<MutableList<Property>> {
        /*
        return withContext(IO) {
            propertyDao.getProperties()
        }
        */
        return propertyDao.getProperties()
    }

    /*suspend*/ fun getPropertiesOrderedByIdFromRoom(): Flow<MutableList<Property>> {
        /*
        return withContext(IO) {
            propertyDao.getPropertiesOrderedById()
        }
        */
        return propertyDao.getPropertiesOrderedById()
    }

    /*suspend*/ fun getPropertiesOrderedByRegistrationDateFromRoom(): Flow<MutableList<Property>> {
        /*
        return withContext(IO) {
            propertyDao.getPropertiesOrderedByRegistrationDate()
        }
        */
        return propertyDao.getPropertiesOrderedByRegistrationDate()
    }

    /***/


    fun propertyFromId(properties: MutableList<Property>, propertyId: Long): Property {
//        return fakeProperties.find { it.propertyId == propertyId }!!
        return properties.first() { it.propertyId == propertyId }
    }

    /*
    fun thumbnailUrl(property: Property): String {
        val smMkAddress1 = property.addressId.toUrl()
        return SM_URL + SM_SIZE + SM_SCALE + SM_TYPE + SM_MK_COLOR1 + smMkAddress1 + SM_KEY
    }
    */
    /*
    fun thumbnailUrl(address: Address): String {
        val smMarkerAddress = address.toUrl()
        return SM_URL + SM_SIZE + SM_SCALE + SM_TYPE + SM_MARKER_COLOR + smMarkerAddress + SM_KEY
    }
    */

    // TODO: Add new property values to arguments
    fun updateProperty(propertyId: Comparable<*>) {
        // TODO: To be deleted
        Toast
            .makeText(
                context,
                "Click on ${context.getString(R.string.valid)}, for id $propertyId, from EditScreen",
                Toast.LENGTH_SHORT
            )
            .show()

        // TODO: Check if it is a creation or a modification of a property and update database

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
    val typesSet: MutableSet<Int> = mutableSetOf(
        R.string._unassigned_,
        R.string.flat, R.string.house, R.string.duplex, R.string.penthouse,
        R.string.loft, R.string.manor, R.string.castle, R.string.hostel
    )

    val poiSet: MutableSet<Int> = mutableSetOf(
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
            propertyId = -1,
//            typeId = R.string.flat,   // "House",
            typeId = "flat",
            // addressId = addressRepository.fakeAddresses[0],
            addressId = -1,
            price = 11111111,
            description = "d1111111\n$loremIpsum",
            surface = 1111,
            nbOfRooms = 11,
            nbOfBathrooms = 11,
            nbOfBedrooms = 11,
            /*
            photos = mutableListOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            */
//            registrationDate = LocalDate.of(2023, 9, 11),  // LocalDate.parse("2023-09-11"),
            registrationDate = "2023-09-11",  // LocalDate.parse("2023-09-11"),
            saleDate = null,
            // poi = mutableListOf("hospital", "restaurant", "shop", "car_park"),
            // poi = mutableListOf(POI.HOSPITAL.key, POI.RESTAURANT.key, POI.SHOP.key, POI.CAR_PARK.key),
            agentId = agentRepository.fakeAgents[0].agentId
        ),
        Property(
            propertyId = -2,
//            typeId = R.string.house,    // "Duplex",
            typeId = "house",
            // addressId = addressRepository.fakeAddresses[1],
            addressId = -2,
            price = 12222222,
            description = "d2222222\n$loremIpsum",
            surface = 1222,
            nbOfRooms = 12,
            nbOfBathrooms = 12,
            nbOfBedrooms = 12,
            /*
            photos = mutableListOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            */
//            registrationDate = LocalDate.of(2023, 9, 12),  // LocalDate.parse("2023-09-12"),
            registrationDate = "2023-09-12",  // LocalDate.parse("2023-09-12"),
            saleDate = null,
            // poi = mutableListOf("hospital", "restaurant", "shop", "car_park"),
            // poi = mutableListOf(POI.HOSPITAL.key, POI.RESTAURANT.key, POI.SHOP.key, POI.CAR_PARK.key),
            agentId = agentRepository.fakeAgents[1].agentId
        ),
        Property(
            propertyId = -3,
//            typeId = R.string.duplex,    // "Penthouse",
            typeId = "duplex",
            // addressId = addressRepository.fakeAddresses[2],
            addressId = -3,
            price = 13333333,
            description = "d3333333\n$loremIpsum",
            surface = 1333,
            nbOfRooms = 13,
            nbOfBathrooms = 13,
            nbOfBedrooms = 13,
            /*
            photos = mutableListOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            */
//            registrationDate = LocalDate.of(2023, 9, 13),  // LocalDate.parse("2023-09-13"),
            registrationDate = "2023-09-13",  // LocalDate.parse("2023-09-13"),
            saleDate = null,
            // poi = mutableListOf("hospital", "restaurant", "shop", "car_park"),
            // poi = mutableListOf(POI.HOSPITAL.key, POI.RESTAURANT.key, POI.SHOP.key, POI.CAR_PARK.key),
            agentId = agentRepository.fakeAgents[2].agentId
        ),
        Property(
            propertyId = -4,
//            typeId = R.string.penthouse,    // "Loft",
            typeId = "penthouse",
            // addressId = addressRepository.fakeAddresses[0],
            addressId = -1,
            price = 14444444,
            description = "d4444444\n$loremIpsum",
            surface = 1444,
            nbOfRooms = 14,
            nbOfBathrooms = 14,
            nbOfBedrooms = 14,
            /*
            photos = mutableListOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            */
//            registrationDate = LocalDate.of(2023, 9, 14),  // LocalDate.parse("2023-09-14"),
            registrationDate = "2023-09-14",  // LocalDate.parse("2023-09-14"),
            saleDate = null,
            // poi = mutableListOf("hospital", "restaurant", "shop", "car_park"),
            // poi = mutableListOf(POI.HOSPITAL.key, POI.RESTAURANT.key, POI.SHOP.key, POI.CAR_PARK.key),
            agentId = agentRepository.fakeAgents[0].agentId
        ),
        Property(
            propertyId = -5,
//            typeId = R.string.loft,    // "Manor",
            typeId = "loft",
            // addressId = addressRepository.fakeAddresses[1],
            addressId = -2,
            price = 15555555,
            description = "d5555555\n$loremIpsum",
            surface = 1555,
            nbOfRooms = 15,
            nbOfBathrooms = 15,
            nbOfBedrooms = 15,
            /*
            photos = mutableListOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            */
//            registrationDate = LocalDate.of(2023, 9, 15),  // LocalDate.parse("2023-09-15"),
            registrationDate = "2023-09-15",  // LocalDate.parse("2023-09-15"),
            saleDate = null,
            // poi = mutableListOf("hospital", "restaurant", "shop", "car_park"),
            // poi = mutableListOf(POI.HOSPITAL.key, POI.RESTAURANT.key, POI.SHOP.key, POI.CAR_PARK.key),
            agentId = agentRepository.fakeAgents[1].agentId
        ),
        Property(
            propertyId = -6,
//            typeId = R.string.manor,    // "Castle",
            typeId = "manor",
            // addressId = addressRepository.fakeAddresses[2],
            addressId = -3,
            price = 16666666,
            description = "d6666666\n$loremIpsum",
            surface = 1666,
            nbOfRooms = 16,
            nbOfBathrooms = 16,
            nbOfBedrooms = 16,
            /*
            photos = mutableListOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            */
//            registrationDate = LocalDate.of(2023, 9, 16),  // LocalDate.parse("2023-09-16"),
            registrationDate = "2023-09-16",  // LocalDate.parse("2023-09-16"),
            saleDate = null,
            // poi = mutableListOf("hospital", "shop", "car_park"),
            // poi = mutableListOf(POI.HOSPITAL.key, POI.SHOP.key, POI.CAR_PARK.key),
            agentId = agentRepository.fakeAgents[2].agentId
        ),
        Property(
            propertyId = -7,
//            typeId = R.string.castle,    // "Hostel",
            typeId = "castle",
            // addressId = addressRepository.fakeAddresses[0],
            addressId = -1,
            price = 17777777,
            description = "d7777777\n$loremIpsum",
            surface = 1777,
            nbOfRooms = 17,
            nbOfBathrooms = 17,
            nbOfBedrooms = 17,
            /*
            photos = mutableListOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            */
//            registrationDate = LocalDate.of(2023, 9, 17),  // LocalDate.parse("2023-09-17"),
            registrationDate = "2023-09-17",  // LocalDate.parse("2023-09-17"),
//            saleDate = LocalDate.of(2023, 9, 30),  // LocalDate.parse("2023-09-17"),
            saleDate = "2023-09-17",  // LocalDate.parse("2023-09-17"),
            // poi = mutableListOf("restaurant", "shop", "car_park"),
            // poi = mutableListOf(POI.RESTAURANT.key, POI.SHOP.key, POI.CAR_PARK.key),
            agentId = agentRepository.fakeAgents[0].agentId
        ),
        Property(
            propertyId = -8,
//            typeId = R.string.hostel,    // "t8888888",
            typeId = "hostel",
            // addressId = addressRepository.fakeAddresses[1],
            addressId = -2,
            price = 18888888,
            description = "d8888888\n$loremIpsum",
            surface = 1888,
            nbOfRooms = 18,
            nbOfBathrooms = 18,
            nbOfBedrooms = 18,
            /*
            photos = mutableListOf(
                fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
                fakePhotos[5], fakePhotos[6], fakePhotos[7]
            ),
            */
//            registrationDate = LocalDate.of(2023, 9, 18),  // LocalDate.parse("2023-09-18"),
            registrationDate = "2023-09-18",  // LocalDate.parse("2023-09-18"),
            saleDate = null,
            // poi = mutableListOf("hospital", "restaurant", "shop", "car_park"),
            // poi = mutableListOf(POI.HOSPITAL.key, POI.RESTAURANT.key, POI.SHOP.key, POI.CAR_PARK.key),
            agentId = agentRepository.fakeAgents[1].agentId
        ),
        Property(
            propertyId = -9,
//            typeId = R.string.flat,   // "t9999999",
            typeId = "unassigned",
            // addressId = addressRepository.fakeAddresses[2],
            addressId = -3,
            price = 19999999,
            description = "d9999999\n$loremIpsum",
            surface = 1999,
            nbOfRooms = 19,
            nbOfBathrooms = 19,
            nbOfBedrooms = 19,
            // photos = mutableListOf(NO_PHOTO),
//            registrationDate = LocalDate.of(2023, 9, 19),  // LocalDate.parse("2023-09-19"),
            registrationDate = "2023-09-19",  // LocalDate.parse("2023-09-19"),
            saleDate = null,
            // poi = mutableListOf("hospital", "restaurant", "shop", "car_park"),
            // poi = mutableListOf(POI.HOSPITAL.key, POI.RESTAURANT.key, POI.SHOP.key, POI.CAR_PARK.key),
            agentId = agentRepository.fakeAgents[2].agentId
        ),

    )
    //
}

