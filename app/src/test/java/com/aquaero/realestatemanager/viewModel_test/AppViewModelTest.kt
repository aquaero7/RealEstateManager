package com.aquaero.realestatemanager.viewModel_test

import android.content.Context
import android.os.Bundle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.test.core.app.ApplicationProvider
import com.aquaero.realestatemanager.AppContentType
import com.aquaero.realestatemanager.GeolocMap
import com.aquaero.realestatemanager.ListAndDetail
import com.aquaero.realestatemanager.Loan
import com.aquaero.realestatemanager.NULL_PROPERTY_ID
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.SearchCriteria
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.propertyKey
import com.aquaero.realestatemanager.repository.AddressRepository
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.PoiRepository
import com.aquaero.realestatemanager.repository.PropertyPoiJoinRepository
import com.aquaero.realestatemanager.repository.PropertyRepository
import com.aquaero.realestatemanager.repository.TypeRepository
import com.aquaero.realestatemanager.utils.CurrencyStore
import com.aquaero.realestatemanager.viewmodel.AppViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.kotlin.doReturn
import org.robolectric.RobolectricTestRunner
import java.util.Locale

//@RunWith(AndroidJUnit4::class)
//@RunWith(MockitoJUnitRunner::class)
@RunWith(RobolectricTestRunner::class)
class AppViewModelTest {
    private lateinit var propertyRepository: PropertyRepository
    private lateinit var addressRepository: AddressRepository
    private lateinit var photoRepository: PhotoRepository
    private lateinit var agentRepository: AgentRepository
    private lateinit var typeRepository: TypeRepository
    private lateinit var poiRepository: PoiRepository
    private lateinit var propertyPoiJoinRepository: PropertyPoiJoinRepository

    private lateinit var viewModel: AppViewModel
    private lateinit var context: Context

    private lateinit var property1: Property
    private lateinit var property2: Property
    private lateinit var property3: Property

    private lateinit var localeUS: Locale
    private lateinit var localeFR: Locale


    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()

        propertyRepository = mock(PropertyRepository::class.java)
        addressRepository = mock(AddressRepository::class.java)
        photoRepository = mock(PhotoRepository::class.java)
        agentRepository = mock(AgentRepository::class.java)
        typeRepository = mock(TypeRepository::class.java)
        poiRepository = mock(PoiRepository::class.java)
        propertyPoiJoinRepository = mock(PropertyPoiJoinRepository::class.java)

        viewModel = AppViewModel(
            propertyRepository = propertyRepository,
            addressRepository = addressRepository,
            photoRepository = photoRepository,
            agentRepository = agentRepository,
            typeRepository = typeRepository,
            poiRepository = poiRepository,
            propertyPoiJoinRepository = propertyPoiJoinRepository,
        )

        property1 = Property(
            propertyId = 1L,
            addressId = null,
            price = null,
            description = null,
            surface = null,
            nbOfRooms = null,
            nbOfBathrooms = null,
            nbOfBedrooms = null,
            registrationDate = null,
            saleDate = null
        )
        property2 = Property(
            propertyId = 2L,
            addressId = null,
            price = null,
            description = null,
            surface = null,
            nbOfRooms = null,
            nbOfBathrooms = null,
            nbOfBedrooms = null,
            registrationDate = null,
            saleDate = null
        )
        property3 = Property(
            propertyId = 3L,
            addressId = null,
            price = null,
            description = null,
            surface = null,
            nbOfRooms = null,
            nbOfBathrooms = null,
            nbOfBedrooms = null,
            registrationDate = null,
            saleDate = null
        )

        localeUS = Locale("en", "US")
        localeFR = Locale("fr", "FR")
    }

    @Test
    fun getStringTypesOrderedByIdWithSuccess() = runTest {
        // Prepare data for the test
        val expectedStringTypes = listOf("Type1", "Type2", "Type3")
        // Configure the mock of the repository to return a Flow
        doReturn(flowOf(expectedStringTypes)).`when`(typeRepository).getStringTypesOrderedByIdFromRoom(context)
        // Call the function and collect the values emitted by the Flow
        val result = viewModel.stringTypesOrderedById(context).first()
        // Check the result
        assertEquals(expectedStringTypes, result)
    }

    @Test
    fun getStringAgentsOrderedByNameWithSuccess() = runTest {
        // Prepare data for the test
        val expectedStringAgents = listOf("Agent1", "Agent2", "Agent3")
        // Configure the mock of the repository to return a Flow
        doReturn(flowOf(expectedStringAgents)).`when`(agentRepository).getStringAgentsOrderedByNameFromRoom(context)
        // Call the function and collect the values emitted by the Flow
        val result = viewModel.stringAgentsOrderedByName(context).first()
        // Check the result
        assertEquals(expectedStringAgents, result)
    }

    @Test
    fun getPropertyIdWithSuccess() {
        // Prepare data for the test
        val currentBackStack = mock(NavBackStackEntry::class.java)
        val properties = mutableListOf(property1, property2, property3)
        // Perform the mock
        val arguments = mock(Bundle::class.java)
        // Configure the mock behavior
        doReturn(arguments).`when`(currentBackStack).arguments
        doReturn("test_property_id").`when`(arguments).getString(propertyKey)
        // Perform the test action
        val result = viewModel.propertyId(currentBackStack, properties)
        // Check the result
        assertEquals("test_property_id", result)
    }

    @Test
    fun accessToCurrencyStoreWithSuccess() {

        /* Case 1: Test with the default US locale */
        // Set Locale US (default)
        Locale.setDefault(localeUS)
        // Perform the test action
        var currencyStore = viewModel.currencyStore(context)
        // Check the result and verify the default currency
        assertNotNull(currencyStore)
        assertEquals("$", currencyStore.getDefaultCurrency)

        /* Test with France locale */
        // Set Locale FR
        Locale.setDefault(localeFR)
        // Perform the test action
        currencyStore = viewModel.currencyStore(context)
        // Check the result and verify the default currency
        assertNotNull(currencyStore)
        assertEquals("€", currencyStore.getDefaultCurrency)
    }

    @Test
    fun getContentTypeWithSuccess() {
        val expandedType = viewModel.contentType(WindowWidthSizeClass.Expanded)
        val compactType = viewModel.contentType(WindowWidthSizeClass.Compact)

        assertEquals(AppContentType.LIST_AND_DETAIL, expandedType)
        assertEquals(AppContentType.LIST_OR_DETAIL, compactType)
    }

    @Test
    fun getCurrencyHelperDataWithSuccess() {
        // Set Locale US (default)
        Locale.setDefault(localeUS)
        var (currencyStore, defaultCurrency) = viewModel.currencyHelper(context)

        assertNotNull(currencyStore)
        assertEquals(context.getString(R.string.dollar), defaultCurrency)


        // Set Locale FR
        Locale.setDefault(localeFR)
        val result = viewModel.currencyHelper(context)
        currencyStore = result.first
        defaultCurrency = result.second

        assertNotNull(currencyStore)
        assertEquals(context.getString(R.string.euro), defaultCurrency)
    }

    @Test
    fun getNavigationDataWithSuccess() {
        // Prepare data for the test
        val properties = mutableListOf<Property>()
        val tabRowScreens = listOf(ListAndDetail, GeolocMap, SearchCriteria, Loan)
        val idForTest = "9"
        // Perform the mocks
        val navBackStackEntry = mock(NavBackStackEntry::class.java)
        val arguments = mock(Bundle::class.java)
        val navDestination = mock(NavDestination::class.java)
        // Configure the mock behavior
        doReturn(arguments).`when`(navBackStackEntry).arguments
        doReturn(idForTest).`when`(arguments).getString(propertyKey)
        doReturn(navDestination).`when`(navBackStackEntry).destination
        doReturn(GeolocMap.route).`when`(navDestination).route

        /* Case 1: currentBackStack not null */
        var (propertyId, currentScreen, currentTabScreen) = viewModel.navigationData(
            currentBackStack = navBackStackEntry,
            properties = properties,
            tabRowScreens = tabRowScreens
        )

        assertEquals(idForTest, propertyId)
        assertEquals(GeolocMap.route, currentScreen)
        assertEquals(GeolocMap, currentTabScreen)

        /* Case 2: currentBackStack null and properties empty */
        var result = viewModel.navigationData(
            currentBackStack = null,
            properties = properties,
            tabRowScreens = tabRowScreens
        )
        propertyId = result.first
        currentScreen = result.second
        currentTabScreen = result.third

        assertEquals(NULL_PROPERTY_ID, propertyId)
        assertNull(currentScreen)
        assertEquals(ListAndDetail, currentTabScreen)

        /* Case 3: currentBackStack null and properties not empty */
        properties.add(property1)
        properties.add(property2)
        properties.add(property3)
        result = viewModel.navigationData(
            currentBackStack = null,
            properties = properties,
            tabRowScreens = tabRowScreens
        )
        propertyId = result.first
        currentScreen = result.second
        currentTabScreen = result.third

        assertEquals(property1.propertyId, propertyId)
        assertNull(currentScreen)
        assertEquals(ListAndDetail, currentTabScreen)
    }

    @Test
    fun getTopBarMenuDataWithSuccess() {
        // Prepare data for the test
        val windowSize = WindowWidthSizeClass.Compact
        val currentBackStack: NavBackStackEntry? = null

        /* Case 1 */
        var currentScreen = GeolocMap.route
        var (menuIcon, menuIconContentDesc, menuEnabled) = viewModel.topBarMenu(
            context = context,
            currentBackStack = currentBackStack,
            currentScreen = currentScreen,
            windowSize = windowSize
        )

        assertEquals(Icons.Default.Edit, menuIcon)
        assertEquals(context.getString(R.string.cd_edit), menuIconContentDesc)
        assertFalse(menuEnabled)

        /* Case 2 */
        currentScreen = Loan.route
        val result = viewModel.topBarMenu(
            context = context,
            currentBackStack = currentBackStack,
            currentScreen = currentScreen,
            windowSize = windowSize
        )
        menuIcon = result.first
        menuIconContentDesc = result.second
        menuEnabled = result.third

        assertEquals(Icons.Default.Check, menuIcon)
        assertEquals(context.getString(R.string.cd_check), menuIconContentDesc)
        assertTrue(menuEnabled)
    }

    @Test
    fun clickOnRadioButtonWithSuccess() = runTest {
        // Prepare data for the test
        val currency = "currency"
        // Perform the mocks
        val context: Context = mock(Context::class.java)
        val currencyStore = mock(CurrencyStore::class.java)
        // Spy on the ViewModel to mock currencyStore method
        val viewModel = spy(AppViewModel(
            propertyRepository = mock(PropertyRepository::class.java),
            addressRepository = mock(AddressRepository::class.java),
            photoRepository = mock(PhotoRepository::class.java),
            agentRepository = mock(AgentRepository::class.java),
            typeRepository = mock(TypeRepository::class.java),
            poiRepository = mock(PoiRepository::class.java),
            propertyPoiJoinRepository = mock(PropertyPoiJoinRepository::class.java)
        ))
        // Configure the mock behavior
        doReturn(currencyStore).`when`(viewModel).currencyStore(context)

        // Perform the test action
        viewModel.onClickRadioButton(context, currency)

        // Verify that saveCurrency was called with the correct parameter
        runBlocking {
            verify(currencyStore).saveCurrency(currency)
        }
    }


}