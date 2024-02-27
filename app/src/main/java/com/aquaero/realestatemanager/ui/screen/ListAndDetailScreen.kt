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
import com.aquaero.realestatemanager.AppContentType
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property

@SuppressLint("NewApi")
@Composable
fun ListAndDetailScreen(
    propertySelected: Boolean,
    // For list screen only
    contentType: AppContentType,
    items: List<Property>,
    addresses: List<Address>,
    photos: List<Photo>,
    itemType: (String) -> String,
    onPropertyClick: (Long) -> Unit,
    onFabClick: () -> Unit,
    // For both list and detail screens
    property: Property?,
    currency: String,
    closeApp: () -> Unit,
    // For detail screen only
    itemPhotos: MutableList<Photo>,
    itemPois: MutableList<Poi>,
    stringType: String,
    stringAgent: String,
    stringAddress: String,
    stringLatitude: String,
    stringLongitude: String,
    thumbnailUrl: String,
    networkAvailable: Boolean,
    popBackStack: () -> Unit,
) {
    Row {
        if (contentType == AppContentType.LIST_AND_DETAIL || !propertySelected) {
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
                    itemType = itemType,
                    onPropertyClick = onPropertyClick,
                    onFabClick = onFabClick,
                    onBackPressed = closeApp,
                )
            }
        }
        if (contentType == AppContentType.LIST_AND_DETAIL) {
            HorizontalDivider(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight(),
                color = Color.LightGray
            )
        }
        if (contentType == AppContentType.LIST_AND_DETAIL || propertySelected) {
            Column(
                modifier = Modifier.weight(2F)
            ) {
                val onBackPressed =
                    if (contentType == AppContentType.LIST_OR_DETAIL) popBackStack else closeApp

                DetailScreen(
                    property = property,
                    currency = currency,
                    itemPhotos = itemPhotos,
                    itemPois = itemPois,
                    stringType = stringType,
                    stringAgent = stringAgent,
                    stringAddress = stringAddress,
                    stringLatitude = stringLatitude,
                    stringLongitude = stringLongitude,
                    thumbnailUrl = thumbnailUrl,
                    networkAvailable = networkAvailable,
                    onBackPressed = onBackPressed,
                )
            }
        }
    }

}

