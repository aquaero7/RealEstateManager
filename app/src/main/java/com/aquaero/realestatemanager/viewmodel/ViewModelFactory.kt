package com.aquaero.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.PropertyRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory:  ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            AppViewModel(PropertyRepository()) as T
        } else if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            ListViewModel() as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            DetailViewModel() as T
        } else if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
            EditViewModel(AgentRepository()) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}