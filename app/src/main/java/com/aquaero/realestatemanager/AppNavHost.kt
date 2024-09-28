package com.aquaero.realestatemanager

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.ui.composable.EditComposable
import com.aquaero.realestatemanager.ui.composable.ListAndDetailComposable
import com.aquaero.realestatemanager.ui.composable.LoanComposable
import com.aquaero.realestatemanager.ui.composable.MapComposable
import com.aquaero.realestatemanager.ui.composable.SearchComposable
import com.aquaero.realestatemanager.viewmodel.AppViewModel
import com.aquaero.realestatemanager.viewmodel.EditViewModel
import com.aquaero.realestatemanager.viewmodel.ListAndDetailViewModel
import com.aquaero.realestatemanager.viewmodel.LoanViewModel
import com.aquaero.realestatemanager.viewmodel.MapViewModel
import com.aquaero.realestatemanager.viewmodel.SearchViewModel

@SuppressLint("NewApi")
@Composable
fun AppNavHost(
    modifier: Modifier,
    context: Context,
    contentType: AppContentType,
    navController: NavHostController,
    properties: MutableList<Property>,
    addresses: MutableList<Address>,
    photos: MutableList<Photo>,
    agents: MutableList<Agent>,
    types: MutableList<Type>,
    pois: MutableList<Poi>,
    propertyPoiJoins: MutableList<PropertyPoiJoin>,
    stringTypes: MutableList<String>,
    stringAgents: MutableList<String>,
    appViewModel: AppViewModel,
    listAndDetailViewModel: ListAndDetailViewModel,
    editViewModel: EditViewModel,
    mapViewModel: MapViewModel,
    searchViewModel: SearchViewModel,
    loanViewModel: LoanViewModel,
    currency: String,
    popBackStack: () -> Unit,
    closeApp: () -> Unit,
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
            navBackStackEntry.arguments?.let {
                val propertyId = it.getString(propertyKey)?.toLong()
                val propertySelected = it.getBoolean(selectedKey)
                ListAndDetailComposable(
                    propertySelected = propertySelected,
                    // For list screen only
                    contentType = contentType,
                    // For both list and detail screens
                    navController = navController,
                    listAndDetailViewModel = listAndDetailViewModel,
                    currency = currency,
                    properties = properties,
                    types = types,
                    stringTypes = stringTypes,
                    closeApp = closeApp,
                    // For detail screen only
                    propertyId = propertyId,
                    context = context,
                    agents = agents,
                    stringAgents = stringAgents,
                    addresses = addresses,
                    photos = photos,
                    pois = pois,
                    propertyPoiJoins = propertyPoiJoins,
                    popBackStack = popBackStack,
                )
            }
        }

        composable(route = GeolocMap.route) {
            MapComposable(
                context = LocalContext.current,
                mapViewModel = mapViewModel,
                properties = properties,
                addresses = addresses,
                popBackStack = popBackStack,
            )
        }

        composable(route = SearchCriteria.route) {
            SearchComposable(
                navController = navController,
                searchViewModel = searchViewModel,
                types = types,
                stringTypes = stringTypes,
                stringAgents = stringAgents,
                currency = currency,
                addresses = addresses,
                photos = photos,
                popBackStack = popBackStack,
            )
        }

        composable(route = Loan.route) {
            LoanComposable(
                navController = navController,
                loanViewModel = loanViewModel,
                currency = currency,
                popBackStack = popBackStack,
            )
        }

        composable(
            route = EditDetail.routeWithArgs,
            arguments = EditDetail.arguments
        ) { navBackStackEntry ->
            val propertyId = navBackStackEntry.arguments!!.getString(propertyKey)!!.toLong()
            EditComposable(
                propertyId = propertyId,
                context = context,
                editViewModel = editViewModel,
                currency = currency,
                properties = properties,
                types = types,
                stringTypes = stringTypes,
                agents = agents,
                stringAgents = stringAgents,
                addresses = addresses,
                photos = photos,
                pois = pois,
                propertyPoiJoins = propertyPoiJoins,
                popBackStack = popBackStack,
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(
    destination: AppDestination, propertyId: String?, viewModel: Any? = null,
) {
    val selected = false
    val route =
        if (destination == ListAndDetail) "${destination.route}/${propertyId}/${selected}"
        else destination.route
    when (destination) {
        SearchCriteria -> {
            with (viewModel as SearchViewModel) { resetData() }
            Log.w("AppNavHost", "SearchScreen data reset")
        }
    }
    this.navigate(route = route) {
        popUpTo(id = this@navigateSingleTopTo.graph.findStartDestination().id) { saveState = false }
        launchSingleTop = true
        restoreState = false
    }
}

fun NavHostController.navigateToDetail(propertyId: String, contentType: AppContentType) {
    if (contentType == AppContentType.LIST_OR_DETAIL) {
        val selected = true
        this.navigate(route = "${ListAndDetail.route}/$propertyId/$selected")
    } else {
        this.navigateSingleTopTo(destination = ListAndDetail, propertyId = propertyId)
    }
}

fun NavHostController.navigateToEditDetail(propertyId: String) {
    this.navigate(route = "${EditDetail.route}/$propertyId")
}
