package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.PropertyRepository

class EditViewModel(
    private val agentRepository: AgentRepository,
    private val propertyRepository: PropertyRepository
): ViewModel() {

    private val context: Context by lazy { ApplicationRoot.getContext() }

    /* TODO: To be deleted
    val agentSet = agentRepository.agentsSet
    val pTypeSet = propertyRepository.pTypesSet
    */

}