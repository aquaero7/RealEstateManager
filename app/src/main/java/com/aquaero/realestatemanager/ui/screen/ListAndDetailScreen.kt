package com.aquaero.realestatemanager.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.utils.AppContentType

@SuppressLint("NewApi")
@Composable
fun ListAndDetailScreen(
    items: List<Property>,
    addresses: List<Address>,
    stringLatitude: String,
    stringLongitude: String,
    photos: List<Photo>,
    contentType: AppContentType,
    onPropertyClick: (Long) -> Unit,
    property: Property?,
    thumbnailUrl: String,
    stringAgent: String,
    stringAddress: String,
    itemPhotos: MutableList<Photo>,
    itemPois: MutableList<Poi>,
    currency: String,
    internetAvailable: Boolean,
    onFabClick: () -> Unit,
    onBackPressed: () -> Unit,
) {
    Row {
        Column(
            modifier = Modifier.weight(1F)
        ) {
            ListScreen(
                items = items,
                addresses = addresses,
                photos = photos,
                contentType = contentType,
                currency = currency,
                property = property,
                onPropertyClick = onPropertyClick,
                onFabClick = onFabClick,
            )
        }
        if (contentType == AppContentType.SCREEN_WITH_DETAIL) {
            // Spacer(modifier = Modifier.width(2.dp))
            HorizontalDivider(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight(),
                color = Color.LightGray
            )
            // Spacer(modifier = Modifier.width(2.dp))
            Column(
                modifier = Modifier.weight(2F)
            ) {
                DetailScreen(
                    property = property,
                    stringAddress = stringAddress,
                    stringLatitude = stringLatitude,
                    stringLongitude = stringLongitude,
                    thumbnailUrl = thumbnailUrl,
                    stringAgent = stringAgent,
                    itemPhotos = itemPhotos,
                    itemPois = itemPois,
                    currency = currency,
                    internetAvailable = internetAvailable,
                    onBackPressed = onBackPressed,
                )
            }
        }
    }
}

