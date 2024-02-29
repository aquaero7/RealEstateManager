package com.aquaero.realestatemanager.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.aquaero.realestatemanager.FINE_LOC_PERMS
import com.aquaero.realestatemanager.LOCATION_PERMISSIONS
import com.aquaero.realestatemanager.LOC_PERMS_SETTINGS
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.ui.component.app.AppDialog
import com.aquaero.realestatemanager.ui.component.map_screen.MapScreenNoMap
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionsScreen(
    onOpenAppSettings: () -> Unit,
    onPermissionsGranted: () -> Unit,
    popBackStack: () -> Unit,
) {
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
            AppDialog(
                subject = FINE_LOC_PERMS,
                title = stringResource(id = R.string.loc_perms_dialog_title),
                text = stringResource(id = R.string.loc_perm_fine_rq),
                okLabel = stringResource(id = R.string.allow),
                onOkClick = { locationPermissionsState.launchMultiplePermissionRequest() },
                cnlLabel = stringResource(id = R.string.dismiss),
                onCnlClick = { onPermissionsGranted() },
                onDismiss = { onPermissionsGranted() }
            )

        } else if (locationPermissionsState.shouldShowRationale) {
            // Both location permissions have been revoked and
            // the user is requested to proceed to the app settings
            AppDialog(
                subject = LOC_PERMS_SETTINGS,
                title = stringResource(id = R.string.loc_perms_dialog_title),
                text = stringResource(id = R.string.loc_perms_denied),
                okLabel = stringResource(id = R.string.allow),
                onOkClick = { onOpenAppSettings() },
                onDismiss = { onOpenAppSettings() }
            )

        } else {
            // Both location permissions have been revoked and
            // the user denied to grant permissions in the app settings.
            // So the map isn't available.
            MapScreenNoMap(
                infoText = stringResource(id = R.string.loc_perms_revoked),
                popBackStack = popBackStack,
            )
        }
    }

}



