package com.aquaero.realestatemanager.ui.component.detail_screen

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OtherHouses
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.aquaero.realestatemanager.NULL_ITEM_ID
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.theme.Magenta
import com.aquaero.realestatemanager.ui.theme.White

@SuppressLint("NewApi")
@Composable
fun DetailScreenColumn1(
    property: Property?,
    stringType: String,
) {
    // Info status
    val labelSold = stringResource(id = R.string.sold)
    val labelForSale = stringResource(id = R.string.for_sale)
    val value by remember {
        mutableStateOf(
            property?.saleDate?.let { labelSold }
                ?: property?.let { if (it.propertyId != NULL_ITEM_ID) labelForSale else "" }
                ?: ""
        )
    }
    DetailScreenInformationItem(
        image = Icons.Default.Info,
        contentDesc = stringResource(id = R.string.cd_status),
        label = stringResource(id = R.string.status),
        value = value,
        valueColor = property?.saleDate?.let { White } ?: MaterialTheme.colorScheme.onSurface,
        valueBackgroundColor = property?.saleDate?.let { Magenta } ?: MaterialTheme.colorScheme.surface,
    )
    // Info type
    DetailScreenInformationItem(
        image = Icons.Default.House,
        contentDesc = stringResource(id = R.string.cd_type),
        label = stringResource(id = R.string.type),
        value = if (property != null && property.propertyId != NULL_ITEM_ID) stringType else "",
//        value = if (property != null) stringType else "",   // TODO: For test only
    )
    // Info surface
    DetailScreenInformationItem(
        image = Icons.Default.AspectRatio,
        contentDesc = stringResource(id = R.string.cd_surface),
        label = stringResource(id = R.string.surface),
        value = property?.surface?.toString() ?: "   ",
        suffix = stringResource(id = R.string.surface_unit)
    )
    // Info number of rooms
    DetailScreenInformationItem(
        image = Icons.Default.OtherHouses,
        contentDesc = stringResource(id = R.string.cd_rooms),
        label = stringResource(id = R.string.rooms),
        value = property?.nbOfRooms?.toString() ?: "",
    )
    // Info number of bathrooms
    DetailScreenInformationItem(
        image = Icons.Default.Bathtub,
        contentDesc = stringResource(id = R.string.cd_bathrooms),
        label = stringResource(id = R.string.bathrooms),
        value = property?.nbOfBathrooms?.toString() ?: "",
    )
    // Info number of bedrooms
    DetailScreenInformationItem(
        image = Icons.Default.Bed,
        contentDesc = stringResource(id = R.string.cd_bedrooms),
        label = stringResource(id = R.string.bedrooms),
        value = property?.nbOfBedrooms?.toString() ?: "",
    )
}