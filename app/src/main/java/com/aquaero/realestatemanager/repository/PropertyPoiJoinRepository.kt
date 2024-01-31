package com.aquaero.realestatemanager.repository

import android.content.Context
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.database.dao.PropertyPoiJoinDao
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PropertyPoiJoinRepository(private val propertyPoiJoinDao: PropertyPoiJoinDao) {

    private val context: Context by lazy { ApplicationRoot.getContext() }


    /** Room: Database CRUD **/

    suspend fun upsertPropertyPoiJoinInRoom(propertyPoiJoin: PropertyPoiJoin) {
        withContext(Dispatchers.IO) {
            propertyPoiJoinDao.upsertPropertyPoiJoin(propertyPoiJoin)
        }
    }

    suspend fun deletePropertyPoiJoinFromRoom(propertyPoiJoin: PropertyPoiJoin) {
        withContext(Dispatchers.IO) {
            propertyPoiJoinDao.deletePropertyPoiJoin(propertyPoiJoin)
        }
    }

    /*suspend*/ fun getPoisForPropertyFromRoom(pId: Long): Flow<MutableList<Poi>> {
        /*
        return withContext(Dispatchers.IO) {
            propertyPoiJoinDao.getPoisForProperty(pId)
        }
        */
        return propertyPoiJoinDao.getPoisForProperty(pId)
    }

    /*suspend*/ fun getPropertiesForPoiFromRoom(poiId: String): Flow<MutableList<Property>> {
        /*
        return withContext(Dispatchers.IO) {
            propertyPoiJoinDao.getPropertiesForPoi(poiId)
        }
        */
        return propertyPoiJoinDao.getPropertiesForPoi(poiId)
    }

    /*suspend*/ fun getPropertyPoiJoinsFromRoom(): Flow<MutableList<PropertyPoiJoin>> {
        /*
        return withContext(Dispatchers.IO) {
            propertyPoiJoinDao.getPropertyPoiJoins()
        }
        */
        return propertyPoiJoinDao.getPropertyPoiJoins()
    }

    /***/


}