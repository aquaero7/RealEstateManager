package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.CACHE_ADDRESS
import com.aquaero.realestatemanager.model.CACHE_PHOTO
import com.aquaero.realestatemanager.model.CACHE_PROPERTY
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.repository.AddressRepository
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.LocationRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.PoiRepository
import com.aquaero.realestatemanager.repository.PropertyPoiJoinRepository
import com.aquaero.realestatemanager.repository.PropertyRepository
import com.aquaero.realestatemanager.repository.TypeRepository
import com.aquaero.realestatemanager.utils.ConnectionState
import com.aquaero.realestatemanager.utils.convertEuroToDollar
import com.aquaero.realestatemanager.utils.randomProvisionalId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class EditViewModel(
    private val propertyRepository: PropertyRepository,
    private val addressRepository: AddressRepository,
    private val photoRepository: PhotoRepository,
    private val agentRepository : AgentRepository,
    private val typeRepository: TypeRepository,
    private val poiRepository: PoiRepository,
    private val propertyPoiJoinRepository: PropertyPoiJoinRepository,
    private val locationRepository: LocationRepository,
) : ViewModel() {
    private val unassignedResId = R.string._unassigned_
    private var unassigned = ""
    private var isNewProperty = false
    private var newPropertyIdFromRoom by Delegates.notNull<Long>()
    private var newAddressIdFromRoom by Delegates.notNull<Long>()
    private var isInternetAvailable = false


    /* Cache data */
    private var cacheProperty: Property = CACHE_PROPERTY.copy()
    private var initialAddress: Address? = null
    private var cacheAddress: Address = CACHE_ADDRESS.copy()
    private var cacheStringType: String = unassigned
    private var cacheStringAgent: String = unassigned
    private var initialItemPois: MutableList<Poi> = mutableListOf()
    private var cacheItemPois: MutableList<Poi> = mutableListOf()

    private var initialItemPhotos: MutableList<Photo> = mutableListOf()
    private var _cacheItemPhotos: MutableList<Photo> = mutableListOf()
    private val _cacheItemPhotosFlow: MutableStateFlow<MutableList<Photo>> = MutableStateFlow(_cacheItemPhotos)
    val cacheItemPhotosFlow: Flow<MutableList<Photo>> = _cacheItemPhotosFlow
    /**/

    init {
        // Init of _cacheItemPhotos at this place is needed to display the first photo added
        _cacheItemPhotos = mutableListOf()
        // _cacheItemPhotosFlow.value = _cacheItemPhotos
    }


    fun connexionStatus(connection: ConnectionState) {
        isInternetAvailable = locationRepository.checkForConnection(connection)
    }

    fun onClickMenu(navController: NavHostController, context: Context) { updateRoomWithCacheData(navController, context) }

    fun propertyFromId(propertyId: Long, properties: MutableList<Property>): Property? {
        return propertyRepository.propertyFromId(propertyId = propertyId, properties = properties)
    }

    private fun poiFromId(poiId: String, pois: MutableList<Poi>): Poi? {
        return poiRepository.poiFromId(poiId = poiId, pois = pois)
    }

    fun stringType(typeId: String, types: MutableList<Type>, stringTypes: MutableList<String>): String {
        return typeRepository.stringType(typeId = typeId, types = types, stringTypes = stringTypes)
    }

    fun stringAgent(agentId: Long, agents: MutableList<Agent>, stringAgents: MutableList<String>): String {
        return agentRepository.stringAgent(agentId = agentId, agents = agents, stringAgents = stringAgents)
    }

    fun address(propertyId: Long?, addresses: MutableList<Address>): Address? {
        return addressRepository.address(propertyId, addresses)
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

    fun getPhotoUri(context: Context): Uri { return photoRepository.getPhotoUri(context) }

    fun onShootPhotoMenuItemClick(
        context: Context,
        uri: Uri,
        cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
        permissionLauncher: ManagedActivityResultLauncher<String, Boolean>
    ) {
        photoRepository.onShootPhotoMenuItemClick(
            context = context, uri = uri, cameraLauncher = cameraLauncher, permissionLauncher = permissionLauncher
        )
    }

    fun onSelectPhotoMenuItemClick(
        pickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
    ) {
        photoRepository.onSelectPhotoMenuItemClick(pickerLauncher = pickerLauncher)
    }

    private fun toastMessage(context: Context, msgResId1: Int, msgResId2: Int? = null) {
        Toast.makeText(
            context,
            context.getString(msgResId1) + (msgResId2?.let { "\n${context.getString(it)}" } ?: ""),
            Toast.LENGTH_SHORT).show()
    }


    /* Cache management */

    fun initCache(
        context: Context,
        property: Property?,
        stringType: String?,
        stringAgent: String?,
        address: Address?,
        itemPhotos: MutableList<Photo>,
        itemPois: MutableList<Poi>
    ) {
        unassigned = context.getString(unassignedResId)

        cacheProperty = property?.copy() ?: CACHE_PROPERTY.copy()
        initialAddress = address
        cacheAddress = address?.copy() ?: CACHE_ADDRESS.copy()
        cacheStringType = stringType ?: unassigned
        cacheStringAgent = stringAgent ?: unassigned
        initialItemPois = itemPois.toMutableList()
        cacheItemPois = itemPois.toMutableList()

        initialItemPhotos = itemPhotos
        _cacheItemPhotos = itemPhotos.toMutableList()
        _cacheItemPhotosFlow.value = _cacheItemPhotos
    }

    fun onDescriptionValueChange(value: String) {
        cacheProperty.description = value
    }

    fun onPriceValueChange(value: String, currency: String) {
        cacheProperty.price = if (value.isNotEmpty() && value.isDigitsOnly()) {
            when (currency) {
                "€" -> convertEuroToDollar(euros = value.toInt())
                else -> value.toInt()
            }!!
        } else null
    }

    fun onSurfaceValueChange(value: String) {
        cacheProperty.surface = if (value.isNotEmpty()) value.toInt() else null
    }

    fun onNbOfRoomsValueChange(value: String) {
        cacheProperty.nbOfRooms = if (value.isNotEmpty()) value.toInt() else null
    }

    fun onNbOfBathroomsValueChange(value: String) {
        cacheProperty.nbOfBathrooms = if (value.isNotEmpty()) value.toInt() else null
    }

    fun onNbOfBedroomsValueChange(value: String) {
        cacheProperty.nbOfBedrooms = if (value.isNotEmpty()) value.toInt() else null
    }

    fun onDropdownMenuValueChange(
        value: String,
        types: MutableList<Type>,
        agents: MutableList<Agent>,
    ) {
        val category = value.substringBefore(delimiter = "#", missingDelimiterValue = "")
        val index = value.substringAfter(delimiter = "#", missingDelimiterValue = value).toInt()
        when (category) {
            DropdownMenuCategory.TYPE.name -> onTypeValueChange(index = index, types = types)
            DropdownMenuCategory.AGENT.name -> onAgentValueChange(index = index, agents = agents)
        }
    }

    private fun onTypeValueChange(index: Int, types: MutableList<Type>) {
        cacheProperty.typeId = types.elementAt(index).typeId
    }

    private fun onAgentValueChange(index: Int, agents: MutableList<Agent>) {
        cacheProperty.agentId = agents.elementAt(index).agentId
    }

    fun onStreetNumberValueChange(value: String) {
        cacheAddress.streetNumber = value
    }

    fun onStreetNameValueChange(value: String) {
        cacheAddress.streetName = value
    }

    fun onAddInfoValueChange(value: String) {
        cacheAddress.addInfo = value
    }

    fun onCityValueChange(value: String) {
        cacheAddress.city = value
    }

    fun onStateValueChange(value: String) {
        cacheAddress.state = value
    }

    fun onZipCodeValueChange(value: String) {
        cacheAddress.zipCode = value
    }

    fun onCountryValueChange(value: String) {
        cacheAddress.country = value
    }

    fun onRegistrationDateValueChange(value: String) {
        cacheProperty.registrationDate = value
    }

    fun onSaleDateValueChange(value: String) {
        cacheProperty.saleDate = value
    }

    fun onPoiClick(poiItem: String, isSelected: Boolean) {
        if (isSelected) cacheItemPois.add(Poi(poiItem)) else cacheItemPois.remove(Poi(poiItem))
    }

    fun onSavePhotoButtonClick(uri: Uri, label: String?) {
        // Check if the photo already exists
        val photo: Photo? = _cacheItemPhotos.find { it.uri == uri.toString() }
        val alreadyExists: Boolean = (photo != null)
        if (alreadyExists) {
            // Save the label modification of an existing photo
            val photoToUpdate = Photo(
                photoId = photo!!.photoId,
                uri = photo.uri,
                label = label,
                propertyId = cacheProperty.propertyId)

            val photoIndex = _cacheItemPhotos.indexOf(photo)
            _cacheItemPhotos[photoIndex] = photoToUpdate
            _cacheItemPhotosFlow.value = _cacheItemPhotos
        } else {
            val photoToAdd = Photo(
                photoId = randomProvisionalId(),
                uri = uri.toString(),
                label = label,
                propertyId = cacheProperty.propertyId)

            _cacheItemPhotos.add(photoToAdd)
            _cacheItemPhotosFlow.value = _cacheItemPhotos
        }
    }

    fun onPhotoDeletionConfirmation(propertyId: Long, photoId: Long) {
        val photoToRemove = photoRepository.photoFromId(photoId = photoId, photos = _cacheItemPhotos)
        photoToRemove?.let {
            _cacheItemPhotos.remove(photoToRemove)
            _cacheItemPhotosFlow.value = _cacheItemPhotos
        } ?: Log.w(
            "EditViewModel",
            "Photo not found in cache: Id = $photoId, PropertyId = $propertyId"
        )
    }

    fun clearCache() {
        cacheProperty = CACHE_PROPERTY.copy()
        initialAddress = null
        cacheAddress = CACHE_ADDRESS.copy()
        cacheStringType = unassigned
        cacheStringAgent = unassigned
        initialItemPois = mutableListOf()
        cacheItemPois = mutableListOf()
        initialItemPhotos = mutableListOf()
        _cacheItemPhotos = mutableListOf()
        _cacheItemPhotosFlow.value = _cacheItemPhotos
    }

    /**/


    /* Room */

    private fun updateRoomWithCacheData(navController: NavHostController, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.w("EditViewModel", "Starting latLng update for address...")
                updateAddressWithLatLng(context = context)
                Log.w("EditViewModel", "Room's update jobs are starting...")
                Log.w("EditViewModel", "Starting address update in Room...")
                upDateRoomWithAddress()
                Log.w("EditViewModel", "Starting property update in Room...")
                upDateRoomWithProperty()
                Log.w("EditViewModel", "Starting photos update in Room...")
                upDateRoomWithPhotos()
                Log.w("EditViewModel", "Starting propertyPoiJoins update in Room...")
                upDateRoomWithPropertyPoiJoins()
                Log.w("EditViewModel", "Room's update jobs ended with success !")
                clearCache()
                withContext(Dispatchers.Main) {
                    toastMessage(
                        context = context,
                        msgResId1 = R.string.recorded,
                        msgResId2 = if (isNewProperty) R.string.property_added else null
                    )
                    navController.popBackStack()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    toastMessage(context = context, msgResId1 = R.string.general_error, msgResId2 = R.string.not_recorded)
                }
            }
        }
    }

    private suspend fun updateAddressWithLatLng(context: Context) {
        val hasNullLatLng = cacheAddress.latitude == null || cacheAddress.longitude == null
        val isEmpty = cacheAddress.isNullOrBlank()
        val isModified = (isEmpty == (initialAddress != null)) || initialAddress?.let {
            cacheAddress.hasDifferencesWith(it)
        } == true

        if (isEmpty) {
            cacheAddress.latitude = null
            cacheAddress.longitude = null
        } else if (isModified || hasNullLatLng) {
            try {
                val latLng =
                    locationRepository.getLocationFromAddress(
                        context,
                        cacheAddress.replaceBlankValuesWithNull().toString(),
                        isInternetAvailable,
                    )
                cacheAddress.latitude = latLng?.latitude
                cacheAddress.longitude = latLng?.longitude
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    toastMessage(context = context, msgResId1 = R.string.latlng_error)
                }
            }
        } else {
            Log.w("EditViewModel", "No address change, so updating LatLng is not necessary")
        }
    }

    private suspend fun upDateRoomWithAddress() {
        val isEmpty = cacheAddress.isNullOrBlank()
        val isUpdate = (initialAddress == null) == isEmpty
        when {
            // The address is empty and not known in the database, so we do nothing
            isEmpty && isUpdate -> {}
            // The address is empty and known in the database, so we delete it
            // and we update the address id in cacheProperty
            isEmpty -> {
                addressRepository.deleteAddressFromRoom(cacheAddress)
                cacheProperty.addressId = null
            }
            // The address is not empty, so we update it or create it
            else -> {
                val addressToUpsert = when {
                    // The address is not empty and known in the database, so we will update it.
                    // We only set blank fields to null...
                    isUpdate -> { cacheAddress.replaceBlankValuesWithNull() }
                    // The address is not empty and not known in the database, so we create it.
                    // We set blank fields to null and we remove the default cache addressId...
                    else -> { cacheAddress.replaceBlankValuesWithNull().withoutId() }
                }
                // ... then we update database and get the Room's new address id in case of creation...
                newAddressIdFromRoom = addressRepository.upsertAddressInRoom(addressToUpsert)
                Log.w("EditViewModel", "newAddressIdFromRoom: $newAddressIdFromRoom")
                // ... and, if the address has been created, we set its id in cacheAddress and cacheProperty,
                if (cacheAddress.addressId == CACHE_ADDRESS.addressId && newAddressIdFromRoom > 0) {
                    Log.w("EditViewModel", "The address is new")                                    // TODO: To be deleted
                    cacheAddress.addressId = newAddressIdFromRoom
                    cacheProperty.addressId = newAddressIdFromRoom
                }
            }
        }
    }

    private suspend fun upDateRoomWithProperty() {
        isNewProperty = cacheProperty.propertyId == CACHE_PROPERTY.propertyId
        val propertyToUpsert = if (isNewProperty) {
            // If the property is not known in the database,
            // we set blank fields to null and removes the default cache propertyId
            cacheProperty.replaceBlankValuesWithNull().withoutId()
        } else {
            // Otherwise, we only set blank fields to null
            cacheProperty.replaceBlankValuesWithNull()
        }

        // We update database and get the Room's new property id in case of creation...
        newPropertyIdFromRoom = propertyRepository.upsertPropertyInRoom(propertyToUpsert)
        Log.w("EditViewModel", "isNewProperty: $isNewProperty / newPropertyIdFromRoom: $newPropertyIdFromRoom")

        // If the property has been created, we set its id in cacheProperty and cacheItemPhotos
        if (isNewProperty && newPropertyIdFromRoom > 0) {
            cacheProperty.propertyId = newPropertyIdFromRoom
            _cacheItemPhotos.forEach { it.propertyId = newPropertyIdFromRoom }
        }
    }

    private suspend fun upDateRoomWithPhotos() {
        // Deletes removed photos from Room
        val cachePhotosIds: List<Long> = _cacheItemPhotos.map { it.photoId }
        val photosToDelete = initialItemPhotos.filter { it.photoId !in cachePhotosIds }.toMutableList()
        if (photosToDelete.isNotEmpty()) photoRepository.deletePhotosFromRoom(photosToDelete)

        // Updates photos or creates new photos in Room
        // Before updating Room, we replace any new photo random id created for cache
        // with 0, in order to let Room autogenerate an id.
        val photosToUpsert = _cacheItemPhotos.map {
            if (it.photoId == CACHE_PHOTO.photoId || it.photoId < 0) it.withoutId() else it
        }.toMutableList()
        // Then, we update database
        if (photosToUpsert.isNotEmpty()) photoRepository.upsertPhotosInRoom(photosToUpsert)
    }

    private suspend fun upDateRoomWithPropertyPoiJoins() {
        val initialPoisIds: List<String> = initialItemPois.map { it.poiId }
        val cachePoisIds: List<String> = cacheItemPois.map { it.poiId }

        // Deletes unselected pois from PropertyPoiJoin in Room
        val poisIdsToRemove = initialPoisIds.filter { it !in cachePoisIds }
        if (poisIdsToRemove.isNotEmpty()) {
            val propertyPoiJoinsToDelete =
                poisIdsToRemove.map { PropertyPoiJoin(cacheProperty.propertyId, it) }.toMutableList()
            propertyPoiJoinRepository.deletePropertyPoiJoinsFromRoom(propertyPoiJoinsToDelete)
        }

        // Adds selected pois to PropertyPoiJoin in Room
        val poisIdsToAdd = cachePoisIds.filter { it !in initialPoisIds }
        if (poisIdsToAdd.isNotEmpty()) {
            val propertyPoiJoinsToAdd =
                poisIdsToAdd.map { PropertyPoiJoin(cacheProperty.propertyId, it) }.toMutableList()
            propertyPoiJoinRepository.upsertPropertyPoiJoinsInRoom(propertyPoiJoinsToAdd)
        }
    }

    /**/

}
