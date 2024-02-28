package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import com.aquaero.realestatemanager.AppContentType
import com.aquaero.realestatemanager.AppDestination
import com.aquaero.realestatemanager.EditDetail
import com.aquaero.realestatemanager.GeolocMap
import com.aquaero.realestatemanager.ListAndDetail
import com.aquaero.realestatemanager.Loan
import com.aquaero.realestatemanager.NULL_PROPERTY_ID
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.Region
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
import com.aquaero.realestatemanager.selectedKey
import com.aquaero.realestatemanager.utils.CurrencyStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(
    propertyRepository: PropertyRepository,
    addressRepository: AddressRepository,
    photoRepository: PhotoRepository,
    private val agentRepository : AgentRepository,
    private val typeRepository : TypeRepository,
    poiRepository: PoiRepository,
    propertyPoiJoinRepository: PropertyPoiJoinRepository,
) : ViewModel() {

    /* Room */

    val properties = propertyRepository.getPropertiesFromRoom()
    val addresses = addressRepository.getAddressesFromRoom()
    val photos = photoRepository.getPhotosFromRoom()
    val agentsOrderedByName = agentRepository.getAgentsOrderedByNameFromRoom()
    val typesOrderedById = typeRepository.getTypesOrderedByIdFromRoom()
    val pois = poiRepository.getPoisFromRoom()
    val propertyPoiJoins = propertyPoiJoinRepository.getPropertyPoiJoinsFromRoom()
    fun stringTypesOrderedById(context: Context) = typeRepository.getStringTypesOrderedByIdFromRoom(context)
    fun stringAgentsOrderedByName(context: Context) = agentRepository.getStringAgentsOrderedByNameFromRoom(context)

    /**/


    /**
     * Init content type, according to window's width,
     * to choose dynamically, on screen state changes, whether to show
     * just a list content, or both a list and detail content
     */
    fun contentType(windowSize: WindowWidthSizeClass): AppContentType = when (windowSize) {
        WindowWidthSizeClass.Expanded -> { AppContentType.LIST_AND_DETAIL }
        else -> { AppContentType.LIST_OR_DETAIL }
    }

    /* Currency */
    private fun currencyStore(context: Context): CurrencyStore = CurrencyStore(context)

    fun currencyHelper(context: Context): Pair<CurrencyStore, String> {
        val currencyStore = currencyStore(context = context)
        val defaultCurrency =
            if (Locale.current.region == Region.FR.name) context.getString(R.string.euro)
            else context.getString(R.string.dollar)
        return Pair(currencyStore, defaultCurrency)
    }


    /* Navigation data */

    private fun propertyId(currentBackStack: NavBackStackEntry?, properties: MutableList<Property>): Comparable<*> {
        val defaultPropertyId = if (properties.isNotEmpty()) properties[0].propertyId else NULL_PROPERTY_ID
        return currentBackStack?.arguments?.getString(propertyKey) ?: defaultPropertyId
    }

    private fun currentScreen(currentBackStack: NavBackStackEntry?): String? {
        val currentDestination = currentBackStack?.destination
        return currentDestination?.route
    }

    private fun currentTabScreen(
        currentScreen: String?,
        tabRowScreens: List<AppDestination>
    ): AppDestination = tabRowScreens.find { it.route == currentScreen } ?: ListAndDetail

    fun navigationData(
        currentBackStack: NavBackStackEntry?,
        properties: MutableList<Property>,
        tabRowScreens: List<AppDestination>,
    ): Triple<Comparable<*>, String?, AppDestination> {
        val propertyId = propertyId(currentBackStack, properties)
        val currentScreen = currentScreen(currentBackStack)
        val currentTabScreen = currentTabScreen(currentScreen, tabRowScreens)
        return Triple(propertyId, currentScreen, currentTabScreen)
    }


    /* TopBar */

    private fun menuIcon(currentScreen: String?) = if (
        currentScreen == EditDetail.routeWithArgs ||
        currentScreen == SearchCriteria.route ||
        currentScreen == Loan.route
    ) Icons.Default.Check else Icons.Default.Edit

    private fun menuIconContentDesc(currentScreen: String?) = if (
        currentScreen == EditDetail.routeWithArgs ||
        currentScreen == SearchCriteria.route ||
        currentScreen == Loan.route
    ) R.string.cd_check else R.string.cd_edit

    private fun menuEnabled(currentScreen: String?, windowSize: WindowWidthSizeClass, propertySelected: Boolean) =
        (currentScreen != GeolocMap.route) &&
            (currentScreen != ListAndDetail.routeWithArgs || propertySelected ||
                    contentType(windowSize) == AppContentType.LIST_AND_DETAIL)

    fun topBarMenu(
        context: Context,
        currentBackStack: NavBackStackEntry?,
        currentScreen: String?,
        windowSize: WindowWidthSizeClass
    ): Triple<ImageVector, String, Boolean> {
        val propertySelected = currentBackStack?.arguments?.getBoolean(selectedKey) ?: false
        val menuIcon = menuIcon(currentScreen = currentScreen)
        val menuIconContentDesc = context.getString(menuIconContentDesc(currentScreen = currentScreen))
        val menuEnabled = menuEnabled(
            currentScreen = currentScreen,
            windowSize = windowSize,
            propertySelected = propertySelected
        )
        return Triple(menuIcon, menuIconContentDesc, menuEnabled)
    }

    fun onClickRadioButton(context: Context, currency: String) {
        // Store selected currency with DataStore
        CoroutineScope(Dispatchers.IO).launch {
            currencyStore(context).saveCurrency(currency)
        }
    }

    /**/


}



