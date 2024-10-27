package com.aquaero.realestatemanager.repository

import com.aquaero.realestatemanager.database.dao.TypeDao
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.model.TypeEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TypeRepository(private val typeDao: TypeDao) {


    /* Room: Database CRUD */

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

    fun getTypeFromRoom(typeId: String): Flow<Type> {
        return typeDao.getType(typeId)
    }

    fun getTypesFromRoom(): Flow<MutableList<Type>> {
        return typeDao.getTypes()
    }

    fun getTypesOrderedByIdFromRoom(): Flow<MutableList<Type>> {
        return typeDao.getTypesOrderedById()
    }

    /**/


    fun typeFromId(typeId: String, types: MutableList<Type>): Type? {
        return types.find {it.typeId == typeId}
    }

    fun stringType(typeId: String, types: MutableList<Type>, stringTypes: MutableList<String>): String {
        val type = types.find { it.typeId == typeId }
        val typeIndex = type?.let { types.indexOf(it) } ?: 0  // Index 0 should correspond to "Unassigned"
        return if (typeIndex != -1 && stringTypes.isNotEmpty() && stringTypes.size == types.size)
            stringTypes.elementAt(typeIndex) else TypeEnum.UNASSIGNED.key
    }

}