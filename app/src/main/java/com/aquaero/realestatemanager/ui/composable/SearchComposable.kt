package com.aquaero.realestatemanager.ui.composable

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.AppContentType
import com.aquaero.realestatemanager.navigateToDetail
import com.aquaero.realestatemanager.ui.screen.SearchScreen
import com.aquaero.realestatemanager.viewmodel.SearchViewModel

@Composable
fun SearchComposable(
    navController: NavHostController,
    contentType: AppContentType,
    searchViewModel: SearchViewModel,
    currency: String,
    popBackStack: () -> Unit,
) {
    val onFieldValueChange: (String, String, String) -> Unit = { field, fieldType, value ->
        searchViewModel.onFieldValueChange(field = field, fieldType = fieldType, value = value, currency = currency)
    }

    SearchScreen(
        currency = currency,
        onFieldValueChange = onFieldValueChange,
        popBackStack = popBackStack,
    )

}