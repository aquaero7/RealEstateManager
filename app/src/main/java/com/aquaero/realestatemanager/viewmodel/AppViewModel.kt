package com.aquaero.realestatemanager.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.EditDetail
import com.aquaero.realestatemanager.GeolocMap
import com.aquaero.realestatemanager.ListAndDetail
import com.aquaero.realestatemanager.Loan
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.SearchCriteria
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.repository.AddressRepository
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.PoiRepository
import com.aquaero.realestatemanager.repository.PropertyPoiJoinRepository
import com.aquaero.realestatemanager.repository.PropertyRepository
import com.aquaero.realestatemanager.utils.AppContentType
import com.aquaero.realestatemanager.utils.CurrencyStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class AppViewModel(
    private val propertyRepository: PropertyRepository,
    private val addressRepository: AddressRepository,
    private val photoRepository: PhotoRepository,
    private val agentRepository : AgentRepository,
    private val poiRepository: PoiRepository,
    private val propertyPoiJoinRepository: PropertyPoiJoinRepository,
) : ViewModel() {

    private val context: Context by lazy { ApplicationRoot.getContext() }

    private var internetAvailable by Delegates.notNull<Boolean>()

    @SuppressLint("NewApi")
    val fakeProperties = propertyRepository.fakeProperties


    /** Room **/

    private val _propertiesStateFlow = MutableStateFlow<MutableList<Property>>(emptyList<Property>().toMutableList())
    val propertiesStateFlow: StateFlow<MutableList<Property>> = _propertiesStateFlow.asStateFlow()

    private val _addressesStateFlow = MutableStateFlow<MutableList<Address>>(emptyList<Address>().toMutableList())
    val addressesStateFlow: StateFlow<MutableList<Address>> = _addressesStateFlow.asStateFlow()

    private val _photosStateFlow = MutableStateFlow<MutableList<Photo>>(emptyList<Photo>().toMutableList())
    val photosStateFlow: StateFlow<MutableList<Photo>> = _photosStateFlow.asStateFlow()

    private val _agentsStateFlow = MutableStateFlow<MutableList<Agent>>(emptyList<Agent>().toMutableList())
    val agentsStateFlow: StateFlow<MutableList<Agent>> = _agentsStateFlow.asStateFlow()

    private val _poisStateFlow = MutableStateFlow<MutableList<Poi>>(emptyList<Poi>().toMutableList())
    val poisStateFlow: StateFlow<MutableList<Poi>> = _poisStateFlow.asStateFlow()

    private val _propertyPoiJoinsStateFlow = MutableStateFlow<MutableList<PropertyPoiJoin>>(emptyList<PropertyPoiJoin>().toMutableList())
    val propertyPoiJoinsStateFlow: StateFlow<MutableList<PropertyPoiJoin>> = _propertyPoiJoinsStateFlow.asStateFlow()

    init {
        viewModelScope.launch(IO) {

            propertyRepository.getPropertiesFromRoom()
                .collect { listOfProperties -> _propertiesStateFlow.value = listOfProperties }

            addressRepository.getAddressesFromRoom()
                .collect { listOfAddresses -> _addressesStateFlow.value = listOfAddresses }

            photoRepository.getPhotosFromRoom()
                .collect { listOfPhotos -> _photosStateFlow.value = listOfPhotos }

            agentRepository.getAgentsFromRoom()
                .collect { listOfAgents -> _agentsStateFlow.value = listOfAgents }

            poiRepository.getPoisFromRoom()
                .collect { listOfPois -> _poisStateFlow.value = listOfPois }

            propertyPoiJoinRepository.getPropertyPoiJoinsFromRoom()
                .collect { listOfPropertyPoiJoins -> _propertyPoiJoinsStateFlow.value = listOfPropertyPoiJoins }

        }
    }

    /***/

    /**
     * Init content type, according to window's width,
     * to choose dynamically, on screen state changes, whether to show
     * just a list content, or both a list and detail content
     */
    fun contentType(windowSize: WindowWidthSizeClass): AppContentType = when (windowSize) {
        WindowWidthSizeClass.Expanded -> {
            AppContentType.SCREEN_WITH_DETAIL
        }

        else -> {
            AppContentType.SCREEN_ONLY
        }
    }

    /* TODO: Function to be deleted
    fun checkForInternet(): Boolean {
        internetAvailable = isInternetAvailable(context)
        return internetAvailable
    }
    */

    fun isInternetAvailable(): Boolean {
        return internetAvailable
    }

    // Init CurrencyStore
    val currencyStore = CurrencyStore(context)

    /**
     * TopBar
     */

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
                    contentType(windowSize) == AppContentType.SCREEN_WITH_DETAIL)

    val onClickRadioButton: (String) -> Unit = { currency: String ->
        // Store selected currency with DataStore
        CoroutineScope(Dispatchers.IO).launch {
            currencyStore.saveCurrency(currency)
        }
    }

    /** End TopBar */


    fun agentFromId(agentId: Long): Agent? {
        return agentRepository.agentFromId(agentId)
    }




}



