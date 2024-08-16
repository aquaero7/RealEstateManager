package com.aquaero.realestatemanager.viewModel_test

import android.content.Context
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.NonEditField
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.repository.AddressRepository
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.CacheRepository
import com.aquaero.realestatemanager.repository.LocationRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.PoiRepository
import com.aquaero.realestatemanager.repository.PropertyPoiJoinRepository
import com.aquaero.realestatemanager.repository.PropertyRepository
import com.aquaero.realestatemanager.repository.TypeRepository
import com.aquaero.realestatemanager.utils.AndroidLogger
import com.aquaero.realestatemanager.utils.Logger
import com.aquaero.realestatemanager.viewmodel.EditViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.lenient
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.never
import org.mockito.kotlin.reset
import kotlin.properties.Delegates

@RunWith(MockitoJUnitRunner::class)
//@RunWith(RobolectricTestRunner::class)
class EditViewModelTestPart1 {
    private lateinit var propertyRepository: PropertyRepository
    private lateinit var addressRepository: AddressRepository
    private lateinit var photoRepository: PhotoRepository
    private lateinit var agentRepository: AgentRepository
    private lateinit var typeRepository: TypeRepository
    private lateinit var poiRepository: PoiRepository
    private lateinit var propertyPoiJoinRepository: PropertyPoiJoinRepository
    private lateinit var locationRepository: LocationRepository
    private lateinit var cacheRepository: CacheRepository
    private lateinit var logger: AndroidLogger

    private lateinit var cacheAddress: Address
    private lateinit var initialAddress: Address
    private lateinit var cacheProperty: Property
    private lateinit var photo1: Photo
    private lateinit var photo2: Photo
    private lateinit var poiId: String
    private lateinit var poi1: Poi
    private lateinit var poi2: Poi

    private val lat: Double = 0.1
    private val lng: Double = 0.2
    private val latLng: LatLng = LatLng(lat, lng)
    private val propertyId: Long = 1L
    private val addressId: Long = 2L

    private var delayInMs by Delegates.notNull<Long>()

    private lateinit var navController: NavHostController
    private lateinit var context: Context
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
        logger = mock(AndroidLogger::class.java)

        cacheAddress = mock(Address::class.java)
        initialAddress = mock(Address::class.java)
        cacheProperty = mock(Property::class.java)
        poi1 = mock(Poi::class.java)
        poi2 = mock(Poi::class.java)

        photo1 = Photo(1L, "", null, 1L)
        photo2 = Photo(2L, "", null, 1L)
        poiId = "poiId"

        delayInMs = 500L

        navController = mock(NavHostController::class.java)
        context = mock(Context::class.java)

        viewModel = EditViewModel(
            propertyRepository,
            addressRepository,
            photoRepository,
            agentRepository,
            typeRepository,
            poiRepository,
            propertyPoiJoinRepository,
            locationRepository,
            cacheRepository,
            logger
        )

        runBlocking {
            // Init mocks for test updateAddressWithLatLng()
            doReturn(cacheAddress).`when`(cacheRepository).getCacheAddress()
            doReturn(cacheAddress).`when`(cacheAddress).replaceBlankValuesWithNull()
            doReturn(initialAddress).`when`(cacheRepository).getInitialAddress()
            doReturn(latLng).`when`(locationRepository)
                .getLocationFromAddress(eq(context), anyString(), anyBoolean())

            // Init some more mocks for test updateRoomWithAddress()
            doReturn(cacheAddress).`when`(cacheAddress).withoutId()
            doReturn(addressId).`when`(addressRepository).upsertAddressInRoom(cacheAddress)

            // Init some more mocks for test upDateRoomWithProperty()
            doReturn(cacheProperty).`when`(cacheRepository).getCacheProperty()
            doReturn(cacheProperty).`when`(cacheProperty).replaceBlankValuesWithNull()
            doReturn(cacheProperty).`when`(cacheProperty).withoutId()

            // Init some more mocks for test upDateRoomWithPhotos()
            doReturn(propertyId).`when`(propertyRepository).upsertPropertyInRoom(cacheProperty)

            // Init some more mocks for test upDateRoomWithPropertyPoiJoins()
            doReturn(propertyId).`when`(cacheProperty).propertyId
            doReturn(poiId).`when`(poi2).poiId
        }
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
            // Case 1: isEmpty = true
            doReturn(true).`when`(cacheAddress).isNullOrBlank()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(cacheRepository).updateCacheAddress(null, null)
            verify(cacheRepository, never()).updateCacheAddress(anyDouble(), anyDouble())

            // Reset mocks to avoid interference between inner test parts
            reset(cacheRepository)
            doReturn(cacheAddress).`when`(cacheRepository).getCacheAddress()
            doReturn(cacheProperty).`when`(cacheRepository).getCacheProperty()

            // Case 2a: isEmpty = false and isModified = true
            doReturn(false).`when`(cacheAddress).isNullOrBlank()
            doReturn(null).`when`(cacheRepository).getInitialAddress()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(cacheRepository, never()).updateCacheAddress(null, null)
            verify(cacheRepository).updateCacheAddress(lat, lng)

            // Reset mocks to avoid interference between inner test parts
            reset(cacheRepository)
            doReturn(cacheAddress).`when`(cacheRepository).getCacheAddress()
            doReturn(cacheProperty).`when`(cacheRepository).getCacheProperty()

            // Case 2b: isEmpty = false and isModified = true
            doReturn(initialAddress).`when`(cacheRepository).getInitialAddress()
            doReturn(true).`when`(cacheAddress).hasDifferencesWith(initialAddress)
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(cacheRepository, never()).updateCacheAddress(null, null)
            verify(cacheRepository).updateCacheAddress(lat, lng)

            // Reset mocks to avoid interference between inner test parts
            reset(cacheRepository)
            doReturn(initialAddress).`when`(cacheRepository).getInitialAddress()
            doReturn(cacheAddress).`when`(cacheRepository).getCacheAddress()
            doReturn(cacheProperty).`when`(cacheRepository).getCacheProperty()

            // Case 3: isEmpty = false and isModified = false and hasNullLatLng = true
            doReturn(false).`when`(cacheAddress).hasDifferencesWith(initialAddress)
            doReturn(null).`when`(cacheAddress).latitude
            lenient().doReturn(null).`when`(cacheAddress).longitude
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(cacheRepository, never()).updateCacheAddress(null, null)
            verify(cacheRepository).updateCacheAddress(lat, lng)

            // Reset mocks to avoid interference between inner test parts
            reset(cacheRepository)
            doReturn(initialAddress).`when`(cacheRepository).getInitialAddress()
            doReturn(cacheAddress).`when`(cacheRepository).getCacheAddress()
            doReturn(cacheProperty).`when`(cacheRepository).getCacheProperty()

            // Case 4: isEmpty = false and isModified = false and hasNullLatLng = false
            doReturn(lat).`when`(cacheAddress).latitude
            doReturn(lng).`when`(cacheAddress).longitude
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(cacheRepository, never()).updateCacheAddress(null, null)
            verify(cacheRepository, never()).updateCacheAddress(anyDouble(), anyDouble())
        }
    }

    @Test
    fun testOnClickMenu_updateRoomWithAddress() {
        runBlocking {
            // Case 1: isEmpty = true and isUpdate = true
            doReturn(true).`when`(cacheAddress).isNullOrBlank()
            doReturn(null).`when`(cacheRepository).getInitialAddress()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(addressRepository, never()).deleteAddressFromRoom(cacheAddress)
            verify(cacheRepository, never()).updateCacheAddress(anyLong())
            verify(cacheRepository, never()).updateCachePropertyItem(NonEditField.ADDRESS_ID.name, null)
            verify(cacheRepository, never()).updateCachePropertyItem(NonEditField.ADDRESS_ID.name, eq(anyLong()))

            // Case 2: isEmpty = true and isUpdate = false
            doReturn(initialAddress).`when`(cacheRepository).getInitialAddress()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(addressRepository).deleteAddressFromRoom(cacheAddress)
            verify(cacheRepository, never()).updateCacheAddress(anyLong())
            verify(cacheRepository).updateCachePropertyItem(NonEditField.ADDRESS_ID.name, null)
            verify(cacheRepository, never()).updateCachePropertyItem(NonEditField.ADDRESS_ID.name, eq(anyLong()))

            // Reset mocks to avoid interference between inner test parts
            reset(addressRepository, cacheRepository)
            doReturn(cacheAddress).`when`(cacheRepository).getCacheAddress()
            doReturn(addressId).`when`(addressRepository).upsertAddressInRoom(cacheAddress)

            // Case 3a: isEmpty = false and isUpdate = true
            doReturn(false).`when`(cacheAddress).isNullOrBlank()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(addressRepository, never()).deleteAddressFromRoom(cacheAddress)
            verify(cacheRepository).updateCacheAddress(addressId)
            verify(cacheRepository, never()).updateCachePropertyItem(NonEditField.ADDRESS_ID.name, null)
            verify(cacheRepository).updateCachePropertyItem(NonEditField.ADDRESS_ID.name, addressId)

            // Reset mocks to avoid interference between inner test parts
            reset(addressRepository, cacheRepository)
            doReturn(cacheAddress).`when`(cacheRepository).getCacheAddress()
            doReturn(addressId).`when`(addressRepository).upsertAddressInRoom(cacheAddress)

            // Case 3b: isEmpty = false and isUpdate = false
            doReturn(null).`when`(cacheRepository).getInitialAddress()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(addressRepository, never()).deleteAddressFromRoom(cacheAddress)
            verify(cacheRepository).updateCacheAddress(addressId)
            verify(cacheRepository, never()).updateCachePropertyItem(NonEditField.ADDRESS_ID.name, null)
            verify(cacheRepository).updateCachePropertyItem(NonEditField.ADDRESS_ID.name, addressId)
        }
    }

    @Test
    fun testOnClickMenu_upDateRoomWithProperty() {
        runBlocking {
            // Case 1: isNewProperty is false and newPropertyIdFromRoom <= 0
            doReturn(1L).`when`(cacheProperty).propertyId
            doReturn(0L).`when`(propertyRepository).upsertPropertyInRoom(cacheProperty)
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(cacheRepository, never()).updateCachePropertyItem(NonEditField.PROPERTY_ID.name, eq(anyLong()))
            verify(cacheRepository, never()).updateCacheItemPhotos(anyLong())

            // Case 2: isNewProperty is true and newPropertyIdFromRoom <= 0
            doReturn(0L).`when`(cacheProperty).propertyId
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(cacheRepository, never()).updateCachePropertyItem(NonEditField.PROPERTY_ID.name, eq(anyLong()))
            verify(cacheRepository, never()).updateCacheItemPhotos(anyLong())

            // Case 3: isNewProperty is false and newPropertyIdFromRoom > 0
            doReturn(1L).`when`(cacheProperty).propertyId
            doReturn(1L).`when`(propertyRepository).upsertPropertyInRoom(cacheProperty)
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(cacheRepository, never()).updateCachePropertyItem(NonEditField.PROPERTY_ID.name, eq(anyLong()))
            verify(cacheRepository, never()).updateCacheItemPhotos(anyLong())

            // Reset mocks to avoid interference between inner test parts
            reset(cacheRepository)
            doReturn(cacheAddress).`when`(cacheRepository).getCacheAddress()
            doReturn(cacheProperty).`when`(cacheRepository).getCacheProperty()

            // Case 4: isNewProperty is true and newPropertyIdFromRoom > 0
            doReturn(0L).`when`(cacheProperty).propertyId
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(cacheRepository).updateCachePropertyItem(NonEditField.PROPERTY_ID.name, 1L)
            verify(cacheRepository).updateCacheItemPhotos(1L)
        }
    }

    @Test
    fun testOnClickMenu_upDateRoomWithPhotos() {
        runBlocking {
            val initialPhotos = mutableListOf(photo1, photo2)
            val cachePhotos = mutableListOf(photo1)

            // Delete a photo
            doReturn(cachePhotos).`when`(cacheRepository).getCacheItemPhotos()
            doReturn(initialPhotos).`when`(cacheRepository).getInitialItemPhotos()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(photoRepository).deletePhotosFromRoom(mutableListOf(photo2))
            verify(photoRepository, never()).upsertPhotosInRoom(initialPhotos)

            // Reset mock to avoid interference between inner test parts
            reset(photoRepository)

            // Add a photo
            doReturn(initialPhotos).`when`(cacheRepository).getCacheItemPhotos()
            doReturn(cachePhotos).`when`(cacheRepository).getInitialItemPhotos()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(photoRepository, never()).deletePhotosFromRoom(mutableListOf(photo2))
            verify(photoRepository).upsertPhotosInRoom(initialPhotos)
        }
    }

    @Test
    fun testOnClickMenu_upDateRoomWithPropertyPoiJoins() {
        runBlocking {
            val initialPois = mutableListOf(poi1, poi2)
            val cachePois = mutableListOf(poi1)
            val propertyPoiJoinsModified = mutableListOf(PropertyPoiJoin(propertyId, poiId))

            // Delete pois
            doReturn(cachePois).`when`(cacheRepository).getCacheItemPois()
            doReturn(initialPois).`when`(cacheRepository).getInitialItemPois()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(propertyPoiJoinRepository).deletePropertyPoiJoinsFromRoom(propertyPoiJoinsModified)
            verify(propertyPoiJoinRepository, never()).upsertPropertyPoiJoinsInRoom(propertyPoiJoinsModified)

            // Reset mock to avoid interference between inner test parts
            reset(propertyPoiJoinRepository)

            // Add pois
            doReturn(initialPois).`when`(cacheRepository).getCacheItemPois()
            doReturn(cachePois).`when`(cacheRepository).getInitialItemPois()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(propertyPoiJoinRepository, never()).deletePropertyPoiJoinsFromRoom(propertyPoiJoinsModified)
            verify(propertyPoiJoinRepository).upsertPropertyPoiJoinsInRoom(propertyPoiJoinsModified)
        }
    }

}