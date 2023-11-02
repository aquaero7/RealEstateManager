package com.aquaero.realestatemanager

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aquaero.realestatemanager.ui.components.AppTabRow
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

        // Init screens list for tabRow
        val tabRowScreens = tabRowScreens
        // Init navController
        val navController = rememberNavController()
        // Fetch current destination
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        // Use 'ListAndDetail' as a backup screen if the returned value is null
        val currentScreen = tabRowScreens.find { it.route == currentDestination?.route } ?: ListAndDetail

        /**
         * Navigate single top to tabs
         */
        fun navigateSingleTop(newScreen: AppDestination) {
            navController.navigateSingleTopTo(newScreen)
        }

        Scaffold (
            bottomBar = {
                AppTabRow(
                    allScreens = tabRowScreens,
                    onTabSelected = { newScreen -> navigateSingleTop(newScreen) },
                    currentScreen = currentScreen
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

