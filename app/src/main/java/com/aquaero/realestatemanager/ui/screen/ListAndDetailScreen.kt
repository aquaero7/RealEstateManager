package com.aquaero.realestatemanager.ui.screen

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
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.utils.AppContentType
import com.aquaero.realestatemanager.viewmodel.DetailViewModel
import com.aquaero.realestatemanager.viewmodel.ListViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListAndDetailScreen(
    // listViewModel: ListViewModel,    // TODO : To be removed
    items: List<Property>,
    thumbnailUrl: String,
    contentType: AppContentType,
    onPropertyClick: (Long) -> Unit,
    property: Property,
    currency: String,
    stringAgent: String,
    onFabClick: () -> Unit,
    onBackPressed: () -> Unit,
) {
    Row {
        Column(
            modifier = Modifier.weight(1F)
        ) {
            ListScreen(
                // listViewModel = listViewModel,   // TODo : To be removed
                items = items,
                contentType = contentType,
                currency = currency,
                property = property,
                onPropertyClick = onPropertyClick,
                onFabClick = onFabClick,
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
                    property = property,
                    thumbnailUrl = thumbnailUrl,
                    stringAgent = stringAgent,
                    currency = currency,
                    onBackPressed = onBackPressed,
                )
            }
        }
    }
}

