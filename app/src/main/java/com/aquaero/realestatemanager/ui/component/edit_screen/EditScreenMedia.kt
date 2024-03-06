package com.aquaero.realestatemanager.ui.component.edit_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaero.realestatemanager.PHOTO_DELETION
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.NO_PHOTO
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.ui.component.app.AppDialog
import com.aquaero.realestatemanager.ui.component.app.BottomActionsSheet
import com.aquaero.realestatemanager.ui.component.app.PhotosLazyRow

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("NewApi")
@Composable
fun EditScreenMedia(
    onShootPhotoMenuItemClick: () -> Unit,
    onSelectPhotoMenuItemClick: () -> Unit,
    buttonAddPhotoEnabled: Boolean,
    onCancelPhotoEditionButtonClick: () -> Unit,
    onSavePhotoButtonClick: (String) -> Unit,
    onEditPhotoMenuItemClick: (Photo) -> Unit,
    onPhotoDeletionConfirmation: (Long) -> Unit,
    painter: Painter,
    photos: MutableList<Photo>,
) {
    var addPhoto by rememberSaveable { mutableStateOf(false) }
    val haptics = LocalHapticFeedback.current
    val lineColor = MaterialTheme.colorScheme.onBackground
    var photoLabel by remember { mutableStateOf("") }
    val onEditPhotoMenuItemClickGetPhoto: (Photo) -> Unit = { photo ->
        photoLabel = photo.label ?: ""
        onEditPhotoMenuItemClick(photo)
    }
    var displayPhotoDeletionDialog by remember { mutableStateOf(false) }
    var photoToDelete by remember { mutableStateOf<Photo?>(null) }
    val onDismiss: () -> Unit = { displayPhotoDeletionDialog = false }
    val onOkClick: () -> Unit = {
        onPhotoDeletionConfirmation(photoToDelete!!.photoId)
        onDismiss()
    }
    fun showDeletionConfirmationDialog(photo: Photo) {
        photoToDelete = photo
        displayPhotoDeletionDialog = true
    }


    if (displayPhotoDeletionDialog) {
        val label =
            if (photoToDelete!!.label.isNullOrEmpty()) stringResource(id = R.string.no_label) else "\"${photoToDelete!!.label}\""

        AppDialog(
            subject = PHOTO_DELETION,
            title = stringResource(id = R.string.photo_deletion_dialog_title),
            text = (stringResource(id = R.string.photo_deletion_dialog_text, label)),
            okLabel = stringResource(id = R.string.confirm),
            onOkClick = onOkClick,
            cnlLabel = stringResource(id = R.string.abort),
            onCnlClick = onDismiss,
            onDismiss = onDismiss,
        )
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .drawWithContent {
                drawContent()
                drawLine(
                    color = lineColor,
                    start = Offset(0F, size.height - 8F),
                    end = Offset(size.width - 0F, size.height - 8F),
                    strokeWidth = 2F
                )
            }
            .background(color = MaterialTheme.colorScheme.surfaceVariant),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {

        // Photos list
        PhotosLazyRow(
            titleFontSize = 14.sp,
            photos = photos.ifEmpty { mutableListOf(NO_PHOTO) },
            longClickPhotoEnabled = photos.isNotEmpty(),
            onEditPhotoMenuItemClickGetPhoto = onEditPhotoMenuItemClickGetPhoto,
            onDeletePhotoMenuItemClick = { photo -> showDeletionConfirmationDialog(photo = photo) },
        )

        // Photo to add
        val photoSize = 120.dp
        val rowHeight = photoSize + 20.dp
        Row(
            modifier = Modifier.height(rowHeight),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(4.dp))

            // Image
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth(),
            ) {
                Image(
                    modifier = Modifier
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.onSurface)
                        .size(photoSize)
                        .padding(4.dp)
                        .background(color = MaterialTheme.colorScheme.surfaceVariant)
                        .alpha(1F)
                        .combinedClickable(enabled = true, onClick = {
                            haptics.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.LongPress)
                            addPhoto = true
                        }),
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    colorFilter = if (!buttonAddPhotoEnabled) ColorFilter.tint(color = MaterialTheme.colorScheme.tertiary) else null
                )
            }

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Description field
                EditScreenTextFieldItem(
                    labelText = stringResource(id = R.string.photo_label),
                    placeHolderText = stringResource(id = R.string.photo_label),
                    icon = Icons.Default.Description,
                    iconCD = stringResource(id = R.string.cd_photo_label),
                    onValueChange = { photoLabel = it },
                    itemText = photoLabel,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Button to cancel photo creation or edition
                    Button(
                        modifier = Modifier
                            .width(112.dp)
                            .alpha(if (buttonAddPhotoEnabled) 1F else 0.5F),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        enabled = buttonAddPhotoEnabled,
                        onClick = {
                            onCancelPhotoEditionButtonClick()
                            photoLabel = ""
                        },
                    ) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = Icons.Default.Cancel,
                            contentDescription = stringResource(id = R.string.cancel),
                            tint = MaterialTheme.colorScheme.tertiary,
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            text = stringResource(id = R.string.cancel),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                    }

                    // Button to valid photo creation or edition
                    Button(
                        modifier = Modifier
                            .width(112.dp)
                            .alpha(if (buttonAddPhotoEnabled) 1F else 0.5F),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        enabled = buttonAddPhotoEnabled,
                        onClick = {
                            onSavePhotoButtonClick(photoLabel)
                            photoLabel = ""
                        },
                    ) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(id = R.string.cd_add_photo),
                            tint = MaterialTheme.colorScheme.tertiary,
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            text = stringResource(id = R.string.save),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

    if (addPhoto) {
        BottomActionsSheet(
            onDismissSheet = { addPhoto = false },
            onShootPhotoMenuItemClick = { onShootPhotoMenuItemClick() },
            onSelectPhotoMenuItemClick = { onSelectPhotoMenuItemClick() },
        )
    }

}

