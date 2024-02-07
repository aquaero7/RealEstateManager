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
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.BuildConfig
import com.aquaero.realestatemanager.database.dao.PhotoDao
import com.aquaero.realestatemanager.model.NO_PHOTO
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

    private val context: Context by lazy { ApplicationRoot.getContext() }


    /** Room: Database CRUD */

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

    /***/


    fun photoFromId(photoId: Long, photos: MutableList<Photo>): Photo {
        return photos.first { it.photoId == photoId }
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

    fun getPhotoUri(): Uri {
        val file = context.createImageFile()
        return FileProvider.getUriForFile(
            Objects.requireNonNull(context),
            BuildConfig.APPLICATION_ID + ".provider", file
        )
    }

    fun onShootPhotoMenuItemClick(
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
        val itemPhotos = photos.filter { it.propertyId == propertyId }.toMutableList()
        return itemPhotos.ifEmpty { mutableListOf(NO_PHOTO) }
    }



    /** FAKE PHOTOS */
    /*
    val fakePhotos = listOf(
        Photo(
            photoId = -1,
            uri = "",  // "U1111111"
            label = "L1111111",
            propertyId = -1,
        ),
        Photo(
            photoId = -2,
            uri = "",  // "U2222222"
            label = "L2222222",
            propertyId = -2,
        ),
        Photo(
            photoId = -3,
            uri = "",  // "U3333333"
            label = "L3333333",
            propertyId = -3,
        ),
        Photo(
            photoId = -4,
            uri = "",  // "U4444444"
            label = "L4444444",
            propertyId = -4,
        ),
        Photo(
            photoId = -5,
            uri = "",  // "U5555555"
            label = "L5555555",
            propertyId = -5,
        ),
        Photo(
            photoId = -6,
            uri = "",  // "U6666666"
            label = "L6666666",
            propertyId = -6,
        ),
        Photo(
            photoId = -7,
            uri = "",  // "U7777777"
            label = "L7777777",
            propertyId = -7,
        ),
        Photo(
            photoId = -8,
            uri = "",  // "U8888888"
            label = "L1888888",
            propertyId = -1,
        ),
        Photo(
            photoId = -9,
            uri = "",  // "U9999999"
            label = "L2999999",
            propertyId = -2,
        ),

    )
    */


}

