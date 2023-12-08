package com.aquaero.realestatemanager.ui.component.edit_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddToPhotos
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.component.app.BottomActionsSheet
import com.aquaero.realestatemanager.ui.component.app.PhotosLazyRowScreen

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("NewApi")
@Composable
fun EditScreenMedia(
    property: Property?,
    onShootPhotoMenuItemClick: () -> Unit,
    onSelectPhotoMenuItemClick: () -> Unit,
    isPhotoReady: Boolean,
    onAddPhotoButtonClick: () -> Unit,
    onDeletePhotoMenuItemClick: (Long) -> Unit,
) {
    var addPhoto by rememberSaveable { mutableStateOf(false) }
    val haptics = LocalHapticFeedback.current



    val lineColor = MaterialTheme.colorScheme.onBackground
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
        PhotosLazyRowScreen(
            property = property,
            longClickPhotoEnabled = true,
            onDeletePhotoMenuItemClick = onDeletePhotoMenuItemClick,
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Photo to add
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image
            Image(
                modifier = Modifier
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.onSurface)
                    .size(120.dp)
                    .padding(4.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    .alpha(1F)
                    .combinedClickable(
                        enabled = true,
                        onClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            addPhoto = true
                        }),
                painter = painterResource(id = R.drawable.baseline_add_a_photo_black_24),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary)
            )

            Spacer(modifier = Modifier.width(20.dp))

            // Button to add the photo to property list
            val buttonAddPhotoEnabled by remember { mutableStateOf(isPhotoReady) }
            Button(
                modifier = Modifier.alpha(if (buttonAddPhotoEnabled) 1F else 0.5F),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                enabled = buttonAddPhotoEnabled,
                onClick = onAddPhotoButtonClick,
            ) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    imageVector = Icons.Default.AddToPhotos,
                    contentDescription = stringResource(id = R.string.cd_add_photo),
                    tint = MaterialTheme.colorScheme.tertiary,
                )
                Text(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    text = stringResource(id = R.string.add_photo),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }


    if (addPhoto) {
        BottomActionsSheet(
            onDismissSheet = { addPhoto = false },
            onShootPhotoMenuItemClick = onShootPhotoMenuItemClick,
            onSelectPhotoMenuItemClick = onSelectPhotoMenuItemClick,
        )
    }

}

