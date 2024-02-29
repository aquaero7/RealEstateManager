package com.aquaero.realestatemanager.ui.composable

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.AppContentType
import com.aquaero.realestatemanager.LatLngItem
import com.aquaero.realestatemanager.NULL_PROPERTY_ID
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.NO_PHOTO
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.navigateToDetail
import com.aquaero.realestatemanager.navigateToEditDetail
import com.aquaero.realestatemanager.ui.screen.ListAndDetailScreen
import com.aquaero.realestatemanager.utils.connectivityState
import com.aquaero.realestatemanager.viewmodel.ListAndDetailViewModel

@Composable
fun ListAndDetailComposable(
    propertySelected: Boolean,
    // For list screen only
    contentType: AppContentType,
    // For both list and detail screens
    navController: NavHostController,
    listAndDetailViewModel: ListAndDetailViewModel,
    currency: String,
    properties: MutableList<Property>,
    types: MutableList<Type>,
    stringTypes: MutableList<String>,
    closeApp: () -> Unit,
    // For detail screen only
    propertyId: Long?,
    context: Context,
    agents: MutableList<Agent>,
    stringAgents: MutableList<String>,
    addresses: MutableList<Address>,
    photos: MutableList<Photo>,
    pois: MutableList<Poi>,
    propertyPoiJoins: MutableList<PropertyPoiJoin>,
    popBackStack: () -> Unit,
) {
    /*
     * In 'list and detail' mode, displays the details of the first property in the list if
     * the latter is not empty and no other property is selected (for example when launching the app).
     */
    // This lambda is also for the use of list screen
    val onPropertyClick: (Long) -> Unit =
        { listAndDetailViewModel.onPropertyClick(navController, it, contentType) }
    if (propertyId == null && contentType == AppContentType.LIST_AND_DETAIL && properties.isNotEmpty()) {
        onPropertyClick(properties[0].propertyId)
    }


    /* For list screen only */
    val itemType: (String) -> String =
        { listAndDetailViewModel.itemType(typeId = it, types = types, stringTypes = stringTypes) }
    val onFabClick = { listAndDetailViewModel.onFabClick(navController = navController) }


    /* For detail screen only, excepted 'property' for both list and detail screens */
    val (itemData1, itemData2, itemData3) =
        listAndDetailViewModel.itemData(
            propertyId = propertyId,
            properties = properties,
            photos = photos,
            propertyPoiJoins = propertyPoiJoins,
            pois = pois,
            types = types,
            stringTypes = stringTypes,
            agents = agents,
            stringAgents = stringAgents,
            addresses = addresses
        )
    val (property, itemPhotos, itemPois) = itemData1
    val (stringType, stringAgent) = itemData2
    val (stringAddress, thumbnailUrl) = itemData3
    fun stringLatLngItem(latLngItem: String): String = listAndDetailViewModel.stringLatLngItem(
        property = property, addresses = addresses, latLngItem = latLngItem, context = context
    )
    val connection by connectivityState()
    val internetAvailable = listAndDetailViewModel.checkForConnection(connection = connection)
    val networkAvailable by remember(internetAvailable) { mutableStateOf(internetAvailable) }

    ListAndDetailScreen(
        propertySelected = propertySelected,
        // For list screen only
        contentType = contentType,
        items = properties,
        addresses = addresses,
        photos = photos,
        itemType = itemType,
        onPropertyClick = onPropertyClick,
        onFabClick = onFabClick,
        // For both list and detail screens
        property = property,
        currency = currency,
        closeApp = closeApp,
        // For detail screen only
        itemPhotos = itemPhotos,
        itemPois = itemPois,
        stringType = stringType,
        stringAgent = stringAgent,
        stringAddress = stringAddress,
        stringLatitude = stringLatLngItem(LatLngItem.LATITUDE.name),
        stringLongitude = stringLatLngItem(LatLngItem.LONGITUDE.name),
        thumbnailUrl = thumbnailUrl,
        networkAvailable = networkAvailable,
        popBackStack = popBackStack,
    )

}