package com.aquaero.realestatemanager.viewModel_test

import android.content.Context
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.DEFAULT_LIST_INDEX
import com.aquaero.realestatemanager.DEFAULT_RADIO_INDEX
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.repository.AddressRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.SearchDataRepository
import com.aquaero.realestatemanager.viewmodel.SearchViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyList
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.mock
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mockingDetails
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import org.mockito.invocation.Invocation
import org.mockito.kotlin.KArgumentCaptor
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.times
import org.robolectric.RobolectricTestRunner

// @RunWith(MockitoJUnitRunner::class)
@RunWith(RobolectricTestRunner::class)
/**
 * Testing applyFilters() through onClickMenu() in searchViewModel
 */
class SearchViewModelTestPart2 {
    private lateinit var addressRepository: AddressRepository
    private lateinit var photoRepository: PhotoRepository
    private lateinit var searchDataRepository: SearchDataRepository
    private lateinit var navController: NavHostController
    private lateinit var context: Context
    private lateinit var viewModel: SearchViewModel

    private lateinit var listArgumentCaptor: KArgumentCaptor<MutableList<Property>>

    private lateinit var poi1: Poi
    private lateinit var property1: Property
    private lateinit var address1: Address
    private lateinit var type1: Type
    private lateinit var agent1: Agent
    private lateinit var propertyPoiJoin1: PropertyPoiJoin

    private lateinit var addresses: MutableList<Address>
    private lateinit var types: MutableList<Type>
    private lateinit var agents: MutableList<Agent>
    private lateinit var photos: MutableList<Photo>
    private lateinit var itemPhotos: MutableList<Photo>

    private lateinit var emptyPropertyList: MutableList<Property>
    private lateinit var emptyPhotoList: MutableList<Photo>
    private lateinit var properties: MutableList<Property>
    private lateinit var propertyPoiJoins: MutableList<PropertyPoiJoin>
    private lateinit var itemPois: MutableList<Poi>

    private val addressId1 = 1L
    private var captorIndex = 0

    @Before
    fun setup() {
        addressRepository = mock(AddressRepository::class.java)
        photoRepository = mock(PhotoRepository::class.java)
        searchDataRepository = mock(SearchDataRepository::class.java)
        navController = mock(NavHostController::class.java)
        context = mock(Context::class.java)

        poi1 = mock(Poi::class.java)
        property1 = mock(Property::class.java)
        address1 = mock(Address::class.java)
        type1 = mock(Type::class.java)
        agent1 = mock(Agent::class.java)
        propertyPoiJoin1 = mock(PropertyPoiJoin::class.java)
        addresses = mock()
        types = mock()
        agents = mock()
        photos = mock()
        itemPhotos = mock()

        emptyPropertyList = mutableListOf()
        emptyPhotoList = mutableListOf()
        properties = mutableListOf(property1)
        propertyPoiJoins = mutableListOf(propertyPoiJoin1)
        itemPois = mutableListOf(poi1)

        viewModel = SearchViewModel(addressRepository, photoRepository, searchDataRepository)

        listArgumentCaptor = argumentCaptor()

        doReturn(100).`when`(property1).surface
        doReturn(4).`when`(property1).nbOfRooms
        doReturn(4).`when`(property1).nbOfBathrooms
        doReturn(4).`when`(property1).nbOfBedrooms
        doReturn(100000).`when`(property1).price
        doReturn("The description of the property").`when`(property1).description
        doReturn(addressId1).`when`(property1).addressId
        doReturn(address1).`when`(addressRepository).addressFromId(addressId1, addresses)
        doReturn("12345").`when`(address1).zipCode
        doReturn("city").`when`(address1).city
        doReturn("state").`when`(address1).state
        doReturn("country").`when`(address1).country
        doReturn("2024-06-15").`when`(property1).registrationDate
        doReturn("2024-06-15").`when`(property1).saleDate
        doReturn(type1).`when`(types).elementAt(0)
        doReturn("typeId").`when`(property1).typeId
        doReturn(agent1).`when`(agents).elementAt(0)
        doReturn(1L).`when`(property1).agentId
        doReturn(1L).`when`(property1).propertyId
        doReturn("poiId").`when`(poi1).poiId
    }

    private fun resetRepository() {
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
    }

    private fun launchTestAndVerify(expectedResult: MutableList<Property>, incrementCaptorIndex: Boolean = true) {
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)

        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        if (incrementCaptorIndex) captorIndex +=1
        assertEquals(expectedResult, listArgumentCaptor.allValues[captorIndex])
    }

    @Test
    fun testOnClickMenu() {
        // Testing surface >= surfaceMin
        resetRepository()
        doReturn("50").`when`(searchDataRepository).surfaceMin
        launchTestAndVerify(expectedResult = properties, incrementCaptorIndex = false)

        // Testing surface < surfaceMin
        resetRepository()
        doReturn("150").`when`(searchDataRepository).surfaceMin
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing surface <= surfaceMax
        resetRepository()
        doReturn("150").`when`(searchDataRepository).surfaceMax
        launchTestAndVerify(expectedResult = properties)

        // Testing surface > surfaceMax
        resetRepository()
        doReturn("50").`when`(searchDataRepository).surfaceMax
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing nbOfRooms >= roomsMin
        resetRepository()
        doReturn("3").`when`(searchDataRepository).roomsMin
        launchTestAndVerify(expectedResult = properties)

        // Testing nbOfRooms < roomsMin
        resetRepository()
        doReturn("5").`when`(searchDataRepository).roomsMin
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing nbOfRooms <= roomsMax
        resetRepository()
        doReturn("5").`when`(searchDataRepository).roomsMax
        launchTestAndVerify(expectedResult = properties)

        // Testing nbOfRooms > roomsMax
        resetRepository()
        doReturn("3").`when`(searchDataRepository).roomsMax
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing nbOfBathrooms >= bathroomsMin
        resetRepository()
        doReturn("3").`when`(searchDataRepository).bathroomsMin
        launchTestAndVerify(expectedResult = properties)

        // Testing nbOfBathrooms < bathroomsMin
        resetRepository()
        doReturn("5").`when`(searchDataRepository).bathroomsMin
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing nbOfBathrooms <= bathroomsMax
        resetRepository()
        doReturn("5").`when`(searchDataRepository).bathroomsMax
        launchTestAndVerify(expectedResult = properties)

        // Testing nbOfBathrooms > bathroomsMax
        resetRepository()
        doReturn("3").`when`(searchDataRepository).bathroomsMax
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing nbOfBedrooms >= bedroomsMin
        resetRepository()
        doReturn("3").`when`(searchDataRepository).bedroomsMin
        launchTestAndVerify(expectedResult = properties)

        // Testing nbOfBedrooms < bedroomsMin
        resetRepository()
        doReturn("5").`when`(searchDataRepository).bedroomsMin
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing nbOfBedrooms <= bedroomsMax
        resetRepository()
        doReturn("5").`when`(searchDataRepository).bedroomsMax
        launchTestAndVerify(expectedResult = properties)

        // Testing nbOfBedrooms > bedroomsMax
        resetRepository()
        doReturn("3").`when`(searchDataRepository).bedroomsMax
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing price >= priceMin
        resetRepository()
        doReturn("50000").`when`(searchDataRepository).priceMin
        launchTestAndVerify(expectedResult = properties)

        // Testing price < priceMin
        resetRepository()
        doReturn("150000").`when`(searchDataRepository).priceMin
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing price <= priceMax
        resetRepository()
        doReturn("150000").`when`(searchDataRepository).priceMax
        launchTestAndVerify(expectedResult = properties)

        // Testing price > priceMax
        resetRepository()
        doReturn("50000").`when`(searchDataRepository).priceMax
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing description matching reference
        resetRepository()
        doReturn("description").`when`(searchDataRepository).description
        launchTestAndVerify(expectedResult = properties)

        // Testing description not matching reference
        resetRepository()
        doReturn("anotherDescription").`when`(searchDataRepository).description
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing zip matching reference
        resetRepository()
        doReturn("12345").`when`(searchDataRepository).zip
        launchTestAndVerify(expectedResult = properties)

        // Testing zip not matching reference
        resetRepository()
        doReturn("54321").`when`(searchDataRepository).zip
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing city matching reference
        resetRepository()
        doReturn("city").`when`(searchDataRepository).city
        launchTestAndVerify(expectedResult = properties)

        // Testing city not matching reference
        resetRepository()
        doReturn("otherCity").`when`(searchDataRepository).city
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing state matching reference
        resetRepository()
        doReturn("state").`when`(searchDataRepository).state
        launchTestAndVerify(expectedResult = properties)

        // Testing state not matching reference
        resetRepository()
        doReturn("otherState").`when`(searchDataRepository).state
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing country matching reference
        resetRepository()
        doReturn("country").`when`(searchDataRepository).country
        launchTestAndVerify(expectedResult = properties)

        // Testing country not matching reference
        resetRepository()
        doReturn("otherCountry").`when`(searchDataRepository).country
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing registrationDate >= registrationDateMin
        resetRepository()
        doReturn("2024-06-01").`when`(searchDataRepository).registrationDateMin
        launchTestAndVerify(expectedResult = properties)

        // Testing registrationDate < registrationDateMin
        resetRepository()
        doReturn("2024-06-30").`when`(searchDataRepository).registrationDateMin
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing registrationDate <= registrationDateMax
        resetRepository()
        doReturn("2024-06-30").`when`(searchDataRepository).registrationDateMax
        launchTestAndVerify(expectedResult = properties)

        // Testing registrationDate > registrationDateMax
        resetRepository()
        doReturn("2024-06-01").`when`(searchDataRepository).registrationDateMax
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing saleDate >= saleDateMin
        resetRepository()
        doReturn("2024-06-01").`when`(searchDataRepository).saleDateMin
        launchTestAndVerify(expectedResult = properties)

        // Testing saleDate < saleDateMin
        resetRepository()
        doReturn("2024-06-30").`when`(searchDataRepository).saleDateMin
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing saleDate <= saleDateMax
        resetRepository()
        doReturn("2024-06-30").`when`(searchDataRepository).saleDateMax
        launchTestAndVerify(expectedResult = properties)

        // Testing saleDate > saleDateMax
        resetRepository()
        doReturn("2024-06-01").`when`(searchDataRepository).saleDateMax
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing type matching reference
        resetRepository()
        doReturn("typeIsNotNull").`when`(searchDataRepository).type
        doReturn("typeId").`when`(type1).typeId
        launchTestAndVerify(expectedResult = properties)

        // Testing type not matching reference
        resetRepository()
        doReturn("typeIsNotNull").`when`(searchDataRepository).type
        doReturn("anotherTypeId").`when`(type1).typeId
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing agent matching reference
        resetRepository()
        doReturn("agentIsNotNull").`when`(searchDataRepository).agent
        doReturn(1L).`when`(agent1).agentId
        launchTestAndVerify(expectedResult = properties)

        // Testing agent not matching reference
        resetRepository()
        doReturn("agentIsNotNull").`when`(searchDataRepository).agent
        doReturn(2L).`when`(agent1).agentId
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing salesRadioIndex with saleDate not null and index = 2
        resetRepository()
        doReturn("2024-06-15").`when`(property1).saleDate
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        launchTestAndVerify(expectedResult = properties)

        // Testing salesRadioIndex with saleDate not null and index = 1
        resetRepository()
        doReturn("2024-06-15").`when`(property1).saleDate
        doReturn(1).`when`(searchDataRepository).salesRadioIndex
        launchTestAndVerify(expectedResult = properties)

        // Testing salesRadioIndex with saleDate not null and index = 0
        resetRepository()
        doReturn("2024-06-15").`when`(property1).saleDate
        doReturn(0).`when`(searchDataRepository).salesRadioIndex
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing salesRadioIndex with saleDate null and index = 2
        resetRepository()
        doReturn(null).`when`(property1).saleDate
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        launchTestAndVerify(expectedResult = properties)

        // Testing salesRadioIndex with saleDate null and index = 1
        resetRepository()
        doReturn(null).`when`(property1).saleDate
        doReturn(1).`when`(searchDataRepository).salesRadioIndex
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing salesRadioIndex with saleDate null and index = 0
        resetRepository()
        doReturn(null).`when`(property1).saleDate
        doReturn(0).`when`(searchDataRepository).salesRadioIndex
        launchTestAndVerify(expectedResult = properties)

        // Testing photosRadioIndex with at least one photo and index = 2
        resetRepository()
        doReturn(itemPhotos).`when`(photoRepository).itemPhotos(anyLong(), anyList())
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        launchTestAndVerify(expectedResult = properties)

        // Testing photosRadioIndex with at least one photo and index = 1
        resetRepository()
        doReturn(itemPhotos).`when`(photoRepository).itemPhotos(anyLong(), anyList())
        doReturn(1).`when`(searchDataRepository).photosRadioIndex
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing photosRadioIndex with at least one photo and index = 0
        resetRepository()
        doReturn(itemPhotos).`when`(photoRepository).itemPhotos(anyLong(), anyList())
        doReturn(0).`when`(searchDataRepository).photosRadioIndex
        launchTestAndVerify(expectedResult = properties)

        // Testing photosRadioIndex with no photo and index = 2
        resetRepository()
        doReturn(emptyPhotoList).`when`(photoRepository).itemPhotos(anyLong(), anyList())
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        launchTestAndVerify(expectedResult = properties)

        // Testing photosRadioIndex with no photo and index = 1
        resetRepository()
        doReturn(emptyPhotoList).`when`(photoRepository).itemPhotos(anyLong(), anyList())
        doReturn(1).`when`(searchDataRepository).photosRadioIndex
        launchTestAndVerify(expectedResult = properties)

        // Testing photosRadioIndex with no photo and index = 0
        resetRepository()
        doReturn(emptyPhotoList).`when`(photoRepository).itemPhotos(anyLong(), anyList())
        doReturn(0).`when`(searchDataRepository).photosRadioIndex
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing itemPois when propertyId matching and poiId matching
        resetRepository()
        doReturn(itemPois).`when`(searchDataRepository).itemPois
        doReturn(1L).`when`(propertyPoiJoin1).propertyId
        doReturn("poiId").`when`(propertyPoiJoin1).poiId
        launchTestAndVerify(expectedResult = properties)

        // Testing itemPois when propertyId matching and poiId not matching
        resetRepository()
        doReturn(itemPois).`when`(searchDataRepository).itemPois
        doReturn(1L).`when`(propertyPoiJoin1).propertyId
        doReturn("otherPoiId").`when`(propertyPoiJoin1).poiId
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing itemPois when propertyId not matching and poiId matching
        resetRepository()
        doReturn(itemPois).`when`(searchDataRepository).itemPois
        doReturn(2L).`when`(propertyPoiJoin1).propertyId
        doReturn("poiId").`when`(propertyPoiJoin1).poiId
        launchTestAndVerify(expectedResult = emptyPropertyList)

        // Testing itemPois when propertyId not matching and poiId not matching
        resetRepository()
        doReturn(itemPois).`when`(searchDataRepository).itemPois
        doReturn(2L).`when`(propertyPoiJoin1).propertyId
        doReturn("otherPoiId").`when`(propertyPoiJoin1).poiId
        launchTestAndVerify(expectedResult = emptyPropertyList)
    }

}