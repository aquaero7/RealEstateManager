package com.aquaero.realestatemanager.viewModel_test

import android.util.Log
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
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockedStatic
import org.mockito.Mockito.anyList
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.lenient
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.KArgumentCaptor
import org.mockito.kotlin.argumentCaptor


@RunWith(MockitoJUnitRunner::class)
//@RunWith(RobolectricTestRunner::class)
/**
 * Testing applyFilters() through onClickMenu()
 */
class SearchViewModelTestPart2 {
    private lateinit var addressRepository: AddressRepository
    private lateinit var photoRepository: PhotoRepository
    private lateinit var searchDataRepository: SearchDataRepository

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

    // Created to avoid class "LOG" error when tests are run with coverage
    private lateinit var logMock: MockedStatic<Log>

    @Before
    fun setup() {
        // Initialize logMock
        logMock = mockStatic(Log::class.java)

        addressRepository = mock(AddressRepository::class.java)
        photoRepository = mock(PhotoRepository::class.java)
        searchDataRepository = mock(SearchDataRepository::class.java)

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

    @After
    fun teardown() {
        // Close logMock
        logMock.close()
    }


    private fun <T, R> launchFilterTest(
        mock1: SearchDataRepository = searchDataRepository,
        mock2: Any? = null,
        getter1: (() -> T),
        getter2: (() -> T)? = null,
        getter2b: (() -> T)? = null,
        mockValue1: R,
        mockValue2: R? = null,
        mockValue2b: R? = null,
        expectedResult: MutableList<Property>,
        incrementCaptorIndex: Boolean = true
    ) {
        // Reset mocks
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex

        doReturn(mockValue1).`when`(mock1).apply { getter1.invoke() }
        mock2?.let {
            when (it) {
                property1, photoRepository -> {
                    // Using lenient strictness to avoid unnecessaryStubbingException
                    lenient().doReturn(mockValue2).`when`(it).apply { getter2?.invoke() }
                }
                type1, agent1 -> {
                    doReturn(mockValue2).`when`(it).apply { getter2?.invoke() }
                }
                propertyPoiJoin1 -> {
                    doReturn(mockValue2).`when`(it).apply { getter2?.invoke() }
                    // Using lenient strictness to avoid unnecessaryStubbingException
                    lenient().doReturn(mockValue2b).`when`(it).apply { getter2b?.invoke() }
                }
                else -> {}
            }
        }

        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)

        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        if (incrementCaptorIndex) captorIndex += 1
        assertEquals(expectedResult, listArgumentCaptor.allValues[captorIndex])
    }


    @Test
    fun testOnClickMenu() {
        // Testing surface >= surfaceMin
        launchFilterTest(
            getter1 = searchDataRepository::surfaceMin, mockValue1 = "50",
            expectedResult = properties, incrementCaptorIndex = false
        )

        // Testing surface < surfaceMin
        launchFilterTest(
            getter1 = searchDataRepository::surfaceMin,
            mockValue1 = "150",
            expectedResult = emptyPropertyList
        )

        // Testing surface <= surfaceMax
        launchFilterTest(
            getter1 = searchDataRepository::surfaceMax,
            mockValue1 = "150",
            expectedResult = properties
        )

        // Testing surface > surfaceMax
        launchFilterTest(
            getter1 = searchDataRepository::surfaceMax,
            mockValue1 = "50",
            expectedResult = emptyPropertyList
        )

        // Testing nbOfRooms >= roomsMin
        launchFilterTest(
            getter1 = searchDataRepository::roomsMin, mockValue1 = "3", expectedResult = properties
        )

        // Testing nbOfRooms < roomsMin
        launchFilterTest(
            getter1 = searchDataRepository::roomsMin,
            mockValue1 = "5",
            expectedResult = emptyPropertyList
        )

        // Testing nbOfRooms <= roomsMax
        launchFilterTest(
            getter1 = searchDataRepository::roomsMax, mockValue1 = "5", expectedResult = properties
        )

        // Testing nbOfRooms > roomsMax
        launchFilterTest(
            getter1 = searchDataRepository::roomsMax,
            mockValue1 = "3",
            expectedResult = emptyPropertyList
        )

        // Testing nbOfBathrooms >= bathroomsMin
        launchFilterTest(
            getter1 = searchDataRepository::bathroomsMin,
            mockValue1 = "3",
            expectedResult = properties
        )

        // Testing nbOfBathrooms < bathroomsMin
        launchFilterTest(
            getter1 = searchDataRepository::bathroomsMin,
            mockValue1 = "5",
            expectedResult = emptyPropertyList
        )

        // Testing nbOfBathrooms <= bathroomsMax
        launchFilterTest(
            getter1 = searchDataRepository::bathroomsMax,
            mockValue1 = "5",
            expectedResult = properties
        )

        // Testing nbOfBathrooms > bathroomsMax
        launchFilterTest(
            getter1 = searchDataRepository::bathroomsMax,
            mockValue1 = "3",
            expectedResult = emptyPropertyList
        )

        // Testing nbOfBedrooms >= bedroomsMin
        launchFilterTest(
            getter1 = searchDataRepository::bedroomsMin,
            mockValue1 = "3",
            expectedResult = properties
        )

        // Testing nbOfBedrooms < bedroomsMin
        launchFilterTest(
            getter1 = searchDataRepository::bedroomsMin,
            mockValue1 = "5",
            expectedResult = emptyPropertyList
        )

        // Testing nbOfBedrooms <= bedroomsMax
        launchFilterTest(
            getter1 = searchDataRepository::bedroomsMax,
            mockValue1 = "5",
            expectedResult = properties
        )

        // Testing nbOfBedrooms > bedroomsMax
        launchFilterTest(
            getter1 = searchDataRepository::bedroomsMax,
            mockValue1 = "3",
            expectedResult = emptyPropertyList
        )

        // Testing price >= priceMin
        launchFilterTest(
            getter1 = searchDataRepository::priceMin,
            mockValue1 = "50000",
            expectedResult = properties
        )

        // Testing price < priceMin
        launchFilterTest(
            getter1 = searchDataRepository::priceMin,
            mockValue1 = "150000",
            expectedResult = emptyPropertyList
        )

        // Testing price <= priceMax
        launchFilterTest(
            getter1 = searchDataRepository::priceMax,
            mockValue1 = "150000",
            expectedResult = properties
        )

        // Testing price > priceMax
        launchFilterTest(
            getter1 = searchDataRepository::priceMax,
            mockValue1 = "50000",
            expectedResult = emptyPropertyList
        )

        // Testing description matching reference
        launchFilterTest(
            getter1 = searchDataRepository::description,
            mockValue1 = "description",
            expectedResult = properties
        )

        // Testing description not matching reference
        launchFilterTest(
            getter1 = searchDataRepository::description,
            mockValue1 = "anotherDescription",
            expectedResult = emptyPropertyList
        )

        // Testing zip matching reference
        launchFilterTest(
            getter1 = searchDataRepository::zip, mockValue1 = "12345", expectedResult = properties
        )

        // Testing zip not matching reference
        launchFilterTest(
            getter1 = searchDataRepository::zip,
            mockValue1 = "54321",
            expectedResult = emptyPropertyList
        )

        // Testing city matching reference
        launchFilterTest(
            getter1 = searchDataRepository::city, mockValue1 = "city", expectedResult = properties
        )

        // Testing city not matching reference
        launchFilterTest(
            getter1 = searchDataRepository::city,
            mockValue1 = "otherCity",
            expectedResult = emptyPropertyList
        )

        // Testing state matching reference
        launchFilterTest(
            getter1 = searchDataRepository::state, mockValue1 = "state", expectedResult = properties
        )

        // Testing state not matching reference
        launchFilterTest(
            getter1 = searchDataRepository::state,
            mockValue1 = "otherState",
            expectedResult = emptyPropertyList
        )

        // Testing country matching reference
        launchFilterTest(
            getter1 = searchDataRepository::country,
            mockValue1 = "country",
            expectedResult = properties
        )

        // Testing country not matching reference
        launchFilterTest(
            getter1 = searchDataRepository::country,
            mockValue1 = "otherCountry",
            expectedResult = emptyPropertyList
        )

        // Testing registrationDate >= registrationDateMin
        launchFilterTest(
            getter1 = searchDataRepository::registrationDateMin,
            mockValue1 = "2024-06-01",
            expectedResult = properties
        )

        // Testing registrationDate < registrationDateMin
        launchFilterTest(
            getter1 = searchDataRepository::registrationDateMin,
            mockValue1 = "2024-06-30",
            expectedResult = emptyPropertyList
        )

        // Testing registrationDate <= registrationDateMax
        launchFilterTest(
            getter1 = searchDataRepository::registrationDateMax,
            mockValue1 = "2024-06-30",
            expectedResult = properties
        )

        // Testing registrationDate > registrationDateMax
        launchFilterTest(
            getter1 = searchDataRepository::registrationDateMax,
            mockValue1 = "2024-06-01",
            expectedResult = emptyPropertyList
        )

        // Testing saleDate >= saleDateMin
        launchFilterTest(
            getter1 = searchDataRepository::saleDateMin,
            mockValue1 = "2024-06-01",
            expectedResult = properties
        )

        // Testing saleDate < saleDateMin
        launchFilterTest(
            getter1 = searchDataRepository::saleDateMin,
            mockValue1 = "2024-06-30",
            expectedResult = emptyPropertyList
        )

        // Testing saleDate <= saleDateMax
        launchFilterTest(
            getter1 = searchDataRepository::saleDateMax,
            mockValue1 = "2024-06-30",
            expectedResult = properties
        )

        // Testing saleDate > saleDateMax
        launchFilterTest(
            getter1 = searchDataRepository::saleDateMax,
            mockValue1 = "2024-06-01",
            expectedResult = emptyPropertyList
        )

        // Testing type matching reference
        launchFilterTest(
            getter1 = searchDataRepository::type, mockValue1 = "typeIsNotNull",
            mock2 = type1, getter2 = type1::typeId, mockValue2 = "typeId",
            expectedResult = properties
        )

        // Testing type not matching reference
        launchFilterTest(
            getter1 = searchDataRepository::type, mockValue1 = "typeIsNotNull",
            mock2 = type1, getter2 = type1::typeId, mockValue2 = "anotherTypeId",
            expectedResult = emptyPropertyList
        )

        // Testing agent matching reference
        launchFilterTest(
            getter1 = searchDataRepository::agent, mockValue1 = "agentIsNotNull",
            mock2 = agent1, getter2 = agent1::agentId, mockValue2 = 1L,
            expectedResult = properties
        )

        // Testing agent not matching reference
        launchFilterTest(
            getter1 = searchDataRepository::agent, mockValue1 = "agentIsNotNull",
            mock2 = agent1, getter2 = agent1::agentId, mockValue2 = 2L,
            expectedResult = emptyPropertyList
        )

        // Testing salesRadioIndex with saleDate not null and index = 2
        launchFilterTest(
            getter1 = searchDataRepository::salesRadioIndex, mockValue1 = 2,
            mock2 = property1, getter2 = property1::saleDate, mockValue2 = "2024-06-15",
            expectedResult = properties
        )

        // Testing salesRadioIndex with saleDate not null and index = 1
        launchFilterTest(
            getter1 = searchDataRepository::salesRadioIndex, mockValue1 = 1,
            mock2 = property1, getter2 = property1::saleDate, mockValue2 = "2024-06-15",
            expectedResult = properties
        )

        // Testing salesRadioIndex with saleDate not null and index = 0
        launchFilterTest(
            getter1 = searchDataRepository::salesRadioIndex, mockValue1 = 0,
            mock2 = property1, getter2 = property1::saleDate, mockValue2 = "2024-06-15",
            expectedResult = emptyPropertyList
        )

        // Testing salesRadioIndex with saleDate null and index = 2
        launchFilterTest(
            getter1 = searchDataRepository::salesRadioIndex, mockValue1 = 2,
            mock2 = property1, getter2 = property1::saleDate, mockValue2 = null,
            expectedResult = properties
        )

        // Testing salesRadioIndex with saleDate null and index = 1
        launchFilterTest(
            getter1 = searchDataRepository::salesRadioIndex, mockValue1 = 1,
            mock2 = property1, getter2 = property1::saleDate, mockValue2 = null,
            expectedResult = emptyPropertyList
        )

        // Testing salesRadioIndex with saleDate null and index = 0
        launchFilterTest(
            getter1 = searchDataRepository::salesRadioIndex, mockValue1 = 0,
            mock2 = property1, getter2 = property1::saleDate, mockValue2 = null,
            expectedResult = properties
        )

        // Testing photosRadioIndex with at least one photo and index = 2
        launchFilterTest(
            getter1 = searchDataRepository::photosRadioIndex,
            mockValue1 = 2,
            mock2 = photoRepository,
            getter2 = { photoRepository.itemPhotos(anyLong(), anyList()) },
            mockValue2 = itemPhotos,
            expectedResult = properties
        )

        // Testing photosRadioIndex with at least one photo and index = 1
        launchFilterTest(
            getter1 = searchDataRepository::photosRadioIndex,
            mockValue1 = 1,
            mock2 = photoRepository,
            getter2 = { photoRepository.itemPhotos(anyLong(), anyList()) },
            mockValue2 = itemPhotos,
            expectedResult = emptyPropertyList
        )

        // Testing photosRadioIndex with at least one photo and index = 0
        launchFilterTest(
            getter1 = searchDataRepository::photosRadioIndex,
            mockValue1 = 0,
            mock2 = photoRepository,
            getter2 = { photoRepository.itemPhotos(anyLong(), anyList()) },
            mockValue2 = itemPhotos,
            expectedResult = properties
        )

        // Testing photosRadioIndex with no photo and index = 2
        launchFilterTest(
            getter1 = searchDataRepository::photosRadioIndex,
            mockValue1 = 2,
            mock2 = photoRepository,
            getter2 = { photoRepository.itemPhotos(anyLong(), anyList()) },
            mockValue2 = emptyPhotoList,
            expectedResult = properties
        )

        // Testing photosRadioIndex with no photo and index = 1
        launchFilterTest(
            getter1 = searchDataRepository::photosRadioIndex,
            mockValue1 = 1,
            mock2 = photoRepository,
            getter2 = { photoRepository.itemPhotos(anyLong(), anyList()) },
            mockValue2 = emptyPhotoList,
            expectedResult = properties
        )

        // Testing photosRadioIndex with no photo and index = 0
        launchFilterTest(
            getter1 = searchDataRepository::photosRadioIndex,
            mockValue1 = 0,
            mock2 = photoRepository,
            getter2 = { photoRepository.itemPhotos(anyLong(), anyList()) },
            mockValue2 = emptyPhotoList,
            expectedResult = emptyPropertyList
        )

        // Testing itemPois when propertyId matching and poiId matching
        launchFilterTest(
            getter1 = searchDataRepository::itemPois, mockValue1 = itemPois,
            mock2 = propertyPoiJoin1, getter2 = propertyPoiJoin1::propertyId, mockValue2 = 1L,
            getter2b = propertyPoiJoin1::poiId, mockValue2b = "poiId",
            expectedResult = properties
        )

        // Testing itemPois when propertyId matching and poiId not matching
        launchFilterTest(
            getter1 = searchDataRepository::itemPois, mockValue1 = itemPois,
            mock2 = propertyPoiJoin1, getter2 = propertyPoiJoin1::propertyId, mockValue2 = 1L,
            getter2b = propertyPoiJoin1::poiId, mockValue2b = "otherPoiId",
            expectedResult = emptyPropertyList
        )

        // Testing itemPois when propertyId not matching and poiId matching
        launchFilterTest(
            getter1 = searchDataRepository::itemPois, mockValue1 = itemPois,
            mock2 = propertyPoiJoin1, getter2 = propertyPoiJoin1::propertyId, mockValue2 = 2L,
            getter2b = propertyPoiJoin1::poiId, mockValue2b = "poiId",
            expectedResult = emptyPropertyList
        )

        // Testing itemPois when propertyId not matching and poiId not matching
        launchFilterTest(
            getter1 = searchDataRepository::itemPois, mockValue1 = itemPois,
            mock2 = propertyPoiJoin1, getter2 = propertyPoiJoin1::propertyId, mockValue2 = 2L,
            getter2b = propertyPoiJoin1::poiId, mockValue2b = "otherPoiId",
            expectedResult = emptyPropertyList
        )
    }

}