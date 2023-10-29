package com.aquaero.realestatemanager

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.aquaero.realestatemanager.ui.screens.ListAndDetailScreen
import com.aquaero.realestatemanager.ui.screens.ListScreen
import com.aquaero.realestatemanager.utils.AppContentType

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppContent(
    contentType: AppContentType,
    onPropertyClick: (Long) -> Unit = {},
    propertyId: String?,
    onEditButtonClick: () -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    if (contentType == AppContentType.SCREEN_ONLY) {
        ListScreen(contentType = contentType, onPropertyClick = onPropertyClick)
    } else {
        ListAndDetailScreen(
            contentType = contentType,
            onPropertyClick = onPropertyClick,
            propertyId = propertyId,
            onEditButtonClick = onEditButtonClick,
            onBackPressed = onBackPressed
        )
    }

}

