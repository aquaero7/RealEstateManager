package com.aquaero.realestatemanager.repository

import android.content.Context
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.database.dao.TypeDao
import com.aquaero.realestatemanager.model.Type
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TypeRepository(private val typeDao: TypeDao) {

    private val context: Context by lazy { ApplicationRoot.getContext() }


    /** Room: Database CRUD **/

    suspend fun upsertTypeInRoom(type: Type) {
        withContext(Dispatchers.IO) {
            typeDao.upsertType(type)
        }
    }

    suspend fun deleteTypeFromRoom(type: Type) {
        withContext(Dispatchers.IO) {
            typeDao.deleteType(type)
        }
    }

    suspend fun getTypeFromRoom(typeId: String): Flow<Type> {
        return withContext(Dispatchers.IO) {
            typeDao.getType(typeId)
        }
    }

    suspend fun getTypesFromRoom(): Flow<MutableList<Type>> {
        return withContext(Dispatchers.IO) {
            typeDao.getTypes()
        }
    }

    suspend fun getTypesOrderedByIdFromRoom(): Flow<MutableList<Type>> {
        return withContext(Dispatchers.IO) {
            typeDao.getTypesOrderedById()
        }
    }

    /***/

}