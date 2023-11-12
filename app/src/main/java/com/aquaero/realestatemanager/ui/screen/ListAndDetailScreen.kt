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
    listViewModel:ListViewModel,
    detailViewModel: DetailViewModel,
    contentType: AppContentType,
    onPropertyClick: (Long) -> Unit,
    property: Property,
    // onEditButtonClick: () -> Unit,  // TODO : To be deleted after TopBar menu action implementation
    onFabClick: () -> Unit,
    onBackPressed: () -> Unit,
) {
    Row {
        Column(
            modifier = Modifier.weight(1F)
        ) {
            ListScreen(
                listViewModel = listViewModel,
                contentType = contentType,
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
                    detailViewModel = detailViewModel,
                    property = property,
                    // onEditButtonClick = onEditButtonClick,  // TODO : To be deleted after TopBar menu action implementation
                    onBackPressed = onBackPressed
                )
            }
        }
    }
}

