package com.aquaero.realestatemanager.viewModel_test

import android.util.Log
import com.aquaero.realestatemanager.DEFAULT_LIST_INDEX
import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.EditField
import com.aquaero.realestatemanager.MAX
import com.aquaero.realestatemanager.MIN
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
import org.mockito.kotlin.verify
import kotlin.reflect.KFunction

@RunWith(MockitoJUnitRunner::class)
//@RunWith(RobolectricTestRunner::class)
/**
 * Testing SearchViewModel
 */
class SearchViewModelTestPart2 {
    private lateinit var addressRepository: AddressRepository
    private lateinit var photoRepository: PhotoRepository
    private lateinit var mockRepository: SearchRepository
    private lateinit var spyRepository: SearchRepository
    private lateinit var searchRepository: SearchRepository
    private lateinit var viewModel: SearchViewModel

    private lateinit var nullableStringArgumentCaptor: KArgumentCaptor<String?>
    private lateinit var stringArgumentCaptor: KArgumentCaptor<String>
    private lateinit var intArgumentCaptor: KArgumentCaptor<Int>

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
}