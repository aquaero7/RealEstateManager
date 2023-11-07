package com.aquaero.realestatemanager.ui.screens

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.ui.components.detail_screen.DetailScreenColumn1
import com.aquaero.realestatemanager.ui.components.detail_screen.DetailScreenColumn2
import com.aquaero.realestatemanager.ui.components.detail_screen.DetailScreenDescription
import com.aquaero.realestatemanager.ui.components.detail_screen.DetailScreenMapThumbnail
import com.aquaero.realestatemanager.ui.components.detail_screen.DetailScreenMedia
import com.aquaero.realestatemanager.ui.components.detail_screen.DetailScreenPoi

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailScreen(
    propertyId: String,
    onEditButtonClick: () -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(state = rememberScrollState(), enabled = true),
    ) {
        // Media (photos row)
        DetailScreenMedia(propertyId = propertyId)

        // Description
        DetailScreenDescription(propertyId = propertyId)

        Spacer(modifier = Modifier.height(32.dp))

        // Information
        Row {
            // Column 1
            Column(
                modifier = Modifier.weight(1F),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DetailScreenColumn1(propertyId = propertyId)
            }
            // Column 2
            Column(
                modifier = Modifier.weight(1F),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DetailScreenColumn2(propertyId = propertyId)
            }
            /*
            // Column x
            Column(
                modifier = Modifier.weight(1F),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DetailScreenColumn3(propertyId = propertyId)
            }
            */
        }

        // POIs
        DetailScreenPoi(propertyId)

        // Map thumbnail
        DetailScreenMapThumbnail(propertyId)





        //
        Spacer(modifier = Modifier.height(80.dp))
        Text(text = "DetailScreen  for $propertyId")
        Button(
            onClick = onEditButtonClick
        ) {
            Text(text = "EditScreen")
        }
        //

        BackHandler(true) {
            Log.w("TAG", "OnBackPressed")
            run(onBackPressed)
        }
    }

}