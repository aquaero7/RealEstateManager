package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aquaero.realestatemanager.database.AppDatabase
import com.aquaero.realestatemanager.database.dao.AddressDao
import com.aquaero.realestatemanager.database.dao.AgentDao
import com.aquaero.realestatemanager.database.dao.PhotoDao
import com.aquaero.realestatemanager.database.dao.PoiDao
import com.aquaero.realestatemanager.database.dao.PropertyDao
import com.aquaero.realestatemanager.database.dao.PropertyPoiJoinDao
import com.aquaero.realestatemanager.database.dao.TypeDao
import com.aquaero.realestatemanager.repository.AddressRepository
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.CacheRepository
import com.aquaero.realestatemanager.repository.LocationRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.PoiRepository
import com.aquaero.realestatemanager.repository.PropertyPoiJoinRepository
import com.aquaero.realestatemanager.repository.PropertyRepository
import com.aquaero.realestatemanager.repository.TypeRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(context: Context):  ViewModelProvider.Factory {

    // DAO
    private val appDatabase: AppDatabase = AppDatabase.getInstance(context = context)
    private val propertyDao: PropertyDao = appDatabase.propertyDao
    private val addressDao: AddressDao = appDatabase.addressDao
    private val photoDao: PhotoDao = appDatabase.photoDao
    private val agentDao: AgentDao = appDatabase.agentDao
    private val typeDao: TypeDao = appDatabase.typeDao
    private val poiDao: PoiDao = appDatabase.poiDao
    private val propertyPoiJoinDao: PropertyPoiJoinDao = appDatabase.propertyPoiJoinDao

    // Repositories
    private val propertyRepository: PropertyRepository = PropertyRepository(propertyDao = propertyDao)
    private val addressRepository: AddressRepository = AddressRepository(addressDao = addressDao)
    private val photoRepository: PhotoRepository = PhotoRepository(photoDao = photoDao)
    private val agentRepository: AgentRepository = AgentRepository(agentDao = agentDao)
    private val typeRepository: TypeRepository = TypeRepository(typeDao = typeDao)
    private val poiRepository: PoiRepository = PoiRepository(poiDao = poiDao)
    private val propertyPoiJoinRepository: PropertyPoiJoinRepository =
        PropertyPoiJoinRepository(propertyPoiJoinDao = propertyPoiJoinDao)
    private val locationRepository: LocationRepository = LocationRepository()
    private val cacheRepository: CacheRepository = CacheRepository()


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            AppViewModel(propertyRepository, addressRepository, photoRepository, agentRepository,
                typeRepository, poiRepository, propertyPoiJoinRepository) as T
        } else if (modelClass.isAssignableFrom(ListAndDetailViewModel::class.java)) {
            ListAndDetailViewModel(propertyRepository, addressRepository, photoRepository,
                agentRepository, typeRepository, poiRepository, locationRepository) as T
        } else if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
            EditViewModel(propertyRepository, addressRepository, photoRepository, agentRepository,
                typeRepository, poiRepository, propertyPoiJoinRepository, locationRepository, cacheRepository) as T
        } else if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            MapViewModel(locationRepository) as T
        } else if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            SearchViewModel(addressRepository, photoRepository) as T
        } else if (modelClass.isAssignableFrom(LoanViewModel::class.java)) {
            LoanViewModel() as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

}