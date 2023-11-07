package com.aquaero.realestatemanager

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aquaero.realestatemanager.data.fakeProperties
import com.aquaero.realestatemanager.ui.screens.DetailScreen
import com.aquaero.realestatemanager.ui.screens.EditScreen
import com.aquaero.realestatemanager.ui.screens.ListAndDetailScreen
import com.aquaero.realestatemanager.ui.screens.LoanScreen
import com.aquaero.realestatemanager.ui.screens.MapScreen
import com.aquaero.realestatemanager.ui.screens.SearchScreen
import com.aquaero.realestatemanager.utils.AppContentType

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    contentType: AppContentType,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ListAndDetail.routeWithArgs,
    ) {

        composable(
            route = ListAndDetail.routeWithArgs,
            arguments = ListAndDetail.arguments
        ) { navBackStackEntry ->
            (navBackStackEntry.arguments!!.getString(Detail.propertyKey)
                ?: fakeProperties[0].pId.toString()).also {

                ListAndDetailScreen(
                    contentType = contentType,
                    onPropertyClick =  { propertyId ->
                        if (contentType == AppContentType.SCREEN_ONLY) {
                            navController.navigateToDetail(propertyId.toString())
                        } else {
                            navController.navigateSingleTopTo(ListAndDetail, propertyId.toString())
                        }
                    },
                    propertyId = it,
                    onEditButtonClick = { navController.navigateToDetailEdit(it) },
                    onBackPressed = { navController.popBackStack() }
                )
            }
        }

        composable(route = GeolocMap.route) {
            MapScreen()
        }

        composable(route = SearchCriteria.route) {
            SearchScreen(
                onButton1Click = { navController.navigateToDetail("1") },
                onButton2Click = { navController.navigateToDetail("2") }
            )
        }

        composable(route = Loan.route) {
            LoanScreen()
        }

        composable(
            route = Detail.routeWithArgs,
            arguments = Detail.arguments
        ) { navBackStackEntry ->
            val propertyId = navBackStackEntry.arguments!!.getString(Detail.propertyKey)!!

            DetailScreen(
                propertyId = propertyId,
                onEditButtonClick = { navController.navigateToDetailEdit(propertyId) },
                onBackPressed = { navController.popBackStack() }
            )
        }

        composable(
            route = EditDetail.routeWithArgs,
            arguments = EditDetail.arguments
        ) { navBackStackEntry ->
            val propertyId = navBackStackEntry.arguments!!.getString(EditDetail.propertyEditKey)!!

            EditScreen(
                propertyId = propertyId,
                onBackPressed = { navController.popBackStack() }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavHostController.navigateSingleTopTo(destination: AppDestination, propertyId: String = fakeProperties[0].pId.toString()) {
    val route = if (destination == ListAndDetail) "${destination.route}/${propertyId}" else destination.route
    this.navigate(route) {
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) { saveState = false }
        launchSingleTop = true
        restoreState = false
    }
}

fun NavHostController.navigateToDetail(propertyId: String) {
    this.navigate("${Detail.route}/$propertyId")
}

fun NavHostController.navigateToDetailEdit(propertyId: String) {
    this.navigate("${EditDetail.route}/$propertyId")
}




