package com.aquaero.realestatemanager.ui.screen

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.AppContentType
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.NO_PHOTO
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.ui.component.list_screen.PropertyCard
import com.aquaero.realestatemanager.ui.theme.White

@Composable
fun ListScreen(
    property: Property?,
    currency: String,
    contentType: AppContentType,
    items: List<Property>,
    addresses: List<Address>,
    photos: List<Photo>,
    itemType: (String) -> String,
    onPropertyClick: (Long) -> Unit,
    onFabClick: () -> Unit,
) {
    Scaffold (
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
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = LazyListState(firstVisibleItemIndex = property?.let { items.indexOf(property) } ?: 0),
            ) {
                items(items = items) { propertyItem ->
                    val photo = photos.find { it.propertyId == propertyItem.propertyId }
                    val phId = photo?.photoId ?: NO_PHOTO.photoId
                    val phUri = photo?.uri ?: NO_PHOTO.uri
                    val pId = propertyItem.propertyId
                    val pType = itemType(propertyItem.typeId)
                    val pCity = addresses.find { it.addressId == propertyItem.addressId }?.city ?: ""
                    val pPriceFormatted = propertyItem.priceFormattedInCurrency(currency)
                    val selected by remember(propertyItem, property) {
                        mutableStateOf(
                            selectedId == propertyItem.propertyId ||
                                    property?.propertyId.toString() == propertyItem.propertyId.toString()
                        )
                    }
                    val unselectedByDefaultDisplay by remember(propertyItem, property) {
                        mutableStateOf(property?.propertyId.toString() != propertyItem.propertyId.toString())
                    }
                    val onSelection by remember(propertyItem) {
                        mutableStateOf({ selectedId = propertyItem.propertyId } )
                    }

                    PropertyCard(
                        contentType = contentType,
                        pId = pId,
                        pType = pType,
                        pCity = pCity,
                        phId = phId,
                        phUri = phUri,
                        pPriceFormatted = pPriceFormatted,
                        selected = selected,
                        unselectedByDefaultDisplay = unselectedByDefaultDisplay,
                        onSelection = onSelection,
                        onPropertyClick = onPropertyClick,
                    )
                }
            }
        }
    }
}