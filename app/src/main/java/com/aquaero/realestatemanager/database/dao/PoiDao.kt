package com.aquaero.realestatemanager.database.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.aquaero.realestatemanager.model.Poi
import kotlinx.coroutines.flow.Flow

@Dao
interface PoiDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun prepopulateWithPois(pois: List<Poi>)

    @Upsert     // Insert ou update (if primary key already exists)
    suspend fun upsertPoi(poi: Poi)

    @Delete
    suspend fun deletePoi(poi: Poi)

    @Query("SELECT * FROM poi WHERE poiId = :poiId")
    fun getPoi(poiId: String): Flow<Poi>

    @Query("SELECT * FROM poi")
    fun getPois(): Flow<MutableList<Poi>>

    @Query("SELECT * FROM poi ORDER BY poiId ASC")
    fun getPoisOrderedById(): Flow<MutableList<Poi>>


    /*
     * ContentProvider
     */

    @Query("SELECT * FROM poi")
    fun getPoisWithCursor(): Cursor

}