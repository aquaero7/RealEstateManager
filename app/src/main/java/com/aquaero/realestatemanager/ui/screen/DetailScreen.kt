package com.aquaero.realestatemanager.ui.screen

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.component.detail_screen.DetailScreenColumn1
import com.aquaero.realestatemanager.ui.component.detail_screen.DetailScreenColumn2
import com.aquaero.realestatemanager.ui.component.detail_screen.DetailScreenDescription
import com.aquaero.realestatemanager.ui.component.detail_screen.DetailScreenMapThumbnail
import com.aquaero.realestatemanager.ui.component.app.PhotosLazyRow
import com.aquaero.realestatemanager.ui.component.detail_screen.DetailScreenPoi

@Composable
fun DetailScreen(
    property: Property,
    thumbnailUrl: String,
    stringAgent: String,
    currency: String,
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
            // property = property, // TODO: To be deleted
            photos = property.photos,
            longClickPhotoEnabled = false,
            onEditPhotoMenuItemClickGetPhoto = {},
            onDeletePhotoMenuItemClick = {},
        )

        // Description
        DetailScreenDescription(description = property.description)

        Spacer(modifier = Modifier.height(32.dp))

        // Information
        Row {
            // Column 1
            Column(
                modifier = Modifier
                    .weight(1F)
                    .height(320.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    DetailScreenColumn1(property = property)
                }
            }
            // Column 2
            Column(
                modifier = Modifier
                    .weight(1F)
                    .height(320.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    DetailScreenColumn2(
                        property = property,
                        stringAgent = stringAgent,
                        currency = currency,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // POIs
        DetailScreenPoi(selectedPoi = property.pPoi)

        Spacer(modifier = Modifier.height(16.dp))

        // Map thumbnail
        DetailScreenMapThumbnail(
            internetAvailable = internetAvailable,
            thumbnailUrl = thumbnailUrl,
            latLng = property.pAddress.latLng,
        )

        // To manage back nav
        BackHandler(true) {
            Log.w("TAG", "OnBackPressed")
            run(onBackPressed)
        }
    }
}