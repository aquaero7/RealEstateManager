package com.aquaero.realestatemanager.ui.composable

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.DEFAULT_START_POSITION_DP
import com.aquaero.realestatemanager.SEARCH_RESULT_START_POSITION_DP
import com.aquaero.realestatemanager.SearchCriteria
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.navigateSingleTopTo
import com.aquaero.realestatemanager.ui.screen.SearchScreen
import com.aquaero.realestatemanager.utils.convertDpToPxInt
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
    val density = LocalDensity.current
    val defaultStartPositionPxInt by remember {
        mutableIntStateOf(convertDpToPxInt(dpValue = DEFAULT_START_POSITION_DP, density = density))
    }
    val searchResultsStartPositionPxInt by remember {
        mutableIntStateOf(convertDpToPxInt(dpValue = SEARCH_RESULT_START_POSITION_DP, density = density))
    }
    val descriptionValue by remember { mutableStateOf(searchViewModel.getDescription()) }
    val zipValue by remember { mutableStateOf(searchViewModel.getZip()) }
    val cityValue by remember { mutableStateOf(searchViewModel.getCity()) }
    val stateValue by remember { mutableStateOf(searchViewModel.getState()) }
    val countryValue by remember { mutableStateOf(searchViewModel.getCountry()) }
    val priceMinValue by remember { mutableStateOf(searchViewModel.getPriceMin()) }
    val priceMaxValue by remember { mutableStateOf(searchViewModel.getPriceMax()) }
    val surfaceMinValue by remember { mutableStateOf(searchViewModel.getSurfaceMin()) }
    val surfaceMaxValue by remember { mutableStateOf(searchViewModel.getSurfaceMax()) }
    val roomsMinValue by remember { mutableStateOf(searchViewModel.getRoomsMin()) }
    val roomsMaxValue by remember { mutableStateOf(searchViewModel.getRoomsMax()) }
    val bathroomsMinValue by remember { mutableStateOf(searchViewModel.getBathroomsMin()) }
    val bathroomsMaxValue by remember { mutableStateOf(searchViewModel.getBathroomsMax()) }
    val bedroomsMinValue by remember { mutableStateOf(searchViewModel.getBedroomsMin()) }
    val bedroomsMaxValue by remember { mutableStateOf(searchViewModel.getBedroomsMax()) }
    val registrationDateMinValue by remember { mutableStateOf(searchViewModel.getRegistrationDateMin()) }
    val registrationDateMaxValue by remember { mutableStateOf(searchViewModel.getRegistrationDateMax()) }
    val saleDateMinValue by remember { mutableStateOf(searchViewModel.getSaleDateMin()) }
    val saleDateMaxValue by remember { mutableStateOf(searchViewModel.getSaleDateMax()) }
    val typeValue by remember { mutableStateOf(searchViewModel.getType() ?: "") }
    val agentValue by remember { mutableStateOf(searchViewModel.getAgent() ?: "") }
    val salesRadioIndex by remember { mutableIntStateOf(searchViewModel.getSalesRadioIndex()) }
    val photosRadioIndex by remember { mutableIntStateOf(searchViewModel.getPhotosRadioIndex()) }
    val itemPois by remember { mutableStateOf(searchViewModel.getItemPois().toList()) }

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
        { searchViewModel.getItemType(typeId = it, types = types, stringTypes = stringTypes) }

    val searchResults: MutableList<Property> by searchViewModel.searchResultsFlow.collectAsState(initial = mutableListOf())
    val scrollToResultsCounter: Int by searchViewModel.scrollToResultsFlow.collectAsState(initial = 0)


    SearchScreen(
        stringTypes = stringTypes,
        stringAgents = stringAgents,
        currency = currency,
        searchResults = searchResults,
        defaultStartPositionPxInt = defaultStartPositionPxInt,
        searchResultsStartPositionPxInt = searchResultsStartPositionPxInt,
        scrollToResultsCounter = scrollToResultsCounter,
        addresses = addresses,
        photos = photos,
        itemType = itemType,
        descriptionValue = descriptionValue,
//        descriptionValue = searchViewModel.description,
        zipValue = zipValue,
//        zipValue = searchViewModel.zip,
        cityValue = cityValue,
//        cityValue = searchViewModel.city,
        stateValue = stateValue,
//        stateValue = searchViewModel.state,
        countryValue = countryValue,
//        countryValue = searchViewModel.country,
        priceMinValue = priceMinValue,
//        priceMinValue = searchViewModel.priceMin,
        priceMaxValue = priceMaxValue,
//        priceMaxValue = searchViewModel.priceMax,
        surfaceMinValue = surfaceMinValue,
//        surfaceMinValue = searchViewModel.surfaceMin,
        surfaceMaxValue = surfaceMaxValue,
//        surfaceMaxValue = searchViewModel.surfaceMax,
        roomsMinValue = roomsMinValue,
//        roomsMinValue = searchViewModel.roomsMin,
        roomsMaxValue = roomsMaxValue,
//        roomsMaxValue = searchViewModel.roomsMax,
        bathroomsMinValue = bathroomsMinValue,
//        bathroomsMinValue = searchViewModel.bathroomsMin,
        bathroomsMaxValue = bathroomsMaxValue,
//        bathroomsMaxValue = searchViewModel.bathroomsMax,
        bedroomsMinValue = bedroomsMinValue,
//        bedroomsMinValue = searchViewModel.bedroomsMin,
        bedroomsMaxValue = bedroomsMaxValue,
//        bedroomsMaxValue = searchViewModel.bedroomsMax,
        registrationDateMinValue = registrationDateMinValue,
//        registrationDateMinValue = searchViewModel.registrationDateMin,
        registrationDateMaxValue = registrationDateMaxValue,
//        registrationDateMaxValue = searchViewModel.registrationDateMax,
        saleDateMinValue = saleDateMinValue,
//        saleDateMinValue = searchViewModel.saleDateMin,
        saleDateMaxValue = saleDateMaxValue,
//        saleDateMaxValue = searchViewModel.saleDateMax,
        typeValue = typeValue,
//        typeValue = searchViewModel.type ?: "",
        agentValue = agentValue,
//        agentValue = searchViewModel.agent ?: "",
        salesRadioIndex = salesRadioIndex,
//        salesRadioIndex = searchViewModel.salesRadioIndex,
        photosRadioIndex = photosRadioIndex,
//        photosRadioIndex = searchViewModel.photosRadioIndex,
        itemPois = itemPois.toMutableList(),
//        itemPois = searchViewModel.itemPois,
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