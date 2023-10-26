package com.aquaero.realestatemanager

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aquaero.realestatemanager.ui.components.AppTabRow
import com.aquaero.realestatemanager.ui.theme.RealEstateManagerTheme

class RealEstateManagerActivity: ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RealEstateManagerApp()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RealEstateManagerApp() {
    RealEstateManagerTheme {
        val navController = rememberNavController()
        // Fetch current destination
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        // Use PropertyList as a backup screen if the returned value is null
        val currentScreen = tabRowScreens.find { it.route == currentDestination?.route } ?: Other   //PropertyList

        /**
         * Navigate single top to tabs, purging Detail and Edit routes from back stack
         */
        fun navigateSingleTopWithDetailBackStackPurge(currentScreen: AppDestination, newScreen: AppDestination) {
            if (currentScreen == Other) { navController.popBackStack() }    // If current screen is Detail or Edit
            if (currentScreen == Other) { navController.popBackStack() }    // If current screen is Detail or Edit
            navController.navigateSingleTopTo(newScreen.route)
        }

        Scaffold (
            bottomBar = {
                AppTabRow(
                    allScreens = tabRowScreens,

                    // onTabSelected = { newScreen -> navController.navigateSingleTopTo(newScreen.route) },
                    onTabSelected = { newScreen ->
                        navigateSingleTopWithDetailBackStackPurge(currentScreen, newScreen) },

                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
                )
        }
    }
}
