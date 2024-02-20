package com.aquaero.realestatemanager.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.aquaero.realestatemanager.BuildConfig
import com.aquaero.realestatemanager.database.dao.PhotoDao
import com.aquaero.realestatemanager.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects

class PhotoRepository(private val photoDao: PhotoDao) {


    /* Room: Database CRUD */

    suspend fun upsertPhotoInRoom(photo: Photo) {
        withContext(Dispatchers.IO) {
            photoDao.upsertPhoto(photo)
        }
    }

    suspend fun deletePhotoFromRoom(photo: Photo) {
        withContext(Dispatchers.IO) {
            photoDao.deletePhoto(photo)
        }
    }

    suspend fun upsertPhotosInRoom(photos: MutableList<Photo>) {
        withContext(Dispatchers.IO) {
            photoDao.upsertPhotos(photos)
        }
    }

    suspend fun deletePhotosFromRoom(photos: MutableList<Photo>) {
        withContext(Dispatchers.IO) {
            photoDao.deletePhotos(photos)
        }
    }

    fun getPhotoFromRoom(phId: Long): Flow<Photo> {
        return photoDao.getPhoto(phId)
    }

    fun getPhotosFromRoom(): Flow<MutableList<Photo>> {
        return photoDao.getPhotos()
    }

    fun getPhotosOrderedByIdFromRoom(): Flow<MutableList<Photo>> {
        return photoDao.getPhotosOrderedById()
    }

    fun getPhotosOrderedByLabelFromRoom(): Flow<MutableList<Photo>> {
        return photoDao.getPhotosOrderedByLabel()
    }

    /**/


    fun photoFromId(photoId: Long, photos: MutableList<Photo>): Photo? {
        return photos.find { it.photoId == photoId }
    }

    private fun Context.createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.FRANCE).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        return File.createTempFile(
            imageFileName,      // Prefix
            ".jpg",       // Suffix
            externalCacheDir    // Directory
        )
    }

    fun getPhotoUri(context: Context): Uri {
        return FileProvider.getUriForFile(
            Objects.requireNonNull(context),
            BuildConfig.APPLICATION_ID + ".provider",
            context.createImageFile()
        )
    }

    fun onShootPhotoMenuItemClick(
        context: Context,
        uri: Uri,
        cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
        permissionLauncher: ManagedActivityResultLauncher<String, Boolean>
    ) {
        val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            // Launch camera
            cameraLauncher.launch(uri)
        } else {
            // Request a permission
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun onSelectPhotoMenuItemClick(
        pickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
    ) {
        pickerLauncher.launch(
            PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    fun itemPhotos(propertyId: Long, photos: MutableList<Photo>): MutableList<Photo> {
        return photos.filter { it.propertyId == propertyId }.toMutableList()
    }

}

