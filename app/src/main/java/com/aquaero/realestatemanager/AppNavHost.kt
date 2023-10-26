package com.aquaero.realestatemanager

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aquaero.realestatemanager.ui.screens.DetailScreen
import com.aquaero.realestatemanager.ui.screens.EditScreen
import com.aquaero.realestatemanager.ui.screens.ListScreen
import com.aquaero.realestatemanager.ui.screens.LoanScreen
import com.aquaero.realestatemanager.ui.screens.MapScreen
import com.aquaero.realestatemanager.ui.screens.SearchScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = PropertyList.route,
        modifier = modifier
    ) {
        composable(route = PropertyList.route) {
            ListScreen(
                onPropertyClick =  { propertyId ->
                    navController.navigateToDetail(propertyId.toString())
                }
            )
        }

        composable(route = PropertyMap.route) {
            MapScreen()
        }

        composable(route = SearchCriteria.route) {
            SearchScreen(
                onButton1Click = { navController.navigateToDetail("propertyId1") },
                onButton2Click = { navController.navigateToDetail("propertyId2") }
            )
        }

        composable(route = Loan.route) {
            LoanScreen()
        }

        composable(
            route = Detail.routeWithArgs,
            arguments = Detail.arguments
        ) { navBackStackEntry ->
            val propertyId = navBackStackEntry.arguments?.getString(Detail.propertyKey)

            DetailScreen(
                propertyId = propertyId,
                onEditButtonClick = { navController.navigateToDetailEdit(propertyId!!) },
                onBackPressed = { navController.popBackStack() }
            )
        }

        composable(
            route = EditDetail.routeWithArgs,
            arguments = EditDetail.arguments
        ) { navBackStackEntry ->
            val propertyId = navBackStackEntry.arguments?.getString(EditDetail.propertyEditKey)

            EditScreen(
                propertyId = propertyId,
                onBackPressed = { navController.popBackStack() }
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) = this.navigate(route) {
    popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) { saveState = true }
    launchSingleTop = true
    restoreState = true
}

fun NavHostController.navigateToDetail(propertyId: String) {
    this.navigate("${Detail.route}/$propertyId")
}

fun NavHostController.navigateToDetailEdit(propertyId: String) {
    this.navigate("${EditDetail.route}/$propertyId")
}

