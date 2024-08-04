package com.aquaero.realestatemanager.viewModel_test

import android.content.Context
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.DEFAULT_LIST_INDEX
import com.aquaero.realestatemanager.DEFAULT_RADIO_INDEX
import com.aquaero.realestatemanager.EditField
import com.aquaero.realestatemanager.MAX
import com.aquaero.realestatemanager.MIN
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
import com.aquaero.realestatemanager.utils.convertEuroToDollar
import com.aquaero.realestatemanager.viewmodel.SearchViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.mock
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mockingDetails
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import org.mockito.kotlin.KArgumentCaptor
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.times
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


    @Before
    fun setup() {
        addressRepository = mock(AddressRepository::class.java)
        photoRepository = mock(PhotoRepository::class.java)
        searchDataRepository = mock(SearchDataRepository::class.java)
//        navController = mock(NavHostController::class.java)
//        context = mock(Context::class.java)

        viewModel = SearchViewModel(addressRepository, photoRepository, searchDataRepository)

        stringArgumentCaptor = argumentCaptor()
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

    private fun resetRepoMockAndCaptor() {
        reset(searchDataRepository)
        stringArgumentCaptor = argumentCaptor()
    }


    @Test
    fun testOnFieldValueChange() {
        val stringValue = "fieldValue"
        val digitalValue = "0"
        val emptyValue = ""
        val euro = "€"
        val dollar = "$"


        // Field surface min is digital
        var args = setupFieldArguments(EditField.SURFACE.name, MIN, digitalValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::surfaceMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).surfaceMin = args.first.third

        // Field surface max is digital
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.SURFACE.name, MAX, digitalValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::surfaceMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).surfaceMax = args.first.third

        // Field surface min is not digital
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.SURFACE.name, MIN, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::surfaceMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).surfaceMin = null

        // Field surface max is not digital
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.SURFACE.name, MAX, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::surfaceMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).surfaceMax = null

        // Field surface min is empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.SURFACE.name, MIN, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::surfaceMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).surfaceMin = null

        // Field surface max is empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.SURFACE.name, MAX, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::surfaceMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).surfaceMax = null


        // Field rooms min is digital
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.ROOMS.name, MIN, digitalValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::roomsMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).roomsMin = args.first.third

        // Field rooms max is digital
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.ROOMS.name, MAX, digitalValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::roomsMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).roomsMax = args.first.third

        // Field rooms min is not digital
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.ROOMS.name, MIN, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::roomsMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).roomsMin = null

        // Field rooms max is not digital
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.ROOMS.name, MAX, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::roomsMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).roomsMax = null

        // Field rooms min is empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.ROOMS.name, MIN, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::roomsMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).roomsMin = null

        // Field rooms max is empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.ROOMS.name, MAX, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::roomsMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).roomsMax = null


        // Field bathrooms min is digital
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BATHROOMS.name, MIN, digitalValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bathroomsMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).bathroomsMin = args.first.third

        // Field bathrooms max is digital
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BATHROOMS.name, MAX, digitalValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bathroomsMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).bathroomsMax = args.first.third

        // Field bathrooms min is not digital
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BATHROOMS.name, MIN, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bathroomsMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).bathroomsMin = null

        // Field bathrooms max is not digital
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BATHROOMS.name, MAX, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bathroomsMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).bathroomsMax = null

        // Field bathrooms min is empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BATHROOMS.name, MIN, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bathroomsMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).bathroomsMin = null

        // Field bathrooms max is empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BATHROOMS.name, MAX, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bathroomsMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).bathroomsMax = null


        // Field bedrooms min is digital
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BEDROOMS.name, MIN, digitalValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bedroomsMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).bedroomsMin = args.first.third

        // Field bedrooms max is digital
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BEDROOMS.name, MAX, digitalValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bedroomsMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).bedroomsMax = args.first.third

        // Field bedrooms min is not digital
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BEDROOMS.name, MIN, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bedroomsMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).bedroomsMin = null

        // Field bedrooms max is not digital
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BEDROOMS.name, MAX, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bedroomsMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).bedroomsMax = null

        // Field bedrooms min is empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BEDROOMS.name, MIN, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bedroomsMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).bedroomsMin = null

        // Field bedrooms max is empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BEDROOMS.name, MAX, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bedroomsMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).bedroomsMax = null


        // Field price min is digital and currency is dollar
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.PRICE.name, MIN, digitalValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::priceMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).priceMin = args.first.third

        // Field price max is digital and currency is dollar
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.PRICE.name, MAX, digitalValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::priceMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).priceMax = args.first.third

        // Field price min is digital and currency is euro
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.PRICE.name, MIN, digitalValue, euro, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::priceMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(convertEuroToDollar(args.first.third.toInt()).toString(), args.second.second.allValues[0])
        verify(searchDataRepository).priceMin = convertEuroToDollar(args.first.third.toInt()).toString()

        // Field price max is digital and currency is euro
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.PRICE.name, MAX, digitalValue, euro, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::priceMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(convertEuroToDollar(args.first.third.toInt()).toString(), args.second.second.allValues[0])
        verify(searchDataRepository).priceMax = convertEuroToDollar(args.first.third.toInt()).toString()

        // Field price min is not digital
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.PRICE.name, MIN, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::priceMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).priceMin = null

        // Field price max is not digital
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.PRICE.name, MAX, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::priceMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).priceMax = null

        // Field price min is empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.PRICE.name, MIN, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::priceMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).priceMin = null

        // Field price max is empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.PRICE.name, MAX, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::priceMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).priceMax = null


        // Field description not empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.DESCRIPTION.name, null, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::description, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).description = args.first.third

        // Field description empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.DESCRIPTION.name, null, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::description, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).description = null


        // Field city not empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.CITY.name, null, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::city, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).city = args.first.third

        // Field city empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.CITY.name, null, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::city, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).city = null


        // Field state not empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.STATE.name, null, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::state, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).state = args.first.third

        // Field state empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.STATE.name, null, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::state, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).state = null


        // Field zip code not empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.ZIP_CODE.name, null, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::zip, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).zip = args.first.third

        // Field zip code empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.ZIP_CODE.name, null, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::zip, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).zip = null


        // Field country not empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.COUNTRY.name, null, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::country, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).country = args.first.third

        // Field country empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.COUNTRY.name, null, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::country, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).country = null


        // Field registration date min not empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.REGISTRATION_DATE.name, MIN, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::registrationDateMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).registrationDateMin = args.first.third

        // Field registration date max not empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.REGISTRATION_DATE.name, MAX, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::registrationDateMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).registrationDateMax = args.first.third

        // Field registration date min empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.REGISTRATION_DATE.name, MIN, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::registrationDateMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).registrationDateMin = null

        // Field registration date max empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.REGISTRATION_DATE.name, MAX, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::registrationDateMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).registrationDateMax = null


        // Field sale date min not empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.SALE_DATE.name, MIN, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::saleDateMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).saleDateMin = args.first.third

        // Field sale date max not empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.SALE_DATE.name, MAX, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::saleDateMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).saleDateMax = args.first.third

        // Field sale date min empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.SALE_DATE.name, MIN, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::saleDateMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).saleDateMin = null

        // Field sale date max empty
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.SALE_DATE.name, MAX, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::saleDateMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).saleDateMax = null
    }

}