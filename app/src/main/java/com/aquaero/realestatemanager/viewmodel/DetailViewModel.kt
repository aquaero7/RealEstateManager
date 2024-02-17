package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.Detail
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.navigateToDetailEdit
import com.aquaero.realestatemanager.repository.AddressRepository
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.PoiRepository
import com.aquaero.realestatemanager.repository.PropertyPoiJoinRepository
import com.aquaero.realestatemanager.repository.PropertyRepository
import com.aquaero.realestatemanager.repository.TypeRepository
import com.aquaero.realestatemanager.utils.ConnectionState

class DetailViewModel(
    private val propertyRepository: PropertyRepository,
    private val addressRepository: AddressRepository,
    private val photoRepository: PhotoRepository,
    private val agentRepository : AgentRepository,
    private val typeRepository : TypeRepository,
    private val poiRepository: PoiRepository,
    private val propertyPoiJoinRepository: PropertyPoiJoinRepository,
): ViewModel() {
    private val context: Context by lazy { ApplicationRoot.getContext() }


    /** Room */

    /***/


    fun onClickMenu(
        navController: NavHostController,
        propertyId: Comparable<*>
    ) {
        Log.w("Click on menu edit", "Screen ${Detail.label} / Property $propertyId")
        navController.navigateToDetailEdit(propertyId = propertyId.toString())
    }

    fun checkForConnection(connection: ConnectionState): Boolean {
        return connection === ConnectionState.Available
    }

    fun propertyFromId(propertyId: Long, properties: MutableList<Property>): Property? {
        return propertyRepository.propertyFromId(propertyId = propertyId, properties = properties)
    }

    private fun poiFromId(poiId: String, pois: MutableList<Poi>): Poi? {
        return poiRepository.poiFromId(poiId = poiId, pois = pois)
    }

    fun thumbnailUrl(addressId: Long, addresses: MutableList<Address>): String {
        return addressRepository.thumbnailUrlFromAddressId(addressId = addressId, addresses = addresses)
    }

    fun stringType(typeId: String, types: MutableList<Type>, stringTypes: MutableList<String>): String {
        return typeRepository.stringType(typeId = typeId, types = types, stringTypes = stringTypes)
    }

    fun stringAgent(agentId: Long, agents: MutableList<Agent>, stringAgents: MutableList<String>): String {
        return agentRepository.stringAgent(agentId = agentId, agents = agents, stringAgents = stringAgents)
    }

    fun stringAddress(addressId: Long, addresses: MutableList<Address>): String {
        return addressRepository.stringAddress(addressId = addressId, addresses = addresses)
    }

    fun stringLatitude(addressId: Long, addresses: MutableList<Address>): String {
        val result = addressRepository.stringLatitude(addressId = addressId, addresses = addresses)
        return result.ifEmpty { context.getString(R.string.unavailable) }
    }

    fun stringLongitude(addressId: Long, addresses: MutableList<Address>): String {
        val result = addressRepository.stringLongitude(addressId = addressId, addresses = addresses)
        return result.ifEmpty { context.getString(R.string.unavailable) }
    }

    fun itemPhotos(propertyId: Long, photos: MutableList<Photo>): MutableList<Photo> {
        return photoRepository.itemPhotos(propertyId = propertyId, photos = photos)
    }

    fun itemPois(propertyId: Long, propertyPoiJoins: MutableList<PropertyPoiJoin>, pois: MutableList<Poi>): MutableList<Poi> {
        return mutableListOf<Poi>().apply {
            if (pois.isNotEmpty()) {
                propertyPoiJoins
                    .filter { join -> join.propertyId == propertyId }
                    .mapTo(this) { join -> poiFromId(poiId = join.poiId, pois = pois)!! }
            }
        }
    }

}

