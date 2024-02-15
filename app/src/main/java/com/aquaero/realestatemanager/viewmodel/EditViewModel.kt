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
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.EditDetail
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
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.PoiRepository
import com.aquaero.realestatemanager.repository.PropertyPoiJoinRepository
import com.aquaero.realestatemanager.repository.PropertyRepository
import com.aquaero.realestatemanager.repository.TypeRepository
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
) : ViewModel() {
    private val context: Context by lazy { ApplicationRoot.getContext() }
    private var newPropertyIdFromRoom by Delegates.notNull<Long>()
    private var newAddressIdFromRoom by Delegates.notNull<Long>()


    /* Cache data */

    private var alreadyInit = false // Flat to avoid data re-init when taking a picking a picture

    /*lateinit*/ var cacheProperty: Property = CACHE_PROPERTY.copy()
    private var initialAddress: Address? = null
    /*lateinit*/ var cacheAddress: Address = CACHE_ADDRESS.copy()
    /*lateinit*/ var cacheStringType: String = context.getString(R.string._unassigned_)
    /*lateinit*/ var cacheStringAgent: String = context.getString(R.string._unassigned_)

    private var initialItemPhotos: MutableList<Photo>? = null
    private var _cacheItemPhotos: MutableList<Photo> = mutableListOf()
    private val _cacheItemPhotosFlow: MutableStateFlow<MutableList<Photo>> = MutableStateFlow(_cacheItemPhotos)
    val cacheItemPhotosFlow: Flow<MutableList<Photo>> = _cacheItemPhotosFlow

    private /*lateinit*/ var initialItemPois: MutableList<Poi> = mutableListOf()
    /*lateinit*/ var cacheItemPois: MutableList<Poi> = mutableListOf()

    /**/


    init {
        // Init of _cacheItemPhotos at this place is needed to display the first photo added
        _cacheItemPhotos = mutableListOf()
        // _cacheItemPhotosFlow.value = _cacheItemPhotos
    }


    fun onClickMenu(
        navController: NavHostController,
        propertyId: Comparable<*>
    ) {
        Log.w("Click on menu valid", "Screen ${EditDetail.label} / Property $propertyId")

        updateRoomWithCacheData(navController)
//        clearCache()
//        navController.popBackStack()
    }

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

    fun getPhotoUri(): Uri {
        return photoRepository.getPhotoUri()
    }

    fun onShootPhotoMenuItemClick(
        uri: Uri,
        cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
        permissionLauncher: ManagedActivityResultLauncher<String, Boolean>
    ) {
        photoRepository.onShootPhotoMenuItemClick(
            uri = uri, cameraLauncher = cameraLauncher, permissionLauncher = permissionLauncher
        )
    }

    fun onSelectPhotoMenuItemClick(
        pickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
    ) {
        photoRepository.onSelectPhotoMenuItemClick(pickerLauncher = pickerLauncher)
    }


    /* Cache management */

    fun initCache(
        property: Property?,
        stringType: String?,
        stringAgent: String?,
        address: Address?,
        itemPhotos: MutableList<Photo>,
        itemPois: MutableList<Poi>
    ) {
        Log.w("EditViewModel", "Property Id: '${property?.propertyId}' / alreadyInit = $alreadyInit / initialItemPhotos = ${initialItemPhotos?.size}")

//        if (!alreadyInit) {

            cacheProperty = property?.copy() ?: CACHE_PROPERTY.copy()
            initialAddress = address
            cacheAddress = address?.copy() ?: CACHE_ADDRESS.copy()
            cacheStringType =
                stringType ?: context.getString(R.string._unassigned_) // CACHE_TYPE.typeId
            cacheStringAgent =
                stringAgent ?: context.getString(R.string._unassigned_) // CACHE_AGENT.toString()
            initialItemPois = itemPois.toMutableList()  // .ifEmpty { mutableListOf(CACHE_POI) }
            cacheItemPois = itemPois.toMutableList()


//            initialItemPhotos = mutableListOf()
//            if (_cacheItemPhotos.isEmpty() && itemPhotos.isNotEmpty()) {

//            if (initialItemPhotos == null) {

//            initialItemPhotos.addAll(itemPhotos)
//            initialItemPhotos = itemPhotos.toMutableList()
                initialItemPhotos = itemPhotos
//            _cacheItemPhotos.addAll(itemPhotos)
                _cacheItemPhotos = itemPhotos.toMutableList()
                _cacheItemPhotosFlow.value = _cacheItemPhotos

//            }

//            alreadyInit = true
//        }
    }

    fun onDescriptionValueChange(propertyId: Long, value: String) {
        cacheProperty.description = value
        Log.w("EditViewModel", "New value for description of property Id '$propertyId' is: $value")
    }

    fun onPriceValueChange(propertyId: Long, value: String, currency: String) {
        cacheProperty.price = if (value.isNotEmpty() && value.isDigitsOnly()) {
            when (currency) {
                "€" -> convertEuroToDollar(euros = value.toInt())
                else -> value.toInt()
            }!!
        } else null
        Log.w(
            "EditViewModel",
            "New value for price of property Id '$propertyId' is: ${cacheProperty.price} dollars and input is $value $currency"
        )
    }

    fun onSurfaceValueChange(propertyId: Long, value: String) {
        cacheProperty.surface = if (value.isNotEmpty()) value.toInt() else null
        Log.w("EditViewModel", "New value for surface of property Id '$propertyId' is: $value")
    }

    fun onNbOfRoomsValueChange(propertyId: Long, value: String) {
        cacheProperty.nbOfRooms = if (value.isNotEmpty()) value.toInt() else null
        Log.w("EditViewModel", "New value for nb of rooms of property Id '$propertyId' is: $value")
    }

    fun onNbOfBathroomsValueChange(propertyId: Long, value: String) {
        cacheProperty.nbOfBathrooms = if (value.isNotEmpty()) value.toInt() else null
        Log.w("EditViewModel", "New value for nb of bathrooms of property Id '$propertyId' is: $value")
    }

    fun onNbOfBedroomsValueChange(propertyId: Long, value: String) {
        cacheProperty.nbOfBedrooms = if (value.isNotEmpty()) value.toInt() else null
        Log.w("EditViewModel", "New value for nb of bedrooms of property Id '$propertyId' is: $value")
    }

    fun onDropdownMenuValueChange(
        propertyId: Long,
        value: String,
        types: MutableList<Type>,
        agents: MutableList<Agent>,
    ) {
        val category = value.substringBefore(delimiter = "#", missingDelimiterValue = "")
        val index = value.substringAfter(delimiter = "#", missingDelimiterValue = value).toInt()
        when (category) {
            DropdownMenuCategory.TYPE.name -> onTypeValueChange(
                propertyId = propertyId,
                index = index,
                types = types,
            )
            DropdownMenuCategory.AGENT.name -> onAgentValueChange(
                propertyId = propertyId,
                index = index,
                agents = agents,
            )
        }
    }

    private fun onTypeValueChange(propertyId: Long, index: Int, types: MutableList<Type>) {
        cacheProperty.typeId = types.elementAt(index).typeId
        Log.w("EditViewModel", "New value for type of property Id '$propertyId' is: ${cacheProperty.typeId} at index: $index")
    }

    private fun onAgentValueChange(propertyId: Long, index: Int, agents: MutableList<Agent>) {
        cacheProperty.agentId = agents.elementAt(index).agentId
        Log.w("EditViewModel", "New value for agent of property Id '$propertyId' is: ${agents.elementAt(index)} at index: $index" )
    }

    fun onStreetNumberValueChange(propertyId: Long, value: String) {
        cacheAddress.streetNumber = value
        Log.w("EditViewModel", "New value for street number of property Id '$propertyId' is: $value")
    }

    fun onStreetNameValueChange(propertyId: Long, value: String) {
        cacheAddress.streetName = value
        Log.w("EditViewModel", "New value for street name of property Id '$propertyId' is: $value")
    }

    fun onAddInfoValueChange(propertyId: Long, value: String) {
        cacheAddress.addInfo = value
        Log.w("EditViewModel", "New value for add info of property Id '$propertyId' is: $value")
    }

    fun onCityValueChange(propertyId: Long, value: String) {
        cacheAddress.city = value
        Log.w("EditViewModel", "New value for city of property Id '$propertyId' is: $value")
    }

    fun onStateValueChange(propertyId: Long, value: String) {
        cacheAddress.state = value
        Log.w("EditViewModel", "New value for state of property Id '$propertyId' is: $value")
    }

    fun onZipCodeValueChange(propertyId: Long, value: String) {
        cacheAddress.zipCode = value
        Log.w("EditViewModel", "New value for ZIP code of property Id '$propertyId' is: $value")
    }

    fun onCountryValueChange(propertyId: Long, value: String) {
        cacheAddress.country = value
        Log.w("EditViewModel", "New value for country of property Id '$propertyId' is: $value")
    }

    fun onRegistrationDateValueChange(propertyId: Long, value: String) {
        cacheProperty.registrationDate = value
        Log.w("EditViewModel", "New value for registration date of property Id '$propertyId' is: ${cacheProperty.registrationDate}")
    }

    fun onSaleDateValueChange(propertyId: Long, value: String) {
        cacheProperty.saleDate = value
        Log.w("EditViewModel", "New value for sale date of property Id '$propertyId' is: ${cacheProperty.saleDate}")
    }

    fun onPoiClick(propertyId: Long, poiItem: String, isSelected: Boolean) {
        /*
        when (poiItem) {
            PoiEnum.HOSPITAL.key -> { poiHospitalSelected = isSelected }
            PoiEnum.SCHOOL.key -> { poiSchoolSelected = isSelected }
            PoiEnum.RESTAURANT.key -> { poiRestaurantSelected = isSelected }
            PoiEnum.SHOP.key -> { poiShopSelected = isSelected }
            PoiEnum.RAILWAY_STATION.key -> { poiRailwayStationSelected = isSelected }
            PoiEnum.CAR_PARK.key -> { poiCarParkSelected = isSelected }
        }
        */
        if (isSelected) cacheItemPois.add(Poi(poiItem)) else cacheItemPois.remove(Poi(poiItem))
        Log.w("EditViewModel", "New value for POI $poiItem selection of property Id '$propertyId' is: $isSelected")
    }

    fun onSavePhotoButtonClick(propertyId: Long, uri: Uri, label: String, itemPhotos: MutableList<Photo>) {
        Log.w("EditViewModel", "Click on save button for photo: Label = $label, Uri = $uri")
        Log.w("EditViewModel", "Before action, cache photos list size is: ${_cacheItemPhotos.size}")
        // Check if the photo already exists
        val photo: Photo? = _cacheItemPhotos.find { it.uri == uri.toString() }
        val alreadyExists: Boolean = (photo != null)
        if (alreadyExists) {
            // Save the label modification of an existing photo
            val photoToUpdate = Photo(
                photoId = photo!!.photoId,
                uri = photo.uri,
                label = label,
//                propertyId = propertyId)
                propertyId = cacheProperty.propertyId)

            val photoIndex = _cacheItemPhotos.indexOf(photo)
            _cacheItemPhotos[photoIndex] = photoToUpdate
            _cacheItemPhotosFlow.value = _cacheItemPhotos
            Log.w(
                "EditViewModel",
                "Photo updated in cache: Id = ${photoToUpdate.photoId}, Old label = ${photo.label}, New label = ${photoToUpdate.label}, PropertyId = ${photoToUpdate.propertyId}"
            )
        } else {
            val photoToAdd = Photo(
                photoId = randomProvisionalId(),
                uri = uri.toString(),
                label = label,
                propertyId = cacheProperty.propertyId)

            _cacheItemPhotos.add(photoToAdd)
            _cacheItemPhotosFlow.value = _cacheItemPhotos
            Log.w(
                "EditViewModel",
                "Photo added to cache: Id = ${photoToAdd.photoId}, Label = ${photoToAdd.label}, PropertyId = ${photoToAdd.propertyId}"
            )
        }
        Log.w("EditViewModel", "After action, cache photos list size is: ${_cacheItemPhotos.size}")
    }

    fun onPhotoDeletionConfirmation(propertyId: Long, photoId: Long, itemPhotos: MutableList<Photo>) {
        Log.w("EditViewModel", "Click on delete photo menu for photo id: $photoId")
        Log.w("EditViewModel", "Before action, cache photos list size is: ${_cacheItemPhotos.size}")
        val photoToRemove = photoRepository.photoFromId(photoId = photoId, photos = _cacheItemPhotos)
        photoToRemove?.let {
            _cacheItemPhotos.remove(photoToRemove)
            _cacheItemPhotosFlow.value = _cacheItemPhotos
            Log.w(
                "EditViewModel",
                "Photo removed from cache: Id = ${photoToRemove.photoId}, Label = ${photoToRemove.label}, PropertyId = ${photoToRemove.propertyId}"
            )
        } ?: Log.w(
            "EditViewModel",
            "Photo not found in cache: Id = $photoId, PropertyId = $propertyId"
        )
        Log.w("EditViewModel", "After action, cache photos list size is: ${_cacheItemPhotos.size}")
    }

    fun clearCache() {
        cacheProperty = CACHE_PROPERTY.copy()
        initialAddress = null
        cacheAddress = CACHE_ADDRESS.copy()
        cacheStringType = context.getString(R.string._unassigned_)
        cacheStringAgent = context.getString(R.string._unassigned_)
        initialItemPois = mutableListOf()
        cacheItemPois = mutableListOf()
        initialItemPhotos = null
        _cacheItemPhotos = mutableListOf()
        _cacheItemPhotosFlow.value = _cacheItemPhotos
        alreadyInit = false
    }

    /**/


    /* Room */

    private fun updateRoomWithCacheData(navController: NavHostController) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.w("EditViewModel", "Starting address update in Room...")
                upDateRoomWithAddress()
                Log.w("EditViewModel", "Starting property update in Room...")
                upDateRoomWithProperty()
                Log.w("EditViewModel", "Starting photos update in Room...")
                upDateRoomWithPhotos()
                Log.w("EditViewModel", "Starting propertyPoiJoins update in Room...")
                upDateRoomWithPropertyPoiJoins()
                Log.w("EditViewModel", "Clearing cache...")
                clearCache()
                withContext(Dispatchers.Main) {
                    Toast
                        .makeText(ApplicationRoot.getContext(), context.getString(R.string.recorded), Toast.LENGTH_SHORT)
                        .show()

                    navController.popBackStack()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast
                        .makeText(
                            ApplicationRoot.getContext(),
                            context.getString(R.string.general_error) + "\n" + context.getString(R.string.not_recorded),
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
            }
        }
    }

    private suspend fun upDateRoomWithAddress() {
        val isAddressEmpty = cacheAddress.isNullOrBlank()
        val isAddressUpdate = (initialAddress == null) == isAddressEmpty

        when {
            // The address is empty and not known in the database, so we do nothing
            isAddressEmpty && isAddressUpdate -> {}
            // The address is empty and known in the database, so we delete it
            isAddressEmpty -> { addressRepository.deleteAddressFromRoom(cacheAddress) }
            // The address is not empty, so we create it or update it
            else -> {
                val addressToUpsert = when {
                    // The address is not empty and known in the database, so we will update it.
                    // We only set blank fields to null...
                    isAddressUpdate -> { cacheAddress.replaceBlankValuesWithNull() }
                    // The address is not empty and not known in the database, so we create it.
                    // We set blank fields to null and we remove the default cache addressId...
                    else -> { cacheAddress.replaceBlankValuesWithNull().withoutId() }
                }
                // ... then we upsert database and get the Room's address id
                newAddressIdFromRoom = addressRepository.upsertAddressInRoom(addressToUpsert)
            }
        }
    }

    private suspend fun upDateRoomWithProperty() {
        val isAddressEmpty = cacheAddress.isNullOrBlank()
        val isAddressUpdate = (initialAddress == null) == isAddressEmpty
        if (!isAddressUpdate && !isAddressEmpty) {
            // An address has been added
            cacheProperty.addressId = newAddressIdFromRoom
        } else if (!isAddressUpdate) {
            // The address has been deleted
            cacheProperty.addressId = null
        }

        val propertyToUpsert = if (cacheProperty.propertyId == CACHE_PROPERTY.propertyId) {
            // If the property is not known in the database,
            // we set blank fields to null and removes the default cache propertyId
            cacheProperty.replaceBlankValuesWithNull().withoutId()
        } else {
            // Otherwise, we only set blank fields to null
            cacheProperty.replaceBlankValuesWithNull()
        }

        newPropertyIdFromRoom = propertyRepository.upsertPropertyInRoom(propertyToUpsert)
    }

    private suspend fun upDateRoomWithPhotos() {
        // Deletes removed photos from Room
        val cachePhotosIds: List<Long> = _cacheItemPhotos.map { it.photoId }
        val photosToDelete = initialItemPhotos!!.filter { it.photoId !in cachePhotosIds }.toMutableList()
        if (photosToDelete.isNotEmpty()) photoRepository.deletePhotosFromRoom(photosToDelete)

        // Updates photos or creates new photos in Room
        // If a new property has been created, for each photo to upsert,
        // we replace the value of the propertyId with the new id generated by Room
        if (cacheProperty.propertyId == CACHE_PROPERTY.propertyId) _cacheItemPhotos.forEach {
            it.propertyId = newPropertyIdFromRoom
        }
        // Before updating Room, we replace any new photo random id created for cache
        // with 0, in order to let Room autogenerate an id.
        val photosToUpsert = _cacheItemPhotos.map {
            if (it.photoId == CACHE_PHOTO.photoId || it.photoId < 0) it.withoutId() else it
        }.toMutableList()
        if (photosToUpsert.isNotEmpty()) photoRepository.upsertPhotosInRoom(photosToUpsert)
    }

    private suspend fun upDateRoomWithPropertyPoiJoins() {
        val initialPoisIds: List<String> = initialItemPois.map { it.poiId }
        val cachePoisIds: List<String> = cacheItemPois.map { it.poiId }

        // Deletes unselected pois from PropertyPoiJoin in Room
        val poisIdsToRemove = initialPoisIds.filter { it !in cachePoisIds }
        if (poisIdsToRemove.isNotEmpty()) {
            val propertyPoiJoinsToDelete =
                poisIdsToRemove.map { PropertyPoiJoin(newPropertyIdFromRoom, it) }.toMutableList()
            propertyPoiJoinRepository.deletePropertyPoiJoinsFromRoom(propertyPoiJoinsToDelete)
        }

        // Adds selected pois to PropertyPoiJoin in Room
        val poisIdsToAdd = cachePoisIds.filter { it !in initialPoisIds }
        if (poisIdsToAdd.isNotEmpty()) {
            val propertyPoiJoinsToAdd =
                poisIdsToAdd.map { PropertyPoiJoin(newPropertyIdFromRoom, it) }.toMutableList()
            propertyPoiJoinRepository.upsertPropertyPoiJoinsInRoom(propertyPoiJoinsToAdd)
        }
    }

    /**/

}
