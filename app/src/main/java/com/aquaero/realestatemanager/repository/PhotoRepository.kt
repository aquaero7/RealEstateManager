package com.aquaero.realestatemanager.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
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
        camPermLauncher: ManagedActivityResultLauncher<String, Boolean>
    ) {
        val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            // Launch camera
            cameraLauncher.launch(uri)
        } else {
            // Request a permission
            camPermLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun onSelectPhotoMenuItemClick(
        pickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
    ) {
        pickerLauncher.launch(
            PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    fun saveToInternalStorage(context: Context, uri: Uri): Uri {
        // Creates photo name from that provided by Picker uri
        val stringUri = uri.toString()
        val fileNameIndex = stringUri.lastIndexOf("/")
        val fullFileName = if (fileNameIndex != -1) stringUri.substring(fileNameIndex) else stringUri
        val extensionIndex = fullFileName.lastIndexOf(".")
        val fileName = if (extensionIndex != -1) {
            "${fullFileName.substring(0, extensionIndex)}.jpg"
        } else {
            "${fullFileName}.jpg"
        }

        // Creates subfolder 'Pictures' in internal storage folder
        val picturesDir = File(context.filesDir, "Pictures")
        if (!picturesDir.exists()) picturesDir.mkdir()

        // Creates full path to the photo
        val outputFile = File(picturesDir, fileName)    // To target the subfolder
//        val outputFile = context.filesDir.resolve(fileName) // To target main internal storage folder

        // Makes copy to the target
        val inputStream = context.contentResolver.openInputStream(uri)
        inputStream.use { input ->
            outputFile.outputStream().use { output ->
                input?.copyTo(output)
            }
        }
        Log.w("PhotoRepository", "uri = $uri")
        Log.w("PhotoRepository", "pickerUri = ${outputFile.toUri()}")
        return outputFile.toUri()
    }

    fun itemPhotos(propertyId: Long, photos: MutableList<Photo>): MutableList<Photo> {
        return photos.filter { it.propertyId == propertyId }.toMutableList()
    }

}

