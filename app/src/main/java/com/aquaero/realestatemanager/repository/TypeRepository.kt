package com.aquaero.realestatemanager.repository

import android.annotation.SuppressLint
import android.content.Context
import com.aquaero.realestatemanager.database.dao.TypeDao
import com.aquaero.realestatemanager.model.Type
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    @SuppressLint("DiscouragedApi")
    fun getStringTypesFromRoom(context: Context): Flow<MutableList<String>> {
        return typeDao.getTypes().map { types ->
            types.map {
                val resourceId =
                    context.resources.getIdentifier(it.typeId, "string", context.packageName)
                if (resourceId != 0) context.getString(resourceId) else it.typeId
            }.toMutableList()
        }
    }

    @SuppressLint("DiscouragedApi")
    fun getStringTypesOrderedByIdFromRoom(context: Context): Flow<MutableList<String>> {
        return typeDao.getTypesOrderedById().map { types ->
            types.map {
                val resourceId =
                    context.resources.getIdentifier(it.typeId, "string", context.packageName)
                if (resourceId != 0) context.getString(resourceId) else it.typeId
            }.toMutableList()
        }
    }

    /**/


    fun typeFromId(typeId: String, types: MutableList<Type>): Type? {
        return types.find {it.typeId == typeId}
    }

    fun stringType(typeId: String, types: MutableList<Type>, stringTypes: MutableList<String>): String {
        val typeIndex = types.indexOf(types.find { it.typeId == typeId } ?: 0)
        return if (typeIndex != -1 && stringTypes.isNotEmpty()) stringTypes.elementAt(typeIndex) else typeId
    }

}