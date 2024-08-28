package com.aquaero.realestatemanager.viewModel_test

import android.util.Log
import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.EditField
import com.aquaero.realestatemanager.MAX
import com.aquaero.realestatemanager.MIN
import com.aquaero.realestatemanager.repository.AddressRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.SearchRepository
import com.aquaero.realestatemanager.utils.convertEuroToDollar
import com.aquaero.realestatemanager.viewmodel.SearchViewModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockedStatic
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyString
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.KArgumentCaptor
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.never
import org.mockito.kotlin.verifyNoMoreInteractions
import kotlin.reflect.KMutableProperty1

/*

@RunWith(MockitoJUnitRunner::class)
//@RunWith(RobolectricTestRunner::class)
/**
 * Testing onFieldValueChange() and onDropdownMenuValueChange()
 */
class SearchViewModelTestPart3 {
    private lateinit var addressRepository: AddressRepository
    private lateinit var photoRepository: PhotoRepository
    private lateinit var searchRepository: SearchRepository

    private lateinit var viewModel: SearchViewModel

    private lateinit var stringArgumentCaptor: KArgumentCaptor<String?>
    private lateinit var stringValue: String
    private lateinit var digitalValue: String
    private lateinit var emptyValue: String
    private lateinit var euro: String
    private lateinit var dollar: String

    private lateinit var stringTypes: MutableList<String>
    private lateinit var type: String
    private lateinit var stringAgents: MutableList<String>
    private lateinit var agent: String

    // Created to avoid class "Log" error when tests are run with coverage
    private lateinit var logMock: MockedStatic<Log>


    @Before
    fun setup() {
        // Initialize logMock
        logMock = mockStatic(Log::class.java)

        addressRepository = mock(AddressRepository::class.java)
        photoRepository = mock(PhotoRepository::class.java)
        searchRepository = mock(SearchRepository::class.java)
        stringTypes = mock()
        stringAgents = mock()

        viewModel = SearchViewModel(addressRepository, photoRepository, searchRepository)

        stringArgumentCaptor = argumentCaptor()
        stringValue = "fieldValue"
        digitalValue = "10"
        emptyValue = ""
        euro = "€"
        dollar = "$"

        type = "type"
        agent = "agent"
    }

    @After
    fun teardown() {
        // Close logMock
        logMock.close()
    }

    private fun <T> captureSetterArgument(
        setter: KMutableProperty1<SearchRepository, T>,
        captor: KArgumentCaptor<T>
    ) {
        doAnswer {
            println("Setter for ${setter.name} called with argument: ${it.arguments[0]}")
            it.callRealMethod()
        }.`when`(searchRepository).apply { setter.set(this, captor.capture()) }
    }

    private fun <T> setupFieldArguments(
        field: String, fieldType: String?, fieldValue: String,
        currency: String, captor: KArgumentCaptor<T>
    ): Pair<Triple<String, String?, String>, Pair<String, KArgumentCaptor<T>>> {
        return Pair(Triple(field, fieldType, fieldValue), Pair(currency, captor))
    }

    private fun <T> launchFieldTest(
        field: String,
        fieldType: String?,
        fieldValue: String,
        currency: String,
        captor: KArgumentCaptor<T>,
        setter: KMutableProperty1<SearchRepository, T>,
    ) {
        // Reset mock and captor
        reset(searchRepository)
        stringArgumentCaptor = argumentCaptor()
//        viewModel = SearchViewModel(addressRepository, photoRepository, searchDataRepository) // TODO : To be deleted

        val args = setupFieldArguments(field, fieldType, fieldValue, currency, captor)
        captureSetterArgument(setter, args.second.second)

        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)

        if (fieldValue == emptyValue ||
            (fieldValue == stringValue && fieldType != null &&
                    field != EditField.REGISTRATION_DATE.name && field != EditField.SALE_DATE.name)
        ) {
            assertNull(args.second.second.allValues[0])
            verify(searchRepository).apply { setter.set(this, null as T) }
        } else {
            if (field == EditField.PRICE.name && currency == euro) {
                assertEquals(convertEuroToDollar(args.first.third.toInt()).toString(), args.second.second.allValues[0])
                verify(searchRepository).apply { setter.set(this, convertEuroToDollar(args.first.third.toInt()).toString() as T) }
            } else {
                assertEquals(args.first.third, args.second.second.allValues[0])
                verify(searchRepository).apply { setter.set(this, args.first.third as T) }
            }
        }
        verifyNoMoreInteractions(searchRepository)
    }

    private fun <T> launchDropdownMenuTest(
        category: String,
        captor: KArgumentCaptor<T>,
        setter: KMutableProperty1<SearchRepository, T>? = null,
    ) {
        val index = 0
        val value = "$category#$index"

        // Reset mock and captor
        stringArgumentCaptor = argumentCaptor()
        reset(searchRepository)
        viewModel = SearchViewModel(addressRepository, photoRepository, searchRepository)

        doReturn(type).`when`(stringTypes).elementAt(anyInt())
        doReturn(agent).`when`(stringAgents).elementAt(anyInt())

        setter?.let { captureSetterArgument(it, captor) }

        viewModel.onDropdownMenuValueChange(value, stringTypes, stringAgents)

        when (category) {
            DropdownMenuCategory.TYPE.name -> {
                assertEquals(type, captor.allValues[0])
                verify(searchRepository).apply { SearchRepository::typeIndex.set(this, index) }
//                verify(searchDataRepository).apply { setter?.set(this, type as T) }
            }

            DropdownMenuCategory.AGENT.name -> {
                assertEquals(agent, captor.allValues[0])
                verify(searchRepository).apply { SearchRepository::agentIndex.set(this, index) }
//                verify(searchDataRepository).apply { setter?.set(this, agent as T) }
            }

            else -> {
                assert(captor.allValues.isEmpty())
                verify(searchRepository, never()).apply { SearchRepository::typeIndex.set(this, anyInt()) }
                verify(searchRepository, never()).apply { SearchRepository::agentIndex.set(this, anyInt()) }
                setter?.let { verify(searchRepository, never()).let { setter.set(it, anyString() as T) } }
            }
        }
    }


    @Test
    fun testOnFieldValueChange() {
        // Field surface min is digital
        launchFieldTest(
            EditField.SURFACE.name,
            MIN,
            digitalValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::surfaceMin
        )
        // Field surface max is digital
        launchFieldTest(
            EditField.SURFACE.name,
            MAX,
            digitalValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::surfaceMax
        )
        // Field surface min is not digital
        launchFieldTest(
            EditField.SURFACE.name,
            MIN,
            stringValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::surfaceMin
        )
        // Field surface max is not digital
        launchFieldTest(
            EditField.SURFACE.name,
            MAX,
            stringValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::surfaceMax
        )
        // Field surface min is empty
        launchFieldTest(
            EditField.SURFACE.name,
            MIN,
            emptyValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::surfaceMin
        )
        // Field surface max is empty
        launchFieldTest(
            EditField.SURFACE.name,
            MAX,
            emptyValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::surfaceMax
        )

        // Field rooms min is digital
        launchFieldTest(
            EditField.ROOMS.name,
            MIN,
            digitalValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::roomsMin
        )
        // Field rooms max is digital
        launchFieldTest(
            EditField.ROOMS.name,
            MAX,
            digitalValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::roomsMax
        )
        // Field rooms min is not digital
        launchFieldTest(
            EditField.ROOMS.name,
            MIN,
            stringValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::roomsMin
        )
        // Field rooms max is not digital
        launchFieldTest(
            EditField.ROOMS.name,
            MAX,
            stringValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::roomsMax
        )
        // Field rooms min is empty
        launchFieldTest(
            EditField.ROOMS.name,
            MIN,
            emptyValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::roomsMin
        )
        // Field rooms max is empty
        launchFieldTest(
            EditField.ROOMS.name,
            MAX,
            emptyValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::roomsMax
        )

        // Field bathrooms min is digital
        launchFieldTest(
            EditField.BATHROOMS.name,
            MIN,
            digitalValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::bathroomsMin
        )
        // Field bathrooms max is digital
        launchFieldTest(
            EditField.BATHROOMS.name,
            MAX,
            digitalValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::bathroomsMax
        )
        // Field bathrooms min is not digital
        launchFieldTest(
            EditField.BATHROOMS.name,
            MIN,
            stringValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::bathroomsMin
        )
        // Field bathrooms max is not digital
        launchFieldTest(
            EditField.BATHROOMS.name,
            MAX,
            stringValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::bathroomsMax
        )
        // Field bathrooms min is empty
        launchFieldTest(
            EditField.BATHROOMS.name,
            MIN,
            emptyValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::bathroomsMin
        )
        // Field bathrooms max is empty
        launchFieldTest(
            EditField.BATHROOMS.name,
            MAX,
            emptyValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::bathroomsMax
        )

        // Field bedrooms min is digital
        launchFieldTest(
            EditField.BEDROOMS.name,
            MIN,
            digitalValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::bedroomsMin
        )
        // Field bedrooms max is digital
        launchFieldTest(
            EditField.BEDROOMS.name,
            MAX,
            digitalValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::bedroomsMax
        )
        // Field bedrooms min is not digital
        launchFieldTest(
            EditField.BEDROOMS.name,
            MIN,
            stringValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::bedroomsMin
        )
        // Field bedrooms max is not digital
        launchFieldTest(
            EditField.BEDROOMS.name,
            MAX,
            stringValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::bedroomsMax
        )
        // Field bedrooms min is empty
        launchFieldTest(
            EditField.BEDROOMS.name,
            MIN,
            emptyValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::bedroomsMin
        )
        // Field bedrooms max is empty
        launchFieldTest(
            EditField.BEDROOMS.name,
            MAX,
            emptyValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::bedroomsMax
        )

        // Field price min is digital and currency is dollar
        launchFieldTest(
            EditField.PRICE.name,
            MIN,
            digitalValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::priceMin
        )
        // Field price max is digital and currency is dollar
        launchFieldTest(
            EditField.PRICE.name,
            MAX,
            digitalValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::priceMax
        )
        // Field price min is digital and currency is euro
        launchFieldTest(
            EditField.PRICE.name,
            MIN,
            digitalValue,
            euro,
            stringArgumentCaptor,
            SearchRepository::priceMin
        )
        // Field price max is digital and currency is euro
        launchFieldTest(
            EditField.PRICE.name,
            MAX,
            digitalValue,
            euro,
            stringArgumentCaptor,
            SearchRepository::priceMax
        )
        // Field price min is not digital
        launchFieldTest(
            EditField.PRICE.name,
            MIN,
            stringValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::priceMin
        )
        // Field price max is not digital
        launchFieldTest(
            EditField.PRICE.name,
            MAX,
            stringValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::priceMax
        )
        // Field price min is empty
        launchFieldTest(
            EditField.PRICE.name,
            MIN,
            emptyValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::priceMin
        )
        // Field price max is empty
        launchFieldTest(
            EditField.PRICE.name,
            MAX,
            emptyValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::priceMax
        )

        // Field description not empty
        launchFieldTest(
            EditField.DESCRIPTION.name,
            null,
            stringValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::description
        )
        // Field description empty
        launchFieldTest(
            EditField.DESCRIPTION.name,
            null,
            emptyValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::description
        )

        // Field city not empty
        launchFieldTest(
            EditField.CITY.name,
            null,
            stringValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::city
        )
        // Field city empty
        launchFieldTest(
            EditField.CITY.name,
            null,
            emptyValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::city
        )

        // Field state not empty
        launchFieldTest(
            EditField.STATE.name,
            null,
            stringValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::state
        )
        // Field state empty
        launchFieldTest(
            EditField.STATE.name,
            null,
            emptyValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::state
        )

        // Field zip code not empty
        launchFieldTest(
            EditField.ZIP_CODE.name,
            null,
            stringValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::zip
        )
        // Field zip code empty
        launchFieldTest(
            EditField.ZIP_CODE.name,
            null,
            emptyValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::zip
        )

        // Field country not empty
        launchFieldTest(
            EditField.COUNTRY.name,
            null,
            stringValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::country
        )
        // Field country empty
        launchFieldTest(
            EditField.COUNTRY.name,
            null,
            emptyValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::country
        )

        // Field registration date min not empty
        launchFieldTest(
            EditField.REGISTRATION_DATE.name,
            MIN,
            stringValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::registrationDateMin
        )
        // Field registration date max not empty
        launchFieldTest(
            EditField.REGISTRATION_DATE.name,
            MAX,
            stringValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::registrationDateMax
        )
        // Field registration date min empty
        launchFieldTest(
            EditField.REGISTRATION_DATE.name,
            MIN,
            emptyValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::registrationDateMin
        )
        // Field registration date max empty
        launchFieldTest(
            EditField.REGISTRATION_DATE.name,
            MAX,
            emptyValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::registrationDateMax
        )

        // Field sale date min not empty
        launchFieldTest(
            EditField.SALE_DATE.name,
            MIN,
            stringValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::saleDateMin
        )
        // Field sale date max not empty
        launchFieldTest(
            EditField.SALE_DATE.name,
            MAX,
            stringValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::saleDateMax
        )
        // Field sale date min empty
        launchFieldTest(
            EditField.SALE_DATE.name,
            MIN,
            emptyValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::saleDateMin
        )
        // Field sale date max empty
        launchFieldTest(
            EditField.SALE_DATE.name,
            MAX,
            emptyValue,
            dollar,
            stringArgumentCaptor,
            SearchRepository::saleDateMax
        )
    }

    @Test
    fun testOnDropdownMenuValueChange() {
        // Category Type
        launchDropdownMenuTest(
            DropdownMenuCategory.TYPE.name,
            stringArgumentCaptor,
            SearchRepository::type
        )
        // Category Agent
        launchDropdownMenuTest(
            DropdownMenuCategory.AGENT.name,
            stringArgumentCaptor,
            SearchRepository::agent
        )
        // Other category
        launchDropdownMenuTest("otherCategory", stringArgumentCaptor)
    }

}

*/