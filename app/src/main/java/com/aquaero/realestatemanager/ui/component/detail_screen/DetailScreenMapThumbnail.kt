package com.aquaero.realestatemanager.ui.component.detail_screen

import android.location.Location
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aquaero.realestatemanager.BuildConfig
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Property
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailScreenMapThumbnail(
    thumbnailUrl: String,
    latLng: LatLng,
) {

    Text(
        text = stringResource(R.string.map_thumbnail),
        modifier = Modifier
            .padding(top = 16.dp, bottom = 8.dp)
            .padding(horizontal = 8.dp)
    )

    // GlideImage to use Glide or AsyncImage to use Coil
    GlideImage(
        model = thumbnailUrl,
        contentDescription = "static-map",
        modifier = Modifier
            //.size(400.dp)
            .wrapContentSize()
            .border(BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.tertiary))
            .padding(4.dp),
        contentScale = ContentScale.Inside,
    )
    
    Spacer(modifier = Modifier.height(8.dp))

    Row {
        Text(text = stringResource(id = R.string.latitude))
        Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = latLng.latitude.toString(),
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = stringResource(id = R.string.longitude))
        Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = latLng.longitude.toString(),
            fontWeight = FontWeight.Bold,
        )
    }

    Spacer(modifier = Modifier.height(20.dp))

}
