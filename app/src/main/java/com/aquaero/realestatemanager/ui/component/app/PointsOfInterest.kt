package com.aquaero.realestatemanager.ui.component.app

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import com.aquaero.realestatemanager.model.PoiEnum
import com.aquaero.realestatemanager.model.Poi

@SuppressLint("NewApi")
@Composable
fun PointsOfInterest(
    onHospitalClick: (Boolean) -> Unit = {},
    onSchoolClick: (Boolean) -> Unit = {},
    onRestaurantClick: (Boolean) -> Unit = {},
    onShopClick: (Boolean) -> Unit = {},
    onRailwayStationClick: (Boolean) -> Unit = {},
    onCarParkClick: (Boolean) -> Unit = {},
    itemPois: MutableList<Poi>,
    clickable: Boolean = false,
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
        PoiIcon(
            imageVector = Icons.Default.LocalHospital,
            contentDesc = PoiEnum.HOSPITAL.key,
            label = stringResource(id = R.string.hospital),
            selected = (itemPois.find { it.poiId == PoiEnum.HOSPITAL.key } != null),
            clickable = clickable,
            onClick = onHospitalClick,
        )
        // School
        PoiIcon(
            imageVector = Icons.Default.School,
            contentDesc = PoiEnum.SCHOOL.key,
            label = stringResource(id = R.string.school),
            selected = (itemPois.find { it.poiId == PoiEnum.SCHOOL.key } != null),
            clickable = clickable,
            onClick = onSchoolClick,
        )
        // Restaurant
        PoiIcon(
            imageVector = Icons.Default.Restaurant,
            contentDesc = PoiEnum.RESTAURANT.key,
            label = stringResource(id = R.string.restaurant),
            selected = (itemPois.find { it.poiId == PoiEnum.RESTAURANT.key } != null),
            clickable = clickable,
            onClick = onRestaurantClick,
        )
        // Shop
        PoiIcon(
            imageVector = Icons.Default.ShoppingBag,
            contentDesc = PoiEnum.SHOP.key,
            label = stringResource(id = R.string.shop),
            selected = (itemPois.find { it.poiId == PoiEnum.SHOP.key } != null),
            clickable = clickable,
            onClick = onShopClick,
        )
        // Railway station
        PoiIcon(
            imageVector = Icons.Default.Train,
            contentDesc = PoiEnum.RAILWAY_STATION.key,
            label = stringResource(id = R.string.railway_station),
            selected = (itemPois.find { it.poiId == PoiEnum.RAILWAY_STATION.key } != null),
            clickable = clickable,
            onClick = onRailwayStationClick,
        )
        // Car park
        PoiIcon(
            imageVector = Icons.Default.LocalParking,
            contentDesc = PoiEnum.CAR_PARK.key,
            label = stringResource(id = R.string.car_park),
            selected = (itemPois.find { it.poiId == PoiEnum.CAR_PARK.key } != null),
            clickable = clickable,
            onClick = onCarParkClick,
        )
    }
}

@Composable
fun PoiIcon(
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

