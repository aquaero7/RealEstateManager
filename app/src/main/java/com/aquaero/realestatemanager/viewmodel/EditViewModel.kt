package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.EditDetail
import com.aquaero.realestatemanager.NO_PHOTO
import com.aquaero.realestatemanager.R
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
    private var descriptionValue by mutableStateOf("")
    private var priceValue by mutableIntStateOf(0)
    private var surfaceValue by mutableIntStateOf(0)
    private var typeValue by mutableStateOf("")
    private var agentValue by mutableStateOf("")
    private lateinit var photos: MutableList<Photo>

    /***/


    fun onClickMenu(
        navController: NavHostController,
        propertyId: Comparable<*>
    ) {
        Log.w("Click on menu valid", "Screen ${EditDetail.label} / Property $propertyId")
        Log.w("Click on menu valid", "New values: ${tempPropertyModificationsValidated()}")

        propertyRepository.updateProperty(propertyId)   // Add new property values to arguments
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

    fun onDescriptionValueChanged(value: String) {
        // propertyRepository.onDescriptionValueChanged(value)

        descriptionValue = value

        Log.w("EditViewModel", "New value for description is: $value")
        Toast.makeText(context, "New value for description is: $value", Toast.LENGTH_SHORT)
            .show()  // TODO: To be deleted
    }

    fun onPriceValueChanged(value: String, currency: String) {
        // propertyRepository.onPriceValueChanged(value, currency)

        priceValue = if (value.isNotEmpty()) {
            when (currency) {
                "€" -> convertEuroToDollar(value.toInt())
                else -> value.toInt()
            }
        } else 0

        Log.w(
            "EditViewModel",
            "New value for price is: $priceValue dollars and input is $value $currency"
        )
        // TODO: To be deleted
        Toast.makeText(
            context,
            "New value for price is: $priceValue dollars and input is $value $currency",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun onSurfaceValueChanged(value: String) {
        // propertyRepository.onSurfaceValueChanged(value)

        surfaceValue = if (value.isNotEmpty()) value.toInt() else 0

        Log.w("EditViewModel", "New value for surface is: $value")
        Toast.makeText(context, "New value for surface is: $value", Toast.LENGTH_SHORT)
            .show()  // TODO: To be deleted
    }

    fun onDropdownMenuValueChanged(value: String) {
        // propertyRepository.onDropdownMenuValueChanged(value, agentsSet)

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
                ?.let { context.getString(it) } -> onTypeValueChanged(
                index = index,
                field = field
            )

            agentsSet().elementAt(index) -> onAgentValueChanged(
                index = index,
                field = field
            )
        }
    }

    private fun onTypeValueChanged(index: Int, field: String) {
        typeValue = field

        Log.w("EditViewModel", "New index for type is: $index / New value for type is: $field")
        Toast.makeText(
            context,
            "New index for type is: $index / New value for type is: $field",
            Toast.LENGTH_SHORT
        )
            .show()  // TODO: To be deleted
    }

    private fun onAgentValueChanged(index: Int, field: String) {
        agentValue = field

        Log.w("EditViewModel", "New index for agent is: $index / New value for agent is: $field")
        Toast.makeText(
            context,
            "New index for agent is: $index / New value for agent is: $field",
            Toast.LENGTH_SHORT
        )
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

    fun tempPropertyModificationsValidated(): String {
        var newValues = ""
        if (descriptionValue != "") newValues += "/$descriptionValue"
        if (priceValue != 0) newValues += "/$priceValue"
        if (surfaceValue != 0) newValues += "/$surfaceValue"
        if (typeValue != "") newValues += "/$typeValue"
        if (agentValue != "") newValues += "/$agentValue"
        if (photos.isNotEmpty()) {
            var photoLabels = ""
            photos.forEach {
                photoLabels += "/${it.phLabel}"
            }
            newValues += "/$photoLabels"
        }
        return newValues
    }

    /***/


}