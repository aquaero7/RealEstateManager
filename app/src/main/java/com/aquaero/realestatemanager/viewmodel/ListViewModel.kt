package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.PropertyRepository
import com.aquaero.realestatemanager.utils.ConnectionState
import com.aquaero.realestatemanager.utils.CurrencyStore
import java.text.NumberFormat
import java.util.Locale

class ListViewModel(
    private val agentRepository: AgentRepository,
    private val propertyRepository: PropertyRepository,
) : ViewModel() {

    private val context: Context by lazy { ApplicationRoot.getContext() }

    fun propertyFromId(propertyId: Long): Property {
        return propertyRepository.propertyFromId(propertyId)
    }

    fun agentFromId(agentId: Long): Agent? {
        return agentRepository.agentFromId(agentId)
    }

    fun checkForConnection(connection: ConnectionState): Boolean {
        return connection === ConnectionState.Available
    }

    fun thumbnailUrl(property: Property): String {
        return propertyRepository.thumbnailUrl(property)
    }


}