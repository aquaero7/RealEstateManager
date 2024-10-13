package com.aquaero.realestatemanager.viewModel_test

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.EMPTY_STRING
import com.aquaero.realestatemanager.EditField
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
import com.aquaero.realestatemanager.utils.AndroidLogger
import com.aquaero.realestatemanager.utils.ConnectionState
import com.aquaero.realestatemanager.viewmodel.EditViewModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.MockedStatic
import org.mockito.Mockito.lenient
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.any
import org.mockito.kotlin.isNotNull
import org.mockito.kotlin.isNull
import org.mockito.kotlin.never
import org.mockito.kotlin.reset
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyNoMoreInteractions
import kotlin.math.roundToInt

@RunWith(MockitoJUnitRunner::class)
//@RunWith(RobolectricTestRunner::class)
class EditViewModelTestPart2 {

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
    private lateinit var capturedImageStringUri: String
    private lateinit var pickerStringUri: String
    private lateinit var photoToAddStringUri: String

    private val propertyId: Long = 1L
    private val agentId: Long = 2L
    private val photoId: Long = 2L

    private lateinit var context: Context
    private lateinit var viewModel: EditViewModel

    // Created to allow tests running with coverage, using MockitoJUnitRunner instead of RobolectricTestRunner
    private lateinit var uriMock: MockedStatic<Uri>


    @Before
    fun setup() {
        // Initialize uriMock
        uriMock = mockStatic(Uri::class.java)

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
        photo = Photo(photoId, uri.toString(), label, propertyId)
        photo1 = Photo(1L, "", null, propertyId)
        photo2 = Photo(2L, "", null, propertyId)
        poiId = "poiId"
        typeId = "typeId"
        type = Type(typeId)
        agent = Agent(agentId, "firstName", "lastName")
        unassigned = "unassigned"
        poiItem = "poiItem"
        capturedImageStringUri = "capturedImageStringUri"
        pickerStringUri = "pickerStringUri"
        photoToAddStringUri = "photoToAddStringUri"

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
            // Init some mocks for test onSavePhotoButtonClick()
            lenient().doReturn(cacheProperty).`when`(cacheRepository).getCacheProperty()
            doReturn(propertyId).`when`(cacheProperty).propertyId

            // Init some more mocks for test testItemData()
            doReturn(stringType).`when`(cacheProperty).typeId
            doReturn(stringType).`when`(typeRepository)
                .stringType(anyString(), anyList(), anyList())
            doReturn(stringAgent).`when`(agentRepository)
                .stringAgent(anyLong(), anyList(), anyList())
            doReturn(address).`when`(addressRepository).addressFromId(anyLong(), anyList())
        }
    }

    @After
    fun teardown() {
        // Close uriMock
        uriMock.close()
    }

    private fun launchStringUrisCheckTest(
        stringUris: Triple<String, String, String>,
        resultsAssertions: Triple<String, String, String>
    ) {
        val capturedImageStringUri = stringUris.first
        val pickerStringUri = stringUris.second
        val photoToAddStringUri = stringUris.third

        val result = viewModel.checkStringUris(capturedImageStringUri, pickerStringUri, photoToAddStringUri)

        assertEquals(resultsAssertions.first, result.first)
        assertEquals(resultsAssertions.second, result.second)
        assertEquals(resultsAssertions.third, result.third)
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
        mockStatic(Toast::class.java).use {
            // When isGranted = false
            `when`(Toast.makeText(eq(context), anyString(), anyInt())).thenReturn(toast)
            viewModel.onResponseToCamPermRequest(false, cameraLauncher, uri, context)
            verify(cameraLauncher, never()).launch(any())   // Not reached
            it.verify { Toast.makeText(eq(context), anyString(), eq(Toast.LENGTH_SHORT)) }
            verify(toast).show()

            // Reset mocks to avoid interference between inner test parts
            reset(cameraLauncher, toast)
            it.reset()

            // When isGranted = true
            viewModel.onResponseToCamPermRequest(true, cameraLauncher, uri, context)
            verify(cameraLauncher).launch(uri)    // Reached
            it.verify({ Toast.makeText(eq(context), anyString(), eq(Toast.LENGTH_SHORT)) }, never())
            verify(toast, never()).show()
        }
    }

    @Test
    fun testOnShootPhotoMenuItemClick() {
        val cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean> = mock()
        val camPermLauncher: ManagedActivityResultLauncher<String, Boolean> = mock()
        viewModel.onShootPhotoMenuItemClick(context, uri, cameraLauncher, camPermLauncher)
        verify(photoRepository).onShootPhotoMenuItemClick(context, uri, cameraLauncher, camPermLauncher)
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
        viewModel.initCache(context, property1, stringType, stringAgent, address, itemPhotos, itemPois)
        verify(cacheRepository).initCache(unassigned, property1, stringType, stringAgent, address, itemPhotos, itemPois)
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
        viewModel.saveToInternalStorage(context, uri)
        verify(photoRepository).saveToInternalStorage(context, uri)
    }

    @Test
    fun testCheckStringUris() {
        // No Uri empty
        var stringUris = Triple(capturedImageStringUri, pickerStringUri, photoToAddStringUri)
        var resultsAssertions = Triple(EMPTY_STRING, EMPTY_STRING, pickerStringUri)
        launchStringUrisCheckTest(stringUris, resultsAssertions)

        // capturedImageUri empty
        stringUris = Triple(EMPTY_STRING, pickerStringUri, photoToAddStringUri)
        resultsAssertions = Triple(EMPTY_STRING, EMPTY_STRING, pickerStringUri)
        launchStringUrisCheckTest(stringUris, resultsAssertions)

        // pickerUri empty
        stringUris = Triple(capturedImageStringUri, EMPTY_STRING, photoToAddStringUri)
        resultsAssertions = Triple(EMPTY_STRING, EMPTY_STRING, capturedImageStringUri)
        launchStringUrisCheckTest(stringUris, resultsAssertions)

        // photoToAddUri empty
        stringUris = Triple(capturedImageStringUri, pickerStringUri, EMPTY_STRING)
        resultsAssertions = Triple(EMPTY_STRING, EMPTY_STRING, pickerStringUri)
        launchStringUrisCheckTest(stringUris, resultsAssertions)

        // capturedImageUri and pickerUri empty
        stringUris = Triple(EMPTY_STRING, EMPTY_STRING, photoToAddStringUri)
        resultsAssertions = Triple(EMPTY_STRING, EMPTY_STRING, photoToAddStringUri)
        launchStringUrisCheckTest(stringUris, resultsAssertions)

        // capturedImageUri and photoToAddUri empty
        stringUris = Triple(EMPTY_STRING, pickerStringUri, EMPTY_STRING)
        resultsAssertions = Triple(EMPTY_STRING, EMPTY_STRING, pickerStringUri)
        launchStringUrisCheckTest(stringUris, resultsAssertions)

        // pickerUri and photoToAddUri empty
        stringUris = Triple(capturedImageStringUri, EMPTY_STRING, EMPTY_STRING)
        resultsAssertions = Triple(EMPTY_STRING, EMPTY_STRING, capturedImageStringUri)
        launchStringUrisCheckTest(stringUris, resultsAssertions)

        // All Uri empty
        stringUris = Triple(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING)
        resultsAssertions = Triple(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING)
        launchStringUrisCheckTest(stringUris, resultsAssertions)
    }

    @Test
    fun testOnCancelPhotoEditionButtonClick() {
        val result = viewModel.onCancelPhotoEditionButtonClick()
        assertEquals(EMPTY_STRING, result)
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
        assertEquals(EMPTY_STRING, result)

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
        assertEquals(EMPTY_STRING, result)
    }

    @Test
    fun testOnEditPhotoMenuItemClick() {
        val photo = Photo(photoId, stringUri, label, propertyId)
        val result = viewModel.onEditPhotoMenuItemClick(photo)
        assertEquals(stringUri, result.first)
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