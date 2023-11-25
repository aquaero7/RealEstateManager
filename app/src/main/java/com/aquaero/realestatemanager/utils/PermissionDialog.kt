package com.aquaero.realestatemanager.utils

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.aquaero.realestatemanager.R

@Composable
fun PermissionDialog(
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.loc_perms_dialog_title),
    text: String,
    okLabel: String = stringResource(id = R.string.allow),
    onOkClick: () -> Unit,
    cnlLabel: String = stringResource(id = R.string.dismiss),
    onCnlClick: (() -> Unit)?,
    onDismiss: () -> Unit,
) {
    if (onCnlClick != null) {
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
    } else {
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
    }

}
