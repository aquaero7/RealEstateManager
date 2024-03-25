package com.aquaero.realestatemanager.ui.composable

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.AppContentType
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.navigateToDetail
import com.aquaero.realestatemanager.ui.screen.SearchScreen
import com.aquaero.realestatemanager.viewmodel.SearchViewModel

@Composable
fun SearchComposable(
//    navController: NavHostController,
//    contentType: AppContentType,
    searchViewModel: SearchViewModel,
    types: MutableList<Type>,
    stringTypes: MutableList<String>,
    agents: MutableList<Agent>,
    stringAgents:MutableList<String>,
    pois: MutableList<Poi>,
    currency: String,
    popBackStack: () -> Unit,
) {
    val onFieldValueChange: (String, String?, String) -> Unit = { field, fieldType, value ->
        searchViewModel.onFieldValueChange(field = field, fieldType = fieldType, value = value, currency = currency)
    }
    val onDropdownMenuValueChange: (String) -> Unit = {
        searchViewModel.onDropdownMenuValueChange(value = it, types = types, agents = agents)
    }
    val onPoiClick: (String, Boolean) -> Unit = { poiItem, isSelected ->
        searchViewModel.onPoiClick(poiItem = poiItem, isSelected = isSelected)
    }
    val onSalesRadioButtonClick: (String) -> Unit = {
        searchViewModel.onSalesRadioButtonClick(it)
    }
    val onPhotosRadioButtonClick: (String) -> Unit = {
        searchViewModel.onPhotosRadioButtonClick(it)
    }

    SearchScreen(
        stringTypes = stringTypes,
        stringAgents = stringAgents,
        currency = currency,
        onFieldValueChange = onFieldValueChange,
        onDropdownMenuValueChange = onDropdownMenuValueChange,
        onPoiClick = onPoiClick,
        onSalesRadioButtonClick = onSalesRadioButtonClick,
        onPhotosRadioButtonClick = onPhotosRadioButtonClick,
        popBackStack = popBackStack,
    )

}