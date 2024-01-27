package com.aquaero.realestatemanager.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.aquaero.realestatemanager.model.Type
import kotlinx.coroutines.flow.Flow

@Dao
interface TypeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun prepopulateWithTypes(types: List<Type>)

    @Upsert     // Insert ou update (if primary key already exists)
    suspend fun upsertType(type: Type)

    @Delete
    suspend fun deleteType(type: Type)

    @Query("SELECT * FROM type WHERE typeId = :typeId")
    fun getType(typeId: String): Flow<Type>

    @Query("SELECT * FROM type")
    fun getTypes(): Flow<MutableList<Type>>

    @Query("SELECT * FROM type ORDER BY typeId ASC")
    fun getTypesOrderedById(): Flow<MutableList<Type>>

}