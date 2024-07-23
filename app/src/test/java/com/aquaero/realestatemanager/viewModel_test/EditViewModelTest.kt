package com.aquaero.realestatemanager.viewModel_test

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.EditField
import com.aquaero.realestatemanager.NonEditField
import com.aquaero.realestatemanager.RATE_OF_DOLLAR_IN_EURO
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
//import org.mockito.Mockito.doReturn               // Choose 1A
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
//import org.mockito.kotlin.any                       // Choose 2B
import org.mockito.kotlin.doReturn                  // Choose 1B
import org.mockito.kotlin.eq
import org.mockito.kotlin.isNotNull
import org.mockito.kotlin.isNull
import org.mockito.kotlin.never
import org.mockito.kotlin.reset
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyNoMoreInteractions
import org.robolectric.RobolectricTestRunner
import kotlin.math.roundToInt
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

    private lateinit var cacheAddress: Address
    private lateinit var initialAddress: Address
    private lateinit var cacheProperty: Property
    private lateinit var address: Address
    private lateinit var stringType: String
    private lateinit var stringAgent: String
    private lateinit var property1: Property
    private lateinit var property2: Property
    private lateinit var photo: Photo
    private lateinit var photo1: Photo
    private lateinit var photo2: Photo
    private lateinit var poiId: String
    private lateinit var poi1: Poi
    private lateinit var poi2: Poi
    private lateinit var toast: Toast
    private lateinit var typeId: String
    private lateinit var type: Type
    private lateinit var agent: Agent
    private lateinit var unassigned: String
    private lateinit var poiItem: String
    private lateinit var uri: Uri
    private lateinit var stringUri: String
    private lateinit var label: String

    private val lat: Double = 0.1
    private val lng: Double = 0.2
    private val latLng: LatLng = LatLng(lat, lng)
    private val propertyId: Long = 1L
    private val addressId: Long = 2L
    private val agentId: Long = 2L
    private val photoId: Long = 2L

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

        cacheAddress = mock(Address::class.java)
        initialAddress = mock(Address::class.java)
        cacheProperty = mock(Property::class.java)
        address = mock(Address::class.java)
        property1 = mock(Property::class.java)
        property2 = mock(Property::class.java)
        poi1 = mock(Poi::class.java)
        poi2 = mock(Poi::class.java)
        toast = mock(Toast::class.java)
        uri = mock(Uri::class.java)

        stringType = "stringType"
        stringAgent = "stringAgent"
        stringUri = "stringUri"
        label = "label"
        photo = Photo(1L, uri.toString(), label, 1L)
        photo1 = Photo(1L, "", null, 1L)
        photo2 = Photo(2L, "", null, 1L)
        poiId = "poiId"
        typeId = "typeId"
        type = Type(typeId)
        agent = Agent(agentId, "firstName", "lastName")
        unassigned = "unassigned"
        poiItem = "poiItem"

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
            cacheRepository
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
            doReturn(1L).`when`(addressRepository).upsertAddressInRoom(cacheAddress)

            // Init some more mocks for test upDateRoomWithProperty()
            doReturn(cacheProperty).`when`(cacheRepository).getCacheProperty()
            doReturn(cacheProperty).`when`(cacheProperty).replaceBlankValuesWithNull()
            doReturn(cacheProperty).`when`(cacheProperty).withoutId()

            // Init some more mocks for test upDateRoomWithPhotos()
            doReturn(1L).`when`(propertyRepository).upsertPropertyInRoom(cacheProperty)

            // Init some more mocks for test upDateRoomWithPropertyPoiJoins()
            doReturn(1L).`when`(cacheProperty).propertyId
            doReturn(poiId).`when`(poi2).poiId

            // Init some more mocks for test testItemData()
            doReturn(stringType).`when`(cacheProperty).typeId
            doReturn(stringType).`when`(typeRepository)
                .stringType(anyString(), anyList(), anyList())
            doReturn(stringAgent).`when`(agentRepository)
                .stringAgent(anyLong(), anyList(), anyList())
            doReturn(address).`when`(addressRepository).address(anyLong(), anyList())

            // Init some more mocks for test xxx()

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
            // Case 1: If isEmpty = true
            doReturn(true).`when`(cacheAddress).isNullOrBlank()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(cacheRepository).updateCacheAddress(null, null)  // Reached
            verify(cacheRepository, never()).updateCacheAddress(
                anyDouble(),
                anyDouble()
            )    // Not reached

            // Reset mocks to avoid interference between inner test parts
            reset(cacheRepository)
            doReturn(cacheAddress).`when`(cacheRepository).getCacheAddress()
            doReturn(cacheProperty).`when`(cacheRepository).getCacheProperty()

            // Case 2a: If isEmpty = false and isModified = true
            doReturn(false).`when`(cacheAddress).isNullOrBlank()
            doReturn(null).`when`(cacheRepository).getInitialAddress()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
//            verify(cacheRepository).updateCacheAddress(null, null)  // Not reached
            verify(cacheRepository, never()).updateCacheAddress(null, null)  // Not reached
            verify(cacheRepository).updateCacheAddress(lat, lng)    // Reached

            // Reset mocks to avoid interference between inner test parts
            reset(cacheRepository)
            doReturn(cacheAddress).`when`(cacheRepository).getCacheAddress()
            doReturn(cacheProperty).`when`(cacheRepository).getCacheProperty()

            // Case 2b: If isEmpty = false and isModified = true
            doReturn(initialAddress).`when`(cacheRepository).getInitialAddress()
            doReturn(true).`when`(cacheAddress).hasDifferencesWith(initialAddress)
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
//            verify(cacheRepository).updateCacheAddress(null, null)  // Not reached
            verify(cacheRepository, never()).updateCacheAddress(null, null)  // Not reached
//            verify(cacheRepository, times(2)).updateCacheAddress(lat, lng)  // Reached
            verify(cacheRepository).updateCacheAddress(lat, lng)  // Reached

            // Reset mocks to avoid interference between inner test parts
            reset(cacheRepository)
            doReturn(initialAddress).`when`(cacheRepository).getInitialAddress()
            doReturn(cacheAddress).`when`(cacheRepository).getCacheAddress()
            doReturn(cacheProperty).`when`(cacheRepository).getCacheProperty()

            // Case 3: If isEmpty = false and isModified = false and hasNullLatLng = true
            doReturn(false).`when`(cacheAddress).hasDifferencesWith(initialAddress)
            doReturn(null).`when`(cacheAddress).latitude
            doReturn(null).`when`(cacheAddress).longitude
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
//            verify(cacheRepository).updateCacheAddress(null, null)  // Not reached
            verify(cacheRepository, never()).updateCacheAddress(null, null)  // Not reached
//            verify(cacheRepository, times(3)).updateCacheAddress(lat, lng)  // Reached
            verify(cacheRepository).updateCacheAddress(lat, lng)  // Reached

            // Reset mocks to avoid interference between inner test parts
            reset(cacheRepository)
            doReturn(initialAddress).`when`(cacheRepository).getInitialAddress()
            doReturn(cacheAddress).`when`(cacheRepository).getCacheAddress()
            doReturn(cacheProperty).`when`(cacheRepository).getCacheProperty()

            // Case 4: If isEmpty = false and isModified = false and hasNullLatLng = false
            doReturn(lat).`when`(cacheAddress).latitude
            doReturn(lng).`when`(cacheAddress).longitude
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
//            verify(cacheRepository).updateCacheAddress(null, null)  // Not reached
            verify(cacheRepository, never()).updateCacheAddress(null, null)  // Not reached
//            verify(cacheRepository, times(3)).updateCacheAddress(anyDouble(), anyDouble())  // Not reached
            verify(cacheRepository, never()).updateCacheAddress(
                anyDouble(),
                anyDouble()
            )  // Not reached
        }
    }

    @Test
    fun testOnClickMenu_updateRoomWithAddress() {
        runBlocking {
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

            // Reset mocks to avoid interference between inner test parts
            reset(addressRepository, cacheRepository)
            doReturn(cacheAddress).`when`(cacheRepository).getCacheAddress()
            doReturn(addressId).`when`(addressRepository).upsertAddressInRoom(cacheAddress)

            // Case 3a: If isEmpty = false and isUpdate = true
            doReturn(false).`when`(cacheAddress).isNullOrBlank()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
//            verify(addressRepository).deleteAddressFromRoom(cacheAddress)   // Not reached
            verify(addressRepository, never()).deleteAddressFromRoom(cacheAddress)   // Not reached
            verify(cacheRepository).updateCacheAddress(addressId)  // Reached
//            verify(cacheRepository).updateCachePropertyItem(NonEditField.ADDRESS_ID.name, null) // Not reached
            verify(cacheRepository, never()).updateCachePropertyItem(
                NonEditField.ADDRESS_ID.name,
                null
            ) // Not reached
            verify(cacheRepository).updateCachePropertyItem(
                NonEditField.ADDRESS_ID.name,
                addressId
            )  // Reached

            // Reset mocks to avoid interference between inner test parts
            reset(addressRepository, cacheRepository)
            doReturn(cacheAddress).`when`(cacheRepository).getCacheAddress()
            doReturn(addressId).`when`(addressRepository).upsertAddressInRoom(cacheAddress)

            // Case 3b: If isEmpty = false and isUpdate = false
            doReturn(null).`when`(cacheRepository).getInitialAddress()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
//            verify(addressRepository).deleteAddressFromRoom(cacheAddress)   // Not reached
            verify(addressRepository, never()).deleteAddressFromRoom(cacheAddress)   // Not reached
//            verify(cacheRepository, times(2)).updateCacheAddress(addressId)  // Reached
            verify(cacheRepository).updateCacheAddress(addressId)  // Reached
//            verify(cacheRepository).updateCachePropertyItem(NonEditField.ADDRESS_ID.name, null) // Not reached
            verify(cacheRepository, never()).updateCachePropertyItem(
                NonEditField.ADDRESS_ID.name,
                null
            ) // Not reached
//            verify(cacheRepository, times(2)).updateCachePropertyItem(NonEditField.ADDRESS_ID.name, addressId)  // Reached
            verify(cacheRepository).updateCachePropertyItem(
                NonEditField.ADDRESS_ID.name,
                addressId
            )  // Reached
        }
    }

    @Test
    fun testOnClickMenu_upDateRoomWithProperty() {
        runBlocking {
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

            // Reset mocks to avoid interference between inner test parts
            reset(cacheRepository)
            doReturn(cacheAddress).`when`(cacheRepository).getCacheAddress()
            doReturn(cacheProperty).`when`(cacheRepository).getCacheProperty()

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
            val initialPhotos = mutableListOf(photo1, photo2)
            val cachePhotos = mutableListOf(photo1)

            // Delete a photo
            doReturn(cachePhotos).`when`(cacheRepository).getCacheItemPhotos()
            doReturn(initialPhotos).`when`(cacheRepository).getInitialItemPhotos()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
            verify(photoRepository).deletePhotosFromRoom(mutableListOf(photo2))   // Reached
            verify(photoRepository, never()).upsertPhotosInRoom(initialPhotos)  // Not reached

            // Reset mock to avoid interference between inner test parts
            reset(photoRepository)

            // Add a photo
            doReturn(initialPhotos).`when`(cacheRepository).getCacheItemPhotos()
            doReturn(cachePhotos).`when`(cacheRepository).getInitialItemPhotos()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
//            verify(photoRepository).deletePhotosFromRoom(mutableListOf(photo2))   // Not reached
            verify(
                photoRepository,
                never()
            ).deletePhotosFromRoom(mutableListOf(photo2))   // Not reached
            verify(photoRepository).upsertPhotosInRoom(initialPhotos)  // Reached
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
            verify(propertyPoiJoinRepository).deletePropertyPoiJoinsFromRoom(
                propertyPoiJoinsModified
            )   // Reached
            verify(propertyPoiJoinRepository, never()).upsertPropertyPoiJoinsInRoom(
                propertyPoiJoinsModified
            )  // Not reached

            // Reset mock to avoid interference between inner test parts
            reset(propertyPoiJoinRepository)

            // Add pois
            doReturn(initialPois).`when`(cacheRepository).getCacheItemPois()
            doReturn(cachePois).`when`(cacheRepository).getInitialItemPois()
            viewModel.onClickMenu(navController, context)
            delay(delayInMs)    // Allow the coroutine to execute
//            verify(propertyPoiJoinRepository).deletePropertyPoiJoinsFromRoom(propertyPoiJoinsModified)   // Not reached
            verify(propertyPoiJoinRepository, never()).deletePropertyPoiJoinsFromRoom(
                propertyPoiJoinsModified
            )   // Not reached
            verify(propertyPoiJoinRepository).upsertPropertyPoiJoinsInRoom(propertyPoiJoinsModified)  // Reached
        }
    }

    @Test
    fun testProperty() {
        // Also testing propertyFromId()
        val properties = mutableListOf(property1, property2)

        doReturn(property1).`when`(propertyRepository).propertyFromId(propertyId, properties)
        assertEquals(property1, viewModel.property(propertyId, properties))
    }

    @Test
    fun testItemData() {
        // Also testing dataFromNullableProperty(), stringType(), stringAgent() and address()
        val result = viewModel.itemData(
            cacheProperty,
            mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf()
        )

        assertEquals(stringType, result.first)
        assertEquals(stringAgent, result.second)
        assertEquals(address, result.third)
    }

    @Test
    fun testItemPhotos() {
        val photos = mutableListOf(photo1, photo2)
        val itemPhotos = mutableListOf(photo1)

        doReturn(itemPhotos).`when`(photoRepository).itemPhotos(propertyId, photos)

        val result = viewModel.itemPhotos(propertyId, photos)
        assertEquals(itemPhotos, result)
    }

    @Test
    fun testItemPois() {
        // Also testing poiFromId()
        val pois = mutableListOf(poi1, poi2)
        val itemPois = mutableListOf(poi1)
        val propertyPoiJoins = mutableListOf(PropertyPoiJoin(propertyId, poiId))

        doReturn(poi1).`when`(poiRepository).poiFromId(poiId, pois)

        val result = viewModel.itemPois(propertyId, propertyPoiJoins, pois)
        assertEquals(itemPois, result)
    }

    @Test
    fun testGetPhotoUri() {
        viewModel.getPhotoUri(context)
        verify(photoRepository).getPhotoUri(context)
    }

    @Test
    fun testOnResponseToCamPermRequest() {
        // Also testing toastMessage()
        val cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean> = mock()
        val cameraUri: Uri = Uri.EMPTY

        mockStatic(Toast::class.java).use {
            // If isGranted = false
            `when`(Toast.makeText(eq(context), anyString(), anyInt())).thenReturn(toast)
            viewModel.onResponseToCamPermRequest(false, cameraLauncher, cameraUri, context)
            verify(cameraLauncher, never()).launch(any())   // Not reached
            it.verify {
                Toast.makeText(
                    eq(context),
                    anyString(),
                    eq(Toast.LENGTH_SHORT)
                )
            }  // Reached
            verify(toast).show()   // Reached

            // Reset mocks to avoid interference between inner test parts
            reset(cameraLauncher, toast)
            it.reset()

            // If isGranted = true
            viewModel.onResponseToCamPermRequest(true, cameraLauncher, cameraUri, context)
            verify(cameraLauncher).launch(cameraUri)    // Reached
            it.verify(
                { Toast.makeText(eq(context), anyString(), eq(Toast.LENGTH_SHORT)) },
                never()
            )  // Not reached
            verify(toast, never()).show()   // Not reached
        }
    }

    @Test
    fun testOnShootPhotoMenuItemClick() {
        val cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean> = mock()
        val camPermLauncher: ManagedActivityResultLauncher<String, Boolean> = mock()
        val uri = Uri.EMPTY

        viewModel.onShootPhotoMenuItemClick(context, uri, cameraLauncher, camPermLauncher)
        verify(photoRepository).onShootPhotoMenuItemClick(
            context,
            uri,
            cameraLauncher,
            camPermLauncher
        )
    }

    @Test
    fun testOnSelectPhotoMenuItemClick() {
        val pickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> = mock()

        viewModel.onSelectPhotoMenuItemClick(pickerLauncher)
        verify(photoRepository).onSelectPhotoMenuItemClick(pickerLauncher)
    }

    @Test
    fun testInitCache() {
        val itemPhotos = mutableListOf(photo1)
        val itemPois = mutableListOf(poi1)

        doReturn(unassigned).`when`(context).getString(anyInt())

        viewModel.initCache(
            context,
            property1,
            stringType,
            stringAgent,
            address,
            itemPhotos,
            itemPois
        )
        verify(cacheRepository).initCache(
            unassigned,
            property1,
            stringType,
            stringAgent,
            address,
            itemPhotos,
            itemPois
        )
    }

    @Test
    fun testCacheData() {
        viewModel.cacheData()
        verify(cacheRepository).cacheData()
    }

    @Test
    fun testOnDropdownMenuValueChange() {
        val types: MutableList<Type> = mutableListOf(type)
        val agents: MutableList<Agent> = mutableListOf(agent)

        // Category TYPE
        var otherCat = DropdownMenuCategory.AGENT.name
        var category = DropdownMenuCategory.TYPE.name
        var value = "$category#0"
        viewModel.onDropdownMenuValueChange(value, types, agents)
        verify(cacheRepository).updateCachePropertyItem(category, type.typeId)
        verify(cacheRepository, never()).updateCachePropertyItem(eq(otherCat), any())

        // Reset mock to avoid interference between inner test parts
        reset(cacheRepository)

        // Category AGENT
        otherCat = DropdownMenuCategory.TYPE.name
        category = DropdownMenuCategory.AGENT.name
        value = "$category#0"
        viewModel.onDropdownMenuValueChange(value, types, agents)
        verify(cacheRepository).updateCachePropertyItem(category, agent.agentId)
        verify(cacheRepository, never()).updateCachePropertyItem(eq(otherCat), any())

        // Reset mock to avoid interference between inner test parts
        reset(cacheRepository)

        // Other category
        category = "category"
        value = "$category#0"
        viewModel.onDropdownMenuValueChange(value, types, agents)
        verify(cacheRepository, never()).updateCachePropertyItem(anyString(), any())
    }

    @Test
    fun testOnFieldValueChange() {
        val euro = "€"
        val dollar = "$"
        val stringValue = "value"
        val digitalValue = "7"
        val valueInDollar = (digitalValue.toInt() / RATE_OF_DOLLAR_IN_EURO).roundToInt()
        val emptyValue = ""

        // Field PRICE with digital value in euro
        var field = EditField.PRICE.name
        viewModel.onFieldValueChange(field, digitalValue, euro)
        verify(cacheRepository).cacheItemPhotosFlow // Init viewmodel
        verify(cacheRepository).updateCacheItemPhotos(mutableListOf())  // Init viewmodel
        verify(cacheRepository).updateCachePropertyItem(field, valueInDollar)
        verifyNoMoreInteractions(cacheRepository)

        // Reset mock to avoid interference between inner test parts
        reset(cacheRepository)

        // Field PRICE with digital value in dollar
        field = EditField.PRICE.name
        viewModel.onFieldValueChange(field, digitalValue, dollar)
        verify(cacheRepository).updateCachePropertyItem(field, digitalValue.toInt())
        verifyNoMoreInteractions(cacheRepository)

        // Reset mock to avoid interference between inner test parts
        reset(cacheRepository)

        // Field PRICE with string value
        field = EditField.PRICE.name
        viewModel.onFieldValueChange(field, stringValue, euro)
        verify(cacheRepository).updateCachePropertyItem(field, null)
        verifyNoMoreInteractions(cacheRepository)

        // Reset mock to avoid interference between inner test parts
        reset(cacheRepository)

        // Field PRICE with empty value
        field = EditField.PRICE.name
        viewModel.onFieldValueChange(field, emptyValue, euro)
        verify(cacheRepository).updateCachePropertyItem(field, null)
        verifyNoMoreInteractions(cacheRepository)

        // Reset mock to avoid interference between inner test parts
        reset(cacheRepository)

        // With field SURFACE with digital value
        field = EditField.SURFACE.name
        viewModel.onFieldValueChange(field, digitalValue, emptyValue)
        verify(cacheRepository).updateCachePropertyItem(field, digitalValue.toInt())
        verifyNoMoreInteractions(cacheRepository)

        // Reset mock to avoid interference between inner test parts
        reset(cacheRepository)

        // With field SURFACE with string value
        field = EditField.SURFACE.name
        viewModel.onFieldValueChange(field, stringValue, emptyValue)
        verify(cacheRepository).updateCachePropertyItem(field, null)
        verifyNoMoreInteractions(cacheRepository)

        // Reset mock to avoid interference between inner test parts
        reset(cacheRepository)

        // With field SURFACE with empty value
        field = EditField.SURFACE.name
        viewModel.onFieldValueChange(field, emptyValue, emptyValue)
        verify(cacheRepository).updateCachePropertyItem(field, null)
        verifyNoMoreInteractions(cacheRepository)

        // Reset mock to avoid interference between inner test parts
        reset(cacheRepository)

        // With field DESCRIPTION
        field = EditField.DESCRIPTION.name
        viewModel.onFieldValueChange(field, stringValue, emptyValue)
        verify(cacheRepository).updateCachePropertyItem(field, stringValue)
        verifyNoMoreInteractions(cacheRepository)

        // Reset mock to avoid interference between inner test parts
        reset(cacheRepository)

        // With field COUNTRY
        field = EditField.COUNTRY.name
        viewModel.onFieldValueChange(field, stringValue, emptyValue)
        verify(cacheRepository).updateCacheAddressItem(field, stringValue)
        verifyNoMoreInteractions(cacheRepository)
    }

    @Test
    fun testOnPoiClick() {
        viewModel.onPoiClick(poiItem, true)
        verify(cacheRepository).updateCacheItemPois(poiItem, true)
    }

    @Test
    fun testSaveToInternalStorage() {
        val uri = Uri.EMPTY
        viewModel.saveToInternalStorage(context, uri)
        verify(photoRepository).saveToInternalStorage(context, uri)
    }

    @Test
    fun testCheckUris() {
        // No Uri empty
        var capturedImageUri = mock(Uri::class.java)
        var pickerUri = mock(Uri::class.java)
        var photoToAddUri = mock(Uri::class.java)
        var result = viewModel.checkUris(capturedImageUri, pickerUri, photoToAddUri)
        assertEquals(Uri.EMPTY, result.first)
        assertEquals(Uri.EMPTY, result.second)
        assertEquals(pickerUri, result.third)

        // capturedImageUri empty
        capturedImageUri = Uri.EMPTY
        pickerUri = mock(Uri::class.java)
        photoToAddUri = mock(Uri::class.java)
        result = viewModel.checkUris(capturedImageUri, pickerUri, photoToAddUri)
        assertEquals(Uri.EMPTY, result.first)
        assertEquals(Uri.EMPTY, result.second)
        assertEquals(pickerUri, result.third)

        // pickerUri empty
        capturedImageUri = mock(Uri::class.java)
        pickerUri = Uri.EMPTY
        photoToAddUri = mock(Uri::class.java)
        result = viewModel.checkUris(capturedImageUri, pickerUri, photoToAddUri)
        assertEquals(Uri.EMPTY, result.first)
        assertEquals(Uri.EMPTY, result.second)
        assertEquals(capturedImageUri, result.third)

        // photoToAddUri empty
        capturedImageUri = mock(Uri::class.java)
        pickerUri = mock(Uri::class.java)
        photoToAddUri = Uri.EMPTY
        result = viewModel.checkUris(capturedImageUri, pickerUri, photoToAddUri)
        assertEquals(Uri.EMPTY, result.first)
        assertEquals(Uri.EMPTY, result.second)
        assertEquals(pickerUri, result.third)

        // capturedImageUri and pickerUri empty
        capturedImageUri = Uri.EMPTY
        pickerUri = Uri.EMPTY
        photoToAddUri = mock(Uri::class.java)
        result = viewModel.checkUris(capturedImageUri, pickerUri, photoToAddUri)
        assertEquals(Uri.EMPTY, result.first)
        assertEquals(Uri.EMPTY, result.second)
        assertEquals(photoToAddUri, result.third)

        // capturedImageUri and photoToAddUri empty
        capturedImageUri = Uri.EMPTY
        pickerUri = mock(Uri::class.java)
        photoToAddUri = Uri.EMPTY
        result = viewModel.checkUris(capturedImageUri, pickerUri, photoToAddUri)
        assertEquals(Uri.EMPTY, result.first)
        assertEquals(Uri.EMPTY, result.second)
        assertEquals(pickerUri, result.third)

        // pickerUri and photoToAddUri empty
        capturedImageUri = mock(Uri::class.java)
        pickerUri = Uri.EMPTY
        photoToAddUri = Uri.EMPTY
        result = viewModel.checkUris(capturedImageUri, pickerUri, photoToAddUri)
        assertEquals(Uri.EMPTY, result.first)
        assertEquals(Uri.EMPTY, result.second)
        assertEquals(capturedImageUri, result.third)

        // All Uri empty
        capturedImageUri = Uri.EMPTY
        pickerUri = Uri.EMPTY
        photoToAddUri = Uri.EMPTY
        result = viewModel.checkUris(capturedImageUri, pickerUri, photoToAddUri)
        assertEquals(Uri.EMPTY, result.first)
        assertEquals(Uri.EMPTY, result.second)
        assertEquals(Uri.EMPTY, result.third)
    }

    @Test
    fun testOnCancelPhotoEditionButtonClick() {
        val result = viewModel.onCancelPhotoEditionButtonClick()
        assertEquals(Uri.EMPTY, result)
    }

    @Test
    fun testOnSavePhotoButtonClick() {
        // The photo already exists
        doReturn(mutableListOf(photo)).`when`(cacheRepository).getCacheItemPhotos()
        var result = viewModel.onSavePhotoButtonClick(uri, label)
        verify(cacheRepository).cacheItemPhotosFlow // Init viewmodel
        verify(cacheRepository).updateCacheItemPhotos(mutableListOf())  // Init viewmodel
        verify(cacheRepository, times(2)).getCacheItemPhotos()
        verify(cacheRepository).getCacheProperty()
        verify(cacheRepository).updateCacheItemPhotos(0, photo)
        verifyNoMoreInteractions(cacheRepository)
        assertEquals(Uri.EMPTY, result)

        // Reset mock to avoid interference between inner test parts
        reset(cacheRepository)
        doReturn(cacheProperty).`when`(cacheRepository).getCacheProperty()

        // The photo doesn't already exist
        doReturn(mutableListOf<Photo>()).`when`(cacheRepository).getCacheItemPhotos()
        result = viewModel.onSavePhotoButtonClick(uri, label)
        verify(cacheRepository, times(1)).getCacheItemPhotos()
        verify(cacheRepository).getCacheProperty()
        verify(cacheRepository).updateCacheItemPhotos(isNotNull(), isNull())
        verifyNoMoreInteractions(cacheRepository)
        assertEquals(Uri.EMPTY, result)
    }

    @Test
    fun testOnEditPhotoMenuItemClick() {
        val uri = Uri.parse(stringUri)
        val photo = Photo(1L, stringUri, label, 1L)

        val result = viewModel.onEditPhotoMenuItemClick(photo)

        assertEquals(uri, result.first)
        assertEquals(true, result.second)
    }

    @Test
    fun testOnPhotoDeletionConfirmation() {
        val photos = mutableListOf(photo1, photo2)

        // photoToRemove is null
        doReturn(photos).`when`(cacheRepository).getCacheItemPhotos()
        doReturn(null).`when`(photoRepository).photoFromId(photoId, photos)
        viewModel.onPhotoDeletionConfirmation(propertyId, photoId)
        verify(photoRepository).photoFromId(photoId, photos)
        verify(cacheRepository).getCacheItemPhotos()
        verify(cacheRepository, never()).updateCacheItemPhotos(isNull(), any())

        // Reset mock to avoid interference between inner test parts
        reset(cacheRepository, photoRepository)

        // photoToRemove isn't null
        doReturn(photos).`when`(cacheRepository).getCacheItemPhotos()
        doReturn(photo2).`when`(photoRepository).photoFromId(photoId, photos)
        viewModel.onPhotoDeletionConfirmation(propertyId, photoId)
        verify(photoRepository).photoFromId(photoId, photos)
        verify(cacheRepository).getCacheItemPhotos()
        verify(cacheRepository).updateCacheItemPhotos(null, photo2)
    }






}