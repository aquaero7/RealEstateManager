package com.aquaero.realestatemanager

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.ui.component.app.AppTabRow
import com.aquaero.realestatemanager.ui.component.app.AppTopBar
import com.aquaero.realestatemanager.ui.theme.RealEstateManagerTheme
import com.aquaero.realestatemanager.utils.AppContentType
import com.aquaero.realestatemanager.utils.CurrencyStore
import com.aquaero.realestatemanager.viewmodel.AppViewModel
import com.aquaero.realestatemanager.viewmodel.DetailViewModel
import com.aquaero.realestatemanager.viewmodel.EditViewModel
import com.aquaero.realestatemanager.viewmodel.ListViewModel
import com.aquaero.realestatemanager.viewmodel.LoanViewModel
import com.aquaero.realestatemanager.viewmodel.MapViewModel
import com.aquaero.realestatemanager.viewmodel.SearchViewModel
import com.aquaero.realestatemanager.viewmodel.ViewModelFactory

class RealEstateManagerActivity : ComponentActivity() {

    // Init ViewModels
    private val appViewModel by viewModels<AppViewModel> { ViewModelFactory }
    private val listViewModel by viewModels<ListViewModel> { ViewModelFactory }
    private val detailViewModel by viewModels<DetailViewModel> { ViewModelFactory }
    private val editViewModel by viewModels<EditViewModel> { ViewModelFactory }
    private val mapViewModel by viewModels<MapViewModel> { ViewModelFactory }
    private val searchViewModel by viewModels<SearchViewModel> { ViewModelFactory }
    private val loanViewModel by viewModels<LoanViewModel> { ViewModelFactory }

    @SuppressLint("NewApi")
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val windowSize = calculateWindowSizeClass(activity = this)
            val currencyStore = appViewModel.currencyStore

            RealEstateManagerApp(
                windowSize = windowSize.widthSizeClass,
                currencyStore = currencyStore,
                appViewModel = appViewModel,
                listViewModel = listViewModel,
                detailViewModel = detailViewModel,
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
        // appViewModel.checkForInternet() // TODO: To be deleted
        mapViewModel.checkForPermissions()
    }

}

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealEstateManagerApp(
    windowSize: WindowWidthSizeClass,
    currencyStore: CurrencyStore,
    appViewModel: AppViewModel,
    listViewModel: ListViewModel,
    detailViewModel: DetailViewModel,
    editViewModel: EditViewModel,
    mapViewModel: MapViewModel,
    searchViewModel: SearchViewModel,
    loanViewModel: LoanViewModel,
) {
    RealEstateManagerTheme(dynamicColor = false) {
        // val properties: List<Property> = appViewModel.fakeProperties
        val properties: MutableList<Property> = appViewModel.propertiesStateFlow.collectAsState().value    // TODO ROOM
        val addresses: MutableList<Address> = appViewModel.addressesStateFlow.collectAsState().value       // TODO ROOM
        val photos: MutableList<Photo> = appViewModel.photosStateFlow.collectAsState().value               // TODO ROOM
        val agents: MutableList<Agent> = appViewModel.agentsStateFlow.collectAsState().value               // TODO ROOM
        val pois: MutableList<Poi> = appViewModel.poisStateFlow.collectAsState().value                     // TODO ROOM
        val propertyPoiJoins: MutableList<PropertyPoiJoin> = appViewModel.propertyPoiJoinsStateFlow.collectAsState().value // TODO ROOM

        /**
         * Init content type, according to window's width,
         * to choose dynamically, on screen state changes, whether to show
         * just a list content, or both a list and detail content
         */
        val contentType: AppContentType = appViewModel.contentType(windowSize)

        /**
         * Init navigation data
         */
        val tabRowScreens = tabRowScreens
        val navController = rememberNavController()
        // Fetch current destination
        val currentBackStack by navController.currentBackStackEntryAsState()
        val propertyId = currentBackStack?.arguments?.getString(propertyKey) ?: 0
        val currentDestination = currentBackStack?.destination
        val currentScreen = currentDestination?.route
        // Use 'ListAndDetail' as a backup screen if the returned value is null
        val currentTabScreen = tabRowScreens.find { it.route == currentScreen } ?: ListAndDetail

        /**
         * TopBar
         */
        // TopBar Menu
        val menuIcon = appViewModel.menuIcon(currentScreen)
        val menuIconContentDesc = stringResource(appViewModel.menuIconContentDesc(currentScreen))
        val menuEnabled = appViewModel.menuEnabled(currentScreen, windowSize)
        val onClickMenu: () -> Unit = when (currentScreen) {
            ListAndDetail.routeWithArgs, Detail.routeWithArgs -> {
                { detailViewModel.onClickMenu(navController, propertyId) }
            }
            EditDetail.routeWithArgs -> {
                { editViewModel.onClickMenu(navController, propertyId) }
            }
            SearchCriteria.route -> {
                { searchViewModel.onClickMenu() }
            }
            Loan.route -> {
                { loanViewModel.onClickMenu() }
            }
            else -> { {} }
        }
        val onClickRadioButton: (String) -> Unit = appViewModel.onClickRadioButton
        // TopBar RadioButtons
        val currency =
            currencyStore.getCurrency.collectAsState(initial = stringResource(id = R.string.dollar)).value

        /**
         * Bottom bar
         */
        // val defaultPropertyId = appViewModel.fakeProperties[0].pId.toString()
        val defaultPropertyId = if (properties.isNotEmpty()) properties[0].propertyId.toString() else null // TODO ROOM


        /**
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
                            newScreen,
                            defaultPropertyId
                        )
                    },
                    currentScreen = currentTabScreen,
                    colorAnimLabel = stringResource(id = R.string.lb_tab_color_anim)
                )
            },
        ) { innerPadding ->
            AppNavHost(
                modifier = Modifier.padding(innerPadding),
                contentType = contentType,
                navController = navController,
                properties = properties,
                addresses = addresses,
                photos = photos,
                agents = agents,
                pois = pois,
                appViewModel = appViewModel,
                listViewModel = listViewModel,
                detailViewModel = detailViewModel,
                editViewModel = editViewModel,
                mapViewModel = mapViewModel,
                searchViewModel = searchViewModel,
                loanViewModel = loanViewModel,
                currency = currency,
            )
        }

    }

}

