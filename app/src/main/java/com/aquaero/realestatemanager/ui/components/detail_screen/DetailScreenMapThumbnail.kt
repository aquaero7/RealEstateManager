package com.aquaero.realestatemanager.ui.components.detail_screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.R

@Composable
fun DetailScreenMapThumbnail(propertyId: String) {
    Text(
        text = stringResource(R.string.map_thumbnail),
        modifier = Modifier
            .padding(top = 16.dp, bottom = 8.dp)
            .padding(horizontal = 8.dp)
    )
}