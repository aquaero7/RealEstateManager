package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.PropertyRepository

class EditViewModel(
    private val agentRepository: AgentRepository,
    private val propertyRepository: PropertyRepository
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
        // return propertyRepository.mutableSetIndex(set, item)

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

}