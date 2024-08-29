package com.aquaero.realestatemanager.viewModel_test

import android.content.Context
import android.util.Log
import com.aquaero.realestatemanager.DEFAULT_LIST_INDEX
import com.aquaero.realestatemanager.DEFAULT_RADIO_INDEX
import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.EditField
import com.aquaero.realestatemanager.MAX
import com.aquaero.realestatemanager.MIN
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Photo
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
import kotlin.reflect.KFunction2

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

    private lateinit var property1: Property
    private lateinit var property2: Property
    private lateinit var address1: Address
    private lateinit var type1: Type
    private lateinit var agent1: Agent
    private lateinit var photo1: Photo
    private lateinit var propertyPoiJoin1: PropertyPoiJoin

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
        agent1 = Agent(1, "firstName", "lastName")
        photo1 = Photo(1, "uri", "label", 2)
        propertyPoiJoin1 = PropertyPoiJoin(1, PoiEnum.SHOP.key)

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
    }

    /*
    private fun <T> captureSetterArgument(
        setter: KMutableProperty1<SearchRepository, T>,
        captor: KArgumentCaptor<T>
    ) {
        doAnswer {
            println("Setter for ${setter.name} called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchRepository).apply { setter.set(this, captor.capture()) }
    }
    */

    /*
    private fun <T> captureFunction2Argument(
        resetCaptors: Boolean,
        function: KFunction2<SearchRepository, T, Unit>,
        captor: KArgumentCaptor<T>,
    ) {
        if (resetCaptors) initCaptors()
        doAnswer {
            println("Function ${function.name} called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchRepository).apply { function.invoke(this, captor.capture()) }
    }
    */

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

    @Suppress("UNCHECKED_CAST")
    private fun <T> launchOnClearButtonTestForFields(
        field: String,
        bounds: Array<String>? = null,
        function1: KFunction2<SearchRepository, T?, Unit>,
        captor1: KArgumentCaptor<T?>,
        expectedValue1: String?,
        valueFromRepository1: T?,
        function2: KFunction2<SearchRepository, T?, Unit>? = null,
        captor2: KArgumentCaptor<T?>? = null,
        expectedValue2: String? = null,
        valueFromRepository2: T? = null,
    ) {
        reset(searchRepository)
        setSpyMode(true)
        // Set captors
        captureFunctionArgument(true, function1, captor1)
        if (function2 != null && captor2 != null) captureFunctionArgument(true, function2, captor2)
        initViewModel()

        when (bounds != null) {
            false -> {
                // Function under test
                viewModel.onClearButtonClick(field)

                // Verifications and assertions
                println("bounds is false => no bound provided")
                verify(searchRepository).apply { function1.invoke(this, expectedValue1 as T?) }

                assertEquals(expectedValue1, valueFromRepository1)
                assertEquals(expectedValue1, captor1.allValues[0])
            }
            true -> {
                // Functions under test
                viewModel.onClearButtonClick(field, bounds[0])
                viewModel.onClearButtonClick(field, bounds[1])

                // Verifications and assertions
                println("bounds is true =>  bounds provided")
                if (function2 != null && captor2 != null) {
                    verify(searchRepository).apply { function1.invoke(this, expectedValue1 as T?) }
                    verify(searchRepository).apply { function2.invoke(this, expectedValue2 as T?) }

                    assertEquals(expectedValue1, valueFromRepository1)
                    assertEquals(expectedValue2, valueFromRepository2)
                    assertEquals(expectedValue1, captor1.allValues[0])
                    assertEquals(expectedValue2, captor2.allValues[1])
                }
            }
        }
    }

    private fun <T> launchOnClearButtonTestForDropdownMenu(
        field: String,
        function: KFunction<*>,
        vararg captors: KArgumentCaptor<*>,
        expectedValues: Array<T?>,
        valuesFromRepository: Array<T?>,
    ) {
        reset(searchRepository)
        setSpyMode(true)
        // Set captors
        captureFunctionArgument(true, function, *captors)
        initViewModel()

        // Function under test
        viewModel.onClearButtonClick(field)

        // Verifications and assertions
        verify(searchRepository).apply {
            function.call(this, field, *expectedValues)
        }

        assertEquals(expectedValues[0], valuesFromRepository[0])
        assertEquals(expectedValues[1], valuesFromRepository[1])

        assertEquals(field, captors[0].allValues[0])
        assertEquals(expectedValues[0], captors[1].allValues[0])
        assertEquals(expectedValues[1], captors[2].allValues[0])
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
        launchOnClearButtonTestForFields(
            field = EditField.DESCRIPTION.name,
            function1 = SearchRepository::setDescription, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getDescription(),
        )
        launchOnClearButtonTestForFields(
            field = EditField.PRICE.name, bounds = arrayOf(MIN, MAX),
            function1 = SearchRepository::setPriceMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getPriceMin(),
            function2 = SearchRepository::setPriceMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = null, valueFromRepository2 = searchRepository.getPriceMax(),
        )
        launchOnClearButtonTestForFields(
            field = EditField.SURFACE.name, bounds = arrayOf(MIN, MAX),
            function1 = SearchRepository::setSurfaceMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getSurfaceMin(),
            function2 = SearchRepository::setSurfaceMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = null, valueFromRepository2 = searchRepository.getSurfaceMax(),
        )
        launchOnClearButtonTestForFields(
            field = EditField.ROOMS.name, bounds = arrayOf(MIN, MAX),
            function1 = SearchRepository::setRoomsMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getRoomsMin(),
            function2 = SearchRepository::setRoomsMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = null, valueFromRepository2 = searchRepository.getRoomsMax(),
        )
        launchOnClearButtonTestForFields(
            field = EditField.BATHROOMS.name, bounds = arrayOf(MIN, MAX),
            function1 = SearchRepository::setBathroomsMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getBathroomsMin(),
            function2 = SearchRepository::setBathroomsMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = null, valueFromRepository2 = searchRepository.getBathroomsMax(),
        )
        launchOnClearButtonTestForFields(
            field = EditField.BEDROOMS.name, bounds = arrayOf(MIN, MAX),
            function1 = SearchRepository::setBedroomsMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getBedroomsMin(),
            function2 = SearchRepository::setBedroomsMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = null, valueFromRepository2 = searchRepository.getBedroomsMax(),
        )
        launchOnClearButtonTestForDropdownMenu(
            field = DropdownMenuCategory.TYPE.name, function = SearchRepository::setDropdownMenuCategory,
            captors = arrayOf(stringArgumentCaptor, intArgumentCaptor, nullableStringArgumentCaptor),
            expectedValues = arrayOf(DEFAULT_LIST_INDEX, null),
            valuesFromRepository = arrayOf(searchRepository.getTypeIndex(), searchRepository.getType()),
        )
        launchOnClearButtonTestForDropdownMenu(
            field = DropdownMenuCategory.AGENT.name, function = SearchRepository::setDropdownMenuCategory,
            captors = arrayOf(stringArgumentCaptor, intArgumentCaptor, nullableStringArgumentCaptor),
            expectedValues = arrayOf(DEFAULT_LIST_INDEX, null),
            valuesFromRepository = arrayOf(searchRepository.getAgentIndex(), searchRepository.getAgent()),
        )
        launchOnClearButtonTestForFields(
            field = EditField.ZIP_CODE.name,
            function1 = SearchRepository::setZip, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getZip(),
        )
        launchOnClearButtonTestForFields(
            field = EditField.CITY.name,
            function1 = SearchRepository::setCity, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getCity(),
        )
        launchOnClearButtonTestForFields(
            field = EditField.STATE.name,
            function1 = SearchRepository::setState, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getState(),
        )
        launchOnClearButtonTestForFields(
            field = EditField.COUNTRY.name,
            function1 = SearchRepository::setCountry, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getCountry(),
        )
        launchOnClearButtonTestForFields(
            field = EditField.REGISTRATION_DATE.name, bounds = arrayOf(MIN, MAX),
            function1 = SearchRepository::setRegistrationDateMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getRegistrationDateMin(),
            function2 = SearchRepository::setRegistrationDateMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = null, valueFromRepository2 = searchRepository.getRegistrationDateMax(),
        )
        launchOnClearButtonTestForFields(
            field = EditField.SALE_DATE.name, bounds = arrayOf(MIN, MAX),
            function1 = SearchRepository::setSaleDateMin, captor1 = nullableStringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getSaleDateMin(),
            function2 = SearchRepository::setSaleDateMax, captor2 = nullableStringArgumentCaptor,
            expectedValue2 = null, valueFromRepository2 = searchRepository.getSaleDateMax(),
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

        verify(searchRepository).updateSearchResultsFlow() // First triggered during viewmodel init{} but not taken into account

        verify(searchRepository, times(1)).updateScrollToResultsFlow(booleanArgumentCaptor.capture()) // First triggered during viewmodel init{} but not taken into account
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
        /* // TODO: To be deleted
        val properties = mutableListOf(property1)
        val addresses = mutableListOf(address1)
        val types = mutableListOf(type1)
        val agents = mutableListOf(agent1)
        val photos = mutableListOf(photo1)
        val propertyPoiJoins = mutableListOf(propertyPoiJoin1)
        val currency = "currency"

        setSpyMode(true)
        initViewModel()
        reset(searchRepository)

        // Function under test
        viewModel.onClickMenu(properties, addresses, types, agents, photos, propertyPoiJoins, currency)

        // Verifications and assertions
        verify(searchRepository, times(1)).updateSearchResultsFlow() // First triggered during init{}

        verify(searchRepository, times(1)).updateScrollToResultsFlow(booleanArgumentCaptor.capture()) // First triggered during init{}
        println("booleanArgumentCaptor size ${booleanArgumentCaptor.allValues.size}")
        assertEquals(1, booleanArgumentCaptor.allValues.size)
        assertEquals(true, booleanArgumentCaptor.allValues[0])
        */
        




    }






}