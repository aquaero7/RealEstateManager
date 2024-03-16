package com.aquaero.realestatemanager.database.dao

import android.database.Cursor
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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun prepopulateWithPhotos(photos: List<Photo>)

    @Upsert     // Insert ou update (if primary key already exists)
    suspend fun upsertPhoto(photo: Photo)

    @Delete
    suspend fun deletePhoto(photo: Photo)

    @Query("SELECT * FROM photo WHERE photoId = :phId")
    fun getPhoto(phId: Long): Flow<Photo>

    @Upsert     // Insert ou update (if primary key already exists)
    suspend fun upsertPhotos(photos: MutableList<Photo>)

    @Delete
    suspend fun deletePhotos(photos: MutableList<Photo>)

    @Query("SELECT * FROM photo")
    fun getPhotos(): Flow<MutableList<Photo>>

    @Query("SELECT * FROM photo ORDER BY photoId ASC")
    fun getPhotosOrderedById(): Flow<MutableList<Photo>>

    @Query("SELECT * FROM photo ORDER BY label ASC")
    fun getPhotosOrderedByLabel(): Flow<MutableList<Photo>>


    /*
     * ContentProvider
     */

    @Query("SELECT * FROM photo")
    fun getPhotosWithCursor(): Cursor

}