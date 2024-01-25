package com.aquaero.realestatemanager.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.aquaero.realestatemanager.model.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun prepopulateWithPhotos(photos: List<Photo>)

    @Upsert     // Insert ou update (if primary key already exists)
    suspend fun upsertPhoto(photo: Photo)

    @Delete
    suspend fun deletePhoto(photo: Photo)

    @Query("SELECT * FROM photo WHERE photoId = :phId")
    fun getPhoto(phId: Long): Flow<Photo>

    @Query("SELECT * FROM photo")
    fun getPhotos(): Flow<MutableList<Photo>>

    @Query("SELECT * FROM photo ORDER BY photoId ASC")
    fun getPhotosOrderedById(): Flow<MutableList<Photo>>

    @Query("SELECT * FROM photo ORDER BY label ASC")
    fun getPhotosOrderedByLabel(): Flow<MutableList<Photo>>

}