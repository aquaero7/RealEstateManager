package com.aquaero.realestatemanager.viewModel_test

import android.content.Context
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.NonEditField
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.repository.AddressRepository
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.CacheRepository
import com.aquaero.realestatemanager.repository.LocationRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.PoiRepository
import com.aquaero.realestatemanager.repository.PropertyPoiJoinRepository
import com.aquaero.realestatemanager.repository.PropertyRepository
import com.aquaero.realestatemanager.repository.TypeRepository
import com.aquaero.realestatemanager.utils.ConnectionState
import com.aquaero.realestatemanager.viewmodel.EditViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
//import org.mockito.Mockito.doReturn               // Choose 1A
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
//import org.mockito.kotlin.any                       // Choose 2B
import org.mockito.kotlin.doReturn                  // Choose 1B
import org.mockito.kotlin.eq
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.robolectric.RobolectricTestRunner
import kotlin.properties.Delegates

//@RunWith(MockitoJUnitRunner::class)
@RunWith(RobolectricTestRunner::class)
class EditViewModelTest {

    private lateinit var propertyRepository: PropertyRepository
    private lateinit var addressRepository: AddressRepository
    private lateinit var photoRepository: PhotoRepository
    private lateinit var agentRepository: AgentRepository
    private lateinit var typeRepository: TypeRepository
    private lateinit var poiRepository: PoiRepository
    private lateinit var propertyPoiJoinRepository: PropertyPoiJoinRepository
    private lateinit var locationRepository: LocationRepository
    private lateinit var cacheRepository: CacheRepository

    private lateinit var navController: NavHostController
    private lateinit var context: Context
    private var delayInMs by Delegates.notNull<Long>()

    //    private var lat by Delegates.notNull<Double>()                                                ///
//    private var lng by Delegates.notNull<Double>()                                                ///
//    private lateinit var latLng: LatLng                                                           ///
    private val lat: Double = 0.1
    private val lng: Double = 0.2
    private val latLng: LatLng = LatLng(lat, lng)

    private lateinit var cacheAddress: Address
    private lateinit var initialAddress: Address
    private lateinit var cacheProperty: Property

    private lateinit var address: Address
    private lateinit var stringType: String
    private lateinit var stringAgent: String

    private lateinit var viewModel: EditViewModel


    @Before
    fun setup() {
        propertyRepository = mock(PropertyRepository::class.java)
        addressRepository = mock(AddressRepository::class.java)
        photoRepository = mock(PhotoRepository::class.java)
        agentRepository = mock(AgentRepository::class.java)
        typeRepository = mock(TypeRepository::class.java)
        poiRepository = mock(PoiRepository::class.java)
        propertyPoiJoinRepository = mock(PropertyPoiJoinRepository::class.java)
        locationRepository = mock(LocationRepository::class.java)
        cacheRepository = mock(CacheRepository::class.java)

        navController = mock(NavHostController::class.java)
        context = mock(Context::class.java)

        cacheAddress = mock(Address::class.java)
        initialAddress = mock(Address::class.java)
        cacheProperty = mock(Property::class.java)

        address = mock(Address::class.java)
        stringType = "stringType"
        stringAgent = "stringAgent"

        delayInMs = 500L

        viewModel = EditViewModel(
            propertyRepository,
            addressRepository,
            photoRepository,
            agentRepository,
            typeRepository,
            poiRepository,
            propertyPoiJoinRepository,
            locationRepository,
            cacheRepository
        )

        runBlocking {
            // Init mocks for test updateAddressWithLatLng()
//            lat = 0.1                                                                             ///
//            lng = 0.2                                                                             ///
//            latLng = LatLng(lat, lng)                                                             ///
            doReturn(cacheAddress).`when`(cacheRepository).getCacheAddress()
            doReturn(cacheAddress).`when`(cacheAddress).replaceBlankValuesWithNull()
            doReturn(initialAddress).`when`(cacheRepository).getInitialAddress()
            doReturn(latLng).`when`(locationRepository)
                .getLocationFromAddress(eq(context), anyString(), anyBoolean())

            // Init mocks for test updateRoomWithAddress()
//            doReturn(cacheAddress).`when`(cacheRepository).getCacheAddress()
//            doReturn(cacheAddress).`when`(cacheAddress).replaceBlankValuesWithNull()
            doReturn(cacheAddress).`when`(cacheAddress).withoutId()
            doReturn(1L).`when`(addressRepository).upsertAddressInRoom(cacheAddress)

            // Init mocks for test upDateRoomWithProperty()
            doReturn(cacheProperty).`when`(cacheRepository).getCacheProperty()
            doReturn(cacheProperty).`when`(cacheProperty).replaceBlankValuesWithNull()
            doReturn(cacheProperty).`when`(cacheProperty).withoutId()

            // Init mocks for test upDateRoomWithPhotos()
            doReturn(1L).`when`(propertyRepository).upsertPropertyInRoom(cacheProperty)

            // Init mocks for test upDateRoomWithPropertyPoiJoins()
            doReturn(1L).`when`(cacheProperty).propertyId

            // Init mocks for test testItemData()
            doReturn(stringType).`when`(cacheProperty).typeId
            doReturn(stringType).`when`(typeRepository).stringType(anyString(), anyList(), anyList())
            doReturn(stringAgent).`when`(agentRepository).stringAgent(anyLong(), anyList(), anyList())
            doReturn(address).`when`(addressRepository).address(anyLong(), anyList())

        }
    }

    @Test
    fun testConnexionStatus() {
        val connection: ConnectionState = mock(ConnectionState::class.java)
        // Test unavailable connection
        doReturn(false).`when`(locationRepository).checkForConnection(connection)
        viewModel.connexionStatus(connection)
        assertFalse(viewModel.getInternetAvailability())
        // Test available connection
        doReturn(true).`when`(locationRepository).checkForConnection(connection)
        viewModel.connexionStatus(connection)
        assertTrue(viewModel.getInternetAvailability())
    }

    @Test
    fun testOnClickMenu() {
        // Also testing updateRoomWithCacheData() and clearCache()
        runBlocking {
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(cacheRepository).clearCache("")
        }
    }

    @Test
    fun testOnClickMenu_updateAddressWithLatLng() {
        runBlocking {
            /*
            val lat = 0.1
            val lng = 0.2
            val latLng = LatLng(lat, lng)

            // Mock the repository calls (moved to setup)
            doReturn(cacheAddress).`when`(cacheRepository).getCacheAddress()
            doReturn(cacheAddress).`when`(cacheAddress).replaceBlankValuesWithNull()
            doReturn(initialAddress).`when`(cacheRepository).getInitialAddress()
            doReturn(latLng).`when`(locationRepository).getLocationFromAddress(eq(context), anyString(), anyBoolean())
            */

            // Case 1: If isEmpty = true
            doReturn(true).`when`(cacheAddress).isNullOrBlank()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(cacheRepository).updateCacheAddress(null, null)  // Reached
            verify(cacheRepository, never()).updateCacheAddress(
                anyDouble(),
                anyDouble()
            )    // Not reached

            // Case 2a: If isEmpty = false and isModified = true
            doReturn(false).`when`(cacheAddress).isNullOrBlank()
            doReturn(null).`when`(cacheRepository).getInitialAddress()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(cacheRepository).updateCacheAddress(null, null)  // Not reached
            verify(cacheRepository).updateCacheAddress(lat, lng)    // Reached

            // Case 2b: If isEmpty = false and isModified = true
            doReturn(initialAddress).`when`(cacheRepository).getInitialAddress()
            doReturn(true).`when`(cacheAddress).hasDifferencesWith(initialAddress)
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(cacheRepository).updateCacheAddress(null, null)  // Not reached
            verify(cacheRepository, times(2)).updateCacheAddress(lat, lng)  // Reached

            // Case 3: If isEmpty = false and isModified = false and hasNullLatLng = true
            doReturn(false).`when`(cacheAddress).hasDifferencesWith(initialAddress)
            doReturn(null).`when`(cacheAddress).latitude
            doReturn(null).`when`(cacheAddress).longitude
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(cacheRepository).updateCacheAddress(null, null)  // Not reached
            verify(cacheRepository, times(3)).updateCacheAddress(lat, lng)  // Reached

            // Case 4: If isEmpty = false and isModified = false and hasNullLatLng = false
            doReturn(lat).`when`(cacheAddress).latitude
            doReturn(lng).`when`(cacheAddress).longitude
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(cacheRepository).updateCacheAddress(null, null)  // Not reached
            verify(cacheRepository, times(3)).updateCacheAddress(
                anyDouble(),
                anyDouble()
            )  // Not reached
        }
    }

    @Test
    fun testOnClickMenu_updateRoomWithAddress() {
        runBlocking {
            /*
            // Mock the repository calls (moved to setup)
            doReturn(cacheAddress).`when`(cacheRepository).getCacheAddress()
            doReturn(cacheAddress).`when`(cacheAddress).replaceBlankValuesWithNull()
            doReturn(cacheAddress).`when`(cacheAddress).withoutId()
            doReturn(1L).`when`(addressRepository).upsertAddressInRoom(cacheAddress)
            */

            // Case 1: If isEmpty = true and isUpdate = true
            doReturn(true).`when`(cacheAddress).isNullOrBlank()
            doReturn(null).`when`(cacheRepository).getInitialAddress()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(addressRepository, never()).deleteAddressFromRoom(cacheAddress)  // Not reached
            verify(cacheRepository, never()).updateCacheAddress(anyLong())  // Not reached
            verify(cacheRepository, never()).updateCachePropertyItem(
                NonEditField.ADDRESS_ID.name,
                null
            )    // Not reached
            verify(cacheRepository, never()).updateCachePropertyItem(
                NonEditField.ADDRESS_ID.name,
                eq(anyLong())
            )  // Not reached

            // Case 2: If isEmpty = true and isUpdate = false
            doReturn(initialAddress).`when`(cacheRepository).getInitialAddress()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(addressRepository).deleteAddressFromRoom(cacheAddress)   // Reached
            verify(cacheRepository, never()).updateCacheAddress(anyLong())  // Not reached
            verify(cacheRepository).updateCachePropertyItem(
                NonEditField.ADDRESS_ID.name,
                null
            ) // Reached
            verify(cacheRepository, never()).updateCachePropertyItem(
                NonEditField.ADDRESS_ID.name,
                eq(anyLong())
            )  // Not reached

            // Case 3a: If isEmpty = false and isUpdate = true
            doReturn(false).`when`(cacheAddress).isNullOrBlank()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(addressRepository).deleteAddressFromRoom(cacheAddress)   // Not reached
            verify(cacheRepository).updateCacheAddress(1L)  // Reached
            verify(cacheRepository).updateCachePropertyItem(
                NonEditField.ADDRESS_ID.name,
                null
            ) // Not reached
            verify(cacheRepository).updateCachePropertyItem(
                NonEditField.ADDRESS_ID.name,
                1L
            )  // Reached

            // Case 3b: If isEmpty = false and isUpdate = false
            doReturn(null).`when`(cacheRepository).getInitialAddress()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(addressRepository).deleteAddressFromRoom(cacheAddress)   // Not reached
            verify(cacheRepository, times(2)).updateCacheAddress(1L)  // Reached
            verify(cacheRepository).updateCachePropertyItem(
                NonEditField.ADDRESS_ID.name,
                null
            ) // Not reached
            verify(cacheRepository, times(2)).updateCachePropertyItem(
                NonEditField.ADDRESS_ID.name,
                1L
            )  // Reached
        }
    }

    @Test
    fun testOnClickMenu_upDateRoomWithProperty() {
        runBlocking {
            /*
            // Mock the repository calls (moved to setup)
            doReturn(cacheProperty).`when`(cacheRepository).getCacheProperty()
            doReturn(cacheProperty).`when`(cacheProperty).replaceBlankValuesWithNull()
            doReturn(cacheProperty).`when`(cacheProperty).withoutId()
            */

            // Case 1: If isNewProperty is false and newPropertyIdFromRoom <= 0
            doReturn(1L).`when`(cacheProperty).propertyId
            doReturn(0L).`when`(propertyRepository).upsertPropertyInRoom(cacheProperty)
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(cacheRepository, never()).updateCachePropertyItem(
                NonEditField.PROPERTY_ID.name,
                eq(anyLong())
            )   // Not reached
            verify(cacheRepository, never()).updateCacheItemPhotos(anyLong())  // Not reached

            // Case 2: If isNewProperty is true and newPropertyIdFromRoom <= 0
            doReturn(0L).`when`(cacheProperty).propertyId
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(cacheRepository, never()).updateCachePropertyItem(
                NonEditField.PROPERTY_ID.name,
                eq(anyLong())
            )   // Not reached
            verify(cacheRepository, never()).updateCacheItemPhotos(anyLong())  // Not reached

            // Case 3: If isNewProperty is false and newPropertyIdFromRoom > 0
            doReturn(1L).`when`(cacheProperty).propertyId
            doReturn(1L).`when`(propertyRepository).upsertPropertyInRoom(cacheProperty)
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(cacheRepository, never()).updateCachePropertyItem(
                NonEditField.PROPERTY_ID.name,
                eq(anyLong())
            )   // Not reached
            verify(cacheRepository, never()).updateCacheItemPhotos(anyLong())  // Not reached

            // Case 4: If isNewProperty is true and newPropertyIdFromRoom > 0
            doReturn(0L).`when`(cacheProperty).propertyId
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(cacheRepository).updateCachePropertyItem(
                NonEditField.PROPERTY_ID.name,
                1L
            )   // Reached
            verify(cacheRepository).updateCacheItemPhotos(1L)  // Reached
        }
    }

    @Test
    fun testOnClickMenu_upDateRoomWithPhotos() {
        runBlocking {
            val photo1 = Photo(1, "", null, 1)
            val photo2 = Photo(2, "", null, 1)
            val initialPhotos = mutableListOf(photo1, photo2)
            val cachePhotos = mutableListOf(photo1)

//            doReturn(1L).`when`(propertyRepository).upsertPropertyInRoom(cacheProperty)

            // Delete a photo
            doReturn(cachePhotos).`when`(cacheRepository).getCacheItemPhotos()
            doReturn(initialPhotos).`when`(cacheRepository).getInitialItemPhotos()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(photoRepository).deletePhotosFromRoom(mutableListOf(photo2))   // Reached
            verify(photoRepository, never()).upsertPhotosInRoom(initialPhotos)  // Not reached

            // Add a photo
            doReturn(initialPhotos).`when`(cacheRepository).getCacheItemPhotos()
            doReturn(cachePhotos).`when`(cacheRepository).getInitialItemPhotos()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(photoRepository).deletePhotosFromRoom(mutableListOf(photo2))   // Not reached
            verify(photoRepository).upsertPhotosInRoom(initialPhotos)  // Reached
        }
    }

    @Test
    fun testOnClickMenu_upDateRoomWithPropertyPoiJoins() {
        runBlocking {
            val poi1 = Poi("poi1")
            val poi2 = Poi("poi2")
            val initialPois = mutableListOf(poi1, poi2)
            val cachePois = mutableListOf(poi1)
            val propertyPoiJoinsModified = mutableListOf(PropertyPoiJoin(1L, "poi2"))

//            doReturn(1L).`when`(propertyRepository).upsertPropertyInRoom(cacheProperty)
//            doReturn(cacheProperty).`when`(cacheRepository).getCacheProperty()
//            doReturn(1L).`when`(cacheProperty).propertyId

            // Delete pois
            doReturn(cachePois).`when`(cacheRepository).getCacheItemPois()
            doReturn(initialPois).`when`(cacheRepository).getInitialItemPois()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(propertyPoiJoinRepository).deletePropertyPoiJoinsFromRoom(
                propertyPoiJoinsModified
            )   // Reached
            verify(propertyPoiJoinRepository, never()).upsertPropertyPoiJoinsInRoom(
                propertyPoiJoinsModified
            )  // Not reached

            // Add pois
            doReturn(initialPois).`when`(cacheRepository).getCacheItemPois()
            doReturn(cachePois).`when`(cacheRepository).getInitialItemPois()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(propertyPoiJoinRepository).deletePropertyPoiJoinsFromRoom(
                propertyPoiJoinsModified
            )   // Not reached
            verify(propertyPoiJoinRepository).upsertPropertyPoiJoinsInRoom(propertyPoiJoinsModified)  // Reached
        }
    }

    @Test
    fun testProperty() {
        // Also testing propertyFromId()
        val propertyId = 1L
        val property1: Property = mock(Property::class.java)
        val property2: Property = mock(Property::class.java)
        val properties = mutableListOf(property1, property2)

        doReturn(property1).`when`(propertyRepository).propertyFromId(propertyId, properties)
        assertEquals(property1, viewModel.property(propertyId, properties))
    }

    @Test
    fun testItemData() {
        // Also testing dataFromNullableProperty()

        /*
        val address = mock(Address::class.java)
        val stringType = "stringType"
        val stringAgent = "stringAgent"

        doReturn(stringType).`when`(cacheProperty).typeId

        doReturn(stringType).`when`(typeRepository).stringType(anyString(), anyList(), anyList())
        doReturn(stringAgent).`when`(agentRepository).stringAgent(anyLong(), anyList(), anyList())
        doReturn(address).`when`(addressRepository).address(anyLong(), anyList())
        */

        val result = viewModel.itemData(
            cacheProperty,
            mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf()
        )

        assertEquals(stringType, result.first)
        assertEquals(stringAgent, result.second)
        assertEquals(address, result.third)
    }

}