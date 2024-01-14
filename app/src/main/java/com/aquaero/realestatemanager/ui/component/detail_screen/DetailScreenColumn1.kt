package com.aquaero.realestatemanager.ui.component.detail_screen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OtherHouses
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.DefaultTintColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.theme.Magenta
import com.aquaero.realestatemanager.ui.theme.White

@SuppressLint("NewApi")
@Composable
fun DetailScreenColumn1(
    property: Property
) {
    Column(
        modifier = Modifier.wrapContentSize()
    ) {

        // Info status
        DetailScreenInformationItem(
            image = Icons.Default.Info,
            contentDesc = stringResource(id = R.string.cd_status),
            label = stringResource(id = R.string.status),
            value = if (property.saleDate != null) stringResource(id = R.string.sold) else stringResource(id = R.string.for_sale),
            valueColor = if (property.saleDate != null) White else MaterialTheme.colorScheme.onSurface,
            valueBackgroundColor = if (property.saleDate != null) Magenta else MaterialTheme.colorScheme.surface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Info type
        DetailScreenInformationItem(
            image = Icons.Default.House,
            contentDesc = stringResource(id = R.string.cd_type),
            label = stringResource(id = R.string.type),
            value = stringResource(property.pType),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Info surface
        DetailScreenInformationItem(
            image = Icons.Default.AspectRatio,
            contentDesc = stringResource(id = R.string.cd_surface),
            label = stringResource(id = R.string.surface),
            value = property.surface.toString(),    // TODO: Convert value to local unit
            suffix = stringResource(id = R.string.surface_unit)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Info number of rooms
        DetailScreenInformationItem(
            image = Icons.Default.OtherHouses,
            contentDesc = stringResource(id = R.string.cd_rooms),
            label = stringResource(id = R.string.rooms),
            value = property.nbOfRooms.toString(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Info number of bathrooms
        DetailScreenInformationItem(
            image = Icons.Default.Bathtub,
            contentDesc = stringResource(id = R.string.cd_bathrooms),
            label = stringResource(id = R.string.bathrooms),
            value = property.nbOfBathrooms.toString(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Info number of bedrooms
        DetailScreenInformationItem(
            image = Icons.Default.Bed,
            contentDesc = stringResource(id = R.string.cd_bedrooms),
            label = stringResource(id = R.string.bedrooms),
            value = property.nbOfBedrooms.toString(),
        )
    }
}