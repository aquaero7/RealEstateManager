package com.aquaero.realestatemanager

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.aquaero.realestatemanager.ui.composable.DetailComposable
import com.aquaero.realestatemanager.ui.composable.EditComposable
import com.aquaero.realestatemanager.ui.composable.ListAndDetailComposable
import com.aquaero.realestatemanager.ui.composable.LoanComposable
import com.aquaero.realestatemanager.ui.composable.MapComposable
import com.aquaero.realestatemanager.ui.composable.SearchComposable
import com.aquaero.realestatemanager.viewmodel.AppViewModel
import com.aquaero.realestatemanager.viewmodel.DetailViewModel
import com.aquaero.realestatemanager.viewmodel.EditViewModel
import com.aquaero.realestatemanager.viewmodel.ListViewModel
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
    listViewModel: ListViewModel,
    detailViewModel: DetailViewModel,
    editViewModel: EditViewModel,
    mapViewModel: MapViewModel,
    searchViewModel: SearchViewModel,
    loanViewModel: LoanViewModel,
    currency: String,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ListAndDetail.routeWithArgs,
    ) {

        composable(
            route = ListAndDetail.routeWithArgs, arguments = ListAndDetail.arguments
        ) { navBackStackEntry ->
            (navBackStackEntry.arguments?.getString(propertyKey)).also {
                val propertyId = it?.let {
                    navBackStackEntry.arguments!!.getString(propertyKey)!!.toLong()
                }
                ListAndDetailComposable(
                    // For list screen only
                    contentType = contentType,
                    // For both list and detail screens
                    navController = navController,
                    listViewModel = listViewModel,
                    currency = currency,
                    properties = properties,
                    types = types,
                    stringTypes = stringTypes,
                    // For detail screen only
                    propertyId = propertyId,
                    context = context,
                    agents = agents,
                    stringAgents = stringAgents,
                    addresses = addresses,
                    photos = photos,
                    pois = pois,
                    propertyPoiJoins = propertyPoiJoins,
                )
            }
        }

        composable(route = GeolocMap.route) {
            MapComposable(
                context = context,
                mapViewModel = mapViewModel,
                properties = properties,
                addresses = addresses,
            )
        }

        composable(route = SearchCriteria.route) {
            SearchComposable(
                navController = navController,
                contentType = contentType
            )
        }

        composable(route = Loan.route) {
            LoanComposable()
        }

        composable(
            route = Detail.routeWithArgs,
            arguments = Detail.arguments
        ) { navBackStackEntry ->
            val propertyId = navBackStackEntry.arguments!!.getString(propertyKey)!!.toLong()
            DetailComposable(
                propertyId = propertyId,
                context = context,
                navController = navController,
                detailViewModel = detailViewModel,
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
                navController = navController,
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
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(destination: AppDestination, propertyId: String?) {
    val route =
        if (destination == ListAndDetail) "${destination.route}/${propertyId}" else destination.route
    this.navigate(route = route) {
        popUpTo(id = this@navigateSingleTopTo.graph.findStartDestination().id) { saveState = false }
        launchSingleTop = true
        restoreState = false
    }
}

fun NavHostController.navigateToDetail(propertyId: String, contentType: AppContentType) {
    if (contentType == AppContentType.LIST_OR_DETAIL) {
        this.navigate(route = "${Detail.route}/$propertyId")
    } else {
        this.navigateSingleTopTo(destination = ListAndDetail, propertyId = propertyId)
//        this.navigate(route = "${ListAndDetail.route}/${propertyId}") // TODO: To be deleted
    }
}

fun NavHostController.navigateToEditDetail(propertyId: String) {
    this.navigate(route = "${EditDetail.route}/$propertyId")
}


