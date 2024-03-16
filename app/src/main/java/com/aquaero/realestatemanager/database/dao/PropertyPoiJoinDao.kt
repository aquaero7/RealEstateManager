package com.aquaero.realestatemanager.database.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyPoiJoinDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun prepopulateWithPropertyPoiJoins(propertyPoiJoins: List<PropertyPoiJoin>)

    @Upsert     // Insert ou update (if primary key already exists)
    suspend fun upsertPropertyPoiJoin(propertyPoiJoin: PropertyPoiJoin)

    @Delete
    suspend fun deletePropertyPoiJoin(propertyPoiJoin: PropertyPoiJoin)

    @Upsert     // Insert ou update (if primary key already exists)
    suspend fun upsertPropertyPoiJoins(propertyPoiJoins: MutableList<PropertyPoiJoin>)

    @Delete
    suspend fun deletePropertyPoiJoins(propertyPoiJoins: MutableList<PropertyPoiJoin>)

    @Query("SELECT * FROM poi INNER JOIN property_poi_join ON poi.poiId = property_poi_join.poiId WHERE property_poi_join.propertyId = :propertyId")
    fun getPoisForProperty(propertyId: Long): Flow<MutableList<Poi>>

    @Query("SELECT * FROM property INNER JOIN property_poi_join ON property.propertyId = property_poi_join.propertyId WHERE property_poi_join.poiId = :poiId")
    fun getPropertiesForPoi(poiId: String): Flow<MutableList<Property>>

    @Query("SELECT * FROM property_poi_join")
    fun getPropertyPoiJoins(): Flow<MutableList<PropertyPoiJoin>>


    /*
     * ContentProvider
     */

    @Query("SELECT * FROM property_poi_join")
    fun getPropertyPoiJoinsWithCursor(): Cursor

}