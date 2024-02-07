package com.aquaero.realestatemanager.viewmodel

import android.annotation.SuppressLint
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
import com.aquaero.realestatemanager.NO_ITEM_ID
import com.aquaero.realestatemanager.NULL_ITEM_ID
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.PoiEnum
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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


    /** Temp data used as a cache for property creation or update */

    private var descriptionValue = "-1"
    private var priceValue = -1
    private var surfaceValue = -1
    private var nbOfRoomsValue = -1
    private var nbOfBathroomsValue = -1
    private var nbOfBedroomsValue = -1
    private var typeValue = "-1"
    private var agentValue = "-1"
    private var addressValue: Address? = null
    private var streetNumberValue = "-1"
    private var streetNameValue = "-1"
    private var addInfoValue = "-1"
    private var cityValue = "-1"
    private var stateValue = "-1"
    private var zipCodeValue = "-1"
    private var countryValue = "-1"
    private var registrationDateValue = "-1"
    private var saleDateValue = "-1"
    private var itemPhotos = mutableListOf<Photo>()

    private var poiHospitalSelected: Boolean? = null
    private var poiSchoolSelected: Boolean? = null
    private var poiRestaurantSelected: Boolean? = null
    private var poiShopSelected: Boolean? = null
    private var poiRailwayStationSelected: Boolean? = null
    private var poiCarParkSelected: Boolean? = null

    /***/


    /** Room */

    /***/


    fun onClickMenu(
        navController: NavHostController,
        propertyId: Comparable<*>
    ) {
        Log.w("Click on menu valid", "Screen ${EditDetail.label} / Property $propertyId")
        Log.w("Click on menu valid", "New values: ${tempPropertyModificationsValidated()}")

        propertyRepository.updateProperty(propertyId = propertyId)   // TODO: Add temp property values to arguments
        clearTempData()
        navController.popBackStack()
    }

    fun propertyFromId(propertyId: Long, properties: MutableList<Property>): Property {
        return propertyRepository.propertyFromId(propertyId = propertyId, properties = properties)
    }

    private fun poiFromId(poiId: String, pois: MutableList<Poi>): Poi {
        return poiRepository.poiFromId(poiId = poiId, pois = pois)
    }

    fun stringType(typeId: String, types: MutableList<Type>, stringTypes: MutableList<String>): String {
        return typeRepository.stringType(typeId = typeId, types = types, stringTypes = stringTypes)
    }

    fun stringAgent(agentId: Long, agents: MutableList<Agent>, stringAgents: MutableList<String>): String {
        return agentRepository.stringAgent(agentId = agentId, agents = agents, stringAgents = stringAgents)
    }

    fun itemPhotos(propertyId: Long, photos: MutableList<Photo>): MutableList<Photo> {
        return photoRepository.itemPhotos(propertyId = propertyId, photos = photos)
    }

    fun itemPois(propertyId: Long, propertyPoiJoins: MutableList<PropertyPoiJoin>, pois: MutableList<Poi>): MutableList<Poi> {
        return mutableListOf<Poi>().apply {
            propertyPoiJoins
                .filter { join -> join.propertyId == propertyId }
                .mapTo(this) { join -> poiFromId(poiId = join.poiId, pois = pois) }
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


    /** Temp data used as a cache for property creation or update */

    fun onDescriptionValueChange(propertyId: Long, value: String) {
        descriptionValue = value
        Log.w("EditViewModel", "New value for description of property Id '$propertyId' is: $value")
    }

    fun onPriceValueChange(propertyId: Long, value: String, currency: String) {
        priceValue = if (value.isNotEmpty() && value.isDigitsOnly()) {
            when (currency) {
                "€" -> convertEuroToDollar(euros = value.toInt())
                else -> value.toInt()
            }!!
        } else 0
        Log.w(
            "EditViewModel",
            "New value for price of property Id '$propertyId' is: $priceValue dollars and input is $value $currency"
        )
    }

    fun onSurfaceValueChange(propertyId: Long, value: String) {
        surfaceValue = if (value.isNotEmpty()) value.toInt() else 0
        Log.w("EditViewModel", "New value for surface of property Id '$propertyId' is: $value")
    }

    fun onNbOfRoomsValueChange(propertyId: Long, value: String) {
        nbOfRoomsValue = if (value.isNotEmpty()) value.toInt() else 0
        Log.w("EditViewModel", "New value for nb of rooms of property Id '$propertyId' is: $value")
    }

    fun onNbOfBathroomsValueChange(propertyId: Long, value: String) {
        nbOfBathroomsValue = if (value.isNotEmpty()) value.toInt() else 0
        Log.w("EditViewModel", "New value for nb of bathrooms of property Id '$propertyId' is: $value")
    }

    fun onNbOfBedroomsValueChange(propertyId: Long, value: String) {
        nbOfBedroomsValue = if (value.isNotEmpty()) value.toInt() else 0
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
        typeValue = types.elementAt(index).typeId
        Log.w("EditViewModel", "New value for type of property Id '$propertyId' is: $typeValue at index: $index")
    }

    private fun onAgentValueChange(propertyId: Long, index: Int, agents: MutableList<Agent>) {
        agentValue = agents.elementAt(index).toString()
        Log.w("EditViewModel", "New value for agent of property Id '$propertyId' is: $agentValue at index: $index" )
    }

    fun onStreetNumberValueChange(propertyId: Long, value: String) {
        streetNumberValue = value
        Log.w("EditViewModel", "New value for street number of property Id '$propertyId' is: $value")
    }

    fun onStreetNameValueChange(propertyId: Long, value: String) {
        streetNameValue = value
        Log.w("EditViewModel", "New value for street name of property Id '$propertyId' is: $value")
    }

    fun onAddInfoValueChange(propertyId: Long, value: String) {
        addInfoValue = value
        Log.w("EditViewModel", "New value for add info of property Id '$propertyId' is: $value")
    }

    fun onCityValueChange(propertyId: Long, value: String) {
        cityValue = value
        Log.w("EditViewModel", "New value for city of property Id '$propertyId' is: $value")
    }

    fun onStateValueChange(propertyId: Long, value: String) {
        stateValue = value
        Log.w("EditViewModel", "New value for state of property Id '$propertyId' is: $value")
    }

    fun onZipCodeValueChange(propertyId: Long, value: String) {
        zipCodeValue = value
        Log.w("EditViewModel", "New value for ZIP code of property Id '$propertyId' is: $value")
    }

    fun onCountryValueChange(propertyId: Long, value: String) {
        countryValue = value
        Log.w("EditViewModel", "New value for country of property Id '$propertyId' is: $value")
    }

    fun onRegistrationDateValueChange(propertyId: Long, value: String) {
//        registrationDateValue = value.ifEmpty { null }
        registrationDateValue = value
        Log.w("EditViewModel", "New value for registration date of property Id '$propertyId' is: $registrationDateValue")
    }

    fun onSaleDateValueChange(propertyId: Long, value: String) {
//        saleDateValue = value.ifEmpty { null }
        saleDateValue = value
        Log.w("EditViewModel", "New value for sale date of property Id '$propertyId' is: $saleDateValue")
    }

    fun onPoiClick(propertyId: Long, poiItem: String, isSelected: Boolean) {
        when (poiItem) {
            PoiEnum.HOSPITAL.key -> { poiHospitalSelected = isSelected }
            PoiEnum.SCHOOL.key -> { poiSchoolSelected = isSelected }
            PoiEnum.RESTAURANT.key -> { poiRestaurantSelected = isSelected }
            PoiEnum.SHOP.key -> { poiShopSelected = isSelected }
            PoiEnum.RAILWAY_STATION.key -> { poiRailwayStationSelected = isSelected }
            PoiEnum.CAR_PARK.key -> { poiCarParkSelected = isSelected }
        }
        Log.w("EditViewModel", "New value for POI $poiItem selection of property Id '$propertyId' is: $isSelected")
    }

    fun onSavePhotoButtonClick(propertyId: Long, uri: Uri, label: String, itemPhotos: MutableList<Photo>) {
        Log.w("EditViewModel", "Click on save button for photo: Label = $label, Uri = $uri")
        Log.w("EditViewModel", "Before action, photos list size is: ${itemPhotos.size}")
        // Check if the photo already exists
        val photo: Photo? = itemPhotos.find { it.uri == uri.toString() }
        val alreadyExists: Boolean = (photo != null)
        if (alreadyExists) {
            // Save the label modification of an existing photo
            val photoToUpdate = Photo(
                photoId = photo!!.photoId,
                uri = photo.uri,
                label = label,
                propertyId = propertyId)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    photoRepository.upsertPhotoInRoom(photo = photoToUpdate)
                    Log.w("EditViewModel", "Photo updated: Label = $label, Uri = $uri")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        } else {
            // Save a new photo
            // if (itemPhotos.size == 1 && itemPhotos.elementAt(0).photoId == 0L) itemPhotos.removeAt(0)
            val photoToAdd = Photo(
                uri = uri.toString(),
                label = label,
                propertyId = NULL_ITEM_ID)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    photoRepository.upsertPhotoInRoom(photo = photoToAdd)
                    Log.w("EditViewModel", "Photo saved: Label = $label, Uri = $uri")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        Log.w("EditViewModel", "After action. photos list size is: ${itemPhotos.size}")
    }

    fun onPhotoDeletionConfirmation(propertyId: Long, photoId: Long, itemPhotos: MutableList<Photo>) {
        Log.w("EditViewModel", "Click on delete photo menu for photo id: $photoId")
        Log.w("EditViewModel", "Before action, photos list size is: ${itemPhotos.size}")
        val photoToRemove = photoRepository.photoFromId(photoId = photoId, photos = itemPhotos)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                photoRepository.deletePhotoFromRoom(photo = photoToRemove)
                Toast
                    .makeText(context, context.getString(R.string.photo_deleted, photoToRemove.label),
                        Toast.LENGTH_SHORT)
                    .show()
                Log.w("EditViewModel", "Photo deleted: Label = ${photoToRemove.label}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        Log.w("EditViewModel", "After action. photos list size is: ${itemPhotos.size}")
        // if (itemPhotos.size == 0) itemPhotos.add(NO_PHOTO)
    }


    /** Temp data management */

    private fun tempPropertyModificationsValidated(): String {
        var newValues = ""
        if (descriptionValue != "-1") newValues += "/$descriptionValue"
        if (priceValue != -1) newValues += "/$priceValue"
        if (surfaceValue != -1) newValues += "/$surfaceValue"
        if (nbOfRoomsValue != -1) newValues += "/$nbOfRoomsValue"
        if (nbOfBathroomsValue != -1) newValues += "/$nbOfBathroomsValue"
        if (nbOfBedroomsValue != -1) newValues += "/$nbOfBedroomsValue"
        if (typeValue != "-1") newValues += "/$typeValue"
        if (agentValue != "-1") newValues += "/$agentValue"
        if (addressValue != null) newValues += "/${addressValue!!.addressId}"
        if (streetNumberValue != "-1") newValues += "/$streetNumberValue"
        if (streetNameValue != "-1") newValues += "/$streetNameValue"
        if (addInfoValue != "-1") newValues += "/$addInfoValue"
        if (cityValue != "-1") newValues += "/$cityValue"
        if (stateValue != "-1") newValues += "/$stateValue"
        if (zipCodeValue != "-1") newValues += "/$zipCodeValue"
        if (countryValue != "-1") newValues += "/$countryValue"
        if (registrationDateValue != "-1") newValues += "/$registrationDateValue"
        if (saleDateValue != "-1") newValues += "/$saleDateValue"

        if (poiHospitalSelected != null) newValues += "/$poiHospitalSelected"
        if (poiSchoolSelected != null) newValues += "/$poiSchoolSelected"
        if (poiRestaurantSelected != null) newValues += "/$poiRestaurantSelected"
        if (poiShopSelected != null) newValues += "/$poiShopSelected"
        if (poiRailwayStationSelected != null) newValues += "/$poiRailwayStationSelected"
        if (poiCarParkSelected != null) newValues += "/$poiCarParkSelected"

        if (itemPhotos.isNotEmpty()) {
            var photoLabels = ""
            itemPhotos.forEach {
                photoLabels += "/${it.label}"
            }
            newValues += "/$photoLabels"
        }
        return newValues
    }

    private fun clearTempData() {
        descriptionValue ="-1"
        priceValue = -1
        surfaceValue = -1
        nbOfRoomsValue = -1
        nbOfBathroomsValue = -1
        nbOfBedroomsValue = -1
        typeValue = "-1"
        agentValue = "-1"
        addressValue = null
        streetNumberValue = "-1"
        streetNameValue = "-1"
        addInfoValue = "-1"
        cityValue = "-1"
        stateValue = "-1"
        zipCodeValue = "-1"
        countryValue = "-1"
        registrationDateValue = "-1"
        saleDateValue = "-1"

        poiHospitalSelected = null
        poiSchoolSelected = null
        poiRestaurantSelected = null
        poiShopSelected = null
        poiRailwayStationSelected = null
        poiCarParkSelected = null

        itemPhotos= mutableListOf()
    }

    /***/


}
