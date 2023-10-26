package com.aquaero.realestatemanager

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * Contract for information needed on every navigation destination
 */
interface AppDestination {
    val icon: ImageVector
    val route: String
}

/**
* App navigation destinations
*/
object PropertyList: AppDestination {
    override val icon = Icons.Filled.ViewList
    override val route = "list"
}

object PropertyMap: AppDestination {
    override val icon = Icons.Filled.Map
    override val route = "map"
}

object SearchCriteria: AppDestination {
    override val icon = Icons.Filled.ManageSearch
    override val route = "search_criteria"
}

object Loan: AppDestination {
    override val icon = Icons.Filled.AccountBalance
    override val route = "loan"
}

// Not displayed in the bottom TabRow
object Detail: AppDestination {
    override val icon = Icons.Filled.Details
    override val route = "detail"
    const val propertyKey = "single_property"
    val routeWithArgs = "$route/{$propertyKey}"
    val arguments = listOf(navArgument(propertyKey) { type = NavType.StringType })
}

// Not displayed in the bottom TabRow
object EditDetail: AppDestination {
    override val icon = Icons.Filled.EditNote
    override val route ="edit_detail"
    const val propertyEditKey = "property_edit"
    val routeWithArgs = "${route}/{$propertyEditKey}"
    val arguments = listOf(navArgument(propertyEditKey) { type = NavType.StringType })
}

/**
 * Destination created to be used as the default one in BottomTabRow
 * when screens Detail or Edit are selected
 */
object Other: AppDestination {
    override val icon = Icons.Filled.Info
    override val route = "other"
}

// Screens displayed in the bottom TabRow
val tabRowScreens = listOf(PropertyList, PropertyMap, SearchCriteria, Loan)
