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

    private val file = context.createImageFile()

    fun getPhotoUri(): Uri {
        // val uri = FileProvider.getUriForFile(
        return FileProvider.getUriForFile(
            Objects.requireNonNull(context),
            BuildConfig.APPLICATION_ID + ".provider", file
        )
    }

    fun onShootPhotoMenuItemClickTest(
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

    fun onSelectPhotoMenuItemClickTest(
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
            0,
            "U0000000",
            "L0000000"
        ),
        Photo(
            -1,
            "U1111111",
            "L1111111"
        ),
        Photo(
            -2,
            "U2222222",
            "L2222222"
        ),
        Photo(
            -3,
            "U3333333",
            "L3333333"
        ),
        Photo(
            -4,
            "U4444444",
            "L4444444"
        ),
        Photo(
            -5,
            "U5555555",
            "L5555555"
        ),
        Photo(
            -6,
            "U6666666",
            "L6666666"
        ),
        Photo(
            -7,
            "U7777777",
            "L7777777"
        )
    )
    //

}

