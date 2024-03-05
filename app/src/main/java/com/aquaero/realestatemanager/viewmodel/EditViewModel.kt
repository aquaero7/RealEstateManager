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
import com.aquaero.realestatemanager.Field
import com.aquaero.realestatemanager.NULL_PROPERTY_ID
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
import com.aquaero.realestatemanager.utils.generateProvisionalId
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
    var firstCompositionFlag = true // Flag to avoid _cacheItemPhotos erasure after screen rotation


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

    fun onClickMenu(navController: NavHostController, context: Context) {
        updateRoomWithCacheData(navController, context)
    }

    private fun propertyFromId(propertyId: Long, properties: MutableList<Property>): Property? {
        return propertyRepository.propertyFromId(propertyId = propertyId, properties = properties)
    }

    fun property(propertyId: Long, properties: MutableList<Property>) =
        if (propertyId != NULL_PROPERTY_ID && properties.isNotEmpty()) {
            // Edition mode
            propertyFromId(propertyId = propertyId, properties = properties)
        } else {
            // Creation mode
            null
        }

    private fun <T> dataFromNullableProperty(property: Property?, function: T, default: T? = null): T? {
        return property?.let { function } ?: default
    }

    fun itemData(
        property: Property?,
        types: MutableList<Type>,
        stringTypes: MutableList<String>,
        agents: MutableList<Agent>,
        stringAgents: MutableList<String>,
        addresses: MutableList<Address>? = null,    // Made optional for cache usage
    ): Triple<String?, String?, Address?> {
        val stringType = dataFromNullableProperty(
            property = property,
            function = property?.let {
                stringType(typeId = it.typeId, types = types, stringTypes = stringTypes)
            }
        )
        val stringAgent = dataFromNullableProperty(
            property = property,
            function = property?.let {
                stringAgent(agentId = it.agentId, agents = agents, stringAgents = stringAgents)
            }
        )
        val address = addresses?.let {
            dataFromNullableProperty(
                property = property,
                function = address(propertyId = property?.addressId, addresses = addresses)
            )
        }
        return Triple(stringType, stringAgent, address)
    }

    private fun poiFromId(poiId: String, pois: MutableList<Poi>): Poi? {
        return poiRepository.poiFromId(poiId = poiId, pois = pois)
    }

    private fun stringType(typeId: String, types: MutableList<Type>, stringTypes: MutableList<String>): String {
        return typeRepository.stringType(typeId = typeId, types = types, stringTypes = stringTypes)
    }

    private fun stringAgent(agentId: Long, agents: MutableList<Agent>, stringAgents: MutableList<String>): String {
        return agentRepository.stringAgent(agentId = agentId, agents = agents, stringAgents = stringAgents)
    }

    private fun address(propertyId: Long?, addresses: MutableList<Address>): Address? {
        return addressRepository.address(propertyId = propertyId, addresses = addresses)
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

    fun getPhotoUri(context: Context): Uri { return photoRepository.getPhotoUri(context = context) }

    fun onResponseToCamPermRequest(
        isGranted: Boolean,
        cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
        cameraUri: Uri,
        context: Context
    ) {
        if (isGranted) cameraLauncher.launch(cameraUri) else toastMessage(context, R.string.camera_perm_revoked)
    }

    fun onShootPhotoMenuItemClick(
        context: Context,
        uri: Uri,
        cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
        camPermLauncher: ManagedActivityResultLauncher<String, Boolean>
    ) {
        photoRepository.onShootPhotoMenuItemClick(
            context = context, uri = uri, cameraLauncher = cameraLauncher, camPermLauncher = camPermLauncher
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

        firstCompositionFlag = false
    }

    fun cacheData(): Triple<Property, Address, MutableList<Poi>> {
        return Triple(cacheProperty, cacheAddress, cacheItemPois)
    }

    fun onDropdownMenuValueChange(
        value: String,
        types: MutableList<Type>,
        agents: MutableList<Agent>,
    ) {
        val category = value.substringBefore(delimiter = "#", missingDelimiterValue = "")
        val index = value.substringAfter(delimiter = "#", missingDelimiterValue = value).toInt()
        when (category) {
            DropdownMenuCategory.TYPE.name -> cacheProperty.typeId = types.elementAt(index).typeId
            DropdownMenuCategory.AGENT.name -> cacheProperty.agentId = agents.elementAt(index).agentId
        }
    }

    fun onFieldValueChange(field: String, value: String, currency: String) {
        val digitalValue = if (value.isNotEmpty() && value.isDigitsOnly()) value.toInt() else null
        when (field) {
            Field.SURFACE.name -> cacheProperty.surface = digitalValue
            Field.ROOMS.name -> cacheProperty.nbOfRooms = digitalValue
            Field.BATHROOMS.name -> cacheProperty.nbOfBathrooms = digitalValue
            Field.BEDROOMS.name -> cacheProperty.nbOfBedrooms = digitalValue
            Field.DESCRIPTION.name -> cacheProperty.description = value
            Field.REGISTRATION_DATE.name -> cacheProperty.registrationDate = value
            Field.SALE_DATE.name -> cacheProperty.saleDate = value

            Field.STREET_NUMBER.name -> cacheAddress.streetNumber = value
            Field.STREET_NAME.name -> cacheAddress.streetName = value
            Field.ADD_INFO.name -> cacheAddress.addInfo = value
            Field.CITY.name -> cacheAddress.city = value
            Field.STATE.name -> cacheAddress.state = value
            Field.ZIP_CODE.name -> cacheAddress.zipCode = value
            Field.COUNTRY.name -> cacheAddress.country = value

            Field.PRICE.name -> cacheProperty.price =
                if (value.isNotEmpty() && value.isDigitsOnly()) {
                    when (currency) {
                        "€" -> convertEuroToDollar(euros = value.toInt())
                        else -> value.toInt()
                    }!!
                } else null
        }
    }

    fun onPoiClick(poiItem: String, isSelected: Boolean) {
        if (isSelected) cacheItemPois.add(Poi(poiId = poiItem)) else cacheItemPois.remove(Poi(poiId = poiItem))
    }

    fun saveToInternalStorage(context: Context, uri: Uri): Uri {
        return photoRepository.saveToInternalStorage(context = context, uri = uri)
    }

    fun checkUris(capturedImageUri: Uri, pickerUri: Uri, photoToAddUri: Uri): Triple<Uri, Uri, Uri> {
        var updatedCapturedImageUri = capturedImageUri
        var updatedPickerUri = pickerUri
        var updatedPhotoToAddUri = photoToAddUri

        if (updatedCapturedImageUri != Uri.EMPTY) {
            updatedPhotoToAddUri = updatedCapturedImageUri
            updatedCapturedImageUri = Uri.EMPTY
        }
        if (updatedPickerUri != Uri.EMPTY) {
            updatedPhotoToAddUri = updatedPickerUri
            updatedPickerUri = Uri.EMPTY
        }
        return Triple(updatedCapturedImageUri, updatedPickerUri, updatedPhotoToAddUri)
    }

    fun onCancelPhotoEditionButtonClick(): Uri = Uri.EMPTY

    fun onSavePhotoButtonClick(uri: Uri, label: String?): Uri {
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
                photoId = generateProvisionalId(),
                uri = uri.toString(),
                label = label,
                propertyId = cacheProperty.propertyId)
            Log.w("EditViewModel", "Generated provisional photo id: ${photoToAdd.photoId}")
            _cacheItemPhotos.add(photoToAdd)
            _cacheItemPhotosFlow.value = _cacheItemPhotos
        }
        return Uri.EMPTY
    }

    fun onEditPhotoMenuItemClick(photo: Photo): Pair<Uri, Boolean> {
        return Pair(Uri.parse(photo.uri), true)
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

        firstCompositionFlag = true
    }


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
        val isModified = (isEmpty == (initialAddress != null))
                || initialAddress?.let { cacheAddress.hasDifferencesWith(other = it) } == true
        if (isEmpty) {
            cacheAddress.latitude = null
            cacheAddress.longitude = null
        } else if (isModified || hasNullLatLng) {
            // TODO : Condition on android version before using Geocoder
            try {
                val latLng = locationRepository.getLocationFromAddress(
                    context = context,
                    strAddress = cacheAddress.replaceBlankValuesWithNull().toString(),
                    isInternetAvailable = isInternetAvailable,
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
                addressRepository.deleteAddressFromRoom(address = cacheAddress)
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
                newAddressIdFromRoom = addressRepository.upsertAddressInRoom(address = addressToUpsert)
                Log.w("EditViewModel", "newAddressIdFromRoom: $newAddressIdFromRoom")
                // ... and, if the address has been created, we set its id in cacheAddress and cacheProperty,
                if (cacheAddress.addressId == CACHE_ADDRESS.addressId && newAddressIdFromRoom > 0) {
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
        newPropertyIdFromRoom = propertyRepository.upsertPropertyInRoom(property = propertyToUpsert)
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
        if (photosToDelete.isNotEmpty()) photoRepository.deletePhotosFromRoom(photos = photosToDelete)

        // Updates photos or creates new photos in Room
        // Before updating Room, we replace any new photo random id created for cache
        // with 0, in order to let Room autogenerate an id.
        val photosToUpsert = _cacheItemPhotos.map {
            if (it.photoId == CACHE_PHOTO.photoId || it.photoId < 0) it.withoutId() else it
        }.toMutableList()
        // Then, we update database
        if (photosToUpsert.isNotEmpty()) photoRepository.upsertPhotosInRoom(photos = photosToUpsert)
    }

    private suspend fun upDateRoomWithPropertyPoiJoins() {
        val initialPoisIds: List<String> = initialItemPois.map { it.poiId }
        val cachePoisIds: List<String> = cacheItemPois.map { it.poiId }

        // Deletes unselected pois from PropertyPoiJoin in Room
        val poisIdsToRemove = initialPoisIds.filter { it !in cachePoisIds }
        if (poisIdsToRemove.isNotEmpty()) {
            val propertyPoiJoinsToDelete =
                poisIdsToRemove.map { PropertyPoiJoin(propertyId = cacheProperty.propertyId, poiId = it) }.toMutableList()
            propertyPoiJoinRepository.deletePropertyPoiJoinsFromRoom(propertyPoiJoins = propertyPoiJoinsToDelete)
        }

        // Adds selected pois to PropertyPoiJoin in Room
        val poisIdsToAdd = cachePoisIds.filter { it !in initialPoisIds }
        if (poisIdsToAdd.isNotEmpty()) {
            val propertyPoiJoinsToAdd =
                poisIdsToAdd.map { PropertyPoiJoin(propertyId = cacheProperty.propertyId, poiId = it) }.toMutableList()
            propertyPoiJoinRepository.upsertPropertyPoiJoinsInRoom(propertyPoiJoins =  propertyPoiJoinsToAdd)
        }
    }

    /**/

}
