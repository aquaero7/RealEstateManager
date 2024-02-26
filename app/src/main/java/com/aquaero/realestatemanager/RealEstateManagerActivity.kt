package com.aquaero.realestatemanager

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.ui.component.app.AppTabRow
import com.aquaero.realestatemanager.ui.component.app.AppTopBar
import com.aquaero.realestatemanager.ui.theme.RealEstateManagerTheme
import com.aquaero.realestatemanager.viewmodel.AppViewModel
import com.aquaero.realestatemanager.viewmodel.EditViewModel
import com.aquaero.realestatemanager.viewmodel.ListAndDetailViewModel
import com.aquaero.realestatemanager.viewmodel.LoanViewModel
import com.aquaero.realestatemanager.viewmodel.MapViewModel
import com.aquaero.realestatemanager.viewmodel.SearchViewModel
import com.aquaero.realestatemanager.viewmodel.ViewModelFactory

class RealEstateManagerActivity : ComponentActivity() {

    // Init ViewModels
    private val appViewModel by viewModels<AppViewModel> { ViewModelFactory }
    private val listAndDetailViewModel by viewModels<ListAndDetailViewModel> { ViewModelFactory }
    private val editViewModel by viewModels<EditViewModel> { ViewModelFactory }
    private val mapViewModel by viewModels<MapViewModel> { ViewModelFactory }
    private val searchViewModel by viewModels<SearchViewModel> { ViewModelFactory }
    private val loanViewModel by viewModels<LoanViewModel> { ViewModelFactory }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RealEstateManagerApp(
                activity = this,
                appViewModel = appViewModel,
                listAndDetailViewModel = listAndDetailViewModel,
                editViewModel = editViewModel,
                mapViewModel = mapViewModel,
                searchViewModel = searchViewModel,
                loanViewModel = loanViewModel,
            )
        }
    }

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        mapViewModel.checkForPermissions()
    }

}

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun RealEstateManagerApp(
    activity: Activity,
    appViewModel: AppViewModel,
    listAndDetailViewModel: ListAndDetailViewModel,
    editViewModel: EditViewModel,
    mapViewModel: MapViewModel,
    searchViewModel: SearchViewModel,
    loanViewModel: LoanViewModel,
) {
    RealEstateManagerTheme(dynamicColor = false) {
        val context = LocalContext.current

        val properties: MutableList<Property> by appViewModel.properties.collectAsState(initial = mutableListOf())
        val addresses: MutableList<Address> by appViewModel.addresses.collectAsState(initial = mutableListOf())
        val photos: MutableList<Photo> by appViewModel.photos.collectAsState(initial = mutableListOf())
        val agents: MutableList<Agent> by appViewModel.agentsOrderedByName.collectAsState(initial = mutableListOf())
        val types: MutableList<Type> by appViewModel.typesOrderedById.collectAsState(initial = mutableListOf())
        val pois: MutableList<Poi> by appViewModel.pois.collectAsState(initial = mutableListOf())
        val propertyPoiJoins: MutableList<PropertyPoiJoin> by appViewModel.propertyPoiJoins.collectAsState(initial = mutableListOf())
        val stringTypes: MutableList<String> by appViewModel.stringTypesOrderedById(context = context).collectAsState(initial = mutableListOf())
        val stringAgents: MutableList<String> by appViewModel.stringAgentsOrderedByName(context = context).collectAsState(initial = mutableListOf())

        /*
         * Init content type, according to window's width,
         * to choose dynamically, on screen state changes, whether to show
         * just a list content, or both a list and detail content
         */
        val windowSize = calculateWindowSizeClass(activity = activity).widthSizeClass
        val contentType: AppContentType = appViewModel.contentType(windowSize = windowSize)

        /*
         * Init navigation data
         */
        val tabRowScreens = tabRowScreens
        val navController = rememberNavController()
        // Fetch current destination
        val currentBackStack by navController.currentBackStackEntryAsState()
        val defaultPropertyId = if (properties.isNotEmpty()) properties[0].propertyId else NULL_PROPERTY_ID
        val propertyId = currentBackStack?.arguments?.getString(propertyKey) ?: defaultPropertyId
        val currentDestination = currentBackStack?.destination
        val currentScreen = currentDestination?.route
        // Use 'ListAndDetail' as a backup screen if the returned value is null
        val currentTabScreen = tabRowScreens.find { it.route == currentScreen } ?: ListAndDetail

        /*
         * TopBar
         */
        // TopBar Menu
        val propertySelected = currentBackStack?.arguments?.getBoolean(selectedKey) ?: false
        val menuIcon = appViewModel.menuIcon(currentScreen = currentScreen)
        val menuIconContentDesc = stringResource(appViewModel.menuIconContentDesc(currentScreen = currentScreen))
        val menuEnabled = appViewModel.menuEnabled(
            currentScreen = currentScreen,
            windowSize = windowSize,
            propertySelected = propertySelected
        )
        val onClickMenu: () -> Unit = when (currentScreen) {
            ListAndDetail.routeWithArgs -> {
                { listAndDetailViewModel.onClickMenu(navController = navController, propertyId = propertyId) }
            }
            EditDetail.routeWithArgs -> {
                { editViewModel.onClickMenu(navController = navController, context = context) }
            }
            SearchCriteria.route -> {
                { searchViewModel.onClickMenu(context = context) }
            }
            Loan.route -> {
                { loanViewModel.onClickMenu(context = context) }
            }
            else -> { {} }
        }
        // TopBar RadioButtons
        val currencyStore = appViewModel.currencyStore(context = context)
        val defaultCurrency =
            if (Locale.current.region == Region.FR.name) context.getString(R.string.euro)
            else context.getString(R.string.dollar)
        val currency =
            currencyStore.getCurrency.collectAsState(initial = defaultCurrency).value
        val onClickRadioButton: (String) -> Unit = {
            appViewModel.onClickRadioButton(context = context, currency = it)
        }


        /*
         * Composable
         */
        Scaffold(
            topBar = {
                AppTopBar(
                    menuIcon = menuIcon,
                    menuIconContentDesc = menuIconContentDesc,
                    menuEnabled = menuEnabled,
                    onClickMenu = onClickMenu,
                    onClickRadioButton = onClickRadioButton,
                    currency = currency,
                )
            },
            bottomBar = {
                AppTabRow(
                    allScreens = tabRowScreens,
                    onTabSelected = { newScreen ->
                        navController.navigateSingleTopTo(
                            destination = newScreen,
                            propertyId = propertyId.toString()
                        )
                    },
                    currentScreen = currentTabScreen,
                    colorAnimLabel = stringResource(id = R.string.lb_tab_color_anim)
                )
            },
        ) { innerPadding ->
            AppNavHost(
                modifier = Modifier.padding(innerPadding),
                context = context,
                contentType = contentType,
                navController = navController,
                properties = properties,
                addresses = addresses,
                photos = photos,
                agents = agents,
                types = types,
                pois = pois,
                propertyPoiJoins = propertyPoiJoins,
                stringTypes = stringTypes,
                stringAgents = stringAgents,
                appViewModel = appViewModel,
                listAndDetailViewModel = listAndDetailViewModel,
                editViewModel = editViewModel,
                mapViewModel = mapViewModel,
                searchViewModel = searchViewModel,
                loanViewModel = loanViewModel,
                currency = currency,
            )
        }

    }

}

