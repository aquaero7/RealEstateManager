package com.aquaero.realestatemanager

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.aquaero.realestatemanager.ui.screen.LocationPermissionsScreen
import com.aquaero.realestatemanager.ui.screen.MapScreen
import com.aquaero.realestatemanager.ui.screen.SearchScreen
import com.aquaero.realestatemanager.utils.AppContentType
import com.aquaero.realestatemanager.utils.MyLocationSource
import com.aquaero.realestatemanager.viewmodel.AppViewModel
import com.aquaero.realestatemanager.viewmodel.DetailViewModel
import com.aquaero.realestatemanager.viewmodel.EditViewModel
import com.aquaero.realestatemanager.viewmodel.ListViewModel
import com.aquaero.realestatemanager.viewmodel.MapViewModel
import com.aquaero.realestatemanager.viewmodel.ViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow

@SuppressLint("NewApi")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    modifier: Modifier,
    contentType: AppContentType,
    navController: NavHostController,
    properties: List<Property>,
    appViewModel: AppViewModel,
    listViewModel: ListViewModel,
    detailViewModel: DetailViewModel,
    editViewModel: EditViewModel,
    mapViewModel: MapViewModel,
    // onClickMenu: () -> Unit
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

                val property = listViewModel.propertyFromId(it.toLong())
                val thumbnailUrl = listViewModel.thumbnailUrl(property)
                val onPropertyClick: (Long) -> Unit = { propertyId ->
                    navController.navigateToDetail(propertyId.toString(), contentType)
                }
                val onFabClick = { navController.navigateToDetailEdit("-1") }
                val onBackPressed: () -> Unit = { navController.popBackStack() }

                ListAndDetailScreen(
                    items = properties,
                    thumbnailUrl = thumbnailUrl,
                    contentType = contentType,
                    onPropertyClick = onPropertyClick,
                    property = property,
                    onFabClick = onFabClick,
                    onBackPressed = onBackPressed,
                )
            }
        }

        composable(route = GeolocMap.route) {
            var locationPermissionsGranted by remember {
                mutableStateOf(mapViewModel.areLocPermsGranted())
            }

            if (locationPermissionsGranted) {
                var showMap by remember { mutableStateOf(false) }
                var currentLocation by remember { mutableStateOf(DEFAULT_LOCATION) }
                val locationFlow = callbackFlow {
                    while (true) {
                        mapViewModel.getCurrentLocation() {
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
                val onOpenAppSettings = { mapViewModel.openAppSettings() }
                val onPermissionsGranted = { locationPermissionsGranted = true }

                LocationPermissionsScreen(
                    onOpenAppSettings = onOpenAppSettings,
                    onPermissionsGranted = onPermissionsGranted,
                )
            }
        }

        composable(route = SearchCriteria.route) {
            val onButton1Click = { navController.navigateToDetail("1", contentType) }
            val onButton2Click = { navController.navigateToDetail("2", contentType) }

            SearchScreen(
                onButton1Click = onButton1Click,
                onButton2Click = onButton2Click,

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
            val property = detailViewModel.propertyFromId(propertyId.toLong())
            val thumbnailUrl = detailViewModel.thumbnailUrl(property)
            val onBackPressed: () -> Unit = { navController.popBackStack() }

            DetailScreen(
                property = property,
                thumbnailUrl = thumbnailUrl,
                onBackPressed = onBackPressed
            )
        }

        composable(
            route = EditDetail.routeWithArgs,
            arguments = EditDetail.arguments
        ) { navBackStackEntry ->
            val propertyId = navBackStackEntry.arguments!!.getString(propertyKey)
            val property: Property? = if (propertyId != "-1") {
                // Edition mode
                editViewModel.propertyFromId(propertyId!!.toLong())
            } else {
                // Creation mode
                null
            }
            val pTypeSet = { editViewModel.pTypeSet }
            val agentSet = editViewModel.agentSet
            val onDescriptionValueChanged: (String) -> Unit = {}                        //TODO

            val onBackPressed: () -> Unit = { navController.popBackStack() }

            EditScreen(
                pTypeSet = pTypeSet,
                agentSet = agentSet,
                property = property,
                // onClickMenu = onClickMenu,
                onBackPressed = onBackPressed,
            )
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


