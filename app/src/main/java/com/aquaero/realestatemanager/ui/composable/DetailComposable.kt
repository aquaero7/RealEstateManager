package com.aquaero.realestatemanager.ui.composable

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.LatLngItem
import com.aquaero.realestatemanager.NEW_ITEM_ID
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.CACHE_PROPERTY
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.ui.screen.DetailScreen
import com.aquaero.realestatemanager.utils.connectivityState
import com.aquaero.realestatemanager.viewmodel.DetailViewModel

@Composable
fun DetailComposable(
    propertyId: Long,
    context: Context,
    navController: NavHostController,
    detailViewModel: DetailViewModel,
    currency: String,
    properties: MutableList<Property>,
    types: MutableList<Type>,
    stringTypes: MutableList<String>,
    agents: MutableList<Agent>,
    stringAgents: MutableList<String>,
    addresses: MutableList<Address>,
    photos: MutableList<Photo>,
    pois: MutableList<Poi>,
    propertyPoiJoins: MutableList<PropertyPoiJoin>
) {
    val property = if (propertyId != NEW_ITEM_ID && properties.isNotEmpty()) {
        detailViewModel.propertyFromId(propertyId = propertyId, properties = properties) ?: properties[0]
    } else if (properties.isNotEmpty()) {
        properties[0]
    } else {
        CACHE_PROPERTY
    }
    val itemPhotos = detailViewModel.itemPhotos(propertyId = propertyId, photos = photos)
    val itemPois = detailViewModel.itemPois(
        propertyId = propertyId, propertyPoiJoins = propertyPoiJoins, pois = pois,
    )
    val stringType = detailViewModel.stringType(
        typeId = property.typeId, types = types, stringTypes = stringTypes,
    )
    val stringAgent = detailViewModel.stringAgent(
        agentId = property.agentId, agents = agents, stringAgents = stringAgents,
    )
    val stringAddress = property.addressId?.let {
        detailViewModel.stringAddress(addressId = it, addresses = addresses)
    } ?: ""
    fun stringLatLngItem(latLngItem: String): String {
        return property.addressId?.let {
            detailViewModel.stringLatLngItem(
                addressId = it,
                addresses = addresses,
                latLngItem = latLngItem,
                context = context
            )
        } ?: ""
    }
    val thumbnailUrl = property.addressId?.let {
        if (addresses.isNotEmpty()) detailViewModel.thumbnailUrl(addressId = it, addresses = addresses) else ""
    } ?: ""
    val connection by connectivityState()
    val internetAvailable = detailViewModel.checkForConnection(connection = connection)
    val networkAvailable by remember(internetAvailable) { mutableStateOf(internetAvailable) }
    val onBackPressed: () -> Unit = { navController.popBackStack() }

    DetailScreen(
        property = property,
        currency = currency,
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