package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.PropertyRepository

class EditViewModel(
    private val agentRepository: AgentRepository,
    private val propertyRepository: PropertyRepository,
    private val photoRepository: PhotoRepository,
) : ViewModel() {

    private val context: Context by lazy { ApplicationRoot.getContext() }

    val pTypeSet = propertyRepository.pTypesSet
    val poiSet = propertyRepository.poiSet
    val agentSet = agentRepository.agentsSet

    fun propertyFromId(propertyId: Long): Property {
        return propertyRepository.propertyFromId(propertyId)
    }

    fun agentFromId(agentId: Long): Agent? {
        return agentRepository.agentFromId(agentId)
    }

    fun onDescriptionValueChanged(value: String) {
        propertyRepository.onDescriptionValueChanged(value)
    }

    fun onPriceValueChanged(value: String, currency: String) {
        propertyRepository.onPriceValueChanged(value, currency)
    }

    fun onSurfaceValueChanged(value: String) {
        propertyRepository.onSurfaceValueChanged(value)
    }

    fun onDropdownMenuValueChanged(value: String) {
        val index = value.substringBefore(
            delimiter = "#",
            missingDelimiterValue = "-1"
        ).toInt()
        val field = value.substringAfter(
            delimiter = "#",
            missingDelimiterValue = value
        )
        when (field) {
            pTypeSet.elementAt(index)
                ?.let { context.getString(it) } -> propertyRepository.onTypeValueChanged(value)

            agentSet().elementAt(index) -> agentRepository.onAgentValueChanged(value)
        }
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

    fun onShootPhotoMenuItemClick() {
        // TODO: To implement
        photoRepository.photoIsReady()
        Log.w("EditViewModel", "Click on shoot photo menu item")
    }

    fun onSelectPhotoMenuItemClick() {
        // TODO: To implement
        photoRepository.photoIsReady()
        Log.w("EditViewModel", "Click on select photo menu item")
    }

    fun onAddPhotoButtonClick() {
        // TODO: To implement
        Log.w("EditViewModel", "Click on add photo button")
    }

    fun onDeletePhotoMenuItemClick(value: Long) {
        // TODO: To implement
        Log.w("EditViewModel", "Click on delete photo $value button")
    }

    fun isPhotoReady(): Boolean {
        return photoRepository.isPhotoReady()
    }



}