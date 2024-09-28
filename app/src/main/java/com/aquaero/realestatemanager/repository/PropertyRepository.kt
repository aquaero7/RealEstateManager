package com.aquaero.realestatemanager.repository

import com.aquaero.realestatemanager.database.dao.PropertyDao
import com.aquaero.realestatemanager.model.Property
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PropertyRepository(private val propertyDao: PropertyDao) {


    /* Room: Database CRUD */

    suspend fun upsertPropertyInRoom(property: Property): Long {
        return withContext(IO) {
            propertyDao.upsertProperty(property)
        }
    }

    fun getPropertyFromRoom(pId: Long): Flow<Property> {
        return propertyDao.getProperty(pId)
    }

    fun getPropertiesFromRoom(): Flow<MutableList<Property>> {
        return propertyDao.getProperties()
    }

    fun getPropertiesOrderedByIdFromRoom(): Flow<MutableList<Property>> {
        return propertyDao.getPropertiesOrderedById()
    }

    fun getPropertiesOrderedByRegistrationDateFromRoom(): Flow<MutableList<Property>> {
        return propertyDao.getPropertiesOrderedByRegistrationDate()
    }

    /**/


    fun propertyFromId(propertyId: Long, properties: MutableList<Property>): Property? {
        return properties.find { it.propertyId == propertyId }
    }

}
