package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.lifecycle.ViewModel
import com.aquaero.realestatemanager.AppContentType
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.EditDetail
import com.aquaero.realestatemanager.GeolocMap
import com.aquaero.realestatemanager.ListAndDetail
import com.aquaero.realestatemanager.Loan
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.SearchCriteria
import com.aquaero.realestatemanager.repository.AddressRepository
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.PoiRepository
import com.aquaero.realestatemanager.repository.PropertyPoiJoinRepository
import com.aquaero.realestatemanager.repository.PropertyRepository
import com.aquaero.realestatemanager.repository.TypeRepository
import com.aquaero.realestatemanager.utils.CurrencyStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(
    propertyRepository: PropertyRepository,
    addressRepository: AddressRepository,
    photoRepository: PhotoRepository,
    agentRepository : AgentRepository,
    typeRepository : TypeRepository,
    poiRepository: PoiRepository,
    propertyPoiJoinRepository: PropertyPoiJoinRepository,
) : ViewModel() {
    private val context: Context by lazy { ApplicationRoot.getContext() }


    /* Room */

    val properties = propertyRepository.getPropertiesFromRoom()
    val addresses = addressRepository.getAddressesFromRoom()
    val photos = photoRepository.getPhotosFromRoom()
    val agentsOrderedByName = agentRepository.getAgentsOrderedByNameFromRoom()
    val typesOrderedById = typeRepository.getTypesOrderedByIdFromRoom()
    val pois = poiRepository.getPoisFromRoom()
    val propertyPoiJoins = propertyPoiJoinRepository.getPropertyPoiJoinsFromRoom()
    val stringTypesOrderedById = typeRepository.getStringTypesOrderedByIdFromRoom(context)
    val stringAgentsOrderedByName = agentRepository.getStringAgentsOrderedByNameFromRoom(context)

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

    // Init CurrencyStore
    val currencyStore = CurrencyStore(context)

    /***/


    /* TopBar */

    fun menuIcon(currentScreen: String?) = if (
        currentScreen == EditDetail.routeWithArgs ||
        currentScreen == SearchCriteria.route ||
        currentScreen == Loan.route
    ) Icons.Default.Check else Icons.Default.Edit

    fun menuIconContentDesc(currentScreen: String?) = if (
        currentScreen == EditDetail.routeWithArgs ||
        currentScreen == Loan.route
    ) R.string.cd_check else R.string.cd_edit

    fun menuEnabled(currentScreen: String?, windowSize: WindowWidthSizeClass) = (
            currentScreen != GeolocMap.route) &&
            (currentScreen != ListAndDetail.routeWithArgs ||
                    contentType(windowSize) == AppContentType.LIST_AND_DETAIL)

    val onClickRadioButton: (String) -> Unit = { currency: String ->
        // Store selected currency with DataStore
        CoroutineScope(Dispatchers.IO).launch {
            currencyStore.saveCurrency(currency)
        }
    }

    /**/

}



