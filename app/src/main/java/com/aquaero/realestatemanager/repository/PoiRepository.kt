package com.aquaero.realestatemanager.repository

import com.aquaero.realestatemanager.database.dao.PoiDao
import com.aquaero.realestatemanager.model.Poi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PoiRepository(private val poiDao: PoiDao) {


    /* Room: Database CRUD */

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

    fun getPoiFromRoom(poiId: String): Flow<Poi> {
        return poiDao.getPoi(poiId)
    }

    fun getPoisFromRoom(): Flow<MutableList<Poi>> {
        return poiDao.getPois()
    }

    fun getPoisOrderedByIdFromRoom(): Flow<MutableList<Poi>> {
        return poiDao.getPoisOrderedById()
    }

    /**/


    fun poiFromId(poiId: String, pois: MutableList<Poi>): Poi? {
        return pois.find { it.poiId == poiId }
    }

}