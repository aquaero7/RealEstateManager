package com.aquaero.realestatemanager.ui.component.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Photo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomActionsSheet(
    onDismissSheet: () -> Unit,
    photo: Photo? = null,
    onEditPhotoMenuItemClickGetPhoto: ((Photo) -> Unit)? = null,
    onDeletePhotoMenuItemClick: ((Long) -> Unit)? = null,
    onShootPhotoMenuItemClick: (() -> Unit)? = null,
    onSelectPhotoMenuItemClick: (() -> Unit)? = null,
) {
    ModalBottomSheet(onDismissRequest = onDismissSheet) {
        // Action = Edit or delete photo
        if (photo != null && onEditPhotoMenuItemClickGetPhoto != null && onDeletePhotoMenuItemClick != null) {
            // Action = Edit a photo
            ListItem(
                modifier = Modifier
                    .clickable(
                        enabled = true,
                        onClick = {
                            onEditPhotoMenuItemClickGetPhoto(photo)
                            onDismissSheet()
                        }
                    ),
                headlineContent = {
                    Text(
                        text = "${stringResource(id = R.string.edit_photo)} ${photo.phLabel}"
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Label,
                        contentDescription = stringResource(id = R.string.cd_menu_item_edit_photo),
                    )
                }
            )
            // Action = Delete a photo
            ListItem(
                modifier = Modifier
                    .clickable(
                        enabled = true,
                        onClick = {
                            onDeletePhotoMenuItemClick(photo.phId)
                            onDismissSheet()
                        }
                    ),
                headlineContent = {
                    Text(
                        text = "${stringResource(id = R.string.delete_photo)} ${photo.phLabel}"
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.cd_menu_item_delete_photo),
                    )
                }
            )
        }
        // Action = Add photo
        if (onShootPhotoMenuItemClick != null && onSelectPhotoMenuItemClick != null) {
            // Action = Shoot a photo
            ListItem(
                modifier = Modifier
                    .clickable(
                        enabled = true,
                        onClick = {
                            onShootPhotoMenuItemClick()
                            onDismissSheet()
                        }
                    ),
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.shoot_photo)
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = stringResource(id = R.string.cd_menu_item_shoot_photo)
                    )
                }
            )
            // Action = Pick a photo
            ListItem(
                modifier = Modifier
                    .clickable(
                        enabled = true,
                        onClick = {
                            onSelectPhotoMenuItemClick()
                            onDismissSheet()
                        }
                    ),
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.select_photo)
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = stringResource(id = R.string.cd_menu_item_select_photo)
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}