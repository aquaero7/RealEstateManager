package com.aquaero.realestatemanager.viewModel_test

import android.content.Context
import android.util.Log
import com.aquaero.realestatemanager.DEFAULT_LIST_INDEX
import com.aquaero.realestatemanager.DEFAULT_RADIO_INDEX
import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.EditField
import com.aquaero.realestatemanager.MAX
import com.aquaero.realestatemanager.MIN
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
class SearchViewModelTest {
    private lateinit var addressRepository: AddressRepository
    private lateinit var photoRepository: PhotoRepository
    private lateinit var mockRepository: SearchRepository
    private lateinit var spyRepository: SearchRepository
    private lateinit var searchRepository: SearchRepository
    private lateinit var context: Context
    private lateinit var viewModel: SearchViewModel

    private lateinit var nullableStringArgumentCaptor: KArgumentCaptor<String?>
    private lateinit var stringArgumentCaptor: KArgumentCaptor<String>
    private lateinit var intArgumentCaptor: KArgumentCaptor<Int>
    private lateinit var booleanArgumentCaptor: KArgumentCaptor<Boolean>

    private lateinit var listArgumentCaptor: KArgumentCaptor<MutableList<*>>
    private lateinit var addressRepositoryArgumentCaptor: KArgumentCaptor<AddressRepository>
    private lateinit var photoRepositoryArgumentCaptor: KArgumentCaptor<PhotoRepository>
    private lateinit var poiCaptor: KArgumentCaptor<Poi>

    private lateinit var property1: Property
    private lateinit var property2: Property
    private lateinit var address1: Address
    private lateinit var type1: Type
    private lateinit var type2: Type
    private lateinit var agent1: Agent
    private lateinit var photo1: Photo
    private lateinit var poi1: Poi
    private lateinit var propertyPoiJoin1: PropertyPoiJoin
    private lateinit var types: MutableList<Type>
    private lateinit var stringTypes: MutableList<String>
    private lateinit var stringAgents: MutableList<String>

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
        context = mock(Context::class.java)

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
        type1 = Type(TypeEnum.UNASSIGNED.key)
        type2 = Type(TypeEnum.LOFT.key)
        agent1 = Agent(1, "firstName", "lastName")
        photo1 = Photo(1, "uri", "label", 2)
        poi1 = Poi("poi1")
        propertyPoiJoin1 = PropertyPoiJoin(1, PoiEnum.SHOP.key)
        types = mutableListOf(type1, type2)
        stringTypes = mutableListOf("type1", "type2")
        stringAgents = mutableListOf("agent1", "agent2")

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
        nullableStringArgumentCaptor = argumentCaptor()
        stringArgumentCaptor = argumentCaptor()
        intArgumentCaptor = argumentCaptor()
        booleanArgumentCaptor = argumentCaptor()
        listArgumentCaptor = argumentCaptor()
        addressRepositoryArgumentCaptor = argumentCaptor()
        photoRepositoryArgumentCaptor = argumentCaptor()
        poiCaptor = argumentCaptor()
    }

    private fun captureFunctionArgument(
        resetCaptors: Boolean,
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
     * For tests onClearButtonClick() and onFieldValueChange()
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T> launchTestsForFields(
        functionUnderTest: KFunction<*>,
        field: String,
        bounds: Array<String>? = null,
        currency: String = "$",
        fieldValue1: String = "",
        function1: KFunction<*>,
        captor1: KArgumentCaptor<T?>,
        expectedValue1: String?,
        getter1: KFunction<*>,
        fieldValue2: String? = "",
        function2: KFunction<*>? = null,
        captor2: KArgumentCaptor<T?>? = null,
        expectedValue2: String? = null,
        getter2: KFunction<*>? = null,
    ) {
        reset(searchRepository)
        setSpyMode(true)
        // Set captors
        captureFunctionArgument(true, function1, captor1)
        if (function2 != null && captor2 != null) captureFunctionArgument(true, function2, captor2)
        initViewModel()

        val values1 = if (functionUnderTest == SearchViewModel::onFieldValueChange)
            arrayOf(fieldValue1, currency) else arrayOf()
        val values2 = if (functionUnderTest == SearchViewModel::onFieldValueChange)
            arrayOf(fieldValue2, currency) else arrayOf()

        when (bounds != null) {
            false -> {
                // Function under test
                viewModel.apply { functionUnderTest.call(this, field, null, *values1) }

                // Verifications and assertions
                println("bounds is false => no bound provided")
                verify(searchRepository).apply { function1.call(this, expectedValue1 as T?) }

                assertEquals(expectedValue1, getter1.call(searchRepository))
                assertEquals(expectedValue1, captor1.allValues[0])
            }
            true -> {
                // Functions under test
                viewModel.apply { functionUnderTest.call(this, field, bounds[0], *values1) }
                viewModel.apply { functionUnderTest.call(this, field, bounds[1], *values2) }

                // Verifications and assertions
                println("bounds is true =>  bounds provided")
                if (function2 != null && captor2 != null && getter2 != null) {
                    verify(searchRepository).apply { function1.call(this, expectedValue1 as T?) }
                    verify(searchRepository).apply { function2.call(this, expectedValue2 as T?) }

                    assertEquals(expectedValue1, getter1.call(searchRepository))
                    assertEquals(expectedValue2, getter2.call(searchRepository))
                    assertEquals(expectedValue1, captor1.allValues[0])
                    assertEquals(expectedValue2, captor2.allValues[1])
                }
            }
        }
    }

    /**
     * For tests onClearButtonClick() and onFieldValueChange()
     */
    private fun <T> launchTestsForDropdownMenu(
        functionUnderTest: KFunction<*>,
        field: String = "",
        fieldValue: String = "",
        function: KFunction<*>,
        vararg captors: KArgumentCaptor<*>,
        expectedValues: Array<T?>,
        getters: Array<KFunction<*>>,
    ) {
        reset(searchRepository)
        setSpyMode(true)
        // Set captors
        captureFunctionArgument(true, function, *captors)
        initViewModel()

        val category = field.ifEmpty {
            fieldValue.substringBefore(delimiter = "#", missingDelimiterValue = "")
        }
        val values = if (functionUnderTest == SearchViewModel::onDropdownMenuValueChange)
            arrayOf(fieldValue, stringTypes, stringAgents) else arrayOf(category, null)

        // Function under test
        viewModel.apply { functionUnderTest.call(this, *values) }

        // Verifications and assertions
        verify(searchRepository).apply { function.call(this, category, *expectedValues) }

        val valuesFromRepository = with(searchRepository) {
            arrayOf(getters[0].call(this), getters[1].call(this))
        }
        assertEquals(expectedValues[0], valuesFromRepository[0])
        assertEquals(expectedValues[1], valuesFromRepository[1])

        assertEquals(category, captors[0].allValues[0])
        assertEquals(expectedValues[0], captors[1].allValues[0])
        assertEquals(expectedValues[1], captors[2].allValues[0])
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
        verify(searchRepository).updateItemPois(booleanArgumentCaptor.capture(), poiCaptor.capture())
        assertEquals(args[1], booleanArgumentCaptor.allValues[0])
        assertEquals(Poi(args[0].toString()), poiCaptor.allValues[0])
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
        captureFunctionArgument(false, SearchRepository::updateScrollToResultsFlow, booleanArgumentCaptor)

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
        captureFunctionArgument(false, SearchRepository::setSalesRadioIndex, intArgumentCaptor)
        captureFunctionArgument(false, SearchRepository::setPhotosRadioIndex, intArgumentCaptor)
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

    @Test
    fun testOnClearButtonClick() {
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onClearButtonClick,
            field = EditField.DESCRIPTION.name,
            function1 = SearchRepository::setDescription, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, getter1 = SearchRepository::getDescription,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onClearButtonClick,
            field = EditField.PRICE.name, bounds = arrayOf(MIN, MAX),
            function1 = SearchRepository::setPriceMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, getter1 = SearchRepository::getPriceMin,
            function2 = SearchRepository::setPriceMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = null, getter2 = SearchRepository::getPriceMax,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onClearButtonClick,
            field = EditField.SURFACE.name, bounds = arrayOf(MIN, MAX),
            function1 = SearchRepository::setSurfaceMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, getter1 = SearchRepository::getSurfaceMin,
            function2 = SearchRepository::setSurfaceMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = null, getter2 = SearchRepository::getSurfaceMax,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onClearButtonClick,
            field = EditField.ROOMS.name, bounds = arrayOf(MIN, MAX),
            function1 = SearchRepository::setRoomsMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, getter1 = SearchRepository::getRoomsMin,
            function2 = SearchRepository::setRoomsMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = null, getter2 = SearchRepository::getRoomsMax,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onClearButtonClick,
            field = EditField.BATHROOMS.name, bounds = arrayOf(MIN, MAX),
            function1 = SearchRepository::setBathroomsMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, getter1 = SearchRepository::getBathroomsMin,
            function2 = SearchRepository::setBathroomsMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = null, getter2 = SearchRepository::getBathroomsMax,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onClearButtonClick,
            field = EditField.BEDROOMS.name, bounds = arrayOf(MIN, MAX),
            function1 = SearchRepository::setBedroomsMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, getter1 = SearchRepository::getBedroomsMin,
            function2 = SearchRepository::setBedroomsMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = null, getter2 = SearchRepository::getBedroomsMax,
        )
        launchTestsForDropdownMenu(
            functionUnderTest = SearchViewModel::onClearButtonClick,
            field = DropdownMenuCategory.TYPE.name, function = SearchRepository::setDropdownMenuCategory,
            captors = arrayOf(stringArgumentCaptor, intArgumentCaptor, nullableStringArgumentCaptor),
            expectedValues = arrayOf(DEFAULT_LIST_INDEX, null),
            getters = arrayOf(SearchRepository::getTypeIndex, SearchRepository::getType),
        )
        launchTestsForDropdownMenu(
            functionUnderTest = SearchViewModel::onClearButtonClick,
            field = DropdownMenuCategory.AGENT.name, function = SearchRepository::setDropdownMenuCategory,
            captors = arrayOf(stringArgumentCaptor, intArgumentCaptor, nullableStringArgumentCaptor),
            expectedValues = arrayOf(DEFAULT_LIST_INDEX, null),
            getters = arrayOf(SearchRepository::getAgentIndex, SearchRepository::getAgent),
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onClearButtonClick,
            field = EditField.ZIP_CODE.name,
            function1 = SearchRepository::setZip, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, getter1 = SearchRepository::getZip,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onClearButtonClick,
            field = EditField.CITY.name,
            function1 = SearchRepository::setCity, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, getter1 = SearchRepository::getCity,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onClearButtonClick,
            field = EditField.STATE.name,
            function1 = SearchRepository::setState, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, getter1 = SearchRepository::getState,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onClearButtonClick,
            field = EditField.COUNTRY.name,
            function1 = SearchRepository::setCountry, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, getter1 = SearchRepository::getCountry,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onClearButtonClick,
            field = EditField.REGISTRATION_DATE.name, bounds = arrayOf(MIN, MAX),
            function1 = SearchRepository::setRegistrationDateMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, getter1 = SearchRepository::getRegistrationDateMin,
            function2 = SearchRepository::setRegistrationDateMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = null, getter2 = SearchRepository::getRegistrationDateMax,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onClearButtonClick,
            field = EditField.SALE_DATE.name, bounds = arrayOf(MIN, MAX),
            function1 = SearchRepository::setSaleDateMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, getter1 = SearchRepository::getSaleDateMin,
            function2 = SearchRepository::setSaleDateMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = null, getter2 = SearchRepository::getSaleDateMax,
        )
    }

    /**
     * Also testing updateSearchResults(), updateSearchResultsFlow() and updateScrollToResultsFlow()
     */
    @Suppress("UNCHECKED_CAST")
    @Test
    fun testOnClickMenu() {
        val properties = mutableListOf(property1)
        val addresses = mutableListOf(address1)
        val types = mutableListOf(type1)
        val agents = mutableListOf(agent1)
        val photos = mutableListOf(photo1)
        val propertyPoiJoins = mutableListOf(propertyPoiJoin1)
        val currency = "currency"
        val filteredList = mutableListOf(property2)

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
    fun testOnFieldValueChange() {
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onFieldValueChange,
            field = EditField.DESCRIPTION.name, fieldValue1 = "",
            function1 = SearchRepository::setDescription, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, getter1 = SearchRepository::getDescription,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onFieldValueChange,
            field = EditField.DESCRIPTION.name, fieldValue1 = "description",
            function1 = SearchRepository::setDescription, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = "description", getter1 = SearchRepository::getDescription,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onFieldValueChange,
            field = EditField.PRICE.name, bounds = arrayOf(MIN, MAX),
            fieldValue1 = "11", fieldValue2 = "22",
            function1 = SearchRepository::setPriceMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = "11", getter1 = SearchRepository::getPriceMin,
            function2 = SearchRepository::setPriceMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = "22", getter2 = SearchRepository::getPriceMax,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onFieldValueChange,
            field = EditField.PRICE.name, bounds = arrayOf(MIN, MAX),
            fieldValue1 = "x1x", fieldValue2 = "",
            function1 = SearchRepository::setPriceMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, getter1 = SearchRepository::getPriceMin,
            function2 = SearchRepository::setPriceMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = null, getter2 = SearchRepository::getPriceMax,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onFieldValueChange,
            field = EditField.SURFACE.name, bounds = arrayOf(MIN, MAX),
            fieldValue1 = "11", fieldValue2 = "22",
            function1 = SearchRepository::setSurfaceMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = "11", getter1 = SearchRepository::getSurfaceMin,
            function2 = SearchRepository::setSurfaceMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = "22", getter2 = SearchRepository::getSurfaceMax,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onFieldValueChange,
            field = EditField.SURFACE.name, bounds = arrayOf(MIN, MAX),
            fieldValue1 = "x1x", fieldValue2 = "",
            function1 = SearchRepository::setSurfaceMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, getter1 = SearchRepository::getSurfaceMin,
            function2 = SearchRepository::setSurfaceMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = null, getter2 = SearchRepository::getSurfaceMax,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onFieldValueChange,
            field = EditField.ROOMS.name, bounds = arrayOf(MIN, MAX),
            fieldValue1 = "11", fieldValue2 = "22",
            function1 = SearchRepository::setRoomsMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = "11", getter1 = SearchRepository::getRoomsMin,
            function2 = SearchRepository::setRoomsMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = "22", getter2 = SearchRepository::getRoomsMax,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onFieldValueChange,
            field = EditField.ROOMS.name, bounds = arrayOf(MIN, MAX),
            fieldValue1 = "x1x", fieldValue2 = "",
            function1 = SearchRepository::setRoomsMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, getter1 = SearchRepository::getRoomsMin,
            function2 = SearchRepository::setRoomsMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = null, getter2 = SearchRepository::getRoomsMax,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onFieldValueChange,
            field = EditField.BATHROOMS.name, bounds = arrayOf(MIN, MAX),
            fieldValue1 = "11", fieldValue2 = "22",
            function1 = SearchRepository::setBathroomsMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = "11", getter1 = SearchRepository::getBathroomsMin,
            function2 = SearchRepository::setBathroomsMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = "22", getter2 = SearchRepository::getBathroomsMax,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onFieldValueChange,
            field = EditField.BATHROOMS.name, bounds = arrayOf(MIN, MAX),
            fieldValue1 = "x1x", fieldValue2 = "",
            function1 = SearchRepository::setBathroomsMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, getter1 = SearchRepository::getBathroomsMin,
            function2 = SearchRepository::setBathroomsMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = null, getter2 = SearchRepository::getBathroomsMax,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onFieldValueChange,
            field = EditField.BEDROOMS.name, bounds = arrayOf(MIN, MAX),
            fieldValue1 = "11", fieldValue2 = "22",
            function1 = SearchRepository::setBedroomsMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = "11", getter1 = SearchRepository::getBedroomsMin,
            function2 = SearchRepository::setBedroomsMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = "22", getter2 = SearchRepository::getBedroomsMax,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onFieldValueChange,
            field = EditField.BEDROOMS.name, bounds = arrayOf(MIN, MAX),
            fieldValue1 = "x1x", fieldValue2 = "",
            function1 = SearchRepository::setBedroomsMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, getter1 = SearchRepository::getBedroomsMin,
            function2 = SearchRepository::setBedroomsMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = null, getter2 = SearchRepository::getBedroomsMax,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onFieldValueChange,
            field = EditField.ZIP_CODE.name, fieldValue1 = "zip",
            function1 = SearchRepository::setZip, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = "zip", getter1 = SearchRepository::getZip,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onFieldValueChange,
            field = EditField.ZIP_CODE.name, fieldValue1 = "",
            function1 = SearchRepository::setZip, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, getter1 = SearchRepository::getZip,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onFieldValueChange,
            field = EditField.CITY.name, fieldValue1 = "city",
            function1 = SearchRepository::setCity, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = "city", getter1 = SearchRepository::getCity,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onFieldValueChange,
            field = EditField.CITY.name, fieldValue1 = "",
            function1 = SearchRepository::setCity, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, getter1 = SearchRepository::getCity,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onFieldValueChange,
            field = EditField.STATE.name, fieldValue1 = "state",
            function1 = SearchRepository::setState, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = "state", getter1 = SearchRepository::getState,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onFieldValueChange,
            field = EditField.STATE.name, fieldValue1 = "",
            function1 = SearchRepository::setState, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, getter1 = SearchRepository::getState,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onFieldValueChange,
            field = EditField.COUNTRY.name, fieldValue1 = "country",
            function1 = SearchRepository::setCountry, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = "country", getter1 = SearchRepository::getCountry,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onFieldValueChange,
            field = EditField.COUNTRY.name, fieldValue1 = "",
            function1 = SearchRepository::setCountry, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, getter1 = SearchRepository::getCountry,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onFieldValueChange,
            field = EditField.REGISTRATION_DATE.name, bounds = arrayOf(MIN, MAX),
            fieldValue1 = "reg date", fieldValue2 = "",
            function1 = SearchRepository::setRegistrationDateMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = "reg date", getter1 = SearchRepository::getRegistrationDateMin,
            function2 = SearchRepository::setRegistrationDateMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = null, getter2 = SearchRepository::getRegistrationDateMax,
        )
        launchTestsForFields(
            functionUnderTest = SearchViewModel::onFieldValueChange,
            field = EditField.SALE_DATE.name, bounds = arrayOf(MIN, MAX),
            fieldValue1 = "sale date", fieldValue2 = "",
            function1 = SearchRepository::setSaleDateMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = "sale date", getter1 = SearchRepository::getSaleDateMin,
            function2 = SearchRepository::setSaleDateMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = null, getter2 = SearchRepository::getSaleDateMax,
        )
    }

    @Test
    fun testOnDropdownMenuValueChange() {
        launchTestsForDropdownMenu(
            functionUnderTest = SearchViewModel::onDropdownMenuValueChange,
            fieldValue = "${DropdownMenuCategory.TYPE.name}#1",
            function = SearchRepository::setDropdownMenuCategory,
            captors = arrayOf(stringArgumentCaptor, intArgumentCaptor, nullableStringArgumentCaptor),
            expectedValues = arrayOf(1, "type2"),
            getters = arrayOf(SearchRepository::getTypeIndex, SearchRepository::getType),
        )
        launchTestsForDropdownMenu(
            functionUnderTest = SearchViewModel::onDropdownMenuValueChange,
            fieldValue = "${DropdownMenuCategory.AGENT.name}#1",
            function = SearchRepository::setDropdownMenuCategory,
            captors = arrayOf(stringArgumentCaptor, intArgumentCaptor, nullableStringArgumentCaptor),
            expectedValues = arrayOf(1, "agent2"),
            getters = arrayOf(SearchRepository::getAgentIndex, SearchRepository::getAgent),
        )
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
        val typeId = TypeEnum.LOFT.key

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