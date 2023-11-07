package com.aquaero.realestatemanager.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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

