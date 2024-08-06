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
    private lateinit var stringValue: String
    private lateinit var digitalValue: String
    private lateinit var emptyValue: String
    private lateinit var euro: String
    private lateinit var dollar: String


    @Before
    fun setup() {
        addressRepository = mock(AddressRepository::class.java)
        photoRepository = mock(PhotoRepository::class.java)
        searchDataRepository = mock(SearchDataRepository::class.java)
//        navController = mock(NavHostController::class.java)
//        context = mock(Context::class.java)

        viewModel = SearchViewModel(addressRepository, photoRepository, searchDataRepository)

        stringArgumentCaptor = argumentCaptor()
        stringValue = "fieldValue"
        digitalValue = "10"
        emptyValue = ""
        euro = "€"
        dollar = "$"
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

    private fun <T> launchTest(
        field: String,
        fieldType: String?,
        fieldValue: String,
        currency: String,
        captor: KArgumentCaptor<T>,
        setter: KMutableProperty1<SearchDataRepository, T>,
    ) {
        reset(searchDataRepository)
        stringArgumentCaptor = argumentCaptor()
        val args = setupFieldArguments(field, fieldType, fieldValue, currency, captor)
        captureSetterArgument(setter, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
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
    }


    @Test
    fun testOnFieldValueChange() {
        // Field surface min is digital
        /*
        var args = setupFieldArguments(EditField.SURFACE.name, MIN, digitalValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::surfaceMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).surfaceMin = args.first.third
        */
        launchTest(EditField.SURFACE.name, MIN, digitalValue, dollar, stringArgumentCaptor, SearchDataRepository::surfaceMin)

        // Field surface max is digital
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.SURFACE.name, MAX, digitalValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::surfaceMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).surfaceMax = args.first.third
        */
        launchTest(EditField.SURFACE.name, MAX, digitalValue, dollar, stringArgumentCaptor, SearchDataRepository::surfaceMax)

        // Field surface min is not digital
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.SURFACE.name, MIN, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::surfaceMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).surfaceMin = null
        */
        launchTest(EditField.SURFACE.name, MIN, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::surfaceMin)

        // Field surface max is not digital
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.SURFACE.name, MAX, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::surfaceMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).surfaceMax = null
        */
        launchTest(EditField.SURFACE.name, MAX, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::surfaceMax)

        // Field surface min is empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.SURFACE.name, MIN, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::surfaceMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).surfaceMin = null
        */
        launchTest(EditField.SURFACE.name, MIN, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::surfaceMin)

        // Field surface max is empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.SURFACE.name, MAX, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::surfaceMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).surfaceMax = null
        */
        launchTest(EditField.SURFACE.name, MAX, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::surfaceMax)


        // Field rooms min is digital
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.ROOMS.name, MIN, digitalValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::roomsMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).roomsMin = args.first.third
        */
        launchTest(EditField.ROOMS.name, MIN, digitalValue, dollar, stringArgumentCaptor, SearchDataRepository::roomsMin)

        // Field rooms max is digital
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.ROOMS.name, MAX, digitalValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::roomsMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).roomsMax = args.first.third
        */
        launchTest(EditField.ROOMS.name, MAX, digitalValue, dollar, stringArgumentCaptor, SearchDataRepository::roomsMax)

        // Field rooms min is not digital
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.ROOMS.name, MIN, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::roomsMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).roomsMin = null
        */
        launchTest(EditField.ROOMS.name, MIN, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::roomsMin)

        // Field rooms max is not digital
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.ROOMS.name, MAX, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::roomsMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).roomsMax = null
        */
        launchTest(EditField.ROOMS.name, MAX, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::roomsMax)

        // Field rooms min is empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.ROOMS.name, MIN, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::roomsMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).roomsMin = null
        */
        launchTest(EditField.ROOMS.name, MIN, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::roomsMin)

        // Field rooms max is empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.ROOMS.name, MAX, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::roomsMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).roomsMax = null
        */
        launchTest(EditField.ROOMS.name, MAX, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::roomsMax)


        // Field bathrooms min is digital
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BATHROOMS.name, MIN, digitalValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bathroomsMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).bathroomsMin = args.first.third
        */
        launchTest(EditField.BATHROOMS.name, MIN, digitalValue, dollar, stringArgumentCaptor, SearchDataRepository::bathroomsMin)

        // Field bathrooms max is digital
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BATHROOMS.name, MAX, digitalValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bathroomsMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).bathroomsMax = args.first.third
        */
        launchTest(EditField.BATHROOMS.name, MAX, digitalValue, dollar, stringArgumentCaptor, SearchDataRepository::bathroomsMax)

        // Field bathrooms min is not digital
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BATHROOMS.name, MIN, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bathroomsMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).bathroomsMin = null
        */
        launchTest(EditField.BATHROOMS.name, MIN, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::bathroomsMin)

        // Field bathrooms max is not digital
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BATHROOMS.name, MAX, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bathroomsMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).bathroomsMax = null
        */
        launchTest(EditField.BATHROOMS.name, MAX, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::bathroomsMax)

        // Field bathrooms min is empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BATHROOMS.name, MIN, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bathroomsMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).bathroomsMin = null
        */
        launchTest(EditField.BATHROOMS.name, MIN, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::bathroomsMin)

        // Field bathrooms max is empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BATHROOMS.name, MAX, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bathroomsMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).bathroomsMax = null
        */
        launchTest(EditField.BATHROOMS.name, MAX, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::bathroomsMax)


        // Field bedrooms min is digital
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BEDROOMS.name, MIN, digitalValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bedroomsMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).bedroomsMin = args.first.third
        */
        launchTest(EditField.BEDROOMS.name, MIN, digitalValue, dollar, stringArgumentCaptor, SearchDataRepository::bedroomsMin)

        // Field bedrooms max is digital
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BEDROOMS.name, MAX, digitalValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bedroomsMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).bedroomsMax = args.first.third
        */
        launchTest(EditField.BEDROOMS.name, MAX, digitalValue, dollar, stringArgumentCaptor, SearchDataRepository::bedroomsMax)

        // Field bedrooms min is not digital
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BEDROOMS.name, MIN, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bedroomsMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).bedroomsMin = null
        */
        launchTest(EditField.BEDROOMS.name, MIN, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::bedroomsMin)

        // Field bedrooms max is not digital
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BEDROOMS.name, MAX, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bedroomsMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).bedroomsMax = null
        */
        launchTest(EditField.BEDROOMS.name, MAX, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::bedroomsMax)

        // Field bedrooms min is empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BEDROOMS.name, MIN, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bedroomsMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).bedroomsMin = null
        */
        launchTest(EditField.BEDROOMS.name, MIN, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::bedroomsMin)

        // Field bedrooms max is empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.BEDROOMS.name, MAX, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::bedroomsMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).bedroomsMax = null
        */
        launchTest(EditField.BEDROOMS.name, MAX, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::bedroomsMax)


        // Field price min is digital and currency is dollar
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.PRICE.name, MIN, digitalValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::priceMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).priceMin = args.first.third
        */
        launchTest(EditField.PRICE.name, MIN, digitalValue, dollar, stringArgumentCaptor, SearchDataRepository::priceMin)

        // Field price max is digital and currency is dollar
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.PRICE.name, MAX, digitalValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::priceMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).priceMax = args.first.third
        */
        launchTest(EditField.PRICE.name, MAX, digitalValue, dollar, stringArgumentCaptor, SearchDataRepository::priceMax)

        // Field price min is digital and currency is euro
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.PRICE.name, MIN, digitalValue, euro, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::priceMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(convertEuroToDollar(args.first.third.toInt()).toString(), args.second.second.allValues[0])
        verify(searchDataRepository).priceMin = convertEuroToDollar(args.first.third.toInt()).toString()
        */
        launchTest(EditField.PRICE.name, MIN, digitalValue, euro, stringArgumentCaptor, SearchDataRepository::priceMin) // TODO

        // Field price max is digital and currency is euro
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.PRICE.name, MAX, digitalValue, euro, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::priceMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(convertEuroToDollar(args.first.third.toInt()).toString(), args.second.second.allValues[0])
        verify(searchDataRepository).priceMax = convertEuroToDollar(args.first.third.toInt()).toString()
        */
        launchTest(EditField.PRICE.name, MAX, digitalValue, euro, stringArgumentCaptor, SearchDataRepository::priceMax) // TODO

        // Field price min is not digital
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.PRICE.name, MIN, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::priceMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).priceMin = null
        */
        launchTest(EditField.PRICE.name, MIN, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::priceMin)

        // Field price max is not digital
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.PRICE.name, MAX, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::priceMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).priceMax = null
        */
        launchTest(EditField.PRICE.name, MAX, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::priceMax)

        // Field price min is empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.PRICE.name, MIN, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::priceMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).priceMin = null
        */
        launchTest(EditField.PRICE.name, MIN, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::priceMin)

        // Field price max is empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.PRICE.name, MAX, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::priceMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).priceMax = null
        */
        launchTest(EditField.PRICE.name, MAX, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::priceMax)


        // Field description not empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.DESCRIPTION.name, null, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::description, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).description = args.first.third
        */
        launchTest(EditField.DESCRIPTION.name, null, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::description)

        // Field description empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.DESCRIPTION.name, null, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::description, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).description = null
        */
        launchTest(EditField.DESCRIPTION.name, null, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::description)


        // Field city not empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.CITY.name, null, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::city, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).city = args.first.third
        */
        launchTest(EditField.CITY.name, null, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::city)

        // Field city empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.CITY.name, null, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::city, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).city = null
        */
        launchTest(EditField.CITY.name, null, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::city)


        // Field state not empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.STATE.name, null, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::state, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).state = args.first.third
        */
        launchTest(EditField.STATE.name, null, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::state)

        // Field state empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.STATE.name, null, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::state, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).state = null
        */
        launchTest(EditField.STATE.name, null, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::state)


        // Field zip code not empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.ZIP_CODE.name, null, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::zip, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).zip = args.first.third
        */
        launchTest(EditField.ZIP_CODE.name, null, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::zip)

        // Field zip code empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.ZIP_CODE.name, null, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::zip, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).zip = null
        */
        launchTest(EditField.ZIP_CODE.name, null, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::zip)


        // Field country not empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.COUNTRY.name, null, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::country, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).country = args.first.third
        */
        launchTest(EditField.COUNTRY.name, null, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::country)

        // Field country empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.COUNTRY.name, null, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::country, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).country = null
        */
        launchTest(EditField.COUNTRY.name, null, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::country)


        // Field registration date min not empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.REGISTRATION_DATE.name, MIN, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::registrationDateMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).registrationDateMin = args.first.third
        */
        launchTest(EditField.REGISTRATION_DATE.name, MIN, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::registrationDateMin)

        // Field registration date max not empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.REGISTRATION_DATE.name, MAX, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::registrationDateMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).registrationDateMax = args.first.third
        */
        launchTest(EditField.REGISTRATION_DATE.name, MAX, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::registrationDateMax)

        // Field registration date min empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.REGISTRATION_DATE.name, MIN, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::registrationDateMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).registrationDateMin = null
        */
        launchTest(EditField.REGISTRATION_DATE.name, MIN, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::registrationDateMin)

        // Field registration date max empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.REGISTRATION_DATE.name, MAX, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::registrationDateMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).registrationDateMax = null
        */
        launchTest(EditField.REGISTRATION_DATE.name, MAX, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::registrationDateMax)


        // Field sale date min not empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.SALE_DATE.name, MIN, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::saleDateMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).saleDateMin = args.first.third
        */
        launchTest(EditField.SALE_DATE.name, MIN, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::saleDateMin)

        // Field sale date max not empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.SALE_DATE.name, MAX, stringValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::saleDateMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertEquals(args.first.third, args.second.second.allValues[0])
        verify(searchDataRepository).saleDateMax = args.first.third
        */
        launchTest(EditField.SALE_DATE.name, MAX, stringValue, dollar, stringArgumentCaptor, SearchDataRepository::saleDateMax)

        // Field sale date min empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.SALE_DATE.name, MIN, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::saleDateMin, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).saleDateMin = null
        */
        launchTest(EditField.SALE_DATE.name, MIN, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::saleDateMin)

        // Field sale date max empty
        /*
        resetRepoMockAndCaptor()
        args = setupFieldArguments(EditField.SALE_DATE.name, MAX, emptyValue, dollar, stringArgumentCaptor)
        captureSetterArgument(SearchDataRepository::saleDateMax, args.second.second)
        //
        viewModel.onFieldValueChange(args.first.first, args.first.second, args.first.third, args.second.first)
        //
        assertNull(args.second.second.allValues[0])
        verify(searchDataRepository).saleDateMax = null
        */
        launchTest(EditField.SALE_DATE.name, MAX, emptyValue, dollar, stringArgumentCaptor, SearchDataRepository::saleDateMax)
    }

}