package com.aquaero.realestatemanager.ui.component.detail_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.BuildConfig
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Property
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailScreenMapThumbnail(property: Property) {

    val smMkAddress1 = property.pAddress.toUrl()
    val thumbnailUrl = smUrl + smSize + smScale + smType + smMkColor1 + smMkAddress1 + SM_KEY

    Text(
        text = stringResource(R.string.map_thumbnail),
        modifier = Modifier
            .padding(top = 16.dp, bottom = 8.dp)
            .padding(horizontal = 8.dp)
    )

    GlideImage(
        model = thumbnailUrl,
        contentDescription = "static-map",
        modifier = Modifier
            //.size(400.dp)
            .wrapContentSize()
            .border(BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.tertiary))
            .padding(4.dp),
        contentScale = ContentScale.Inside
    )

    Spacer(modifier = Modifier.height(8.dp))

}

const val smUrl = "https://maps.googleapis.com/maps/api/staticmap?"
const val smSize = "size=400x400"
const val smScale = "&scale=2"
const val smType = "&maptype=hybrid"
const val smMkColor1 = "&markers=color:red%7C"
const val SM_KEY = "&key=${BuildConfig.MAPS_API_KEY}"