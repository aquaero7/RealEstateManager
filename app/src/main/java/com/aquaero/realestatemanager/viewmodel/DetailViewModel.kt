package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.Detail
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.navigateToDetailEdit
import com.aquaero.realestatemanager.repository.AddressRepository
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.PoiRepository
import com.aquaero.realestatemanager.repository.PropertyPoiJoinRepository
import com.aquaero.realestatemanager.repository.PropertyRepository
import com.aquaero.realestatemanager.utils.ConnectionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel(
    private val propertyRepository: PropertyRepository,
    private val addressRepository: AddressRepository,
    private val photoRepository: PhotoRepository,
    private val agentRepository : AgentRepository,
    private val poiRepository: PoiRepository,
    private val propertyPoiJoinRepository: PropertyPoiJoinRepository,
): ViewModel() {

    private val context: Context by lazy { ApplicationRoot.getContext() }


    /** Room **/

    private val _propertiesStateFlow = MutableStateFlow<MutableList<Property>>(emptyList<Property>().toMutableList())
    val propertiesStateFlow: StateFlow<MutableList<Property>> = _propertiesStateFlow.asStateFlow()

    private val _addressesStateFlow = MutableStateFlow<MutableList<Address>>(emptyList<Address>().toMutableList())
    val addressesStateFlow: StateFlow<MutableList<Address>> = _addressesStateFlow.asStateFlow()

    private val _photosStateFlow = MutableStateFlow<MutableList<Photo>>(emptyList<Photo>().toMutableList())
    val photosStateFlow: StateFlow<MutableList<Photo>> = _photosStateFlow.asStateFlow()

    private val _agentsStateFlow = MutableStateFlow<MutableList<Agent>>(emptyList<Agent>().toMutableList())
    val agentsStateFlow: StateFlow<MutableList<Agent>> = _agentsStateFlow.asStateFlow()

    private val _poisStateFlow = MutableStateFlow<MutableList<Poi>>(emptyList<Poi>().toMutableList())
    val poisStateFlow: StateFlow<MutableList<Poi>> = _poisStateFlow.asStateFlow()

    private val _propertyPoiJoinsStateFlow = MutableStateFlow<MutableList<PropertyPoiJoin>>(emptyList<PropertyPoiJoin>().toMutableList())
    val propertyPoiJoinsStateFlow: StateFlow<MutableList<PropertyPoiJoin>> = _propertyPoiJoinsStateFlow.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {

            propertyRepository.getPropertiesFromRoom()
                .collect { listOfProperties -> _propertiesStateFlow.value = listOfProperties }

            addressRepository.getAddressesFromRoom()
                .collect { listOfAddresses -> _addressesStateFlow.value = listOfAddresses }

            photoRepository.getPhotosFromRoom()
                .collect { listOfPhotos -> _photosStateFlow.value = listOfPhotos }

            agentRepository.getAgentsFromRoom()
                .collect { listOfAgents -> _agentsStateFlow.value = listOfAgents }

            poiRepository.getPoisFromRoom()
                .collect { listOfPois -> _poisStateFlow.value = listOfPois }

            propertyPoiJoinRepository.getPropertyPoiJoinsFromRoom()
                .collect { listOfPropertyPoiJoins -> _propertyPoiJoinsStateFlow.value = listOfPropertyPoiJoins }

        }
    }

    /***/


    fun onClickMenu(
        navController: NavHostController,
        propertyId: Comparable<*>
    ) {
        Log.w("Click on menu edit", "Screen ${Detail.label} / Property $propertyId")
        navController.navigateToDetailEdit(propertyId.toString())
    }

    fun propertyFromId(propertyId: Long): Property {
        return propertyRepository.propertyFromId(propertyId)
    }

    fun checkForConnection(connection: ConnectionState): Boolean {
        return connection === ConnectionState.Available
    }

    fun thumbnailUrl(propertyId: Long): String {
        val address = addressRepository.addressFromId(propertyFromId(propertyId).addressId)
        return propertyRepository.thumbnailUrl(address)
    }

    fun stringAgent(agentId: Long): String {
        val agents = agentsStateFlow.value
        return agentRepository.stringAgent(agents, agentId)
    }

    fun stringAddress(addressId: Long): String {
        val addresses = addressesStateFlow.value
        return addressRepository.stringAddress(addresses, addressId)
    }

    fun stringLatitude(addressId: Long): String {
        val addresses = addressesStateFlow.value
        return addressRepository.stringLatitude(addresses, addressId)
    }

    fun stringLongitude(addressId: Long): String {
        val addresses = addressesStateFlow.value
        return addressRepository.stringLongitude(addresses, addressId)
    }

    fun itemPhotos(propertyId: Long): MutableList<Photo> {
        val photos = photosStateFlow.value
        return photoRepository.itemPhotos(photos, propertyId)
    }

    fun itemPois(propertyId: Long, onResult: (MutableList<Poi>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val itemPois = propertyPoiJoinRepository.getPoisForPropertyFromRoom(propertyId).first()
                withContext(Dispatchers.Main) {
                    onResult(itemPois)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onResult(emptyList<Poi>().toMutableList())
                }
            }
        }
    }

}

