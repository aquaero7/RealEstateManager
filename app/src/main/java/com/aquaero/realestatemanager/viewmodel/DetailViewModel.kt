package com.aquaero.realestatemanager.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.ui.res.stringResource
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
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.model.TypeEnum
import com.aquaero.realestatemanager.navigateToDetailEdit
import com.aquaero.realestatemanager.repository.AddressRepository
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.PoiRepository
import com.aquaero.realestatemanager.repository.PropertyPoiJoinRepository
import com.aquaero.realestatemanager.repository.PropertyRepository
import com.aquaero.realestatemanager.repository.TypeRepository
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
    private val typeRepository : TypeRepository,
    private val poiRepository: PoiRepository,
    private val propertyPoiJoinRepository: PropertyPoiJoinRepository,
): ViewModel() {

    private val context: Context by lazy { ApplicationRoot.getContext() }

    val pTypesSet = propertyRepository.typesSet
    val agentsSet = agentRepository.agentsSet

    /** Room **/
/*

    private val _propertiesStateFlow = MutableStateFlow(mutableListOf<Property>())
    val propertiesStateFlow: StateFlow<MutableList<Property>> = _propertiesStateFlow.asStateFlow()

    private val _addressesStateFlow = MutableStateFlow(mutableListOf<Address>())
    val addressesStateFlow: StateFlow<MutableList<Address>> = _addressesStateFlow.asStateFlow()

    private val _photosStateFlow = MutableStateFlow(mutableListOf<Photo>())
    val photosStateFlow: StateFlow<MutableList<Photo>> = _photosStateFlow.asStateFlow()

    private val _agentsStateFlow = MutableStateFlow(mutableListOf<Agent>())
    val agentsStateFlow: StateFlow<MutableList<Agent>> = _agentsStateFlow.asStateFlow()

    private val _poisStateFlow = MutableStateFlow(mutableListOf<Poi>())
    val poisStateFlow: StateFlow<MutableList<Poi>> = _poisStateFlow.asStateFlow()

    private val _propertyPoiJoinsStateFlow = MutableStateFlow(mutableListOf<PropertyPoiJoin>())
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
*/

    /***/


    fun onClickMenu(
        navController: NavHostController,
        propertyId: Comparable<*>
    ) {
        Log.w("Click on menu edit", "Screen ${Detail.label} / Property $propertyId")
        navController.navigateToDetailEdit(propertyId.toString())
    }

    fun propertyFromId(properties: MutableList<Property>, propertyId: Long): Property? {
        return propertyRepository.propertyFromId(properties, propertyId)
    }

    fun poiFromId(pois: MutableList<Poi>, poiId: String): Poi {
        return poiRepository.poiFromId(pois, poiId)
    }

    fun mutableSetIndex(set: MutableSet<Any>, item: String): Int {
        var index = 0
        for (setItem in set) {
            if (setItem is Int) {
                if (context.getString(setItem) == item) index = set.indexOf(setItem)
            } else {
                if (setItem as String == item) index = set.indexOf(setItem)
            }
        }
        return index
    }

    fun checkForConnection(connection: ConnectionState): Boolean {
        return connection === ConnectionState.Available
    }

    fun thumbnailUrl(addresses: MutableList<Address>, addressId: Long): String {
//        val address = addressRepository.addressFromId(propertyFromId(propertyId).addressId)
//        return propertyRepository.thumbnailUrl(address)
        return addressRepository.thumbnailUrlFromAddressId(addresses, addressId)
    }

    @SuppressLint("DiscouragedApi")
    fun stringType(types: MutableList<Type>, typeId: String): String {
        val type = types.find { it.typeId == typeId }?.typeId ?: TypeEnum.UNASSIGNED.key
        val resourceId = context.resources.getIdentifier(type, "string", context.packageName)
        return if (resourceId != 0) context.getString(resourceId) else type
    }

    fun stringType(types: MutableList<Type>, stringTypes: MutableList<String>, typeId: String): String {
        return typeRepository.stringType(types, stringTypes, typeId)
    }

    fun stringAgent(agents: MutableList<Agent>, agentId: Long): String {
//        val agents = agentsStateFlow.value
        return agentRepository.stringAgent(agents, agentId)
    }

    fun stringAgent(agents: MutableList<Agent>, stringAgents: MutableList<String>, agentId: Long): String {
        return agentRepository.stringAgent(agents, stringAgents, agentId)
    }

    fun stringAddress(addresses: MutableList<Address>, addressId: Long): String {
//        val addresses = addressesStateFlow.value
        return addressRepository.stringAddress(addresses, addressId)
    }

    fun stringLatitude(addresses: MutableList<Address>, addressId: Long): String {
//        val addresses = addressesStateFlow.value
        return addressRepository.stringLatitude(addresses, addressId)
    }

    fun stringLongitude(addresses: MutableList<Address>, addressId: Long): String {
//        val addresses = addressesStateFlow.value
        return addressRepository.stringLongitude(addresses, addressId)
    }

    fun itemPhotos(photos: MutableList<Photo>, propertyId: Long): MutableList<Photo> {
//        val photos = photosStateFlow.value
        return photoRepository.itemPhotos(photos, propertyId)
    }

    /** Option 1 for itemPois **/   // TODO: To be deleted if option 1 works (in AppNavHost)
    fun itemPois(propertyId: Long, onResult: (MutableList<Poi>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val itemPois = propertyPoiJoinRepository.getPoisForPropertyFromRoom(propertyId).first()
                withContext(Dispatchers.Main) {
                    onResult(itemPois)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onResult(mutableListOf())
                }
            }
        }
    }

    /** Option 2 for itemPois **/
    fun itemPois(propertyPoiJoins: MutableList<PropertyPoiJoin>, pois: MutableList<Poi>, propertyId: Long): MutableList<Poi> {
        return mutableListOf<Poi>().apply {
            /*
            for (join in propertyPoiJoins.filter { propertyPoi ->
                propertyPoi.propertyId == propertyId
            }) {
                this.add(poiFromId(pois, join.poiId))
            }
            */
            propertyPoiJoins
                .filter { join -> join.propertyId == propertyId }
                .mapTo(this) { join -> poiFromId(pois, join.poiId) }
        }
    }

}

