package com.aquaero.realestatemanager.viewModel_test

import android.content.Context
import android.util.Log
import com.aquaero.realestatemanager.DEFAULT_LIST_INDEX
import com.aquaero.realestatemanager.DEFAULT_RADIO_INDEX
import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.EditField
import com.aquaero.realestatemanager.MAX
import com.aquaero.realestatemanager.MIN
import com.aquaero.realestatemanager.model.Property
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
//    private lateinit var viewModelM: SearchViewModel
//    private lateinit var viewModelS: SearchViewModel
    private lateinit var viewModel: SearchViewModel

    private lateinit var stringArgumentCaptor: KArgumentCaptor<String?>
    private lateinit var stringArgumentCaptorNonNullable: KArgumentCaptor<String>
    private lateinit var intArgumentCaptor: KArgumentCaptor<Int>
    private lateinit var booleanArgumentCaptor: KArgumentCaptor<Boolean>
    private lateinit var listArgumentCaptor: KArgumentCaptor<MutableList<Property>>

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

//        viewModelM = SearchViewModel(addressRepository, photoRepository, mockRepository)
//        viewModelS = SearchViewModel(addressRepository, photoRepository, spyRepository)

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
        stringArgumentCaptorNonNullable = argumentCaptor()
        intArgumentCaptor = argumentCaptor()
        booleanArgumentCaptor = argumentCaptor()
        listArgumentCaptor = argumentCaptor()
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

    private fun <T> captureFunction2Argument(
        function: KFunction2<SearchRepository, T, Unit>,
        captor: KArgumentCaptor<T>,
    ) {
        initCaptors()
        doAnswer {
            println("Function ${function.name} called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchRepository).apply { function.invoke(this, captor.capture()) }
    }

    private fun captureFunctionArgument(
        function: KFunction<*>,
        vararg captors: KArgumentCaptor<*>,
    ) {
        initCaptors()
        doAnswer {
            println(
                "Function ${function.name} called with argument1: ${it.arguments[0]}, " +
                    "argument2: ${it.arguments[1]}, argument3: ${it.arguments[2]}"
            )
            it.callRealMethod()
        }.`when`(searchRepository).apply {
            function.call(this, *captors.map { it.capture() }.toTypedArray())
        }
    }

    private fun <T> launchOnClearButtonTestForFields(
        field: String,
        bound1: String? = null,
        bound2: String? = null,
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
        captureFunction2Argument(function1, captor1)
        if (function2 != null && captor2 != null) captureFunction2Argument(function2, captor2)
        initViewModel()

        val bounds: Boolean = bound1 != null && bound2 != null
        when (bounds) {
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
                viewModel.onClearButtonClick(field, MIN)
                viewModel.onClearButtonClick(field, MAX)

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
        expectedValue1: T?,
        expectedValue2: T?,
        valueFromRepository1: T?,
        valueFromRepository2: T?,
    ) {
        reset(searchRepository)
        setSpyMode(true)
        // Set captors
        captureFunctionArgument(function, *captors)
        initViewModel()

        // Function under test
        viewModel.onClearButtonClick(field)

        // Verifications and assertions
        verify(searchRepository).apply {
            function.call(this, field, expectedValue1, expectedValue2)
        }

        assertEquals(expectedValue1, valueFromRepository1)
        assertEquals(expectedValue2, valueFromRepository2)

        assertEquals(field, captors[0].allValues[0])
        assertEquals(expectedValue1, captors[1].allValues[0])
        assertEquals(expectedValue2, captors[2].allValues[0])
    }


    /**
     * Also testing clearSearchResults(), updateSearchResultsFlow(),
     * resetScrollToResults() and updateScrollToResultsFlow().
     */
    @Test
    fun testInit() {
        setSpyMode(true)
        // Set captor
        captureFunction2Argument(SearchRepository::updateScrollToResultsFlow, booleanArgumentCaptor)

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
        captureFunction2Argument(SearchRepository::setSalesRadioIndex, intArgumentCaptor)
        captureFunction2Argument(SearchRepository::setPhotosRadioIndex, intArgumentCaptor)
        initViewModel()

        // Function under test
        viewModel.resetData()

        // Through clearCriteria()
        verify(searchRepository).setSalesRadioIndex(DEFAULT_RADIO_INDEX)    // + test captor
        verify(searchRepository).setPhotosRadioIndex(DEFAULT_RADIO_INDEX)   // + test captor
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
            function1 = SearchRepository::setDescription, captor1 = stringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getDescription(),
        )
        launchOnClearButtonTestForFields(
            field = EditField.PRICE.name, bound1 = MIN, bound2 = MAX,
            function1 = SearchRepository::setPriceMin, captor1 = stringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getPriceMin(),
            function2 = SearchRepository::setPriceMax, captor2 = stringArgumentCaptor,
            expectedValue2 = null, valueFromRepository2 = searchRepository.getPriceMax(),
        )
        launchOnClearButtonTestForFields(
            field = EditField.SURFACE.name, bound1 = MIN, bound2 = MAX,
            function1 = SearchRepository::setSurfaceMin, captor1 = stringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getSurfaceMin(),
            function2 = SearchRepository::setSurfaceMax, captor2 = stringArgumentCaptor,
            expectedValue2 = null, valueFromRepository2 = searchRepository.getSurfaceMax(),
        )
        launchOnClearButtonTestForFields(
            field = EditField.ROOMS.name, bound1 = MIN, bound2 = MAX,
            function1 = SearchRepository::setRoomsMin, captor1 = stringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getRoomsMin(),
            function2 = SearchRepository::setRoomsMax, captor2 = stringArgumentCaptor,
            expectedValue2 = null, valueFromRepository2 = searchRepository.getRoomsMax(),
        )
        launchOnClearButtonTestForFields(
            field = EditField.BATHROOMS.name, bound1 = MIN, bound2 = MAX,
            function1 = SearchRepository::setBathroomsMin, captor1 = stringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getBathroomsMin(),
            function2 = SearchRepository::setBathroomsMax, captor2 = stringArgumentCaptor,
            expectedValue2 = null, valueFromRepository2 = searchRepository.getBathroomsMax(),
        )
        launchOnClearButtonTestForFields(
            field = EditField.BEDROOMS.name, bound1 = MIN, bound2 = MAX,
            function1 = SearchRepository::setBedroomsMin, captor1 = stringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getBedroomsMin(),
            function2 = SearchRepository::setBedroomsMax, captor2 = stringArgumentCaptor,
            expectedValue2 = null, valueFromRepository2 = searchRepository.getBedroomsMax(),
        )
        launchOnClearButtonTestForDropdownMenu(
            field = DropdownMenuCategory.TYPE.name, function = SearchRepository::setDropdownMenuCategory,
            captors = arrayOf(stringArgumentCaptorNonNullable, intArgumentCaptor, stringArgumentCaptor),
            expectedValue1 = DEFAULT_LIST_INDEX, expectedValue2 = null,
            valueFromRepository1 = searchRepository.getTypeIndex(), valueFromRepository2 = searchRepository.getType(),
        )
        launchOnClearButtonTestForDropdownMenu(
            field = DropdownMenuCategory.AGENT.name, function = SearchRepository::setDropdownMenuCategory,
            captors = arrayOf(stringArgumentCaptorNonNullable, intArgumentCaptor, stringArgumentCaptor),
            expectedValue1 = DEFAULT_LIST_INDEX, expectedValue2 = null,
            valueFromRepository1 = searchRepository.getAgentIndex(), valueFromRepository2 = searchRepository.getAgent(),
        )
        launchOnClearButtonTestForFields(
            field = EditField.ZIP_CODE.name,
            function1 = SearchRepository::setZip, captor1 = stringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getZip(),
        )
        launchOnClearButtonTestForFields(
            field = EditField.CITY.name,
            function1 = SearchRepository::setCity, captor1 = stringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getCity(),
        )
        launchOnClearButtonTestForFields(
            field = EditField.STATE.name,
            function1 = SearchRepository::setState, captor1 = stringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getState(),
        )
        launchOnClearButtonTestForFields(
            field = EditField.COUNTRY.name,
            function1 = SearchRepository::setCountry, captor1 = stringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getCountry(),
        )
        launchOnClearButtonTestForFields(
            field = EditField.REGISTRATION_DATE.name, bound1 = MIN, bound2 = MAX,
            function1 = SearchRepository::setRegistrationDateMin, captor1 = stringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getRegistrationDateMin(),
            function2 = SearchRepository::setRegistrationDateMax, captor2 = stringArgumentCaptor,
            expectedValue2 = null, valueFromRepository2 = searchRepository.getRegistrationDateMax(),
        )
        launchOnClearButtonTestForFields(
            field = EditField.SALE_DATE.name, bound1 = MIN, bound2 = MAX,
            function1 = SearchRepository::setSaleDateMin, captor1 = stringArgumentCaptor,
            expectedValue1 = null, valueFromRepository1 = searchRepository.getSaleDateMin(),
            function2 = SearchRepository::setSaleDateMax, captor2 = stringArgumentCaptor,
            expectedValue2 = null, valueFromRepository2 = searchRepository.getSaleDateMax(),
        )
    }




}