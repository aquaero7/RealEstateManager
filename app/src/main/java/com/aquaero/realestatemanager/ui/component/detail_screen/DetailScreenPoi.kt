package com.aquaero.realestatemanager.ui.component.detail_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.ui.theme.GrayDisabled
import com.aquaero.realestatemanager.ui.theme.Pink40

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailScreenPoi(selectedPoi: List<String>    /*property: Property*/) {
    Text(
        text = stringResource(R.string.poi),
        modifier = Modifier
            .padding(top = 16.dp, bottom = 8.dp)
            .padding(horizontal = 8.dp)
    )
    Row (
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        DetailScreenIcon(
            imageVector = Icons.Default.LocalHospital,
            contentDesc = stringResource(id = R.string.cd_hospital),
            selected = selectedPoi.contains(stringResource(id = R.string.key_hospital))
        )

        DetailScreenIcon(
            imageVector = Icons.Default.School,
            contentDesc = stringResource(id = R.string.cd_school),
            selected = selectedPoi.contains(stringResource(id = R.string.key_school))
        )

        DetailScreenIcon(
            imageVector = Icons.Default.Restaurant,
            contentDesc = stringResource(id = R.string.cd_restaurant),
            selected = selectedPoi.contains(stringResource(id = R.string.key_restaurant))
        )

        DetailScreenIcon(
            imageVector = Icons.Default.ShoppingBag,
            contentDesc = stringResource(id = R.string.cd_shop),
            selected = selectedPoi.contains(stringResource(id = R.string.key_shop))
        )

        DetailScreenIcon(
            imageVector = Icons.Default.Train,
            contentDesc = stringResource(id = R.string.cd_railway_station),
            selected = selectedPoi.contains(stringResource(id = R.string.key_railway_station))
        )

        DetailScreenIcon(
            imageVector = Icons.Default.LocalParking,
            contentDesc = stringResource(id = R.string.cd_car_park),
            selected = selectedPoi.contains(stringResource(id = R.string.key_car_park))
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailScreenIcon(
    imageVector: ImageVector,
    contentDesc: String,
    selected: Boolean,
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDesc,
        tint = if (selected) Pink40 else GrayDisabled,
        modifier = Modifier
            .size(40.dp)
            .padding(4.dp)
            .border(
                BorderStroke(
                    width = 1.dp,
                    color = if (selected) Pink40 else GrayDisabled
                )
            ),
    )
}

