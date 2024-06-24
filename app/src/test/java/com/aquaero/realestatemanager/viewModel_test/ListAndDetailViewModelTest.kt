package com.aquaero.realestatemanager.viewModel_test

import android.content.Context
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.AppContentType
import com.aquaero.realestatemanager.NULL_PROPERTY_ID
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.SM_KEY
import com.aquaero.realestatemanager.SM_MARKER_COLOR
import com.aquaero.realestatemanager.SM_SCALE
import com.aquaero.realestatemanager.SM_SIZE
import com.aquaero.realestatemanager.SM_TYPE
import com.aquaero.realestatemanager.SM_URL
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.PoiEnum
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.model.TypeEnum
import com.aquaero.realestatemanager.navigateToDetail
import com.aquaero.realestatemanager.navigateToEditDetail
import com.aquaero.realestatemanager.repository.AddressRepository
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.LocationRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.PoiRepository
import com.aquaero.realestatemanager.repository.PropertyRepository
import com.aquaero.realestatemanager.repository.TypeRepository
import com.aquaero.realestatemanager.utils.ConnectionState
import com.aquaero.realestatemanager.viewmodel.ListAndDetailViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.robolectric.RobolectricTestRunner

//@RunWith(MockitoJUnitRunner::class)
@RunWith(RobolectricTestRunner::class)
class ListAndDetailViewModelTest {
    // @get:Rule
    // var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var propertyRepository: PropertyRepository
    private lateinit var addressRepository: AddressRepository
    private lateinit var photoRepository: PhotoRepository
    private lateinit var agentRepository: AgentRepository
    private lateinit var typeRepository: TypeRepository
    private lateinit var poiRepository: PoiRepository
    private lateinit var locationRepository: LocationRepository

    private lateinit var navController: NavHostController
    private lateinit var viewModel: ListAndDetailViewModel


    @Before
    fun setup() {
        propertyRepository = mock(PropertyRepository::class.java)
        addressRepository = mock(AddressRepository::class.java)
        photoRepository = mock(PhotoRepository::class.java)
        agentRepository = mock(AgentRepository::class.java)
        typeRepository = mock(TypeRepository::class.java)
        poiRepository = mock(PoiRepository::class.java)
        locationRepository = mock(LocationRepository::class.java)
        navController = mock(NavHostController::class.java)

        viewModel = ListAndDetailViewModel(
            propertyRepository,
            addressRepository,
            photoRepository,
            agentRepository,
            typeRepository,
            poiRepository,
            locationRepository
        )
    }


    @Test
    fun clickOnMenuWithSuccess() {
        val idForTest = 9L
        viewModel.onClickMenu(navController, idForTest)
        verify(navController).navigateToEditDetail(idForTest.toString())
    }

    @Test
    fun clickOnPropertyWithSuccess() {
        val idForTest = 9L
        val contentType: AppContentType = AppContentType.LIST_OR_DETAIL

        viewModel.onPropertyClick(navController, idForTest, contentType)

        verify(navController).navigateToDetail(idForTest.toString(), contentType)
    }

    @Test
    fun clickOnFabWithSuccess() {
        viewModel.onFabClick(navController)
        verify(navController).navigateToEditDetail(NULL_PROPERTY_ID.toString())
    }

    @Test
    fun checkForConnectionWithSuccess() {
        val connection: ConnectionState = mock(ConnectionState::class.java)

        // Test when connection check returns false
        doReturn(false).`when`(locationRepository).checkForConnection(connection)
        var result = viewModel.checkForConnection(connection)
        assertFalse(result)

        // Test when connection check returns true
        doReturn(true).`when`(locationRepository).checkForConnection(connection)
        result = viewModel.checkForConnection(connection)
        assertTrue(result)
    }

    @Test
    fun getItemDataWithSuccess() = runTest {
        val property1 = Property(
            propertyId = 1L,
            addressId = 1L,
            typeId = TypeEnum.UNASSIGNED.key,
            price = null,
            description = null,
            surface = null,
            nbOfRooms = null,
            nbOfBathrooms = null,
            nbOfBedrooms = null,
            registrationDate = null,
            saleDate = null
        )
        val photo1 = Photo(
            photoId = 1L,
            uri = "uri1",
            label = null,
            propertyId = 1L
        )
        val address1 = Address(
            addressId = 1L,
            streetNumber = "1",
            streetName = "streetName",
            addInfo = null,
            city = "city",
            state = null,
            zipCode = "zipCode",
            country = "country",
            latitude = null,
            longitude = null,
        )
        val propertyPoiJoin1 = PropertyPoiJoin(1L, PoiEnum.SHOP.key)
        val poi1 = Poi(PoiEnum.SHOP.key)
        val type1 = Type(TypeEnum.UNASSIGNED.key)
        val agent1 = Agent(1L, "agentFirstName", "agentLastName")

        val stringType = TypeEnum.UNASSIGNED.key
        val stringAgent = agent1.toString()
        val stringAddress = address1.toString()
        val addressMarker = address1.toUrl()
        val thumbNailUrl = SM_URL + SM_SIZE + SM_SCALE + SM_TYPE + SM_MARKER_COLOR + addressMarker + SM_KEY

        // Mock data setup
        val propertyId = 1L
        val mockProperties = mutableListOf(property1)
        val mockPhotos = mutableListOf(photo1)
        val mockPropertyPoiJoins = mutableListOf(propertyPoiJoin1)
        val mockPois = mutableListOf(poi1)
        val mockTypes = mutableListOf(type1)
        val mockStringTypes = mutableListOf(TypeEnum.UNASSIGNED.key)
        val mockAgents = mutableListOf(agent1)
        val mockStringAgents = mutableListOf(agent1.toString())
        val mockAddresses = mutableListOf(address1)

        // Mock repository calls
        doReturn(property1).`when`(propertyRepository).propertyFromId(eq(propertyId), eq(mockProperties))
        doReturn(poi1).`when`(poiRepository).poiFromId(anyString(), anyList())
        doReturn(mockPhotos).`when`(photoRepository).itemPhotos(anyLong(), anyList())
        doReturn(stringType).`when`(typeRepository).stringType(anyString(), anyList(), anyList())
        doReturn(stringAgent).`when`(agentRepository).stringAgent(anyLong(), anyList(), anyList())
        doReturn(stringAddress).`when`(addressRepository).stringAddress(anyLong(), anyList())
        doReturn(thumbNailUrl).`when`(addressRepository).thumbnailUrlFromAddressId(anyLong(), anyList())

        // Call the function under test
        val result = viewModel.itemData(
            propertyId,
            mockProperties,
            mockPhotos,
            mockPropertyPoiJoins,
            mockPois,
            mockTypes,
            mockStringTypes,
            mockAgents,
            mockStringAgents,
            mockAddresses
        )

        // Assert expected results
        assertEquals(mockProperties.first(), result.first.first)
        assertEquals(mockPhotos, result.first.second)
        assertEquals(mockPois, result.first.third)
        assertEquals(TypeEnum.UNASSIGNED.key, result.second.first)
        assertEquals(agent1.toString(), result.second.second)
        assertEquals(stringAddress, result.third.first)
        assertEquals(thumbNailUrl, result.third.second)
    }

    @Test
    fun getStringLatLngItemWithSuccess() {
        val addresses = mutableListOf<Address>()
        val latLngItem = "latLngItem"
        // Mock data setup
        val context = mock(Context::class.java)
        val property = mock(Property::class.java)

        // Case 1: Configure the mocks when addressId is not null
        doReturn(1L).`when`(property).addressId
        doReturn("latLngString").`when`(addressRepository).stringLatLngItem(anyLong(), anyList(), anyString())
        // Call the method under test
        var result = viewModel.stringLatLngItem(property, addresses, latLngItem, context)
        // Verify the result
        assertEquals("latLngString", result)


        // Case 2: Configure the mocks when addressId is null
        doReturn(null).`when`(property).addressId
        doReturn("Unavailable").`when`(context).getString(R.string.unavailable)
        // Call the method under test
        result = viewModel.stringLatLngItem(property, addresses, latLngItem, context)
        // Verify the result
        assertEquals("Unavailable", result)
    }

    @Test
    fun getItemTypeWithSuccess() {
        val typeId = "typeId"
        val stringType = "typeString"
        val types = mutableListOf<Type>()
        val stringTypes = mutableListOf<String>()

        // Case 1: Configure the mocks when type is null (types is empty)
        // Call the method under test
        var result = viewModel.itemType(typeId, types, stringTypes)
        // Verify the result
        assertEquals("", result)

        // Case 2: Configure the mocks when type is not null and stringTypes is empty
        val type = mock(Type::class.java)
        types.add(type)
        doReturn(typeId).`when`(type).typeId
        // Call the method under test
        result = viewModel.itemType(typeId, types, stringTypes)
        // Verify the result
        assertEquals("", result)

        // Case 3: Configure the mocks when type is not null and stringTypes is not empty
        stringTypes.add(stringType)
        // Call the method under test
        result = viewModel.itemType(typeId, types, stringTypes)
        // Verify the result
        assertEquals("typeString", result)
    }

    // All private functions of the viewModel are indirectly tested in test getItemDataWithSuccess()

}