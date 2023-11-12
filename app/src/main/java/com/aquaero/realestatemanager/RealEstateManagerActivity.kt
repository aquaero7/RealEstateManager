package com.aquaero.realestatemanager

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aquaero.realestatemanager.ui.component.app.AppTabRow
import com.aquaero.realestatemanager.ui.component.app.AppTopBar
import com.aquaero.realestatemanager.ui.theme.RealEstateManagerTheme
import com.aquaero.realestatemanager.utils.AppContentType
import com.aquaero.realestatemanager.viewmodel.AppViewModel
import com.aquaero.realestatemanager.viewmodel.DetailViewModel
import com.aquaero.realestatemanager.viewmodel.EditViewModel
import com.aquaero.realestatemanager.viewmodel.ListViewModel
import com.aquaero.realestatemanager.viewmodel.ViewModelFactory

class RealEstateManagerActivity: ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Init ViewModels
        val appViewModel by viewModels<AppViewModel> { ViewModelFactory() }
        val listViewModel by viewModels<ListViewModel> { ViewModelFactory() }
        val detailViewModel by viewModels<DetailViewModel> { ViewModelFactory() }
        val editViewModel by viewModels<EditViewModel> { ViewModelFactory() }

        setContent {
            val windowSize = calculateWindowSizeClass(activity = this)
            RealEstateManagerApp(
                windowSize = windowSize.widthSizeClass,
                appViewModel = appViewModel,
                listViewModel = listViewModel,
                detailViewModel = detailViewModel,
                editViewModel = editViewModel,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RealEstateManagerApp(
    windowSize: WindowWidthSizeClass,
    appViewModel: AppViewModel,
    listViewModel: ListViewModel,
    detailViewModel: DetailViewModel,
    editViewModel: EditViewModel,
) {
    RealEstateManagerTheme {

        val context = LocalContext.current

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

        val onClickMenu = { when(currentScreen) {
                ListAndDetail.routeWithArgs, Detail.routeWithArgs -> {
                    Log.w("Click on edit property", "Property $propertyId")
                    navController.navigateToDetailEdit(propertyId.toString())
                }
                // TODO: Replace toast with specific action
                else -> Toast
                    .makeText(context, "Click on ${context.getString(R.string.valid)}",
                        Toast.LENGTH_SHORT)
                    .show()
            }
        }


        /**
         * Composable
         */
        Scaffold (
            topBar = {
                AppTopBar(
                    menuIcon = appViewModel.menuIcon(currentScreen),
                    menuIconContentDesc = stringResource(appViewModel.menuIconContentDesc(currentScreen)),
                    menuEnabled = appViewModel.menuEnabled(currentScreen, windowSize),
                    // onClickMenu = { appViewModel.onClickMenu(currentBackStack, navController) },
                    onClickMenu = onClickMenu,
                    onClickRadioButton = appViewModel.onClickRadioButton,
                )
            },
            bottomBar = {
                AppTabRow(
                    allScreens = tabRowScreens,
                    onTabSelected = { newScreen -> navController.navigateSingleTopTo(newScreen, appViewModel.fakeProperties[0].pId.toString()) },
                    currentScreen = currentTabScreen,
                    colorAnimLabel = stringResource(id = R.string.lb_tab_color_anim)
                )
            },
        ) { innerPadding ->
            AppNavHost(
                modifier = Modifier.padding(innerPadding),
                contentType = contentType,
                navController = navController,
                appViewModel = appViewModel,
                listViewModel = listViewModel,
                detailViewModel = detailViewModel,
                editViewModel = editViewModel,
            )
        }
    }
}

