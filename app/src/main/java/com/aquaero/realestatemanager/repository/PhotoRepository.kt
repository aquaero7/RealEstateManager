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
import com.aquaero.realestatemanager.model.Photo
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects

class PhotoRepository() {

    private val context: Context by lazy { ApplicationRoot.getContext() }

    fun photoFromId(photos: MutableList<Photo>, photoId: Long): Photo {
        return photos.find { it.phId == photoId }!!
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

        // val uri = FileProvider.getUriForFile(
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
        val permissionCheckResult =
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
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








    //
    /**
     * FAKE PHOTOS
     */

    val fakePhotos = listOf(
        Photo(
            phId = 0,
            phUri = Uri.EMPTY,  //"U0000000"
            phLabel = "L0000000"
        ),
        Photo(
            phId = -1,
            phUri = Uri.EMPTY,  // "U1111111"
            phLabel = "L1111111"
        ),
        Photo(
            phId = -2,
            phUri = Uri.EMPTY,  // "U2222222"
            phLabel = "L2222222"
        ),
        Photo(
            phId = -3,
            phUri = Uri.EMPTY,  // "U3333333"
            phLabel = "L3333333"
        ),
        Photo(
            phId = -4,
            phUri = Uri.EMPTY,  // "U4444444"
            phLabel = "L4444444"
        ),
        Photo(
            phId = -5,
            phUri = Uri.EMPTY,  // "U5555555"
            phLabel = "L5555555"
        ),
        Photo(
            phId = -6,
            phUri = Uri.EMPTY,  // "U6666666"
            phLabel = "L6666666"
        ),
        Photo(
            phId = -7,
            phUri = Uri.EMPTY,  // "U7777777"
            phLabel = "L7777777"
        )
    )
    //

}

