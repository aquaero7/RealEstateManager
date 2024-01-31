package com.aquaero.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.database.AppDatabase
import com.aquaero.realestatemanager.database.DatabaseProvider
import com.aquaero.realestatemanager.database.dao.AddressDao
import com.aquaero.realestatemanager.database.dao.AgentDao
import com.aquaero.realestatemanager.database.dao.PhotoDao
import com.aquaero.realestatemanager.database.dao.PoiDao
import com.aquaero.realestatemanager.database.dao.PropertyDao
import com.aquaero.realestatemanager.database.dao.PropertyPoiJoinDao
import com.aquaero.realestatemanager.database.dao.TypeDao
import com.aquaero.realestatemanager.repository.AddressRepository
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.LocationRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.PoiRepository
import com.aquaero.realestatemanager.repository.PropertyPoiJoinRepository
import com.aquaero.realestatemanager.repository.PropertyRepository
import com.aquaero.realestatemanager.repository.TypeRepository

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
    private val addressDao: AddressDao = appDatabase.addressDao
    private val photoDao: PhotoDao = appDatabase.photoDao
    private val agentDao: AgentDao = appDatabase.agentDao
    private val typeDao: TypeDao = appDatabase.typeDao
    private val poiDao: PoiDao = appDatabase.poiDao
    private val propertyPoiJoinDao: PropertyPoiJoinDao = appDatabase.propertyPoiJoinDao
    //

    private val propertyRepository: PropertyRepository = PropertyRepository(propertyDao)
    private val addressRepository: AddressRepository = AddressRepository(addressDao)
    private val photoRepository: PhotoRepository = PhotoRepository(photoDao)
    private val agentRepository: AgentRepository = AgentRepository(agentDao)
    private val typeRepository: TypeRepository = TypeRepository(typeDao)
    private val poiRepository: PoiRepository = PoiRepository(poiDao)
    private val propertyPoiJoinRepository: PropertyPoiJoinRepository = PropertyPoiJoinRepository(propertyPoiJoinDao)
    private val locationRepository: LocationRepository = LocationRepository()


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            AppViewModel(propertyRepository, addressRepository, photoRepository, agentRepository,
                typeRepository, poiRepository, propertyPoiJoinRepository) as T
        } else if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            ListViewModel(propertyRepository, addressRepository, photoRepository, agentRepository,
                typeRepository, poiRepository, propertyPoiJoinRepository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            DetailViewModel(propertyRepository, addressRepository, photoRepository, agentRepository,
                typeRepository, poiRepository, propertyPoiJoinRepository) as T
        } else if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
            EditViewModel(propertyRepository, addressRepository, photoRepository, agentRepository,
                typeRepository, poiRepository, propertyPoiJoinRepository) as T
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