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
    // For both list and detail screens
    property: Property?,
    currency: String,
    // For list screen only
    contentType: AppContentType,
    items: List<Property>,
    addresses: List<Address>,
    photos: List<Photo>,
    onPropertyClick: (Long) -> Unit,
    onFabClick: () -> Unit,
    // For detail screen only
    itemPhotos: MutableList<Photo>,
    itemPois: MutableList<Poi>,
    pTypeSet: () -> MutableSet<Int>,
    pTypeIndex: Int,
    stringType: String,
    stringAgent: String,
    stringAddress: String,
    stringLatitude: String,
    stringLongitude: String,
    thumbnailUrl: String,
    internetAvailable: Boolean,
    onBackPressed: () -> Unit,
) {
    Row {
        Column(
            modifier = Modifier.weight(1F)
        ) {
            ListScreen(
                property = property,
                currency = currency,
                contentType = contentType,
                items = items,
                addresses = addresses,
                photos = photos,
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
                    currency = currency,
                    itemPhotos = itemPhotos,
                    itemPois = itemPois,
                    pTypeSet = pTypeSet,
                    pTypeIndex = pTypeIndex,
                    stringType = stringType,
                    stringAgent = stringAgent,
                    stringAddress = stringAddress,
                    stringLatitude = stringLatitude,
                    stringLongitude = stringLongitude,
                    thumbnailUrl = thumbnailUrl,
                    internetAvailable = internetAvailable,
                    onBackPressed = onBackPressed,
                )
            }
        }
    }
}

