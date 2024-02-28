package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.AppContentType
import com.aquaero.realestatemanager.LatLngItem
import com.aquaero.realestatemanager.NULL_PROPERTY_ID
import com.aquaero.realestatemanager.R
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
import com.aquaero.realestatemanager.repository.AddressRepository
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.LocationRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.PoiRepository
import com.aquaero.realestatemanager.repository.PropertyRepository
import com.aquaero.realestatemanager.repository.TypeRepository
import com.aquaero.realestatemanager.utils.ConnectionState

class ListAndDetailViewModel(
    private val propertyRepository: PropertyRepository,
    private val addressRepository: AddressRepository,
    private val photoRepository: PhotoRepository,
    private val agentRepository: AgentRepository,
    private val typeRepository : TypeRepository,
    private val poiRepository: PoiRepository,
    private val locationRepository: LocationRepository,
) : ViewModel() {

    fun onClickMenu(
        navController: NavHostController,
        propertyId: Comparable<*>
    ) {
        navController.navigateToEditDetail(propertyId = propertyId.toString())
    }

    fun onPropertyClick(navController: NavHostController, propertyId: Long, contentType: AppContentType) =
        navController.navigateToDetail(propertyId = propertyId.toString(), contentType = contentType)

    fun onFabClick(navController: NavHostController) =
        navController.navigateToEditDetail(propertyId = NULL_PROPERTY_ID.toString())

    fun checkForConnection(connection: ConnectionState): Boolean {
        return locationRepository.checkForConnection(connection)
    }

    private fun propertyFromId(propertyId: Long, properties: MutableList<Property>): Property? {
        return propertyRepository.propertyFromId(propertyId = propertyId, properties = properties)
    }

    private fun property(propertyId: Long?, properties: MutableList<Property>) =
        propertyId?.let {
            if (it != NULL_PROPERTY_ID && properties.isNotEmpty()) propertyFromId(
                propertyId = it,
                properties = properties
            ) else null
        }

    private fun <T> dataFromNullableLongId(longId: Long?, function: T, default: T): T {
        return longId?.let { function } ?: default
    }

    @Suppress("SameParameterValue")
    private fun <T> dataFromNullableProperty(property: Property?, function: T, default: T): T {
        return property?.let { function } ?: default
    }

    fun itemData(
        propertyId: Long?,
        properties: MutableList<Property>,
        photos: MutableList<Photo>,
        propertyPoiJoins: MutableList<PropertyPoiJoin>,
        pois: MutableList<Poi>,
        types: MutableList<Type>,
        stringTypes: MutableList<String>,
        agents: MutableList<Agent>,
        stringAgents: MutableList<String>,
        addresses: MutableList<Address>,
    ): Triple<Triple<Property?, MutableList<Photo>, MutableList<Poi>>, Pair<String, String>, Pair<String, String>> {
        val itemPhotos = dataFromNullableLongId(propertyId, propertyId?.let { itemPhotos(it, photos) } ?: mutableListOf(NO_PHOTO), mutableListOf(NO_PHOTO))
        val itemPois = dataFromNullableLongId(propertyId, propertyId?.let { itemPois(it, propertyPoiJoins, pois) } ?: mutableListOf(), mutableListOf())
        val property = dataFromNullableLongId(propertyId, property(propertyId, properties), null)
        val stringType = dataFromNullableProperty(property, property?.typeId?.let { stringType(it, types, stringTypes) } ?: "", "")
        val stringAgent = dataFromNullableProperty(property, property?.agentId?.let { stringAgent(it, agents, stringAgents) } ?: "", "")
        val stringAddress = dataFromNullableProperty(property, property?.addressId?.let { stringAddress(it, addresses) } ?: "", "")
        val thumbnailUrl = dataFromNullableProperty(property, property?.addressId?.let { thumbnailUrl(it, addresses) } ?: "", "")

        return Triple(Triple(property, itemPhotos, itemPois), Pair(stringType, stringAgent), Pair(stringAddress, thumbnailUrl))
    }

    private fun poiFromId(poiId: String, pois: MutableList<Poi>): Poi? {
        return poiRepository.poiFromId(poiId = poiId, pois =pois)
    }

    private fun thumbnailUrl(addressId: Long, addresses: MutableList<Address>): String {
        return addressRepository.thumbnailUrlFromAddressId(addressId = addressId, addresses = addresses)
    }

    private fun stringType(typeId: String, types: MutableList<Type>, stringTypes: MutableList<String>): String {
        return typeRepository.stringType(typeId = typeId, types = types, stringTypes = stringTypes)
    }

    private fun stringAgent(agentId: Long, agents: MutableList<Agent>, stringAgents: MutableList<String>): String {
        return agentRepository.stringAgent(agentId = agentId, agents = agents, stringAgents = stringAgents)
    }

    private fun stringAddress(addressId: Long, addresses: MutableList<Address>): String {
        return addressRepository.stringAddress(addressId = addressId, addresses = addresses)
    }

    fun stringLatLngItem(property: Property?, addresses: MutableList<Address>, latLngItem: String, context: Context): String  {
        val result = property?.addressId?.let { addressId ->
        addressRepository.stringLatLngItem(
            addressId = addressId, addresses = addresses, latLngItem = latLngItem
        ) } ?: ""
        return result.ifEmpty { context.getString(R.string.unavailable) }
    }

    private fun itemPhotos(propertyId: Long, photos: MutableList<Photo>): MutableList<Photo> {
        return photoRepository.itemPhotos(propertyId = propertyId, photos = photos)
    }

    private fun itemPois(propertyId: Long, propertyPoiJoins: MutableList<PropertyPoiJoin>, pois: MutableList<Poi>): MutableList<Poi> {
        return mutableListOf<Poi>().apply {
            if (pois.isNotEmpty()) {
                propertyPoiJoins
                    .filter { join -> join.propertyId == propertyId }
                    .mapTo(this) { join -> poiFromId(poiId = join.poiId, pois = pois)!! }
            }
        }
    }

    fun itemType(typeId: String, types: MutableList<Type>, stringTypes: MutableList<String>): String {
        val type = types.find { it.typeId == typeId }
        return type?.let { if (stringTypes.isNotEmpty()) stringTypes.elementAt(types.indexOf(it)) else "" } ?: ""
    }

}