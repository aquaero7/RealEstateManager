package com.aquaero.realestatemanager

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.screen.DetailScreen
import com.aquaero.realestatemanager.ui.screen.EditScreen
import com.aquaero.realestatemanager.ui.screen.ListAndDetailScreen
import com.aquaero.realestatemanager.ui.screen.LoanScreen
import com.aquaero.realestatemanager.ui.screen.LocationPermissionsScreen
import com.aquaero.realestatemanager.ui.screen.MapScreen
import com.aquaero.realestatemanager.ui.screen.SearchScreen
import com.aquaero.realestatemanager.utils.AppContentType
import com.aquaero.realestatemanager.utils.MyLocationSource
import com.aquaero.realestatemanager.viewmodel.AppViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow

@SuppressLint("NewApi")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    modifier: Modifier,
    contentType: AppContentType,
    navController: NavHostController,
    appViewModel: AppViewModel,
    properties: List<Property>,
    onOpenAppSettings: () -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ListAndDetail.routeWithArgs,
    ) {

        composable(
            route = ListAndDetail.routeWithArgs, arguments = ListAndDetail.arguments
        ) { navBackStackEntry ->
            (navBackStackEntry.arguments!!.getString(propertyKey)
                ?: properties[0].pId.toString()).also {

                val property = appViewModel.propertyFromId(it.toLong())

                ListAndDetailScreen(
                    items = properties,
                    thumbnailUrl = appViewModel.thumbnailUrl(property),
                    contentType = contentType,
                    onPropertyClick = { propertyId ->
                        navController.navigateToDetail(propertyId.toString(), contentType)
                    },
                    property = property,
                    onFabClick = { navController.navigateToDetailEdit("-1") },
                    onBackPressed = { navController.popBackStack() },
                )
            }
        }

        composable(route = GeolocMap.route) {
            val context = LocalContext.current
            var locationPermissionsGranted by remember {
                mutableStateOf(appViewModel.areLocPermsGranted())
            }

            if (locationPermissionsGranted) {
                var showMap by remember { mutableStateOf(false) }
                var currentLocation by remember { mutableStateOf(DEFAULT_LOCATION) }
                val locationFlow = callbackFlow {
                    while (true) {
                        appViewModel.getCurrentLocation() {
                            currentLocation = it
                            showMap = true
                            trySend(it)
                        }
                        delay(1)
                    }
                }
                val locationState = locationFlow.collectAsState(initial = currentLocation)
                val locationSource = MyLocationSource()
                MapScreen(
                    showMap = showMap,
                    properties = properties,
                    locationState = locationState,
                    locationSource = locationSource,
                )

            } else {
                LocationPermissionsScreen(
                    onOpenAppSettings = onOpenAppSettings,
                    onPermissionsGranted = { locationPermissionsGranted = true },
                )
            }
        }

        composable(route = SearchCriteria.route) {
            SearchScreen(
                onButton1Click = { navController.navigateToDetail("1", contentType) },
                onButton2Click = { navController.navigateToDetail("2", contentType) },
            )
        }

        composable(route = Loan.route) {
            LoanScreen()
        }

        composable(
            route = Detail.routeWithArgs, arguments = Detail.arguments
        ) { navBackStackEntry ->
            val propertyId = navBackStackEntry.arguments!!.getString(propertyKey)!!
            val property = appViewModel.propertyFromId(propertyId.toLong())

            DetailScreen(property = property,
                thumbnailUrl = appViewModel.thumbnailUrl(property),
                onBackPressed = { navController.popBackStack() })
        }

        composable(
            route = EditDetail.routeWithArgs, arguments = EditDetail.arguments
        ) { navBackStackEntry ->
            val propertyId = navBackStackEntry.arguments!!.getString(propertyKey)
            val property: Property? = if (propertyId != "-1") {
                // Edition mode
                appViewModel.propertyFromId(propertyId!!.toLong())
            } else {
                // Creation mode
                null
            }

            EditScreen(pTypeSet = { appViewModel.pTypeSet },
                agentSet = appViewModel.agentSet,
                property = property,
                onBackPressed = { navController.popBackStack() })
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavHostController.navigateSingleTopTo(destination: AppDestination, propertyId: String) {
    val route =
        if (destination == ListAndDetail) "${destination.route}/${propertyId}" else destination.route
    this.navigate(route) {
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) { saveState = false }
        launchSingleTop = true
        restoreState = false
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavHostController.navigateToDetail(propertyId: String, contentType: AppContentType) {
    if (contentType == AppContentType.SCREEN_ONLY) {
        this.navigate("${Detail.route}/$propertyId")
    } else {
        this.navigateSingleTopTo(ListAndDetail, propertyId)
    }
}

fun NavHostController.navigateToDetailEdit(propertyId: String) {
    this.navigate("${EditDetail.route}/$propertyId")
}


