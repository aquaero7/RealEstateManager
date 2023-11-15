package com.aquaero.realestatemanager

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.screen.DetailScreen
import com.aquaero.realestatemanager.ui.screen.EditScreen
import com.aquaero.realestatemanager.ui.screen.ListAndDetailScreen
import com.aquaero.realestatemanager.ui.screen.LoanScreen
import com.aquaero.realestatemanager.ui.screen.MapScreen
import com.aquaero.realestatemanager.ui.screen.SearchScreen
import com.aquaero.realestatemanager.utils.AppContentType
import com.aquaero.realestatemanager.viewmodel.AppViewModel
import com.aquaero.realestatemanager.viewmodel.DetailViewModel
import com.aquaero.realestatemanager.viewmodel.EditViewModel
import com.aquaero.realestatemanager.viewmodel.ListViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    modifier: Modifier,
    contentType: AppContentType,
    navController: NavHostController,
    appViewModel: AppViewModel,
    listViewModel: ListViewModel,
    detailViewModel: DetailViewModel,
    editViewModel: EditViewModel,
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
            (navBackStackEntry.arguments!!.getString(propertyKey)
                ?: appViewModel.fakeProperties[0].pId.toString()).also {

                ListAndDetailScreen(
                    listViewModel = listViewModel,
                    detailViewModel = detailViewModel,
                    contentType = contentType,
                    onPropertyClick =  { propertyId ->
                        if (contentType == AppContentType.SCREEN_ONLY) {
                            navController.navigateToDetail(propertyId.toString())
                        } else {
                            navController.navigateSingleTopTo(ListAndDetail, propertyId.toString())
                        }
                    },
                    property = appViewModel.propertyFromId(it.toLong()),
                    // onEditButtonClick = { navController.navigateToDetailEdit(it) }, // TODO : To be deleted after TopBar menu action implementation
                    onFabClick = { navController.navigateToDetailEdit("-1") },
                    onBackPressed = { navController.popBackStack() },
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
            val propertyId = navBackStackEntry.arguments!!.getString(propertyKey)!!

            DetailScreen(
                detailViewModel = detailViewModel,
                property = appViewModel.propertyFromId(propertyId.toLong()),
                // onEditButtonClick = { navController.navigateToDetailEdit(propertyId) }, // TODO : To be deleted after TopBar menu action implementation
                onBackPressed = { navController.popBackStack() }
            )
        }

        composable(
            route = EditDetail.routeWithArgs,
            arguments = EditDetail.arguments
        ) { navBackStackEntry ->
            val propertyId = navBackStackEntry.arguments!!.getString(propertyKey)
            val property: Property? = if (propertyId != "-1") {
                // Edition mode
                appViewModel.propertyFromId(propertyId!!.toLong())
            } else {
                // Creation mode
                null
            }

            EditScreen(
                editViewModel = editViewModel,
                property = property,
                onBackPressed = { navController.popBackStack() }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavHostController.navigateSingleTopTo(destination: AppDestination, propertyId: String) {
    val route = if (destination == ListAndDetail) "${destination.route}/${propertyId}" else destination.route
    this.navigate(route) {
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) { saveState = false }
        launchSingleTop = true
        restoreState = false
    }
}

fun NavHostController.navigateToDetail(propertyId: String) {
    this.navigate("${ListAndDetail.route}/$propertyId")
}

fun NavHostController.navigateToDetailEdit(propertyId: String) {
    this.navigate("${EditDetail.route}/$propertyId")
}




