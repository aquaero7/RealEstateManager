package com.aquaero.realestatemanager.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.component.list_screen.PropertyCard
import com.aquaero.realestatemanager.utils.AppContentType
import com.aquaero.realestatemanager.viewmodel.ListViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListScreen(
    listViewModel: ListViewModel,
    contentType: AppContentType,
    property: Property,
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
            items(items = listViewModel.fakeProperties) { propertyItem ->
                PropertyCard(
                    propertyItem.pId,
                    propertyItem.pType,
                    propertyItem.pAddress.city,
                    propertyItem.pPrice,
                    // propertyItem.photos?.get(0)?.phId,
                    R.drawable.ic_launcher_background.toLong(),
                    contentType,
                    selectedId == propertyItem.pId ||
                            property.pId.toString() == propertyItem.pId.toString(),                 // For compatibility with ListAndDetailScreen
                    property.pId.toString() != propertyItem.pId.toString(),   // For compatibility with ListAndDetailScreen
                    { selectedId = propertyItem.pId },                                              // For compatibility with ListAndDetailScreen
                    onPropertyClick,
                )
            }
        }
    }
}