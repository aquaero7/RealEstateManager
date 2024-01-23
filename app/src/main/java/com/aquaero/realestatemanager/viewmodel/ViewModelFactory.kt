package com.aquaero.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.database.AppDatabase
import com.aquaero.realestatemanager.database.DatabaseProvider
import com.aquaero.realestatemanager.database.dao.PropertyDao
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.LocationRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.PropertyRepository

@Suppress("UNCHECKED_CAST")
object ViewModelFactory:  ViewModelProvider.Factory {

    /* Config 1
    private val propertyDao: PropertyDao = DatabaseProvider.getInstance().propertyDao
    */

    /* Config 2
    private val appDatabase: AppDatabase = DatabaseProvider.provideAppDatabase()
    private val propertyDao: PropertyDao = DatabaseProvider.providePropertyDao(appDatabase)
    */

    // Config 3
    private val appDatabase: AppDatabase = AppDatabase.getInstance()
    private val propertyDao: PropertyDao = appDatabase.propertyDao
    //

    private val propertyRepository: PropertyRepository = PropertyRepository(propertyDao)
    private val agentRepository: AgentRepository = AgentRepository()
    private val locationRepository: LocationRepository = LocationRepository()
    private val photoRepository: PhotoRepository = PhotoRepository()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            AppViewModel(agentRepository, propertyRepository) as T
        } else if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            ListViewModel(agentRepository, propertyRepository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            DetailViewModel(propertyRepository) as T
        } else if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
            EditViewModel(agentRepository, propertyRepository, photoRepository) as T
        } else if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            MapViewModel(locationRepository) as T
        } else if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            SearchViewModel() as T
        } else if (modelClass.isAssignableFrom(LoanViewModel::class.java)) {
            LoanViewModel() as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}