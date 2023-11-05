package com.aquaero.realestatemanager.ui.screens

import android.graphics.drawable.shapes.Shape
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.data.fakeProperties
import com.aquaero.realestatemanager.ui.components.PropertyCard
import com.aquaero.realestatemanager.utils.AppContentType

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListAndDetailScreen(
    contentType: AppContentType,
    onPropertyClick: (Long) -> Unit = {},
    propertyId: String,
    onEditButtonClick: () -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    Row {
        Column(
            modifier = Modifier.weight(1F)
        ) {
            ListScreen(
                contentType = contentType,
                propertyId = propertyId,
                onPropertyClick = onPropertyClick
            )
        }
        if (contentType == AppContentType.SCREEN_WITH_DETAIL) {
            // Spacer(modifier = Modifier.width(2.dp))
            Divider(
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
                    propertyId = propertyId,
                    onEditButtonClick = onEditButtonClick,
                    onBackPressed = onBackPressed
                )
            }
        }
    }
}

