package com.aquaero.realestatemanager.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.Detail
import com.aquaero.realestatemanager.EditDetail
import com.aquaero.realestatemanager.GeolocMap
import com.aquaero.realestatemanager.ListAndDetail
import com.aquaero.realestatemanager.Loan
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.SearchCriteria
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.navigateToDetailEdit
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.PropertyRepository
import com.aquaero.realestatemanager.utils.AppContentType
import com.aquaero.realestatemanager.utils.CurrencyStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class AppViewModel(
    private val agentRepository : AgentRepository,
    private val propertyRepository: PropertyRepository,
) : ViewModel() {

    private val context: Context by lazy { ApplicationRoot.getContext() }

    private var internetAvailable by Delegates.notNull<Boolean>()

    @SuppressLint("NewApi")
    val fakeProperties = propertyRepository.fakeProperties

    // TODO ROOM                                                                                    // TODO ROOM
    private val _items = MutableStateFlow<List<Property>>(emptyList())
    val items: StateFlow<List<Property>> = _items.asStateFlow()
    init {
        viewModelScope.launch(IO) {
            propertyRepository.getPropertiesFromRoom()
                .collect { listOfItems -> _items.value = listOfItems }
        }
    }
    //

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



