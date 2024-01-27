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
import com.aquaero.realestatemanager.EditDetail
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.POI
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.repository.AddressRepository
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.PoiRepository
import com.aquaero.realestatemanager.repository.PropertyPoiJoinRepository
import com.aquaero.realestatemanager.repository.PropertyRepository
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
    private val poiRepository: PoiRepository,
    private val propertyPoiJoinRepository: PropertyPoiJoinRepository,
) : ViewModel() {

    private val context: Context by lazy { ApplicationRoot.getContext() }

    val pTypesSet = propertyRepository.typesSet
    // val poiSet = propertyRepository.poiSet
    val agentsSet = agentRepository.agentsSet

    /*
    private val poiHospital = context.getString(R.string.key_hospital)
    private val poiSchool = context.getString(R.string.key_school)
    private val poiRestaurant = context.getString(R.string.key_restaurant)
    private val poiShop = context.getString(R.string.key_shop)
    private val poiRailwayStation = context.getString(R.string.key_railway_station)
    private val poiCarPark = context.getString(R.string.key_car_park)
    */

    /**
     * Temp data used as a cache for property creation ou update
     */
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
    private var registrationDateValue: String? = "-1"
    private var saleDateValue: String? = "-1"
    private var itemPhotos = mutableListOf<Photo>()

    private var poiHospitalSelected: Boolean? = null
    private var poiSchoolSelected: Boolean? = null
    private var poiRestaurantSelected: Boolean? = null
    private var poiShopSelected: Boolean? = null
    private var poiRailwayStationSelected: Boolean? = null
    private var poiCarParkSelected: Boolean? = null

    /***/


    /** Room **/

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

    /***/


    fun onClickMenu(
        navController: NavHostController,
        propertyId: Comparable<*>
    ) {
        Log.w("Click on menu valid", "Screen ${EditDetail.label} / Property $propertyId")
        Log.w("Click on menu valid", "New values: ${tempPropertyModificationsValidated()}")

        propertyRepository.updateProperty(propertyId)   // TODO: Add temp property values to arguments
        clearTempData()
        navController.popBackStack()
    }

    fun propertyFromId(propertyId: Long): Property {
        return propertyRepository.propertyFromId(propertyId)
    }

    fun agentFromId(agentId: Long): Agent? {
        return agentRepository.agentFromId(agentId)
    }

    fun mutableSetIndex(set: MutableSet<Any?>, item: String): Int {
        var index: Int = 0
        for (setItem in set) {
            if (setItem is Int?) {
                if (setItem.let { context.getString(it as Int) } == item) index =
                    set.indexOf(setItem)
            } else {
                if (setItem.let { it as String } == item) index = set.indexOf(setItem)
            }
        }
        return index
    }

    fun getPhotoUri(): Uri {
        return photoRepository.getPhotoUri()
    }

    fun onShootPhotoMenuItemClick(
        uri: Uri,
        cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
        permissionLauncher: ManagedActivityResultLauncher<String, Boolean>
    ) {
        photoRepository.onShootPhotoMenuItemClick(uri, cameraLauncher, permissionLauncher)
    }

    fun onSelectPhotoMenuItemClick(
        pickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
    ) {
        photoRepository.onSelectPhotoMenuItemClick(pickerLauncher)
    }


    /**
     * Temp data used as a cache for property creation or update
     */

    fun onDescriptionValueChange(propertyId: String, value: String) {
        descriptionValue = value

        Log.w("EditViewModel", "New value for description of property Id '$propertyId' is: $value")
        Toast.makeText(context, "New value for description of property Id '$propertyId' is: $value", Toast.LENGTH_SHORT)
            .show()  // TODO: To be deleted
    }

    fun onPriceValueChange(propertyId: String, value: String, currency: String) {

        priceValue = if (value.isNotEmpty() && value.isDigitsOnly()) {
            when (currency) {
                "€" -> convertEuroToDollar(value.toInt())
                else -> value.toInt()
            }!!
        } else 0

        Log.w(
            "EditViewModel",
            "New value for price of property Id '$propertyId' is: $priceValue dollars and input is $value $currency"
        )
        // TODO: To be deleted
        Toast.makeText(
            context,
            "New value for price of property Id '$propertyId' is: $priceValue dollars and input is $value $currency",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun onSurfaceValueChange(propertyId: String, value: String) {
        surfaceValue = if (value.isNotEmpty()) value.toInt() else 0

        Log.w("EditViewModel", "New value for surface of property Id '$propertyId' is: $value")
        Toast.makeText(context, "New value for surface of property Id '$propertyId' is: $value", Toast.LENGTH_SHORT)
            .show()  // TODO: To be deleted
    }

    fun onNbOfRoomsValueChange(propertyId: String, value: String) {
        nbOfRoomsValue = if (value.isNotEmpty()) value.toInt() else 0

        Log.w("EditViewModel", "New value for nb of rooms of property Id '$propertyId' is: $value")
        Toast.makeText(context, "New value for nb of rooms of property Id '$propertyId' is: $value", Toast.LENGTH_SHORT)
            .show()  // TODO: To be deleted
    }

    fun onNbOfBathroomsValueChange(propertyId: String, value: String) {
        nbOfBathroomsValue = if (value.isNotEmpty()) value.toInt() else 0

        Log.w("EditViewModel", "New value for nb of bathrooms of property Id '$propertyId' is: $value")
        Toast.makeText(context, "New value for nb of bathrooms of property Id '$propertyId' is: $value", Toast.LENGTH_SHORT)
            .show()  // TODO: To be deleted
    }

    fun onNbOfBedroomsValueChange(propertyId: String, value: String) {
        nbOfBedroomsValue = if (value.isNotEmpty()) value.toInt() else 0

        Log.w("EditViewModel", "New value for nb of bedrooms of property Id '$propertyId' is: $value")
        Toast.makeText(context, "New value for nb of bedrooms of property Id '$propertyId' is: $value", Toast.LENGTH_SHORT)
            .show()  // TODO: To be deleted
    }

    fun onDropdownMenuValueChange(propertyId: String, value: String) {
        val index = value.substringBefore(
            delimiter = "#",
            missingDelimiterValue = "-1"
        ).toInt()
        val field = value.substringAfter(
            delimiter = "#",
            missingDelimiterValue = value
        )
        when (field) {
            pTypesSet.elementAt(index)
                ?.let { context.getString(it) } -> onTypeValueChange(
                propertyId = propertyId,
                index = index,
                field = field
            )

            agentsSet().elementAt(index) -> onAgentValueChange(
                propertyId = propertyId,
                index = index,
                field = field
            )
        }
    }

    private fun onTypeValueChange(propertyId: String, index: Int, field: String) {
        typeValue = field

        Log.w("EditViewModel", "New index for type of property Id '$propertyId' is: $index / New value for type is: $field")
        Toast.makeText(
            context,
            "New index for type of property Id '$propertyId' is: $index / New value for type is: $field",
            Toast.LENGTH_SHORT
        )
            .show()  // TODO: To be deleted
    }

    private fun onAgentValueChange(propertyId: String, index: Int, field: String) {
        agentValue = field

        Log.w("EditViewModel", "New index for agent of property Id '$propertyId' is: $index / New value for agent is: $field")
        Toast.makeText(
            context,
            "New index for agent of property Id '$propertyId' is: $index / New value for agent is: $field",
            Toast.LENGTH_SHORT
        ).show()  // TODO: To be deleted
    }

    fun onStreetNumberValueChange(propertyId: String, value: String) {
        streetNumberValue = value

        Log.w("EditViewModel", "New value for street number of property Id '$propertyId' is: ${value}")
        Toast.makeText(context, "New value for street number of property Id '$propertyId' is: ${value}", Toast.LENGTH_SHORT)
            .show()  // TODO: To be deleted
    }

    fun onStreetNameValueChange(propertyId: String, value: String) {
        streetNameValue = value

        Log.w("EditViewModel", "New value for street name of property Id '$propertyId' is: ${value}")
        Toast.makeText(context, "New value for street name of property Id '$propertyId' is: ${value}", Toast.LENGTH_SHORT)
            .show()  // TODO: To be deleted
    }

    fun onAddInfoValueChange(propertyId: String, value: String) {
        addInfoValue = value

        Log.w("EditViewModel", "New value for add info of property Id '$propertyId' is: ${value}")
        Toast.makeText(context, "New value for add info of property Id '$propertyId' is: ${value}", Toast.LENGTH_SHORT)
            .show()  // TODO: To be deleted
    }

    fun onCityValueChange(propertyId: String, value: String) {
        cityValue = value

        Log.w("EditViewModel", "New value for city of property Id '$propertyId' is: ${value}")
        Toast.makeText(context, "New value for city of property Id '$propertyId' is: ${value}", Toast.LENGTH_SHORT)
            .show()  // TODO: To be deleted
    }

    fun onStateValueChange(propertyId: String, value: String) {
        stateValue = value

        Log.w("EditViewModel", "New value for state of property Id '$propertyId' is: ${value}")
        Toast.makeText(context, "New value for state of property Id '$propertyId' is: ${value}", Toast.LENGTH_SHORT)
            .show()  // TODO: To be deleted
    }

    fun onZipCodeValueChange(propertyId: String, value: String) {
        zipCodeValue = value

        Log.w("EditViewModel", "New value for ZIP code of property Id '$propertyId' is: ${value}")
        Toast.makeText(context, "New value for ZIP code of property Id '$propertyId' is: ${value}", Toast.LENGTH_SHORT)
            .show()  // TODO: To be deleted
    }

    fun onCountryValueChange(propertyId: String, value: String) {
        countryValue = value

        Log.w("EditViewModel", "New value for country of property Id '$propertyId' is: ${value}")
        Toast.makeText(context, "New value for country of property Id '$propertyId' is: ${value}", Toast.LENGTH_SHORT)
            .show()  // TODO: To be deleted
    }

    fun onRegistrationDateValueChange(propertyId: String, value: String) {
        registrationDateValue = value.ifEmpty { null }

        Log.w("EditViewModel", "New value for registration date of property Id '$propertyId' is: $registrationDateValue")
        Toast.makeText(context, "New value for registration date of property Id '$propertyId' is: $registrationDateValue", Toast.LENGTH_SHORT)
            .show()  // TODO: To be deleted
    }

    fun onSaleDateValueChange(propertyId: String, value: String) {
        saleDateValue = value.ifEmpty { null }

        Log.w("EditViewModel", "New value for sale date of property Id '$propertyId' is: $saleDateValue")
        Toast.makeText(context, "New value for sale date of property Id '$propertyId' is: $saleDateValue", Toast.LENGTH_SHORT)
            .show()  // TODO: To be deleted
    }

    fun onPoiClick(propertyId: String, poiItem: String, isSelected: Boolean) {
        when (poiItem) {
            // poiHospital -> { poiHospitalSelected = isSelected }
            POI.HOSPITAL.key -> { poiHospitalSelected = isSelected }
            // poiSchool -> { poiSchoolSelected = isSelected }
            POI.SCHOOL.key -> { poiSchoolSelected = isSelected }
            // poiRestaurant -> { poiRestaurantSelected = isSelected }
            POI.RESTAURANT.key -> { poiRestaurantSelected = isSelected }
            // poiShop -> { poiShopSelected = isSelected }
            POI.SHOP.key -> { poiShopSelected = isSelected }
            // poiRailwayStation -> { poiRailwayStationSelected = isSelected }
            POI.RAILWAY_STATION.key -> { poiRailwayStationSelected = isSelected }
            // poiCarPark -> { poiCarParkSelected = isSelected }
            POI.CAR_PARK.key -> { poiCarParkSelected = isSelected }
        }
        Log.w("EditViewModel", "New value for POI $poiItem selection of property Id '$propertyId' is: $isSelected")
        Toast.makeText(context, "New value for POI $poiItem selection of property Id '$propertyId' is: $isSelected", Toast.LENGTH_SHORT)
            .show()  // TODO: To be deleted
    }

    fun onSavePhotoButtonClick(propertyId: Long, uri: Uri, label: String) {
        Log.w("EditViewModel", "Click on save photo button")
        Log.w("EditViewModel", "Saving photo: Label = $label, Uri = $uri")

        // itemPhotos = propertyFromId(propertyId).photos
        itemPhotos = photoRepository.itemPhotos(photosStateFlow.value, propertyId)

        // Check if the photo already exists
        val photo: Photo? = itemPhotos.find { it.uri == uri }
        val alreadyExists: Boolean = (photo != null)
        /*
        var photoId: Long? = null
        itemPhotos.forEach {
            if (it.uri == uri) {
                // The photo already exists
                photoId = it.photoId
            }
        }
        */

        // if (photoId != null) {
        if (alreadyExists) {
            // Save the label modification of an existing photo
            // photoRepository.photoFromId(itemPhotos, photoId!!).label = label
            val photoToUpdate = Photo(
                photoId = photo!!.photoId,
                uri = photo.uri,
                label = label,
                propertyId = propertyId)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    photoRepository.upsertPhotoInRoom(photoToUpdate)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, context.getString(R.string.general_error), Toast.LENGTH_SHORT)
                        .show()
                }
            }

            Toast.makeText(
                context, "Updating photo: Label = $label, Uri = $uri", Toast.LENGTH_SHORT)
                .show()  // TODO: To be deleted
        } else {
            // Save a new photo
            // if (itemPhotos.size == 1 && itemPhotos.elementAt(0).photoId == 0L) itemPhotos.removeAt(0)
            // photos.add(Photo((Math.random() * 9999).toLong(), uri, label, propertyId))
            // itemPhotos.add(Photo(uri = uri, label = label, propertyId = propertyId))
            val photoToAdd = Photo(
                uri = uri,
                label = label,
                propertyId = propertyId)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    photoRepository.upsertPhotoInRoom(photoToAdd)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, context.getString(R.string.general_error), Toast.LENGTH_SHORT)
                        .show()
                }
            }

            Toast.makeText(context, "Saving photo: Label = $label, Uri = $uri", Toast.LENGTH_SHORT)
                .show()  // TODO: To be deleted
        }
    }

    fun onPhotoDeletionConfirmation(photoId: Long, propertyId: Long) {
        Log.w("EditViewModel", "Click on delete photo (id: $photoId) button")

        // itemPhotos = propertyFromId(propertyId).photos
        itemPhotos = photoRepository.itemPhotos(photosStateFlow.value, propertyId)
        val photoToRemove = photoRepository.photoFromId(photos = itemPhotos, photoId = photoId)

        Log.w("EditViewModel", "Before action, photos list size is: ${itemPhotos.size}")

        // itemPhotos.remove(photoToRemove)
        // if (itemPhotos.size == 0) itemPhotos.add(NO_PHOTO)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                photoRepository.deletePhotoFromRoom(photoToRemove)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, context.getString(R.string.general_error), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        Toast
            .makeText(context, context.getString(R.string.photo_deleted, photoToRemove.label),
                Toast.LENGTH_SHORT)
            .show()

        Log.w("EditViewModel", "After action. photos list size is: ${itemPhotos.size}")
    }

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
