package com.aquaero.realestatemanager.ui.component.app

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaero.realestatemanager.R

@SuppressLint("NewApi")
@Composable
fun DetailScreenPoi(
    selectedPoi: MutableList<String>,
    clickable: Boolean = false,
    onHospitalClick: (Boolean) -> Unit = {},
    onSchoolClick: (Boolean) -> Unit = {},
    onRestaurantClick: (Boolean) -> Unit = {},
    onShopClick: (Boolean) -> Unit = {},
    onRailwayStationClick: (Boolean) -> Unit = {},
    onCarParkClick: (Boolean) -> Unit = {},
) {
    Text(
        text = stringResource(R.string.poi),
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier
            .padding(top = 16.dp, bottom = 8.dp)
            .padding(horizontal = 8.dp)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .horizontalScroll(
                state = rememberScrollState(),
                enabled = true
            ),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        // Hospital
        DetailScreenIcon(
            imageVector = Icons.Default.LocalHospital,
            contentDesc = stringResource(id = R.string.cd_hospital),
            label = stringResource(id = R.string.hospital),
            selected = selectedPoi.contains(stringResource(id = R.string.key_hospital)),
            clickable = clickable,
            onClick = onHospitalClick,
        )
        // School
        DetailScreenIcon(
            imageVector = Icons.Default.School,
            contentDesc = stringResource(id = R.string.cd_school),
            label = stringResource(id = R.string.school),
            selected = selectedPoi.contains(stringResource(id = R.string.key_school)),
            clickable = clickable,
            onClick = onSchoolClick,
        )
        // Restaurant
        DetailScreenIcon(
            imageVector = Icons.Default.Restaurant,
            contentDesc = stringResource(id = R.string.cd_restaurant),
            label = stringResource(id = R.string.restaurant),
            selected = selectedPoi.contains(stringResource(id = R.string.key_restaurant)),
            clickable = clickable,
            onClick = onRestaurantClick,
        )
        // Shop
        DetailScreenIcon(
            imageVector = Icons.Default.ShoppingBag,
            contentDesc = stringResource(id = R.string.cd_shop),
            label = stringResource(id = R.string.shop),
            selected = selectedPoi.contains(stringResource(id = R.string.key_shop)),
            clickable = clickable,
            onClick = onShopClick,
        )
        // Railway station
        DetailScreenIcon(
            imageVector = Icons.Default.Train,
            contentDesc = stringResource(id = R.string.cd_railway_station),
            label = stringResource(id = R.string.railway_station),
            selected = selectedPoi.contains(stringResource(id = R.string.key_railway_station)),
            clickable = clickable,
            onClick = onRailwayStationClick,
        )
        // Car park
        DetailScreenIcon(
            imageVector = Icons.Default.LocalParking,
            contentDesc = stringResource(id = R.string.cd_car_park),
            label = stringResource(id = R.string.car_park),
            selected = selectedPoi.contains(stringResource(id = R.string.key_car_park)),
            clickable = clickable,
            onClick = onCarParkClick,
        )
    }
}

@Composable
fun DetailScreenIcon(
    clickable: Boolean,
    onClick: (Boolean) -> Unit,
    imageVector: ImageVector,
    contentDesc: String,
    label: String,
    selected: Boolean,
) {
    var isSelected by remember { mutableStateOf(selected) }
    val iconColor = MaterialTheme.colorScheme.tertiary
    val borderColor = MaterialTheme.colorScheme.secondary

    Column(
        modifier = Modifier.wrapContentWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDesc,
            tint = if (isSelected) iconColor else iconColor.copy(alpha = 0.3F),
            modifier = Modifier
                .size(52.dp)
                .padding(4.dp)
                .border(
                    width = 1.dp,
                    color = if (isSelected) borderColor else borderColor.copy(alpha = 0.3F)
                )
                .clickable(
                    enabled = clickable,
                    onClick = {
                        isSelected = !isSelected
                        onClick(isSelected)
                    },
                ),
        )
        Text(
            modifier = Modifier.width(64.dp),
            text = label,
            textAlign = TextAlign.Center,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            lineHeight = 11.sp
        )
    }






}

