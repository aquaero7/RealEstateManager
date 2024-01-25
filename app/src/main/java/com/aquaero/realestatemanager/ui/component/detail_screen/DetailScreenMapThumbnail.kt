package com.aquaero.realestatemanager.ui.component.detail_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.ui.theme.Gray
import com.aquaero.realestatemanager.ui.theme.Red
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailScreenMapThumbnail(
    internetAvailable: Boolean,
    thumbnailUrl: String,
    stringLatitude: String,
    stringLongitude: String,
) {
    Text(
        text = stringResource(R.string.map_thumbnail),
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier
            .padding(top = 16.dp, bottom = 8.dp)
            .padding(horizontal = 8.dp)
    )

    if (internetAvailable && thumbnailUrl.isNotEmpty()) {
        // Network is available, so display static map
        // GlideImage if using Glide or AsyncImage if using Coil
        GlideImage(
            model = thumbnailUrl,
            contentDescription = stringResource(R.string.cd_static_map),
            modifier = Modifier
                //.size(400.dp)
                .wrapContentSize()
                .border(width = 2.dp, color = MaterialTheme.colorScheme.tertiary)
                .padding(4.dp),
            contentScale = ContentScale.Inside,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Text(
                text = stringResource(id = R.string.latitude),
                color = MaterialTheme.colorScheme.tertiary,
            )
            Text(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = stringLatitude,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(id = R.string.longitude),
                color = MaterialTheme.colorScheme.tertiary,
            )
            Text(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = stringLongitude,
                fontWeight = FontWeight.Bold,
            )
        }

    } else {
        // Network is unavailable, so no map is available
        Image(
            modifier = Modifier
                .size(320.dp)
                .border(BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.tertiary))
                .padding(4.dp),
            imageVector = Icons.Default.LocationOff,
            contentDescription = stringResource(R.string.cd_no_static_map),
            alpha = 0.5F,
            colorFilter = ColorFilter.tint(Gray),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.network_unavailable),
            color = Red
        )
    }

    Spacer(modifier = Modifier.height(20.dp))

}
