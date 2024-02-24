package com.aquaero.realestatemanager.ui.composable

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.AppContentType
import com.aquaero.realestatemanager.navigateToDetail
import com.aquaero.realestatemanager.ui.screen.SearchScreen

@Composable
fun SearchComposable(
    navController: NavHostController,
    contentType: AppContentType,
) {
    val onButton1Click = { navController.navigateToDetail("1", contentType) }
    val onButton2Click = { navController.navigateToDetail("2", contentType) }

    SearchScreen(
        onButton1Click = onButton1Click,
        onButton2Click = onButton2Click
    )

}