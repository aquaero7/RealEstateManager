package com.aquaero.realestatemanager

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Map
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument


/**
 * Contract for information needed on every navigation destination
 */
interface AppDestination {
    val icon: ImageVector
    val route: String
    val labelResId: Int
}


/* App navigation destinations */

object ListAndDetail: AppDestination {
    override val icon = Icons.AutoMirrored.Filled.ViewList
    override val route = AppRoute.LIST.value
    override val labelResId = R.string.list
    val routeWithArgs = "$route/{$propertyKey}/{$selectedKey}"
    val arguments = listOf(
        navArgument(propertyKey) { type = NavType.StringType },
        navArgument(selectedKey) { type = NavType.BoolType }
    )
}

object GeolocMap: AppDestination {
    override val icon = Icons.Filled.Map
    override val route = AppRoute.MAP.value
    override val labelResId = R.string.map
}

object SearchCriteria: AppDestination {
    override val icon = Icons.AutoMirrored.Filled.ManageSearch
    override val route = AppRoute.SEARCH.value
    override val labelResId = R.string.search_criteria
}

object Loan: AppDestination {
    override val icon = Icons.Filled.AccountBalance
    override val route = AppRoute.LOAN.value
    override val labelResId = R.string.loan
}

/* Destination not displayed in the bottom TabRow */
object EditDetail: AppDestination {
    override val icon = Icons.Filled.EditNote
    override val route = AppRoute.EDIT.value
    override val labelResId = R.string.edit_detail
    val routeWithArgs = "${route}/{$propertyKey}"
    val arguments = listOf(navArgument(propertyKey) { type = NavType.StringType })
}


/* Screens displayed in the bottom TabRow */
val tabRowScreens = listOf(ListAndDetail, GeolocMap, SearchCriteria, Loan)
