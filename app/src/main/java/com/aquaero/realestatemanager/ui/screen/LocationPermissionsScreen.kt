package com.aquaero.realestatemanager.ui.screen

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.aquaero.realestatemanager.LOCATION_PERMISSIONS
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.ui.component.location_permissions_screen.PermissionDialog
import com.aquaero.realestatemanager.ui.component.map_screen.MapScreenNoMap
import com.aquaero.realestatemanager.viewmodel.AppViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionsScreen(
    onOpenAppSettings: () -> Unit,
    onPermissionsGranted: () -> Unit,
) {
    /**
     * Method 1 using Google accompanist-permissions experimental API
     */
    /*
    // Set the list of requested permissions
    val locationPermissionsState = rememberMultiplePermissionsState(LOCATION_PERMISSIONS)
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

    /** Do actions according to permissions state */

    if (locationPermissionsState.allPermissionsGranted) {
        // All permissions are accepted
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
            // Both location permissions have been revoked and
            // the user is requested to proceed to the app settings
            PermissionDialog(
                text = stringResource(id = R.string.loc_perms_denied),
                onOkClick = { onOpenAppSettings() },
                onCnlClick = null,
                onDismiss = { onOpenAppSettings() }
            )
        } else {
            // Both location permissions have been revoked and
            // the user denied to grant permissions. So the map isn't available.
            MapScreenNoMap()
        }
    }
    */

    /**
     * Method 2 using Android API
     */
    //
    var coarseOnly by remember { mutableStateOf(false) }
    var areRevoked by remember { mutableStateOf(false) }
    var areGranted by remember { mutableStateOf(false) }

    // Set permissions status according to their state
    val locationPermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            areGranted = permissions.values.reduce { accumulate, isPermissionGranted ->
                accumulate && isPermissionGranted
            }
            areRevoked = permissions.values.reduce { accumulate, isPermissionGranted ->
                !accumulate && !isPermissionGranted
            }
            coarseOnly = !areGranted && !areRevoked
        }
    )

    // Set permissions to be requested
    fun requestLocationPermissions() {
        locationPermissionsLauncher.launch(LOCATION_PERMISSIONS.toTypedArray())
    }

    // Request permissions inside lifecycle scope
    val lifeCycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifeCycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START && !areGranted && !areRevoked && !coarseOnly) {
                requestLocationPermissions()
            }
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose { lifeCycleOwner.lifecycle.removeObserver(observer) }
    })

    // Do actions according to permissions state
    if (areGranted) {
        onPermissionsGranted()
    } else if (coarseOnly) {
        // The user accepted the COARSE location permission, but not the FINE one.
        PermissionDialog(
            text = stringResource(id = R.string.loc_perm_fine_rq),
            onOkClick = { requestLocationPermissions() },
            onCnlClick = { onPermissionsGranted() },
            onDismiss = { onPermissionsGranted() }
        )
    } else if (areRevoked) {
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
        MapScreenNoMap()

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
    //

}



