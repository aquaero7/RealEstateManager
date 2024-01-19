package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.EditDetail
import com.aquaero.realestatemanager.NO_PHOTO
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.PropertyRepository
import com.aquaero.realestatemanager.utils.convertEuroToDollar

class EditViewModel(
    private val agentRepository: AgentRepository,
    private val propertyRepository: PropertyRepository,
    private val photoRepository: PhotoRepository,
) : ViewModel() {

    private val context: Context by lazy { ApplicationRoot.getContext() }

    val pTypesSet = propertyRepository.pTypesSet
    val poiSet = propertyRepository.poiSet
    val agentsSet = agentRepository.agentsSet

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
    private var photos = mutableListOf<Photo>()


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
            }
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

    fun onLocationValuesChange(propertyId: String, value: Address) {
        addressValue = value

        Log.w("EditViewModel", "New value for address Id of property Id '$propertyId' is: ${value.addressId}")
        Toast.makeText(context, "New value for address Id of property Id '$propertyId' is: ${value.addressId}", Toast.LENGTH_SHORT)
            .show()  // TODO: To be deleted
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






    fun onSavePhotoButtonClick(propertyId: Long, uri: Uri, label: String) {
        Log.w("EditViewModel", "Click on save photo button")
        Log.w("EditViewModel", "Saving photo: Label = $label, Uri = $uri")

        photos = propertyFromId(propertyId).photos

        // Check if the photo already exists
        var photoId: Long? = null
        photos.forEach {
            if (it.phUri == uri) {
                // The photo already exists
                photoId = it.phId
            }
        }

        if (photoId != null) {
            // Save the label modification of an existing photo
            photoRepository.photoFromId(photos, photoId!!).phLabel = label

            Toast.makeText(
                context,
                "Updating photo: Label = $label, Uri = $uri",
                Toast.LENGTH_SHORT
            )
                .show()  // TODO: To be deleted
        } else {
            // Save a new photo
            if (photos.size == 1 && photos.elementAt(0).phId == 0L) photos.removeAt(0)
            photos.add(Photo((Math.random() * 9999).toLong(), uri, label))

            Toast.makeText(context, "Saving photo: Label = $label, Uri = $uri", Toast.LENGTH_SHORT)
                .show()  // TODO: To be deleted
        }
    }

    fun onPhotoDeletionConfirmation(photoId: Long, propertyId: Long) {
        Log.w("EditViewModel", "Click on delete photo (id: $photoId) button")

        photos = propertyFromId(propertyId).photos
        val photoToRemove = photoRepository.photoFromId(photos = photos, photoId = photoId)

        Log.w("EditViewModel", "Before action, photos list size is: ${photos.size}")

        photos.remove(photoToRemove)
        if (photos.size == 0) photos.add(NO_PHOTO)
        Toast
            .makeText(context, context.getString(R.string.photo_deleted, photoToRemove.phLabel),
                Toast.LENGTH_SHORT)
            .show()

        Log.w("EditViewModel", "After action. photos list size is: ${photos?.size}")
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
        if (photos.isNotEmpty()) {
            var photoLabels = ""
            photos.forEach {
                photoLabels += "/${it.phLabel}"
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
        photos= mutableListOf()
    }

    /***/


}
