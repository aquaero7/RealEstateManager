package com.aquaero.realestatemanager.viewModel_test

import android.util.Log
import com.aquaero.realestatemanager.DEFAULT_RADIO_INDEX
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.RadioButtonCategory
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.PoiEnum
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.model.TypeEnum
import com.aquaero.realestatemanager.repository.AddressRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.SearchRepository
import com.aquaero.realestatemanager.viewmodel.SearchViewModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.KArgumentCaptor
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.reset
import org.mockito.kotlin.spy
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import kotlin.reflect.KFunction

@RunWith(MockitoJUnitRunner::class)
//@RunWith(RobolectricTestRunner::class)
/**
 * Testing SearchViewModel
 */
class SearchViewModelTestPart1 {
    private lateinit var addressRepository: AddressRepository
    private lateinit var photoRepository: PhotoRepository
    private lateinit var mockRepository: SearchRepository
    private lateinit var spyRepository: SearchRepository
    private lateinit var searchRepository: SearchRepository
    private lateinit var viewModel: SearchViewModel

    private lateinit var stringArgumentCaptor: KArgumentCaptor<String>
    private lateinit var intArgumentCaptor: KArgumentCaptor<Int>
    private lateinit var booleanArgumentCaptor: KArgumentCaptor<Boolean>

    private lateinit var listArgumentCaptor: KArgumentCaptor<MutableList<*>>
    private lateinit var addressRepositoryArgumentCaptor: KArgumentCaptor<AddressRepository>
    private lateinit var photoRepositoryArgumentCaptor: KArgumentCaptor<PhotoRepository>
    private lateinit var poiArgumentCaptor: KArgumentCaptor<Poi>

    private lateinit var property1: Property
    private lateinit var property2: Property
    private lateinit var address1: Address
    private lateinit var type1: Type
    private lateinit var agent1: Agent
    private lateinit var photo1: Photo
    private lateinit var poi1: Poi
    private lateinit var propertyPoiJoin1: PropertyPoiJoin
    private lateinit var properties: MutableList<Property>
    private lateinit var addresses: MutableList<Address>
    private lateinit var types: MutableList<Type>
    private lateinit var agents: MutableList<Agent>
    private lateinit var photos: MutableList<Photo>
    private lateinit var propertyPoiJoins: MutableList<PropertyPoiJoin>
    private lateinit var currency: String
    private lateinit var filteredList: MutableList<Property>
    private lateinit var stringTypes: MutableList<String>
    private lateinit var typeId: String

    // Created to avoid class "LOG" error when tests are run with coverage
    private lateinit var logMock: MockedStatic<Log>

    @Before
    fun setup() {
        // Initialize logMock
        logMock = Mockito.mockStatic(Log::class.java)

        addressRepository = mock(AddressRepository::class.java)
        photoRepository = mock(PhotoRepository::class.java)
        mockRepository = mock(SearchRepository::class.java)
        spyRepository = spy(SearchRepository())

        property1 = Property(
            1, TypeEnum.UNASSIGNED.name,
            null, null, null, null, null,
            null, null, null, null, 2
        )
        property2 = Property(
            2, TypeEnum.UNASSIGNED.name,
            null, null, null, null, null,
            null, null, null, null, 3
        )
        address1 = Address(
            1, null, null, null,
            null, null, null, null, null, null
        )
        type1 = Type(TypeEnum.LOFT.key)
        agent1 = Agent(1, "firstName", "lastName")
        photo1 = Photo(1, "uri", "label", 2)
        poi1 = Poi("poi1")
        propertyPoiJoin1 = PropertyPoiJoin(1, PoiEnum.SHOP.key)
        properties = mutableListOf(property1)
        addresses = mutableListOf(address1)
        types = mutableListOf(type1)
        agents = mutableListOf(agent1)
        photos = mutableListOf(photo1)
        propertyPoiJoins = mutableListOf(propertyPoiJoin1)
        currency = "currency"
        filteredList = mutableListOf(property2)
        stringTypes = mutableListOf("type1")
        typeId = TypeEnum.LOFT.key

        // Default
        setSpyMode(false)

        initViewModel()
        initCaptors()
    }

    @After
    fun teardown() {
        // Close logMock
        logMock.close()
    }

    /**
     * If false: The test will use searchViewModel with mockRepository (viewmodelM).
     * If true: The test will use  searchViewModel with spyRepository (viewModelS).
     */
    private fun setSpyMode(spyMode: Boolean) {
        searchRepository = if (spyMode) spyRepository else mockRepository
    }

    private fun initViewModel() {
        viewModel = SearchViewModel(addressRepository, photoRepository, searchRepository)
    }

    private fun initCaptors() {
        stringArgumentCaptor = argumentCaptor()
        intArgumentCaptor = argumentCaptor()
        booleanArgumentCaptor = argumentCaptor()
        listArgumentCaptor = argumentCaptor()
        addressRepositoryArgumentCaptor = argumentCaptor()
        photoRepositoryArgumentCaptor = argumentCaptor()
        poiArgumentCaptor = argumentCaptor()
    }

    private fun captureFunctionArgument(
        resetCaptors: Boolean = false,
        function: KFunction<*>,
        vararg captors: KArgumentCaptor<*>,
    ) {
        if (resetCaptors) initCaptors()
        doAnswer {
            println("Function ${function.name} called with argument1: ${it.arguments.toList()}")
            it.callRealMethod()
        }.`when`(searchRepository).apply {
            function.call(this, *captors.map { it.capture() }.toTypedArray())
        }
    }

    /**
     * For tests onPoiClick()
     */
    private fun launchTestsForPois(
        functionUnderTest: KFunction<*>,
        vararg args: Any,
        expectedPoisBefore: MutableList<Poi>,
        expectedPoisAfter: MutableList<Poi>,
    ) {
        setSpyMode(true)
        initViewModel()

        // Set and verify the pois list at the beginning of the test
        searchRepository.setItemPois(expectedPoisBefore)
        assertEquals(expectedPoisBefore, searchRepository.getItemPois())

        // Reset invocations and captors.
        // Also needed so that captors do not take into account the init of the viewmodel
        reset(searchRepository)
        initCaptors()

        // Function under test
        viewModel.apply { functionUnderTest.call(this, *args) }

        // Verifications and assertions
        verify(searchRepository).updateItemPois(booleanArgumentCaptor.capture(), poiArgumentCaptor.capture())
        assertEquals(args[1], booleanArgumentCaptor.allValues[0])
        assertEquals(Poi(args[0].toString()), poiArgumentCaptor.allValues[0])
        verify(searchRepository).getItemPois()
        assertEquals(expectedPoisAfter, searchRepository.getItemPois())
    }

    /**
     * For tests onRadioButtonClick()
     */
    private fun launchTestsForRadioButtons(
        functionUnderTest: KFunction<*>,
        vararg args: Any,
        function: KFunction<*>,
        expectedValueBefore: Int,
        expectedValueAfter: Int,
        getter: KFunction<*>,
    ) {
        setSpyMode(true)
        initViewModel()

        // Set and verify the index at the beginning of the test
        function.call(searchRepository, expectedValueBefore)
        assertEquals(expectedValueBefore, getter.call(searchRepository))

        // Reset invocations and captors.
        // Also needed so that captors do not take into account the init of the viewmodel
        reset(searchRepository)
        initCaptors()

        // Function under test
        viewModel.apply { functionUnderTest.call(this, *args) }

        // Verifications and assertions
        verify(searchRepository).apply { function.call(this, intArgumentCaptor.capture()) }
        assertEquals(expectedValueAfter, intArgumentCaptor.allValues[0])
        assertEquals(expectedValueAfter, getter.call(searchRepository))
    }


    /**
     * Also testing clearSearchResults(), updateSearchResultsFlow(),
     * resetScrollToResults() and updateScrollToResultsFlow().
     */
    @Test
    fun testInit() {
        setSpyMode(true)
        // Set captor
        captureFunctionArgument(
            function = SearchRepository::updateScrollToResultsFlow,
            captors = arrayOf(booleanArgumentCaptor)
        )

        // Init viewModel. That will trigger the viewModel init{} block
        initViewModel()

        verify(searchRepository).clearSearchResults()
        verify(searchRepository).updateSearchResultsFlow()
        verify(searchRepository).updateScrollToResultsFlow(scroll = false)

        assertEquals(0, searchRepository.getScrollToResultsStateFlow().value)
        assertEquals(false, booleanArgumentCaptor.allValues[0])
    }

    /**
     * Also testing clearCriteria(), clearSearchResults(), updateSearchResultsFlow()
     * and onClearButtonClick() that is tested apart.
     */
    @Test
    fun testResetData() {
        setSpyMode(true)
        // Set captors
        captureFunctionArgument(
            function = SearchRepository::setSalesRadioIndex,
            captors = arrayOf(intArgumentCaptor)
        )
        captureFunctionArgument(
            function = SearchRepository::setPhotosRadioIndex,
            captors = arrayOf(intArgumentCaptor)
        )
        initViewModel()

        // Function under test
        viewModel.resetData()

        // Through clearCriteria()
        verify(searchRepository).setSalesRadioIndex(DEFAULT_RADIO_INDEX)
        verify(searchRepository).setPhotosRadioIndex(DEFAULT_RADIO_INDEX)
        verify(searchRepository).clearItemPois()

        assertEquals(DEFAULT_RADIO_INDEX, searchRepository.getSalesRadioIndex())
        assertEquals(DEFAULT_RADIO_INDEX, intArgumentCaptor.allValues[0])
        assertEquals(DEFAULT_RADIO_INDEX, searchRepository.getPhotosRadioIndex())
        assertEquals(DEFAULT_RADIO_INDEX, intArgumentCaptor.allValues[1])

        // Through clearSearchResults() (triggered for the first time during viewModel init)
        verify(searchRepository, times(2)).clearSearchResults()

        // Through updateSearchResultsFlow() (triggered for the first time during viewModel init)
        verify(searchRepository, times(2)).updateSearchResultsFlow()
    }

    /**
     * Also testing updateSearchResults(), updateSearchResultsFlow() and updateScrollToResultsFlow()
     */
    @Suppress("UNCHECKED_CAST")
    @Test
    fun testOnClickMenu() {
        setSpyMode(true)
        initViewModel()
        reset(searchRepository) // Needed so that captors do not take into account the init of the viewmodel

        doReturn(filteredList).`when`(searchRepository).applyFilters(
            properties, addresses, types, agents, photos, propertyPoiJoins, currency,
            addressRepository, photoRepository
        )

        // Function under test
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins, currency)

        // Verifications and assertions
        verify(searchRepository).updateSearchResults(
            (listArgumentCaptor as KArgumentCaptor<MutableList<Property>>).capture()
        )
        println("listArgumentCaptor size ${listArgumentCaptor.allValues.size}")
        assertEquals(1, listArgumentCaptor.allValues.size)
        assertEquals(filteredList, listArgumentCaptor.allValues[0])

        // First triggered during viewmodel init but not taken into account with searchRepository reset
        verify(searchRepository).updateSearchResultsFlow()

        // First triggered during viewmodel init but not taken into account with searchRepository reset
        verify(searchRepository, times(1)).updateScrollToResultsFlow(booleanArgumentCaptor.capture())
        println("booleanArgumentCaptor size ${booleanArgumentCaptor.allValues.size}")
        assertEquals(1, booleanArgumentCaptor.allValues.size)
        assertEquals(true, booleanArgumentCaptor.allValues[0])

        // Through updateSearchResults()
        verify(searchRepository).applyFilters(
            (listArgumentCaptor as KArgumentCaptor<MutableList<Property>>).capture(),
            (listArgumentCaptor as KArgumentCaptor<MutableList<Address>>).capture(),
            (listArgumentCaptor as KArgumentCaptor<MutableList<Type>>).capture(),
            (listArgumentCaptor as KArgumentCaptor<MutableList<Agent>>).capture(),
            (listArgumentCaptor as KArgumentCaptor<MutableList<Photo>>).capture(),
            (listArgumentCaptor as KArgumentCaptor<MutableList<PropertyPoiJoin>>).capture(),
            stringArgumentCaptor.capture(),
            addressRepositoryArgumentCaptor.capture(),
            photoRepositoryArgumentCaptor.capture(),
        )
        println("listArgumentCaptor size ${listArgumentCaptor.allValues.size}")
        println("stringArgumentCaptor size ${stringArgumentCaptor.allValues.size}")
        println("addressRepositoryArgumentCaptor size ${addressRepositoryArgumentCaptor.allValues.size}")
        println("photoRepositoryArgumentCaptor size ${photoRepositoryArgumentCaptor.allValues.size}")
        assertEquals(7, listArgumentCaptor.allValues.size)
        assertEquals(1, stringArgumentCaptor.allValues.size)
        assertEquals(1, addressRepositoryArgumentCaptor.allValues.size)
        assertEquals(1, photoRepositoryArgumentCaptor.allValues.size)
        assertEquals(properties, listArgumentCaptor.allValues[1])
        assertEquals(addresses, listArgumentCaptor.allValues[2])
        assertEquals(types, listArgumentCaptor.allValues[3])
        assertEquals(agents, listArgumentCaptor.allValues[4])
        assertEquals(photos, listArgumentCaptor.allValues[5])
        assertEquals(propertyPoiJoins, listArgumentCaptor.allValues[6])
        assertEquals(currency, stringArgumentCaptor.allValues[0])
        assertEquals(addressRepository, addressRepositoryArgumentCaptor.allValues[0])
        assertEquals(photoRepository, photoRepositoryArgumentCaptor.allValues[0])
    }

    @Test
    fun testOnPoiClick() {
        launchTestsForPois(
            functionUnderTest = SearchViewModel::onPoiClick, args = arrayOf("poi1", true),
            expectedPoisBefore = mutableListOf(), expectedPoisAfter = mutableListOf(poi1),
        )
        launchTestsForPois(
            functionUnderTest = SearchViewModel::onPoiClick, args = arrayOf("poi1", false),
            expectedPoisBefore = mutableListOf(poi1), expectedPoisAfter = mutableListOf(),
        )
    }

    @Test
    fun testOnRadioButtonClick() {
        launchTestsForRadioButtons(
            functionUnderTest = SearchViewModel::onRadioButtonClick,
            args = arrayOf(RadioButtonCategory.SALES.name, R.string.sold),
            function = SearchRepository::setSalesRadioIndex,
            expectedValueBefore = DEFAULT_RADIO_INDEX, expectedValueAfter = 1,
            getter = SearchRepository::getSalesRadioIndex
        )
        launchTestsForRadioButtons(
            functionUnderTest = SearchViewModel::onRadioButtonClick,
            args = arrayOf(RadioButtonCategory.SALES.name, -1),
            function = SearchRepository::setSalesRadioIndex,
            expectedValueBefore = 1, expectedValueAfter = DEFAULT_RADIO_INDEX,
            getter = SearchRepository::getSalesRadioIndex
        )
        launchTestsForRadioButtons(
            functionUnderTest = SearchViewModel::onRadioButtonClick,
            args = arrayOf(RadioButtonCategory.PHOTOS.name, R.string.without_photo),
            function = SearchRepository::setPhotosRadioIndex,
            expectedValueBefore = DEFAULT_RADIO_INDEX, expectedValueAfter = 1,
            getter = SearchRepository::getPhotosRadioIndex
        )
        launchTestsForRadioButtons(
            functionUnderTest = SearchViewModel::onRadioButtonClick,
            args = arrayOf(RadioButtonCategory.PHOTOS.name, -1),
            function = SearchRepository::setPhotosRadioIndex,
            expectedValueBefore = 1, expectedValueAfter = DEFAULT_RADIO_INDEX,
            getter = SearchRepository::getPhotosRadioIndex
        )
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun testGetItemType() {
        // Function under test
        viewModel.getItemType(typeId, types, stringTypes)

        // Verifications and assertions
        verify(searchRepository).getItemType(
            stringArgumentCaptor.capture(),
            (listArgumentCaptor as KArgumentCaptor<MutableList<Type>>).capture(),
            (listArgumentCaptor as KArgumentCaptor<MutableList<String>>).capture()
        )

        assertEquals(typeId, stringArgumentCaptor.allValues[0])
        assertEquals(types, listArgumentCaptor.allValues[0])
        assertEquals(stringTypes, listArgumentCaptor.allValues[1])
    }
}