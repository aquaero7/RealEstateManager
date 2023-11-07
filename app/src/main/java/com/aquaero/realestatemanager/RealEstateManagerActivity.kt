package com.aquaero.realestatemanager

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aquaero.realestatemanager.ui.components.app.AppTabRow
import com.aquaero.realestatemanager.ui.components.app.AppTopBar
import com.aquaero.realestatemanager.ui.theme.RealEstateManagerTheme
import com.aquaero.realestatemanager.utils.AppContentType

class RealEstateManagerActivity: ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val windowSize = calculateWindowSizeClass(activity = this)
            RealEstateManagerApp(windowSize.widthSizeClass)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RealEstateManagerApp(
    windowSize: WindowWidthSizeClass
) {
    RealEstateManagerTheme {
        /**
         * Init content type, according to window's width,
         * to choose dynamically, on screen state changes, whether to show
         * just a list content, or both a list and detail content
         */
        val contentType: AppContentType = when (windowSize) {
            WindowWidthSizeClass.Expanded -> { AppContentType.SCREEN_WITH_DETAIL }
            else -> { AppContentType.SCREEN_ONLY }
        }

        /** Init data */
        val context = LocalContext.current

        /**
         * Navigation
         */
        val tabRowScreens = tabRowScreens
        val navController = rememberNavController()
        // Fetch current destination
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = currentDestination?.route
        // Use 'ListAndDetail' as a backup screen if the returned value is null
        val currentTabScreen = tabRowScreens.find { it.route == currentScreen } ?: ListAndDetail
        // Init the click on topBar menu icon

        /**
         * TopBar
         */
        val onClickMenu = {
            val msg = when (currentScreen) {
                ListAndDetail.routeWithArgs -> R.string.edit
                Detail.routeWithArgs -> R.string.edit
                else -> R.string.valid
            }
            Toast.makeText(context, "Click on ${context.getString(msg)}", Toast.LENGTH_SHORT).show()
        }

        val menuIcon = if (
            currentScreen == EditDetail.routeWithArgs ||
            currentScreen == SearchCriteria.route ||
            currentScreen == Loan.route
            ) Icons.Default.Check else Icons.Default.Edit

        val menuIconContentDesc = if (
            currentScreen == EditDetail.routeWithArgs ||
            currentScreen == Loan.route
            ) stringResource(id = R.string.cd_check) else stringResource(id = R.string.cd_edit)

        val menuEnabled = (
                currentScreen != GeolocMap.route) &&
                (currentScreen != ListAndDetail.routeWithArgs ||
                contentType == AppContentType.SCREEN_WITH_DETAIL)

        val onClickRadioButton = { currency: String -> Toast
            .makeText(context, "Click on $currency", Toast.LENGTH_SHORT)
            .show()
        }


        /**
         * Navigate single top to tabs
         */
        fun navigateSingleTop(newScreen: AppDestination) {
            navController.navigateSingleTopTo(newScreen)
        }

        /**
         * Composable
         */

        Scaffold (
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
                    onTabSelected = { newScreen -> navigateSingleTop(newScreen) },
                    currentScreen = currentTabScreen
                )
            }
        ) { innerPadding ->
            AppNavHost(
                contentType = contentType,
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

