package com.aquaero.realestatemanager.repository

import android.content.Context
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.database.dao.PoiDao
import com.aquaero.realestatemanager.model.Poi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PoiRepository(private val poiDao: PoiDao) {

    private val context: Context by lazy { ApplicationRoot.getContext() }


    /** Room: Database CRUD **/

    suspend fun upsertPoiInRoom(poi: Poi) {
        withContext(Dispatchers.IO) {
            poiDao.upsertPoi(poi)
        }
    }

    suspend fun deletePoiFromRoom(poi: Poi) {
        withContext(Dispatchers.IO) {
            poiDao.deletePoi(poi)
        }
    }

    /*suspend*/ fun getPoiFromRoom(poiId: String): Flow<Poi> {
        /*
        return withContext(Dispatchers.IO) {
            poiDao.getPoi(poiId)
        }
        */
        return poiDao.getPoi(poiId)
    }

    /*suspend*/ fun getPoisFromRoom(): Flow<MutableList<Poi>> {
        /*
        return withContext(Dispatchers.IO) {
            poiDao.getPois()
        }
        */
        return poiDao.getPois()
    }

    /*suspend*/ fun getPoisOrderedByIdFromRoom(): Flow<MutableList<Poi>> {
        /*
        return withContext(Dispatchers.IO) {
            poiDao.getPoisOrderedById()
        }
        */
        return poiDao.getPoisOrderedById()
    }

    /***/


    fun poiFromId(pois: MutableList<Poi>, poiId: String): Poi {
        return pois.first { it.poiId == poiId }
    }

}