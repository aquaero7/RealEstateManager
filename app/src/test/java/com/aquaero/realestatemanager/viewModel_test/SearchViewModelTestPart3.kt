package com.aquaero.realestatemanager.viewModel_test

import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.EditField
import com.aquaero.realestatemanager.MAX
import com.aquaero.realestatemanager.MIN
import com.aquaero.realestatemanager.repository.AddressRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.SearchDataRepository
import com.aquaero.realestatemanager.utils.convertEuroToDollar
import com.aquaero.realestatemanager.viewmodel.SearchViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.any
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyString
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import org.mockito.kotlin.KArgumentCaptor
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.never
import org.mockito.kotlin.verifyNoMoreInteractions
import org.robolectric.RobolectricTestRunner
import kotlin.reflect.KMutableProperty1

// @RunWith(MockitoJUnitRunner::class)
@RunWith(RobolectricTestRunner::class)
/**
 * Testing onFieldValueChange()
 */
class SearchViewModelTestPart3 {
    private lateinit var addressRepository: AddressRepository
    private lateinit var photoRepository: PhotoRepository
    private lateinit var searchDataRepository: SearchDataRepository
//    private lateinit var navController: NavHostController
//    private lateinit var context: Context
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


    @Before
    fun setup() {
        addressRepository = mock(AddressRepository::class.java)
        photoRepository = mock(PhotoRepository::class.java)
        searchDataRepository = mock(SearchDataRepository::class.java)
//        navController = mock(NavHostController::class.java)
//        context = mock(Context::class.java)
        stringTypes = mock()
        stringAgents = mock()

        viewModel = SearchViewModel(addressRepository, photoRepository, searchDataRepository)

        stringArgumentCaptor = argumentCaptor()
        stringValue = "fieldValue"
        digitalValue = "10"
        emptyValue = ""
        euro = "€"
        dollar = "$"

        type = "type"
        agent = "agent"
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
        setter: KMutableProperty1<SearchDataRepository, T>,
    ) {
        // Reset mock and captor
        reset(searchDataRepository)
        stringArgumentCaptor = argumentCaptor()
//        viewModel = SearchViewModel(addressRepository, photoRepository, searchDataRepository)

        val args = setupFieldArguments(field, fieldType, fieldValue, currency, captor)
        captureSetterArgument(setter, args.second.second)

        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)

        if (fieldValue == emptyValue ||
            (fieldValue == stringValue && fieldType != null &&
                    field != EditField.REGISTRATION_DATE.name && field != EditField.SALE_DATE.name)
        ) {
            assertNull(args.second.second.allValues[0])
            verify(searchDataRepository).apply { setter.set(this, null as T) }
        } else {
            if (field == EditField.PRICE.name && currency == euro) {
                assertEquals(convertEuroToDollar(args.first.third.toInt()).toString(), args.second.second.allValues[0])
                verify(searchDataRepository).apply { setter.set(this, convertEuroToDollar(args.first.third.toInt()).toString() as T) }
            } else {
                assertEquals(args.first.third, args.second.second.allValues[0])
                verify(searchDataRepository).apply { setter.set(this, args.first.third as T) }
            }
        }
        verifyNoMoreInteractions(searchDataRepository)
    }

    private fun <T> launchDropdownMenuTest(
        category: String,
        captor: KArgumentCaptor<T>,
        setter: KMutableProperty1<SearchDataRepository, T>? = null,
    ) {
        val index = 0
        val value = "$category#$index"

        // Reset mock and captor
        stringArgumentCaptor = argumentCaptor()
        reset(searchDataRepository)
        viewModel = SearchViewModel(addressRepository, photoRepository, searchDataRepository)

        doReturn(type).`when`(stringTypes).elementAt(anyInt())
        doReturn(agent).`when`(stringAgents).elementAt(anyInt())

        setter?.let { captureSetterArgument(it, captor) }

        viewModel.onDropdownMenuValueChange(value, stringTypes, stringAgents)

        when (category) {
            DropdownMenuCategory.TYPE.name -> {
                assertEquals(type, captor.allValues[0])
                verify(searchDataRepository).apply { SearchDataRepository::typeIndex.set(this, index) }
                verify(searchDataRepository).apply { setter?.set(this, type as T) }
            }
            DropdownMenuCategory.AGENT.name -> {
                assertEquals(agent, captor.allValues[0])
                verify(searchDataRepository).apply { SearchDataRepository::agentIndex.set(this, index) }
                verify(searchDataRepository).apply { setter?.set(this, agent as T) }
            }
            else -> {
                assert(captor.allValues.isEmpty())
                verify(searchDataRepository, never()).apply { SearchDataRepository::typeIndex.set(this, anyInt()) }
                verify(searchDataRepository, never()).apply { SearchDataRepository::agentIndex.set(this, anyInt()) }
                verify(searchDataRepository, never()).apply { setter?.set(this, anyString() as T) }
            }
        }
    }


    @Test
    fun testOnFieldValueChange() {
        // Field surface min is digital
        launchFieldTest(EditField.SURFACE.name, MIN, digitalValue, dollar, stringArgumentCaptor, SearchDataRepository::surfaceMin)
        // Field surface max is digital
        launchFieldTest(EditField.SURFACE.name, MAX, digitalValue, dollar, stringArgumentCaptor, SearchDataRepository::surfaceMax)
        // Field surface min is not digital
        launchFieldTest(EditField.SURFACE.name, MIN, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::surfaceMin)
        // Field surface max is not digital
        launchFieldTest(EditField.SURFACE.name, MAX, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::surfaceMax)
        // Field surface min is empty
        launchFieldTest(EditField.SURFACE.name, MIN, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::surfaceMin)
        // Field surface max is empty
        launchFieldTest(EditField.SURFACE.name, MAX, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::surfaceMax)

        // Field rooms min is digital
        launchFieldTest(EditField.ROOMS.name, MIN, digitalValue, dollar, stringArgumentCaptor, SearchDataRepository::roomsMin)
        // Field rooms max is digital
        launchFieldTest(EditField.ROOMS.name, MAX, digitalValue, dollar, stringArgumentCaptor, SearchDataRepository::roomsMax)
        // Field rooms min is not digital
        launchFieldTest(EditField.ROOMS.name, MIN, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::roomsMin)
        // Field rooms max is not digital
        launchFieldTest(EditField.ROOMS.name, MAX, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::roomsMax)
        // Field rooms min is empty
        launchFieldTest(EditField.ROOMS.name, MIN, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::roomsMin)
        // Field rooms max is empty
        launchFieldTest(EditField.ROOMS.name, MAX, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::roomsMax)

        // Field bathrooms min is digital
        launchFieldTest(EditField.BATHROOMS.name, MIN, digitalValue, dollar, stringArgumentCaptor, SearchDataRepository::bathroomsMin)
        // Field bathrooms max is digital
        launchFieldTest(EditField.BATHROOMS.name, MAX, digitalValue, dollar, stringArgumentCaptor, SearchDataRepository::bathroomsMax)
        // Field bathrooms min is not digital
        launchFieldTest(EditField.BATHROOMS.name, MIN, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::bathroomsMin)
        // Field bathrooms max is not digital
        launchFieldTest(EditField.BATHROOMS.name, MAX, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::bathroomsMax)
        // Field bathrooms min is empty
        launchFieldTest(EditField.BATHROOMS.name, MIN, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::bathroomsMin)
        // Field bathrooms max is empty
        launchFieldTest(EditField.BATHROOMS.name, MAX, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::bathroomsMax)

        // Field bedrooms min is digital
        launchFieldTest(EditField.BEDROOMS.name, MIN, digitalValue, dollar, stringArgumentCaptor, SearchDataRepository::bedroomsMin)
        // Field bedrooms max is digital
        launchFieldTest(EditField.BEDROOMS.name, MAX, digitalValue, dollar, stringArgumentCaptor, SearchDataRepository::bedroomsMax)
        // Field bedrooms min is not digital
        launchFieldTest(EditField.BEDROOMS.name, MIN, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::bedroomsMin)
        // Field bedrooms max is not digital
        launchFieldTest(EditField.BEDROOMS.name, MAX, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::bedroomsMax)
        // Field bedrooms min is empty
        launchFieldTest(EditField.BEDROOMS.name, MIN, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::bedroomsMin)
        // Field bedrooms max is empty
        launchFieldTest(EditField.BEDROOMS.name, MAX, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::bedroomsMax)

        // Field price min is digital and currency is dollar
        launchFieldTest(EditField.PRICE.name, MIN, digitalValue, dollar, stringArgumentCaptor, SearchDataRepository::priceMin)
        // Field price max is digital and currency is dollar
        launchFieldTest(EditField.PRICE.name, MAX, digitalValue, dollar, stringArgumentCaptor, SearchDataRepository::priceMax)
        // Field price min is digital and currency is euro
        launchFieldTest(EditField.PRICE.name, MIN, digitalValue, euro, stringArgumentCaptor, SearchDataRepository::priceMin)
        // Field price max is digital and currency is euro
        launchFieldTest(EditField.PRICE.name, MAX, digitalValue, euro, stringArgumentCaptor, SearchDataRepository::priceMax)
        // Field price min is not digital
        launchFieldTest(EditField.PRICE.name, MIN, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::priceMin)
        // Field price max is not digital
        launchFieldTest(EditField.PRICE.name, MAX, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::priceMax)
        // Field price min is empty
        launchFieldTest(EditField.PRICE.name, MIN, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::priceMin)
        // Field price max is empty
        launchFieldTest(EditField.PRICE.name, MAX, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::priceMax)

        // Field description not empty
        launchFieldTest(EditField.DESCRIPTION.name, null, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::description)
        // Field description empty
        launchFieldTest(EditField.DESCRIPTION.name, null, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::description)

        // Field city not empty
        launchFieldTest(EditField.CITY.name, null, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::city)
        // Field city empty
        launchFieldTest(EditField.CITY.name, null, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::city)

        // Field state not empty
        launchFieldTest(EditField.STATE.name, null, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::state)
        // Field state empty
        launchFieldTest(EditField.STATE.name, null, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::state)

        // Field zip code not empty
        launchFieldTest(EditField.ZIP_CODE.name, null, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::zip)
        // Field zip code empty
        launchFieldTest(EditField.ZIP_CODE.name, null, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::zip)

        // Field country not empty
        launchFieldTest(EditField.COUNTRY.name, null, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::country)
        // Field country empty
        launchFieldTest(EditField.COUNTRY.name, null, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::country)

        // Field registration date min not empty
        launchFieldTest(EditField.REGISTRATION_DATE.name, MIN, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::registrationDateMin)
        // Field registration date max not empty
        launchFieldTest(EditField.REGISTRATION_DATE.name, MAX, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::registrationDateMax)
        // Field registration date min empty
        launchFieldTest(EditField.REGISTRATION_DATE.name, MIN, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::registrationDateMin)
        // Field registration date max empty
        launchFieldTest(EditField.REGISTRATION_DATE.name, MAX, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::registrationDateMax)

        // Field sale date min not empty
        launchFieldTest(EditField.SALE_DATE.name, MIN, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::saleDateMin)
        // Field sale date max not empty
        launchFieldTest(EditField.SALE_DATE.name, MAX, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::saleDateMax)
        // Field sale date min empty
        launchFieldTest(EditField.SALE_DATE.name, MIN, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::saleDateMin)
        // Field sale date max empty
        launchFieldTest(EditField.SALE_DATE.name, MAX, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::saleDateMax)
    }

    @Test
    fun testOnDropdownMenuValueChange() {
        // Category Type
        launchDropdownMenuTest(DropdownMenuCategory.TYPE.name, stringArgumentCaptor, SearchDataRepository::type)
        // Category Agent
        launchDropdownMenuTest(DropdownMenuCategory.AGENT.name, stringArgumentCaptor, SearchDataRepository::agent)
        // Other category
        launchDropdownMenuTest("otherCategory", stringArgumentCaptor)
    }

}