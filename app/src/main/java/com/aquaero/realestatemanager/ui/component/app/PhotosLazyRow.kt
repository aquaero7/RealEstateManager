package com.aquaero.realestatemanager.ui.component.app

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Photo

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotosLazyRow(
    photos: MutableList<Photo>,
    longClickPhotoEnabled: Boolean,
    onEditPhotoMenuItemClickGetPhoto: (Photo) -> Unit,
    onDeletePhotoMenuItemClick: (Long) -> Unit,
) {
    var contextMenuPhotoId by rememberSaveable { mutableStateOf<Long?>(null) }
    val haptics = LocalHapticFeedback.current

    Text(
        text = stringResource(R.string.media),
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier
            .padding(top = 8.dp)
            .padding(horizontal = 8.dp),
    )
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentHeight()
            .padding(top = 4.dp, start = 4.dp, end = 6.dp)
    ) {
        items(items = photos) { photo ->
            val resourceUri =
                if (photo.phId.toInt() > 0) photo.phUri else R.drawable.baseline_photo_camera_black_24
            val colorFilter =
                if (photo.phId.toInt() > 0) null else ColorFilter.tint(MaterialTheme.colorScheme.tertiary)
            val alpha = if (photo.phId.toInt() > 0) 1F else 0.2F

            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .padding(horizontal = 2.dp),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Image(
                    // painter = painterResource(id = resourceId),  // TODO: To be deleted
                    painter = rememberAsyncImagePainter(resourceUri),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    colorFilter = colorFilter,
                    modifier = Modifier
                        .width(120.dp)
                        .height(120.dp)
                        .padding(0.dp)
                        .alpha(alpha)
                        .combinedClickable(
                            enabled = (longClickPhotoEnabled && photo.phId != 0L),
                            onLongClick = {
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                contextMenuPhotoId = photo.phId
                            }) {}
                )
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(0.dp)
                        .alpha(0.6F),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                ) {
                    Text(
                        text = photo.phLabel,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        // color = MaterialTheme.colorScheme.tertiary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }
        }
    }

    if (contextMenuPhotoId != null) {
        BottomActionsSheet(
            onDismissSheet = { contextMenuPhotoId = null },
            photo = photos.first { it.phId == contextMenuPhotoId },
            onEditPhotoMenuItemClickGetPhoto = onEditPhotoMenuItemClickGetPhoto,
            onDeletePhotoMenuItemClick = onDeletePhotoMenuItemClick,
        )
    }

}

