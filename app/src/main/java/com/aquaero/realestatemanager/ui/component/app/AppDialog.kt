package com.aquaero.realestatemanager.ui.component.app

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aquaero.realestatemanager.FINE_LOC_PERMS
import com.aquaero.realestatemanager.LOC_PERMS_SETTINGS
import com.aquaero.realestatemanager.PHOTO_DELETION

@Composable
fun AppDialog(
    modifier: Modifier = Modifier,
    subject: String,
    title: String,
    text: String,
    okLabel: String,
    onOkClick: () -> Unit,
    cnlLabel: String = "",
    onCnlClick: (() -> Unit) = {},
    onDismiss: () -> Unit,
) {
    when (subject) {

        FINE_LOC_PERMS -> // Request fine location permissions
            AlertDialog(
                modifier = modifier,
                title = { Text(text = title) },
                text = { Text(text = text) },
                confirmButton = {
                    Button(
                        onClick = onOkClick
                    ) {
                        Text(text = okLabel)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = onCnlClick,
                    ) {
                        Text(text = cnlLabel)
                    }
                },
                onDismissRequest = onDismiss,
            )

        LOC_PERMS_SETTINGS -> // Request to proceed to app settings to grant permissions
            AlertDialog(
                modifier = modifier,
                title = { Text(text = title) },
                text = { Text(text = text) },
                confirmButton = {
                    Button(
                        onClick = onOkClick
                    ) {
                        Text(text = okLabel)
                    }
                },
                onDismissRequest = onDismiss,
            )

        PHOTO_DELETION -> // Request photo deletion confirmation
            AlertDialog(
                modifier = modifier,
                title = { Text(text = title) },
                text = { Text(text = text) },
                confirmButton = {
                    Button(
                        onClick = onOkClick
                    ) {
                        Text(text = okLabel)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = onCnlClick,
                    ) {
                        Text(text = cnlLabel)
                    }
                },
                onDismissRequest = onDismiss,
            )

    }

}