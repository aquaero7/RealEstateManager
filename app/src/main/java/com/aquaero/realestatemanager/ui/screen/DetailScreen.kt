package com.aquaero.realestatemanager.ui.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.component.detail_screen.DetailScreenColumn1
import com.aquaero.realestatemanager.ui.component.detail_screen.DetailScreenColumn2
import com.aquaero.realestatemanager.ui.component.detail_screen.DetailScreenDescription
import com.aquaero.realestatemanager.ui.component.detail_screen.DetailScreenMapThumbnail
import com.aquaero.realestatemanager.ui.component.app.PhotosLazyRow
import com.aquaero.realestatemanager.ui.component.app.PointsOfInterest

@Composable
fun DetailScreen(
    property: Property?,
    currency: String,
    itemPhotos: MutableList<Photo>,
    itemPois: MutableList<Poi>,
    stringType: String,
    stringAgent: String,
    stringAddress: String,
    stringLatitude: String,
    stringLongitude: String,
    thumbnailUrl: String,
    internetAvailable: Boolean,
    onBackPressed: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                state = rememberScrollState(),
                enabled = true
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Media (photos row)
        PhotosLazyRow(
            photos = itemPhotos,
            longClickPhotoEnabled = false,
            onEditPhotoMenuItemClickGetPhoto = {},
            onDeletePhotoMenuItemClick = {},
        )

        // Description
        DetailScreenDescription(description = property?.description)

        Spacer(modifier = Modifier.height(32.dp))

        // Information
        Row(
            modifier = Modifier.height(IntrinsicSize.Max)
        ) {
            // Column 1
            Column(
                modifier = Modifier
                    .weight(1F)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                DetailScreenColumn1(property = property, stringType = stringType,)
            }
            // Column 2
            Column(
                modifier = Modifier
                    .weight(1F)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                DetailScreenColumn2(
                    property = property,
                    stringAddress = stringAddress,
                    stringAgent = stringAgent,
                    currency = currency,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // POIs
        PointsOfInterest(itemPois = itemPois)

        Spacer(modifier = Modifier.height(16.dp))

        // Map thumbnail
        DetailScreenMapThumbnail(
            internetAvailable = internetAvailable,
            thumbnailUrl = thumbnailUrl,
            stringLatitude = stringLatitude,
            stringLongitude = stringLongitude,
        )

        // To manage back nav
        BackHandler(true) {
            Log.w("TAG", "OnBackPressed")
            run(onBackPressed)
        }
    }
}