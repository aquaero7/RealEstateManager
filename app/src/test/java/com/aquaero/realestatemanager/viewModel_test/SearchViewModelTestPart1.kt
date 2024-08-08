package com.aquaero.realestatemanager.viewModel_test

import android.content.Context
import android.util.Log
import com.aquaero.realestatemanager.DEFAULT_LIST_INDEX
import com.aquaero.realestatemanager.DEFAULT_RADIO_INDEX
import com.aquaero.realestatemanager.R
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockedStatic
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.mockingDetails
import org.mockito.Mockito.reset
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.KArgumentCaptor
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.times
import kotlin.reflect.KMutableProperty1

@RunWith(MockitoJUnitRunner::class)
//@RunWith(RobolectricTestRunner::class)
/**
 * Testing searchViewModel functions
 * except applyFilters(), onFieldValueChange() and onDropdownMenuValueChange()
 */
class SearchViewModelTestPart1 {
    private lateinit var addressRepository: AddressRepository
    private lateinit var photoRepository: PhotoRepository
    private lateinit var searchDataRepository: SearchDataRepository
    private lateinit var context: Context
    private lateinit var viewModel: SearchViewModel

    private lateinit var stringArgumentCaptor: KArgumentCaptor<String?>
    private lateinit var intArgumentCaptor: KArgumentCaptor<Int>
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
    private lateinit var spyItemPois: MutableList<Poi>
    private lateinit var poiId: String
    private lateinit var poi: Poi

    // Created to avoid class "LOG" error when tests are run with coverage
    private lateinit var logMock: MockedStatic<Log>

    @Before
    fun setup() {
        // Initialize logMock
        logMock = mockStatic(Log::class.java)

        addressRepository = mock(AddressRepository::class.java)
        photoRepository = mock(PhotoRepository::class.java)
        searchDataRepository = mock(SearchDataRepository::class.java)
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
        spyItemPois = spy(itemPois)

        poiId = "poiId"
        poi = Poi(poiId = poiId)

        viewModel = SearchViewModel(addressRepository, photoRepository, searchDataRepository)

        stringArgumentCaptor = argumentCaptor()
        intArgumentCaptor = argumentCaptor()
        listArgumentCaptor = argumentCaptor()

        doReturn(itemPois).`when`(searchDataRepository).itemPois
        doReturn(filteredList).`when`(searchDataRepository).filteredList
    }

     @After
     fun teardown() {
         // Close logMock
         logMock.close()
     }


    private fun <T> captureSetterArgument(
        setter: KMutableProperty1<SearchDataRepository, T>,
        captor: KArgumentCaptor<T>
    ) {
        doAnswer {
            println("Setter for ${setter.name} called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchDataRepository).apply { setter.set(this, captor.capture()) }
    }

    private fun launchPoiTest(isSelected: Boolean) {
        spyItemPois = spy(itemPois)
        doReturn(spyItemPois).`when`(searchDataRepository).itemPois

        viewModel.onPoiClick(poiId, isSelected)

        if (isSelected) {
            // The poi is selected
            verify(spyItemPois).add(poi)
            assertTrue(spyItemPois.contains(poi))
        } else {
            // The poi is unselected
            verify(spyItemPois).remove(poi)
            assertFalse(spyItemPois.contains(poi))
        }
    }

    private fun <T> launchRadioButtonTest(setter: KMutableProperty1<SearchDataRepository, T>) {
        val forSale = "For sale"
        val sold = "Sold"
        val withPhoto = "With photo"
        val withoutPhoto = "Without photo"
        val default = "Default"
        val salesButtons = listOf(forSale, sold, default)
        val photosButtons = listOf(withPhoto, withoutPhoto, default)
        val indexes = listOf(0, 1, DEFAULT_RADIO_INDEX)

        doReturn(forSale).`when`(context).getString(R.string.for_sale)
        doReturn(sold).`when`(context).getString(R.string.sold)
        doReturn(withPhoto).`when`(context).getString(R.string.with_photo)
        doReturn(withoutPhoto).`when`(context).getString(R.string.without_photo)

        indexes.forEach {
            intArgumentCaptor = argumentCaptor()
            captureSetterArgument(setter, intArgumentCaptor as KArgumentCaptor<T>)
            when (setter) {
                SearchDataRepository::salesRadioIndex -> viewModel.onSalesRadioButtonClick(context, salesButtons[it])
                SearchDataRepository::photosRadioIndex -> viewModel.onPhotosRadioButtonClick(context, photosButtons[it])
            }
            assertEquals(it, intArgumentCaptor.allValues[0])
        }
    }


    @Test
    fun testResetData() {
        /* Also testing clearCriteria() and onClearButtonClick() */

        // Capture setters arguments
        captureSetterArgument(SearchDataRepository::description, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::priceMin, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::priceMax, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::surfaceMin, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::surfaceMax, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::roomsMin, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::roomsMax, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bathroomsMin, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bathroomsMax, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bedroomsMin, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bedroomsMax, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::typeIndex, intArgumentCaptor)
        captureSetterArgument(SearchDataRepository::type, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::agentIndex, intArgumentCaptor)
        captureSetterArgument(SearchDataRepository::agent, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::zip, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::city, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::state, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::country, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::registrationDateMin, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::registrationDateMax, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::saleDateMin, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::saleDateMax, stringArgumentCaptor)

        assertEquals(1, searchDataRepository.itemPois.size)
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
        for (index: Int in 0..<stringArgumentCaptor.allValues.size) {
            assertNull(stringArgumentCaptor.allValues[index])
        }

        assertEquals(2, intArgumentCaptor.allValues.size)
        for (index: Int in 0..<intArgumentCaptor.allValues.size) {
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
        reset(searchDataRepository) // To reset invocations count during init
        captureSetterArgument(SearchDataRepository::scrollToResults, intArgumentCaptor)

        viewModel.resetScrollToResults()

        verify(searchDataRepository).scrollToResults = 0
        assertEquals(0, intArgumentCaptor.allValues[0])

        val invocations = mockingDetails(searchDataRepository).invocations
        val filteredInvocations = invocations.filter {
            it.method.name == "updateScrollToResultsFlow" && it.arguments[0] == 0
        }
        assertEquals(1, filteredInvocations.size)
    }

    @Test
    fun testOnClickMenu() {
        val scrollValue = searchDataRepository.scrollToResults  // Reference value to test increment
        reset(searchDataRepository)     // To reset invocations count (above and during init)

        captureSetterArgument(SearchDataRepository::searchResults, listArgumentCaptor)
        captureSetterArgument(SearchDataRepository::scrollToResults, intArgumentCaptor)

        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins)

        verify(searchDataRepository).searchResults = properties.toMutableList()
        assertEquals(properties.toMutableList(), listArgumentCaptor.allValues[0])

        verify(searchDataRepository, times(3)).scrollToResults
        verify(searchDataRepository).updateScrollToResultsFlow(anyInt())
        assertEquals(scrollValue + 1, intArgumentCaptor.allValues[0])
    }

    @Test
    fun testOnPoiClick() {
        // Test selected poi
        launchPoiTest(true)
        // Test unselected poi
        launchPoiTest(false)
    }

    @Test
    fun testOnSalesRadioButtonClick() {
        launchRadioButtonTest(SearchDataRepository::salesRadioIndex)
    }

    @Test
    fun testOnPhotosRadioButtonClick() {
        launchRadioButtonTest(SearchDataRepository::photosRadioIndex)
    }

    @Test
    fun testItemType() {
        val typeId = "typeId"
        val otherTypeId = "otherTypeId"
        val type = Type(typeId = typeId)
        val types = mutableListOf(type)
        val stringType = "stringType"
        val stringTypes = mutableListOf(stringType)
        val emptyString = ""
        val emptyStringTypes = mutableListOf<String>()

        // types contains typeId and stringTypes isn't empty
        assertEquals(stringType, viewModel.itemType(typeId, types, stringTypes))
        // types contains typeId and stringTypes is empty
        assertEquals(emptyString, viewModel.itemType(typeId, types, emptyStringTypes))
        // types doesn't contain typeId
        assertEquals(emptyString, viewModel.itemType(otherTypeId, types, stringTypes))
    }

}