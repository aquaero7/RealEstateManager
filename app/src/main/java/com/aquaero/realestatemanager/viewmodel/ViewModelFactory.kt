package com.aquaero.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.LocationRepository
import com.aquaero.realestatemanager.repository.PropertyRepository

@Suppress("UNCHECKED_CAST")
object ViewModelFactory:  ViewModelProvider.Factory {

    private val propertyRepository: PropertyRepository = PropertyRepository()
    private val agentRepository: AgentRepository = AgentRepository()
    private val locationRepository: LocationRepository = LocationRepository()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            AppViewModel(propertyRepository) as T
        } else if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            ListViewModel(propertyRepository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            DetailViewModel(propertyRepository) as T
        } else if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
            EditViewModel(agentRepository, propertyRepository) as T
        } else if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            MapViewModel(locationRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}