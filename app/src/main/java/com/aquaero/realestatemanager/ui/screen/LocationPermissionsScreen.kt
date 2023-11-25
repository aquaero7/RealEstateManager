package com.aquaero.realestatemanager.ui.screen

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.utils.PermissionDialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LocationPermissionsScreen(
    onOpenAppSettings: () -> Unit,
    onPermissionsGranted: () -> Unit,
) {
    /**
     * Method 1 using Google accompanist-permissions experimental API
     */
    //
    // Set permissions to be requested
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    )
    // Request permissions inside lifecycle scope
    val lifeCycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifeCycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    locationPermissionsState.launchMultiplePermissionRequest()
                }
                else -> {}
            }
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose { lifeCycleOwner.lifecycle.removeObserver(observer) }
    })

    // Do actions according to permissions state
    if (locationPermissionsState.allPermissionsGranted) {
        Text(stringResource(id = R.string.loc_perms_granted))
        onPermissionsGranted()

    } else {
        val allPermissionsRevoked =
            locationPermissionsState.permissions.size == locationPermissionsState.revokedPermissions.size

        if (!allPermissionsRevoked) {
            // The user accepted the COARSE location permission, but not the FINE one.
            PermissionDialog(
                text = stringResource(id = R.string.loc_perm_fine_rq),
                onOkClick = { locationPermissionsState.launchMultiplePermissionRequest() },
                onCnlClick = { onPermissionsGranted() },
                onDismiss = { onPermissionsGranted() }
            )
        } else if (locationPermissionsState.shouldShowRationale) {
            // Both location permissions have been denied
            PermissionDialog(
                text = stringResource(id = R.string.loc_perms_denied),
                onOkClick = { onOpenAppSettings() },
                onCnlClick = null,
                onDismiss = { onOpenAppSettings() }
            )
        } else {
            EmptyMapScreen()

            /* TODO : To be removed cause handled in DisposableEffect
            // First time the user sees this feature or the user doesn't want to be asked again
            PermissionDialog(
                text = stringResource(id = R.string.loc_perms_rq),
                onOkClick = { locationPermissionsState.launchMultiplePermissionRequest() },
                onCnlClick = null,
                onDismiss = { locationPermissionsState.launchMultiplePermissionRequest() }
            )
            */
        }
    }
    //

    /**
     * Method 2 using Android API
     */
    /*
    var coarsePerm by remember { mutableStateOf(false) }
    var areDenied by remember { mutableStateOf(false) }
    var areGranted by remember { mutableStateOf(false) }

    // Set permissions status according to their state
    val locationPermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            areGranted = permissions.values.reduce { accumulate, isPermissionGranted ->
                accumulate && isPermissionGranted
            }
            areDenied = permissions.values.reduce { accumulate, isPermissionGranted ->
                !accumulate && !isPermissionGranted
            }
            coarsePerm = !areGranted && !areDenied
        }
    )

    // Set permissions to be requested
    fun requestLocationPermissions() {
        locationPermissionsLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )
    }

    // Request permissions inside lifecycle scope
    val lifeCycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifeCycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START && !areGranted && !areDenied && !coarsePerm) {
                requestLocationPermissions()
            }
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose { lifeCycleOwner.lifecycle.removeObserver(observer) }
    })

    // Do actions according to permissions state
    if (areGranted) {
        onPermissionsGranted()
    } else if (coarsePerm) {
        // The user accepted the COARSE location permission, but not the FINE one.
        PermissionDialog(
            text = stringResource(id = R.string.loc_perm_fine_rq),
            onOkClick = { requestLocationPermissions() },
            onCnlClick = { onPermissionsGranted() },
            onDismiss = { onPermissionsGranted() }
        )
    } else if (areDenied) {
        // Both location permissions have been denied
        PermissionDialog(
            text = stringResource(id = R.string.loc_perms_denied),
            onOkClick = {
                onOpenAppSettings()
                requestLocationPermissions()
            },
            onCnlClick = null,
            onDismiss = {
                onOpenAppSettings()
                requestLocationPermissions()
            }
        )
    } else {
        EmptyMapScreen()

        /* TODO : To be removed cause handled in DisposableEffect
        // First time the user sees this feature or the user doesn't want to be asked again
        PermissionDialog(
            text = stringResource(id = R.string.loc_perms_rq),
            onOkClick = { requestLocationPermissions() },
            onCnlClick = null,
            onDismiss = { requestLocationPermissions() }
        )
        */
    }
    */

}



