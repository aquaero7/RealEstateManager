package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.PropertyRepository

class EditViewModel(
    private val agentRepository: AgentRepository,
    private val propertyRepository: PropertyRepository
): ViewModel() {

    private val context: Context by lazy { ApplicationRoot.getContext() }

    fun propertyFromId(propertyId: Long): Property {
        return propertyRepository.propertyFromId(propertyId)
    }

    val agentSet = agentRepository.agentsSet

    val pTypeSet = propertyRepository.pTypesSet

}