package com.aquaero.realestatemanager

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.component.app.AppTabRow
import com.aquaero.realestatemanager.ui.component.app.AppTopBar
import com.aquaero.realestatemanager.ui.theme.RealEstateManagerTheme
import com.aquaero.realestatemanager.utils.AppContentType
import com.aquaero.realestatemanager.viewmodel.AppViewModel
import com.aquaero.realestatemanager.viewmodel.ViewModelFactory

class RealEstateManagerActivity : ComponentActivity() {

    // Init ViewModels
    private val appViewModel by viewModels<AppViewModel> { ViewModelFactory() }
    // val appViewModel by viewModels<AppViewModel> { ViewModelFactory() }
    // val listViewModel by viewModels<ListViewModel> { ViewModelFactory() }
    // val detailViewModel by viewModels<DetailViewModel> { ViewModelFactory() }
    // val editViewModel by viewModels<EditViewModel> { ViewModelFactory() }

    @SuppressLint("NewApi")
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val windowSize = calculateWindowSizeClass(activity = this)
            RealEstateManagerApp(
                windowSize = windowSize.widthSizeClass,
                appViewModel = appViewModel,
                activity = this,
                // listViewModel = listViewModel,
                // detailViewModel = detailViewModel,
                // editViewModel = editViewModel,
            )
        }
    }

    override fun onResume() {
        super.onResume()
        appViewModel.checkForPermissions()
    }

}

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealEstateManagerApp(
    windowSize: WindowWidthSizeClass,
    appViewModel: AppViewModel,
    activity: Activity,
    // listViewModel: ListViewModel,
    // detailViewModel: DetailViewModel,
    // editViewModel: EditViewModel,
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
        val context = LocalContext.current
        val onClickMenu = {
            when (currentScreen) {

                ListAndDetail.routeWithArgs, Detail.routeWithArgs -> {
                    Log.w("Click on menu edit", "Property $propertyId")
                    navController.navigateToDetailEdit(propertyId.toString())
                }

                EditDetail.routeWithArgs, SearchCriteria.route, Loan.route -> {
                    Log.w("Click on menu valid", "Property $propertyId")
                    // TODO: Replace toast with specific action
                    Toast
                        .makeText(
                            context, "Click on ${context.getString(R.string.valid)}",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
            }
        }


        /**
         * Composable
         */
        Scaffold(
            topBar = {
                AppTopBar(
                    menuIcon = appViewModel.menuIcon(currentScreen),
                    menuIconContentDesc = stringResource(
                        appViewModel.menuIconContentDesc(
                            currentScreen
                        )
                    ),
                    menuEnabled = appViewModel.menuEnabled(currentScreen, windowSize),
                    // onClickMenu = { appViewModel.onClickMenu(currentBackStack, navController) },
                    onClickMenu = onClickMenu,
                    onClickRadioButton = appViewModel.onClickRadioButton,
                )
            },
            bottomBar = {
                AppTabRow(
                    allScreens = tabRowScreens,
                    onTabSelected = { newScreen ->
                        navController.navigateSingleTopTo(
                            newScreen,
                            appViewModel.fakeProperties[0].pId.toString()
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
                appViewModel = appViewModel,
                properties = properties,
                onOpenAppSettings = activity::openAppSettings,
            )
        }
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)

}




