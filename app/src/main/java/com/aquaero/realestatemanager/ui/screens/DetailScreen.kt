package com.aquaero.realestatemanager.ui.screens

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.OtherHouses
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.ui.components.DetailScreenColumn1
import com.aquaero.realestatemanager.ui.components.DetailScreenColumn2
import com.aquaero.realestatemanager.ui.components.DetailScreenDescription
import com.aquaero.realestatemanager.ui.components.DetailScreenMedia
import com.aquaero.realestatemanager.ui.theme.Gray66Trans66
import com.aquaero.realestatemanager.utils.getProperty
import com.aquaero.realestatemanager.utils.getPropertyPictures

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