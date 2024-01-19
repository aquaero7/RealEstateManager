package com.aquaero.realestatemanager.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.NO_PHOTO
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.component.list_screen.PropertyCard
import com.aquaero.realestatemanager.ui.theme.White
import com.aquaero.realestatemanager.utils.AppContentType

@Composable
fun ListScreen(
    items: List<Property>,
    contentType: AppContentType,
    currency: String,
    property: Property,
    onPropertyClick: (Long) -> Unit,
    onFabClick: () -> Unit,
) {
    Scaffold (
        // floatingActionButtonPosition = FabPosition.End, // = default
        floatingActionButton = {
            SmallFloatingActionButton(
                onClick = onFabClick,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = White,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp),
            ) {
                Icon(
                    modifier = Modifier.scale(1.0F),
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.cd_fab),
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            var selectedId by remember { mutableLongStateOf(-1L) }  // For compatibility with ListAndDetailScreen

            Spacer(modifier = Modifier.height(4.dp))
            LazyColumn(
                modifier = Modifier
                    // .padding(vertical = 20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = LazyListState(firstVisibleItemIndex = items.indexOf(property)),
            ) {
                items(items = items) { propertyItem ->
                    // val phId = if (propertyItem.photos != null && property.photos!![0].phId > 0L)
                    val phId = if (property.photos[0].phId > 0L)
                        propertyItem.photos[0].phId else NO_PHOTO.phId
                    PropertyCard(
                        pId = propertyItem.pId,
                        pType = propertyItem.pType,
                        pCity = propertyItem.pAddress.city,
                        phId = phId,
                        pPriceFormatted = propertyItem.priceStringInCurrency(currency),
                        contentType = contentType,
                        selected = selectedId == propertyItem.pId ||
                                property.pId.toString() == propertyItem.pId.toString(), // For compatibility with ListAndDetailScreen
                        unselectedByDefaultDisplay =
                                property.pId.toString() != propertyItem.pId.toString(), // For compatibility with ListAndDetailScreen
                        onSelection = { selectedId = propertyItem.pId },                // For compatibility with ListAndDetailScreen
                        onPropertyClick = onPropertyClick,
                    )
                }
            }
        }
    }
}