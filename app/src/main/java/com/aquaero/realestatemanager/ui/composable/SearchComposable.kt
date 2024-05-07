package com.aquaero.realestatemanager.ui.composable

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.SearchCriteria
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.navigateSingleTopTo
import com.aquaero.realestatemanager.ui.screen.SearchScreen
import com.aquaero.realestatemanager.viewmodel.SearchViewModel

@Composable
fun SearchComposable(
    navController: NavHostController,
    searchViewModel: SearchViewModel,
    types: MutableList<Type>,
    stringTypes: MutableList<String>,
    stringAgents:MutableList<String>,
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
        searchViewModel.onDropdownMenuValueChange(value = it, stringTypes = stringTypes, stringAgents = stringAgents)
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
    val onClearButtonClick: (String, String) -> Unit = { bound, field ->
        searchViewModel.onClearButtonClick(bound, field)
    }
    val onClearAllButtonClick: () -> Unit = {
        searchViewModel.resetScrollToResults()
        navController.navigateSingleTopTo(
            destination = SearchCriteria, propertyId = null, viewModel = searchViewModel,
        )
        Log.w("SearchComposable", "Click on clear all button")
    }
    val itemType: (String) -> String =
        { searchViewModel.itemType(typeId = it, types = types, stringTypes = stringTypes) }

    val searchResults: MutableList<Property> by searchViewModel.searchResultsFlow.collectAsState(initial = mutableListOf())
    val scrollToResultsCounter: Int by searchViewModel.scrollToResultsFlow.collectAsState(initial = 0)

    SearchScreen(
        stringTypes = stringTypes,
        stringAgents = stringAgents,
        currency = currency,
        searchResults = searchResults,
        scrollToResultsCounter = scrollToResultsCounter,
        addresses = addresses,
        photos = photos,
        itemType = itemType,
        descriptionValue = searchViewModel.description,
        zipValue = searchViewModel.zip,
        cityValue = searchViewModel.city,
        stateValue = searchViewModel.state,
        countryValue = searchViewModel.country,
        priceMinValue = searchViewModel.priceMin,
        priceMaxValue = searchViewModel.priceMax,
        surfaceMinValue = searchViewModel.surfaceMin,
        surfaceMaxValue = searchViewModel.surfaceMax,
        roomsMinValue = searchViewModel.roomsMin,
        roomsMaxValue = searchViewModel.roomsMax,
        bathroomsMinValue = searchViewModel.bathroomsMin,
        bathroomsMaxValue = searchViewModel.bathroomsMax,
        bedroomsMinValue = searchViewModel.bedroomsMin,
        bedroomsMaxValue = searchViewModel.bedroomsMax,
        registrationDateMinValue = searchViewModel.registrationDateMin,
        registrationDateMaxValue = searchViewModel.registrationDateMax,
        saleDateMinValue = searchViewModel.saleDateMin,
        saleDateMaxValue = searchViewModel.saleDateMax,
        typeValue = searchViewModel.type ?: "",
        agentValue = searchViewModel.agent ?: "",
        salesRadioIndex = searchViewModel.salesRadioIndex,
        photosRadioIndex = searchViewModel.photosRadioIndex,
        itemPois = searchViewModel.itemPois,
        onFieldValueChange = onFieldValueChange,
        onDropdownMenuValueChange = onDropdownMenuValueChange,
        onPoiClick = onPoiClick,
        onSalesRadioButtonClick = onSalesRadioButtonClick,
        onPhotosRadioButtonClick = onPhotosRadioButtonClick,
        onClearButtonClick = onClearButtonClick,
        onClearAllButtonClick = onClearAllButtonClick,
        popBackStack = popBackStack,
    )

}