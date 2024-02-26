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
    // For detail screen only
    propertyId: Long?,
    context: Context,
    agents: MutableList<Agent>,
    stringAgents: MutableList<String>,
    addresses: MutableList<Address>,
    photos: MutableList<Photo>,
    pois: MutableList<Poi>,
    propertyPoiJoins: MutableList<PropertyPoiJoin>
) {
    /* For list screen only */
    val itemType: (String) -> String = { typeId ->
        listAndDetailViewModel.itemType(typeId, types, stringTypes)
    }
    val onPropertyClick: (Long) -> Unit = { propId ->
        navController.navigateToDetail(propertyId = propId.toString(), contentType = contentType)
    }
    val onFabClick = {
        navController.navigateToEditDetail(propertyId = NULL_PROPERTY_ID.toString())
    }


    /* For both list and detail screens */
    val property = propertyId?.let {
        if (it != NULL_PROPERTY_ID && properties.isNotEmpty()) listAndDetailViewModel.propertyFromId(
            propertyId = it,
            properties = properties
        )else null
    }


    /* For detail screen only */
    val itemPhotos =
        propertyId?.let { listAndDetailViewModel.itemPhotos(propertyId = it, photos = photos)
        } ?: mutableListOf(NO_PHOTO)
    val itemPois = propertyId?.let {
        listAndDetailViewModel.itemPois(propertyId = it, propertyPoiJoins = propertyPoiJoins, pois = pois)
    } ?: mutableListOf()
    val stringType = property?.let {
        listAndDetailViewModel.stringType(typeId = property.typeId, types = types, stringTypes = stringTypes)
    } ?: ""
    val stringAgent = property?.let {
        listAndDetailViewModel.stringAgent(agentId = property.agentId, agents = agents, stringAgents = stringAgents)
    } ?: ""
    val stringAddress = property?.addressId?.let { addressId ->
        listAndDetailViewModel.stringAddress(addressId = addressId, addresses = addresses)
    } ?: ""
    fun stringLatLngItem(latLngItem: String): String {
        return property?.addressId?.let { addressId ->
            listAndDetailViewModel.stringLatLngItem(
                addressId = addressId,
                addresses = addresses,
                latLngItem = latLngItem,
                context = context
            )
        } ?: ""
    }
    val thumbnailUrl = property?.addressId?.let { addressId ->
        if (addresses.isNotEmpty()) listAndDetailViewModel.thumbnailUrl(addressId = addressId, addresses = addresses) else ""
    } ?: ""
    val connection by connectivityState()
    val internetAvailable = listAndDetailViewModel.checkForConnection(connection = connection)
    val networkAvailable by remember(internetAvailable) { mutableStateOf(internetAvailable) }
    val onBackPressed: () -> Unit = { navController.popBackStack() }

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
        onBackPressed = onBackPressed,
    )
}