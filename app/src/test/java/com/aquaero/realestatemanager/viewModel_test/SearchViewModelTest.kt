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
class SearchViewModelTest {
    private lateinit var addressRepository: AddressRepository
    private lateinit var photoRepository: PhotoRepository
    private lateinit var searchDataRepository: SearchDataRepository
    private lateinit var navController: NavHostController
    private lateinit var context: Context
    private lateinit var viewModel: SearchViewModel

    private lateinit var stringArgumentCaptor: ArgumentCaptor<String>
    private lateinit var intArgumentCaptor: ArgumentCaptor<Int>
    private lateinit var listArgumentCaptor: KArgumentCaptor<MutableList<Property>>

    private lateinit var poi1: Poi
    private lateinit var property1: Property
    private lateinit var address1: Address
    private lateinit var type1: Type
    private lateinit var agent1: Agent
    private lateinit var photo1: Photo
    private lateinit var propertyPoiJoin1: PropertyPoiJoin

    private lateinit var addresses: MutableList<Address>
    private lateinit var types: MutableList<Type>
    private lateinit var agents: MutableList<Agent>
    private lateinit var photos: MutableList<Photo>
    private lateinit var itemPhotos: MutableList<Photo>

    private lateinit var emptyPropertyList: MutableList<Property>
    private lateinit var emptyPhotoList: MutableList<Photo>
    private lateinit var filteredList: MutableList<Property>
    private lateinit var properties: MutableList<Property>
    private lateinit var propertyPoiJoins: MutableList<PropertyPoiJoin>
    private lateinit var itemPois: MutableList<Poi>

    private val addressId1 = 1L


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
        photo1 = mock(Photo::class.java)
        propertyPoiJoin1 = mock(PropertyPoiJoin::class.java)
        addresses = mock()
        types = mock()
        agents = mock()
        photos = mock()
        itemPhotos = mock()

        emptyPropertyList = mutableListOf()
        emptyPhotoList = mutableListOf()
        filteredList = mutableListOf(property1)
        properties = mutableListOf(property1)
        propertyPoiJoins = mutableListOf(propertyPoiJoin1)
        itemPois = mutableListOf(poi1)

        viewModel = SearchViewModel(addressRepository, photoRepository, searchDataRepository)

        stringArgumentCaptor = ArgumentCaptor.captor()
        intArgumentCaptor = ArgumentCaptor.captor()
        listArgumentCaptor = argumentCaptor()
    }

    @Test
    fun testResetData() {
        /* Also testing clearCriteria() and onClearButtonClick() */

        // Capture setters arguments

        doAnswer {
            println("Setter for description called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).description = stringArgumentCaptor.capture()

        doAnswer {
            println("Setter for priceMin called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).priceMin = stringArgumentCaptor.capture()

        doAnswer {
            println("Setter for priceMax called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).priceMax = stringArgumentCaptor.capture()

        doAnswer {
            println("Setter for surfaceMin called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).surfaceMin = stringArgumentCaptor.capture()

        doAnswer {
            println("Setter for surfaceMax called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).surfaceMax = stringArgumentCaptor.capture()

        doAnswer {
            println("Setter for roomsMin called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).roomsMin = stringArgumentCaptor.capture()

        doAnswer {
            println("Setter for roomsMax called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).roomsMax = stringArgumentCaptor.capture()

        doAnswer {
            println("Setter for bathroomsMin called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).bathroomsMin = stringArgumentCaptor.capture()

        doAnswer {
            println("Setter for bathroomsMax called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).bathroomsMax = stringArgumentCaptor.capture()

        doAnswer {
            println("Setter for bedroomsMin called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).bedroomsMin = stringArgumentCaptor.capture()

        doAnswer {
            println("Setter for bedroomsMax called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).bedroomsMax = stringArgumentCaptor.capture()

        doAnswer {
            println("Setter for typeIndex called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).typeIndex = intArgumentCaptor.capture()

        doAnswer {
            println("Setter for type called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).type = stringArgumentCaptor.capture()

        doAnswer {
            println("Setter for agentIndex called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).agentIndex = intArgumentCaptor.capture()

        doAnswer {
            println("Setter for agent called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).agent = stringArgumentCaptor.capture()

        doAnswer {
            println("Setter for zip called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).zip = stringArgumentCaptor.capture()

        doAnswer {
            println("Setter for city called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).city = stringArgumentCaptor.capture()

        doAnswer {
            println("Setter for state called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).state = stringArgumentCaptor.capture()

        doAnswer {
            println("Setter for country called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).country = stringArgumentCaptor.capture()

        doAnswer {
            println("Setter for registrationDateMin called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).registrationDateMin = stringArgumentCaptor.capture()

        doAnswer {
            println("Setter for registrationDateMax called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).registrationDateMax = stringArgumentCaptor.capture()

        doAnswer {
            println("Setter for saleDateMin called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).saleDateMin = stringArgumentCaptor.capture()

        doAnswer {
            println("Setter for saleDateMax called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).saleDateMax = stringArgumentCaptor.capture()

        //

        doReturn(itemPois).`when`(searchDataRepository).itemPois
        assertEquals(1, searchDataRepository.itemPois.size)
        doReturn(filteredList).`when`(searchDataRepository).filteredList
        assertEquals(1, searchDataRepository.filteredList.size)

        viewModel.resetData()

        verify(searchDataRepository).description = null
        verify(searchDataRepository).priceMin = null
        verify(searchDataRepository).priceMax = null
        verify(searchDataRepository).surfaceMin = null
        verify(searchDataRepository).surfaceMax = null
        verify(searchDataRepository).roomsMin = null
        verify(searchDataRepository).roomsMax = null
        verify(searchDataRepository).bathroomsMin = null
        verify(searchDataRepository).bathroomsMax = null
        verify(searchDataRepository).bedroomsMin = null
        verify(searchDataRepository).bedroomsMax = null
        verify(searchDataRepository).typeIndex = DEFAULT_LIST_INDEX
        verify(searchDataRepository).type = null
        verify(searchDataRepository).agentIndex = DEFAULT_LIST_INDEX
        verify(searchDataRepository).agent = null
        verify(searchDataRepository).zip = null
        verify(searchDataRepository).city = null
        verify(searchDataRepository).state = null
        verify(searchDataRepository).country = null
        verify(searchDataRepository).registrationDateMin = null
        verify(searchDataRepository).registrationDateMax = null
        verify(searchDataRepository).saleDateMin = null
        verify(searchDataRepository).saleDateMax = null

        assertEquals(21, stringArgumentCaptor.allValues.size)
        for (index: Int in 0..< stringArgumentCaptor.allValues.size) {
            assertNull(stringArgumentCaptor.allValues[index])
        }

        assertEquals(2, intArgumentCaptor.allValues.size)
        for (index: Int in 0..< intArgumentCaptor.allValues.size) {
            assertEquals(DEFAULT_LIST_INDEX, intArgumentCaptor.allValues[index])
        }

        verify(searchDataRepository).salesRadioIndex = DEFAULT_RADIO_INDEX
        verify(searchDataRepository).photosRadioIndex = DEFAULT_RADIO_INDEX
        assertEquals(0, searchDataRepository.itemPois.size)
        assertEquals(0, searchDataRepository.filteredList.size)

        val invocations = mockingDetails(searchDataRepository).invocations
        val filteredInvocations = invocations.filter {
            it.method.name == "updateSearchResultsFlow" && it.arguments[0] === filteredList
        }
        assertEquals(1, filteredInvocations.size)
    }

    @Test
    fun testResetScrollToResults() {
        doAnswer {
            println("Setter for scrollToResults called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).scrollToResults = intArgumentCaptor.capture()

        viewModel.resetScrollToResults()

        verify(searchDataRepository, times(2)).scrollToResults = 0  // Also called during init
        assertEquals(0, intArgumentCaptor.allValues[0])

        val invocations = mockingDetails(searchDataRepository).invocations
        val filteredInvocations = invocations.filter {
            it.method.name == "updateScrollToResultsFlow" && it.arguments[0] == 0
        }
        assertEquals(2, filteredInvocations.size)   // Also called during init
    }

    @Test
    fun testOnClickMenu() {
        // Also testing applyFilters()

        val scrollValue = searchDataRepository.scrollToResults  // Reference value to test increment
        reset(searchDataRepository)     // To reset invocations count (above and during init)

        // Testing onClickMenu()

        doAnswer {
            println("Setter for searchResults called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).searchResults = listArgumentCaptor.capture()

        doAnswer {
            println("Setter for scrollToResults called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).scrollToResults = intArgumentCaptor.capture()

        var captorIndex = 0

        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)

        verify(searchDataRepository).searchResults = properties.toMutableList()
        assertEquals(properties.toMutableList(), listArgumentCaptor.allValues[captorIndex])

        verify(searchDataRepository, times(3)).scrollToResults
        verify(searchDataRepository).updateScrollToResultsFlow(anyInt())
        assertEquals(scrollValue + 1, intArgumentCaptor.allValues[0])



        // Testing applyFilters()

        // Testing surface >= surfaceMin
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("50").`when`(searchDataRepository).surfaceMin
        doReturn(100).`when`(property1).surface
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing surface < surfaceMin
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("150").`when`(searchDataRepository).surfaceMin
        doReturn(100).`when`(property1).surface
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing surface <= surfaceMax
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("150").`when`(searchDataRepository).surfaceMax
        doReturn(100).`when`(property1).surface
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing surface > surfaceMax
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("50").`when`(searchDataRepository).surfaceMax
        doReturn(100).`when`(property1).surface
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing nbOfRooms >= roomsMin
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("3").`when`(searchDataRepository).roomsMin
        doReturn(4).`when`(property1).nbOfRooms
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing nbOfRooms < roomsMin
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("5").`when`(searchDataRepository).roomsMin
        doReturn(4).`when`(property1).nbOfRooms
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing nbOfRooms <= roomsMax
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("5").`when`(searchDataRepository).roomsMax
        doReturn(4).`when`(property1).nbOfRooms
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing nbOfRooms > roomsMax
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("3").`when`(searchDataRepository).roomsMax
        doReturn(4).`when`(property1).nbOfRooms
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing nbOfBathrooms >= bathroomsMin
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("3").`when`(searchDataRepository).bathroomsMin
        doReturn(4).`when`(property1).nbOfBathrooms
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing nbOfBathrooms < bathroomsMin
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("5").`when`(searchDataRepository).bathroomsMin
        doReturn(4).`when`(property1).nbOfBathrooms
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing nbOfBathrooms <= bathroomsMax
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("5").`when`(searchDataRepository).bathroomsMax
        doReturn(4).`when`(property1).nbOfBathrooms
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing nbOfBathrooms > bathroomsMax
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("3").`when`(searchDataRepository).bathroomsMax
        doReturn(4).`when`(property1).nbOfBathrooms
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing nbOfBedrooms >= bedroomsMin
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("3").`when`(searchDataRepository).bedroomsMin
        doReturn(4).`when`(property1).nbOfBedrooms
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing nbOfBedrooms < bedroomsMin
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("5").`when`(searchDataRepository).bedroomsMin
        doReturn(4).`when`(property1).nbOfBedrooms
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing nbOfBedrooms <= bedroomsMax
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("5").`when`(searchDataRepository).bedroomsMax
        doReturn(4).`when`(property1).nbOfBedrooms
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing nbOfBedrooms > bedroomsMax
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("3").`when`(searchDataRepository).bedroomsMax
        doReturn(4).`when`(property1).nbOfBedrooms
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing price >= priceMin
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("50000").`when`(searchDataRepository).priceMin
        doReturn(100000).`when`(property1).price
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing price < priceMin
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("150000").`when`(searchDataRepository).priceMin
        doReturn(100000).`when`(property1).price
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing price <= priceMax
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("150000").`when`(searchDataRepository).priceMax
        doReturn(100000).`when`(property1).price
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing price > priceMax
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("50000").`when`(searchDataRepository).priceMax
        doReturn(100000).`when`(property1).price
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing description matching reference
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("description").`when`(searchDataRepository).description
        doReturn("Correct description of the property").`when`(property1).description
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing description not matching reference
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("description").`when`(searchDataRepository).description
        doReturn("Wrong desc. of the property").`when`(property1).description
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing zip matching reference
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("12345").`when`(searchDataRepository).zip
        doReturn(addressId1).`when`(property1).addressId
        doReturn(address1).`when`(addressRepository).addressFromId(addressId1, addresses)
        doReturn("12345").`when`(address1).zipCode
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing zip not matching reference
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("54321").`when`(searchDataRepository).zip
        doReturn(addressId1).`when`(property1).addressId
        doReturn(address1).`when`(addressRepository).addressFromId(addressId1, addresses)
        doReturn("12345").`when`(address1).zipCode
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing city matching reference
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("city").`when`(searchDataRepository).city
        doReturn(addressId1).`when`(property1).addressId
        doReturn(address1).`when`(addressRepository).addressFromId(addressId1, addresses)
        doReturn("city").`when`(address1).city
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing city not matching reference
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("otherCity").`when`(searchDataRepository).city
        doReturn(addressId1).`when`(property1).addressId
        doReturn(address1).`when`(addressRepository).addressFromId(addressId1, addresses)
        doReturn("city").`when`(address1).city
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing state matching reference
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("state").`when`(searchDataRepository).state
        doReturn(addressId1).`when`(property1).addressId
        doReturn(address1).`when`(addressRepository).addressFromId(addressId1, addresses)
        doReturn("state").`when`(address1).state
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing state not matching reference
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("otherState").`when`(searchDataRepository).state
        doReturn(addressId1).`when`(property1).addressId
        doReturn(address1).`when`(addressRepository).addressFromId(addressId1, addresses)
        doReturn("state").`when`(address1).state
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing country matching reference
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("country").`when`(searchDataRepository).country
        doReturn(addressId1).`when`(property1).addressId
        doReturn(address1).`when`(addressRepository).addressFromId(addressId1, addresses)
        doReturn("country").`when`(address1).country
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing country not matching reference
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("otherCountry").`when`(searchDataRepository).country
        doReturn(addressId1).`when`(property1).addressId
        doReturn(address1).`when`(addressRepository).addressFromId(addressId1, addresses)
        doReturn("country").`when`(address1).country
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing registrationDate >= registrationDateMin
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("2024-06-01").`when`(searchDataRepository).registrationDateMin
        doReturn("2024-06-15").`when`(property1).registrationDate
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing registrationDate < registrationDateMin
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("2024-06-30").`when`(searchDataRepository).registrationDateMin
        doReturn("2024-06-15").`when`(property1).registrationDate
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing registrationDate <= registrationDateMax
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("2024-06-30").`when`(searchDataRepository).registrationDateMax
        doReturn("2024-06-15").`when`(property1).registrationDate
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing registrationDate > registrationDateMax
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("2024-06-01").`when`(searchDataRepository).registrationDateMax
        doReturn("2024-06-15").`when`(property1).registrationDate
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing saleDate >= saleDateMin
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("2024-06-01").`when`(searchDataRepository).saleDateMin
        doReturn("2024-06-15").`when`(property1).saleDate
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing saleDate < saleDateMin
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("2024-06-30").`when`(searchDataRepository).saleDateMin
        doReturn("2024-06-15").`when`(property1).saleDate
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing saleDate <= saleDateMax
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("2024-06-30").`when`(searchDataRepository).saleDateMax
        doReturn("2024-06-15").`when`(property1).saleDate
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing saleDate > saleDateMax
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("2024-06-01").`when`(searchDataRepository).saleDateMax
        doReturn("2024-06-15").`when`(property1).saleDate
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing type matching reference
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("typeIsNotNull").`when`(searchDataRepository).type
        doReturn(type1).`when`(types).elementAt(0)
        doReturn("typeId").`when`(type1).typeId
        doReturn("typeId").`when`(property1).typeId
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing type not matching reference
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("typeIsNotNull").`when`(searchDataRepository).type
        doReturn(type1).`when`(types).elementAt(0)
        doReturn("anotherTypeId").`when`(type1).typeId
        doReturn("typeId").`when`(property1).typeId
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing agent matching reference
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("agentIsNotNull").`when`(searchDataRepository).agent
        doReturn(agent1).`when`(agents).elementAt(0)
        doReturn(1L).`when`(agent1).agentId
        doReturn(1L).`when`(property1).agentId
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing agent not matching reference
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("agentIsNotNull").`when`(searchDataRepository).agent
        doReturn(agent1).`when`(agents).elementAt(0)
        doReturn(2L).`when`(agent1).agentId
        doReturn(1L).`when`(property1).agentId
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing salesRadioIndex with saleDate not null and index = 2
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        doReturn("2024-06-15").`when`(property1).saleDate
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing salesRadioIndex with saleDate not null and index = 1
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        doReturn("2024-06-15").`when`(property1).saleDate
        doReturn(1).`when`(searchDataRepository).salesRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing salesRadioIndex with saleDate not null and index = 0
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        doReturn("2024-06-15").`when`(property1).saleDate
        doReturn(0).`when`(searchDataRepository).salesRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing salesRadioIndex with saleDate null and index = 2
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        doReturn(null).`when`(property1).saleDate
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing salesRadioIndex with saleDate null and index = 1
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        doReturn(null).`when`(property1).saleDate
        doReturn(1).`when`(searchDataRepository).salesRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing salesRadioIndex with saleDate null and index = 0
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        doReturn(null).`when`(property1).saleDate
        doReturn(0).`when`(searchDataRepository).salesRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing photosRadioIndex with at least one photo and index = 2
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(itemPhotos).`when`(photoRepository).itemPhotos(anyLong(), anyList())
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing photosRadioIndex with at least one photo and index = 1
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(itemPhotos).`when`(photoRepository).itemPhotos(anyLong(), anyList())
        doReturn(1).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing photosRadioIndex with at least one photo and index = 0
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(itemPhotos).`when`(photoRepository).itemPhotos(anyLong(), anyList())
        doReturn(0).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing photosRadioIndex with no photo and index = 2
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(emptyPhotoList).`when`(photoRepository).itemPhotos(anyLong(), anyList())
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing photosRadioIndex with no photo and index = 1
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(emptyPhotoList).`when`(photoRepository).itemPhotos(anyLong(), anyList())
        doReturn(1).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing photosRadioIndex with no photo and index = 0
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(emptyPhotoList).`when`(photoRepository).itemPhotos(anyLong(), anyList())
        doReturn(0).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing itemPois when propertyId matching and poiId matching
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        doReturn(itemPois).`when`(searchDataRepository).itemPois
        doReturn(1L).`when`(property1).propertyId
        doReturn(1L).`when`(propertyPoiJoin1).propertyId
        doReturn("poiId").`when`(propertyPoiJoin1).poiId
        doReturn("poiId").`when`(poi1).poiId
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

        // Testing itemPois when propertyId matching and poiId not matching
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        doReturn(itemPois).`when`(searchDataRepository).itemPois
        doReturn(1L).`when`(property1).propertyId
        doReturn(1L).`when`(propertyPoiJoin1).propertyId
        doReturn("otherPoiId").`when`(propertyPoiJoin1).poiId
        doReturn("poiId").`when`(poi1).poiId
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing itemPois when propertyId not matching and poiId matching
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        doReturn(itemPois).`when`(searchDataRepository).itemPois
        doReturn(1L).`when`(property1).propertyId
        doReturn(2L).`when`(propertyPoiJoin1).propertyId
        doReturn("poiId").`when`(propertyPoiJoin1).poiId
        doReturn("poiId").`when`(poi1).poiId
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        // Testing itemPois when propertyId not matching and poiId not matching
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        doReturn(itemPois).`when`(searchDataRepository).itemPois
        doReturn(1L).`when`(property1).propertyId
        doReturn(2L).`when`(propertyPoiJoin1).propertyId
        doReturn("otherPoiId").`when`(propertyPoiJoin1).poiId
        doReturn("poiId").`when`(poi1).poiId
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        captorIndex +=1
        assertEquals(emptyPropertyList, listArgumentCaptor.allValues[captorIndex])

        /* */





    }

    @Test
    fun testAny() {
        val emptyList = mutableListOf<Property>()
        val properties = mutableListOf(property1)
        val addresses = mutableListOf(address1)
        val types: MutableList<Type> = mock()
        val agents = mutableListOf(agent1)
        val photos = mutableListOf(photo1)
        val propertyPoiJoins = mutableListOf(propertyPoiJoin1)

        // Testing type matching reference
        reset(searchDataRepository)
        doReturn(properties).`when`(searchDataRepository).searchResults
        doReturn("typeIsNotNull").`when`(searchDataRepository).type
        doReturn(type1).`when`(types).elementAt(0)
        doReturn("typeId").`when`(type1).typeId               // Matching
//        doReturn("anotherTypeId").`when`(type1).typeId        // Not matching
        doReturn("typeId").`when`(property1).typeId
        doReturn(2).`when`(searchDataRepository).salesRadioIndex
        doReturn(2).`when`(searchDataRepository).photosRadioIndex
        //
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)
        //
        verify(searchDataRepository).updateSearchResultsFlow(listArgumentCaptor.capture())
        var captorIndex = 0
        assertEquals(properties, listArgumentCaptor.allValues[captorIndex])

    }




}