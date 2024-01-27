package com.aquaero.realestatemanager.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.aquaero.realestatemanager.model.Property
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun prepopulateWithProperties(properties: List<Property>)

    @Upsert     // Insert ou update (if primary key already exists)
    suspend fun upsertProperty(property: Property)

    /* Commented because a property should not be deletable
    @Delete     // We use DAO annotations where SQL query is not required
    suspend fun deleteProperty(property: Property)
    */

    @Query("SELECT * FROM property WHERE propertyId = :pId")
    fun getProperty(pId: Long): Flow<Property>

    @Query("SELECT * FROM property")
    fun getProperties(): Flow<MutableList<Property>>

    @Query("SELECT * FROM property ORDER BY propertyId ASC")
    fun getPropertiesOrderedById(): Flow<MutableList<Property>>

    @Query("SELECT * FROM property ORDER BY registrationDate DESC")
    fun getPropertiesOrderedByRegistrationDate(): Flow<MutableList<Property>>

}