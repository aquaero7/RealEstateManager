package com.aquaero.realestatemanager.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.Type
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
//    pois: MutableList<Poi>,
    currency: String,
    addresses: List<Address>,
    photos: List<Photo>,
    popBackStack: () -> Unit,
) {
    val context = LocalContext.current
    val onFieldValueChange: (String, String?, String) -> Unit = { field, fieldType, value ->
        searchViewModel.onFieldValueChange(field = field, fieldType = fieldType, fieldValue = value, currency = currency)
    }
    val onDropdownMenuValueChange: (String) -> Unit = {
        searchViewModel.onDropdownMenuValueChange(value = it, types = types, agents = agents)
    }
    val onPoiClick: (String, Boolean) -> Unit = { poiItem, isSelected ->
        searchViewModel.onPoiClick(poiItem = poiItem, isSelected = isSelected)
    }
    val onSalesRadioButtonClick: (String) -> Unit = {
        searchViewModel.onSalesRadioButtonClick(context = context , button = it)
    }
    val onPhotosRadioButtonClick: (String) -> Unit = {
        searchViewModel.onPhotosRadioButtonClick(context = context , button = it)
    }
    val itemType: (String) -> String =
        { searchViewModel.itemType(typeId = it, types = types, stringTypes = stringTypes) }

    val searchResults: MutableList<Property> by searchViewModel.searchResultsFlow.collectAsState(initial = mutableListOf())

    SearchScreen(
        stringTypes = stringTypes,
        stringAgents = stringAgents,
        currency = currency,
        searchResults = searchResults,
        addresses = addresses,
        photos = photos,
        itemType = itemType,
        onFieldValueChange = onFieldValueChange,
        onDropdownMenuValueChange = onDropdownMenuValueChange,
        onPoiClick = onPoiClick,
        onSalesRadioButtonClick = onSalesRadioButtonClick,
        onPhotosRadioButtonClick = onPhotosRadioButtonClick,
        popBackStack = popBackStack,
    )

}