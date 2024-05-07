package com.aquaero.realestatemanager

import android.app.Activity
import android.content.Context
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

    /*  // TODO: To be deleted
    // Init ViewModels
    private val appViewModel by viewModels<AppViewModel> { ViewModelFactory }
    private val listAndDetailViewModel by viewModels<ListAndDetailViewModel> { ViewModelFactory }
    private val editViewModel by viewModels<EditViewModel> { ViewModelFactory }
    private val mapViewModel by viewModels<MapViewModel> { ViewModelFactory }
    private val searchViewModel by viewModels<SearchViewModel> { ViewModelFactory }
    private val loanViewModel by viewModels<LoanViewModel> { ViewModelFactory }
    */
    // Declare ViewModels
    private lateinit var appViewModel: AppViewModel
    private lateinit var listAndDetailViewModel: ListAndDetailViewModel
    private lateinit var editViewModel: EditViewModel
    private lateinit var mapViewModel: MapViewModel
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var loanViewModel: LoanViewModel

//    @SuppressLint("NewApi")   // TODO: To be deleted
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModels(this.applicationContext)
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

//    @SuppressLint("NewApi")       // TODO: To be deleted
    override fun onResume() {
        super.onResume()
        mapViewModel.checkForPermissions(context = this.applicationContext)
    }

    private fun initViewModels(context: Context) {
        val viewModelFactory = ViewModelFactory(context = context)
        appViewModel = viewModels<AppViewModel> { viewModelFactory }.value
        listAndDetailViewModel = viewModels<ListAndDetailViewModel> { viewModelFactory }.value
        editViewModel = viewModels<EditViewModel> { viewModelFactory }.value
        mapViewModel = viewModels<MapViewModel> { viewModelFactory }.value
        searchViewModel = viewModels<SearchViewModel> { viewModelFactory }.value
        loanViewModel = viewModels<LoanViewModel> { viewModelFactory }.value
    }

}

//@SuppressLint("NewApi")       // TODO: To be deleted
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
         * Navigation data
         */
        val tabRowScreens = tabRowScreens
        val navController = rememberNavController()
        // Fetch current screen
        val currentBackStack by navController.currentBackStackEntryAsState()
        val (propertyId, currentScreen, currentTabScreen) =
            appViewModel.navigationData(
                currentBackStack = currentBackStack,
                properties = properties,
                tabRowScreens = tabRowScreens
            )

        /*
         * TopBar
         */
        // TopBar Menu
        val (menuIcon, menuIconContentDesc, menuEnabled) = appViewModel.topBarMenu(
            context = context,
            currentBackStack = currentBackStack,
            currentScreen = currentScreen,
            windowSize = windowSize
        )

        // TopBar RadioButtons
        val (currencyStore, defaultCurrency) = appViewModel.currencyHelper(context)
        val currency = currencyStore.getCurrency.collectAsState(initial = defaultCurrency).value
        val onClickRadioButton: (String) -> Unit = {
            appViewModel.onClickRadioButton(context = context, currency = it)
        }

        val onClickMenu: () -> Unit = when (currentScreen) {
            ListAndDetail.routeWithArgs -> {
                { listAndDetailViewModel.onClickMenu(navController = navController, propertyId = propertyId) }
            }
            EditDetail.routeWithArgs -> {
                { editViewModel.onClickMenu(navController = navController, context = context) }
            }
            SearchCriteria.route -> {
                {
                    searchViewModel.onClickMenu(
                        properties = properties,
                        addresses = addresses,
                        types = types,
                        agents = agents,
                        photos = photos,
                        propertyPoiJoins = propertyPoiJoins
                    )
                }
            }
            Loan.route -> {
                { loanViewModel.onClickMenu(context = context) }
            }
            else -> { {} }
        }

        /*
         * Back navigation management
         */
        val popBackStack: () -> Unit = { navController.popBackStack() }
        val closeApp: () -> Unit = { activity.finish() }


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
                        val viewModel = when (newScreen) {
                            SearchCriteria -> searchViewModel
                            else -> null
                        }
                        if (newScreen == SearchCriteria) searchViewModel.resetScrollToResults()
                        navController.navigateSingleTopTo(
                            destination = newScreen,
                            propertyId = propertyId.toString(),
                            viewModel = viewModel,
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
                popBackStack = popBackStack,
                closeApp = closeApp,
            )
        }
    }

}

