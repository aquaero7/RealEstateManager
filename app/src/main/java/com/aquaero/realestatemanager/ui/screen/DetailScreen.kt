package com.aquaero.realestatemanager.ui.screen

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
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.component.detail_screen.DetailScreenColumn1
import com.aquaero.realestatemanager.ui.component.detail_screen.DetailScreenColumn2
import com.aquaero.realestatemanager.ui.component.detail_screen.DetailScreenDescription
import com.aquaero.realestatemanager.ui.component.detail_screen.DetailScreenMapThumbnail
import com.aquaero.realestatemanager.ui.component.detail_screen.DetailScreenMedia
import com.aquaero.realestatemanager.ui.component.detail_screen.DetailScreenPoi
import com.aquaero.realestatemanager.viewmodel.DetailViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel,
    property: Property,
    onEditButtonClick: () -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(state = rememberScrollState(), enabled = true),
    ) {
        // Media (photos row)
        DetailScreenMedia(property = property)

        // Description
        DetailScreenDescription(description = property.description)

        Spacer(modifier = Modifier.height(32.dp))

        // Information
        Row {
            // Column 1
            Column(
                modifier = Modifier.weight(1F),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DetailScreenColumn1(property = property)
            }
            // Column 2
            Column(
                modifier = Modifier.weight(1F),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DetailScreenColumn2(property = property)
            }
            /*
            // Column x
            Column(
                modifier = Modifier.weight(1F),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DetailScreenColumn3(property = property)
            }
            */
        }

        // POIs
        DetailScreenPoi(selectedPoi = property.pPoi)

        // Map thumbnail
        DetailScreenMapThumbnail(property)

        // To manage back nav
        BackHandler(true) {
            Log.w("TAG", "OnBackPressed")
            run(onBackPressed)
        }



        // TODO : To be deleted after TopBar menu action implementation
        Spacer(modifier = Modifier.height(80.dp))
        // Text(text = "DetailScreen  for $propertyId")
        Button(
            onClick = onEditButtonClick
        ) {
            Text(text = "EditScreen")
        }
        //
    }

}