package com.aquaero.realestatemanager.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.data.fakeProperties
import com.aquaero.realestatemanager.ui.components.PropertyCard
import com.aquaero.realestatemanager.utils.AppContentType

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListScreen(
    contentType: AppContentType,
    propertyId: String?,
    onPropertyClick: (Long) -> Unit = {}
) {
    Column {
        var selectedId by remember { mutableLongStateOf(-1L) }  // For compatibility with ListAndDetailScreen

        Spacer(modifier = Modifier.height(4.dp))
        LazyColumn(
            modifier = Modifier
                // .padding(vertical = 20.dp)
                .fillMaxWidth()
        ) {
            items(items = fakeProperties) { property ->
                PropertyCard(
                    property.pId,
                    property.pType,
                    property.pAddress.city,
                    property.pPrice,
                    // property.photos?.get(0)?.phId,
                    R.drawable.ic_launcher_background.toLong(),
                    contentType,
                    selectedId == property.pId || propertyId == property.pId.toString(), // For compatibility with ListAndDetailScreen
                    propertyId != property.pId.toString(),  // For compatibility with ListAndDetailScreen
                    { selectedId = property.pId },      // For compatibility with ListAndDetailScreen
                    onPropertyClick,
                )
            }
        }
    }
}