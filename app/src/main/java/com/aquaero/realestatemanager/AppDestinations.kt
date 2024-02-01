package com.aquaero.realestatemanager

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.navArgument

val context by lazy { ApplicationRoot.getContext() }

/**
 * Contract for information needed on every navigation destination
 */
interface AppDestination {
    val icon: ImageVector
    val route: String
    val label: String
}

/**
* App navigation destinations
*/

const val propertyKey = "single_property"

object ListAndDetail: AppDestination {
    override val icon = Icons.AutoMirrored.Filled.ViewList
    override val route = "list"
    override val label = context.getString(R.string.list)
    val routeWithArgs = "$route/{$propertyKey}"
    val arguments = listOf(navArgument(propertyKey) { type = NavType.StringType })
}

object GeolocMap: AppDestination {
    override val icon = Icons.Filled.Map
    override val route = "map"
    override val label = context.getString(R.string.map)
}

object SearchCriteria: AppDestination {
    override val icon = Icons.AutoMirrored.Filled.ManageSearch
    override val route = "search_criteria"
    override val label = context.getString(R.string.search_criteria)
}

object Loan: AppDestination {
    override val icon = Icons.Filled.AccountBalance
    override val route = "loan"
    override val label = context.getString(R.string.loan)
}

// Destination not displayed in the bottom TabRow
object Detail: AppDestination {
    override val icon = Icons.Filled.Details
    override val route = "detail"
    override val label = context.getString(R.string.detail)
    val routeWithArgs = "$route/{$propertyKey}"
    val arguments = listOf(navArgument(propertyKey) { type = NavType.StringType })
}

// Destination not displayed in the bottom TabRow
object EditDetail: AppDestination {
    override val icon = Icons.Filled.EditNote
    override val route ="edit_detail"
    override val label = context.getString(R.string.edit_detail)
    val routeWithArgs = "${route}/{$propertyKey}"
    val arguments = listOf(navArgument(propertyKey) { type = NavType.StringType })
}


// Screens displayed in the bottom TabRow
val tabRowScreens = listOf(ListAndDetail, GeolocMap, SearchCriteria, Loan)
