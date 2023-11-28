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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.component.app.AppTabRow
import com.aquaero.realestatemanager.ui.component.app.AppTopBar
import com.aquaero.realestatemanager.ui.theme.RealEstateManagerTheme
import com.aquaero.realestatemanager.utils.AppContentType
import com.aquaero.realestatemanager.viewmodel.AppViewModel
import com.aquaero.realestatemanager.viewmodel.DetailViewModel
import com.aquaero.realestatemanager.viewmodel.EditViewModel
import com.aquaero.realestatemanager.viewmodel.ListViewModel
import com.aquaero.realestatemanager.viewmodel.MapViewModel
import com.aquaero.realestatemanager.viewmodel.ViewModelFactory

class RealEstateManagerActivity : ComponentActivity() {

    // Init ViewModels
    private val appViewModel by viewModels<AppViewModel> { ViewModelFactory }
    private val listViewModel by viewModels<ListViewModel> { ViewModelFactory }
    private val detailViewModel by viewModels<DetailViewModel> { ViewModelFactory }
    private val editViewModel by viewModels<EditViewModel> { ViewModelFactory }
    private val mapViewModel by viewModels<MapViewModel> { ViewModelFactory }

    @SuppressLint("NewApi")
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val windowSize = calculateWindowSizeClass(activity = this)
            RealEstateManagerApp(
                windowSize = windowSize.widthSizeClass,
                appViewModel = appViewModel,
                listViewModel = listViewModel,
                detailViewModel = detailViewModel,
                editViewModel = editViewModel,
                mapViewModel = mapViewModel,
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealEstateManagerApp(
    windowSize: WindowWidthSizeClass,
    appViewModel: AppViewModel,
    listViewModel: ListViewModel,
    detailViewModel: DetailViewModel,
    editViewModel: EditViewModel,
    mapViewModel: MapViewModel,
) {
    RealEstateManagerTheme(dynamicColor = false) {
        val properties: List<Property> = appViewModel.fakeProperties
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
        val propertyId = currentBackStack?.arguments?.getString(propertyKey) ?: -1
        val currentDestination = currentBackStack?.destination
        val currentScreen = currentDestination?.route
        // Use 'ListAndDetail' as a backup screen if the returned value is null
        val currentTabScreen = tabRowScreens.find { it.route == currentScreen } ?: ListAndDetail

        /**
         * TopBar menu
         */
        val menuIcon = appViewModel.menuIcon(currentScreen)
        val menuIconContentDesc = stringResource(appViewModel.menuIconContentDesc(currentScreen))
        val menuEnabled = appViewModel.menuEnabled(currentScreen, windowSize)
        val onClickMenu = { appViewModel.onClickMenu(currentScreen, navController, propertyId) }
        val onClickRadioButton = appViewModel.onClickRadioButton

        /**
         * Bottom bar
         */
        val defaultPropertyId = appViewModel.fakeProperties[0].pId.toString()


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
                appViewModel = appViewModel,
                listViewModel = listViewModel,
                detailViewModel = detailViewModel,
                editViewModel = editViewModel,
                mapViewModel = mapViewModel,
            )
        }
    }
}

