package com.aquaero.realestatemanager.ui.components.detail_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.utils.getProperty

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailScreenColumn2(
    propertyId: String
) {
    val property = getProperty(propertyId.toLong())

    Column(
        modifier = Modifier.wrapContentSize()
    ) {

        // Info price
        DetailScreenInformationItem(
            image = Icons.Default.Money,
            contentDesc = stringResource(id = R.string.cd_price),
            label = stringResource(id = R.string.price),
            value = property.pPrice.toString(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Info location
        DetailScreenInformationItem(
            image = Icons.Default.LocationOn,
            contentDesc = stringResource(id = R.string.cd_address),
            label = stringResource(id = R.string.address),
            value = property.pAddress.toString(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Info agent
        DetailScreenInformationItem(
            image = Icons.Default.Person,
            contentDesc = stringResource(id = R.string.cd_agent),
            label = stringResource(id = R.string.agent),
            value = property.agent.toString(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Info registration date
        DetailScreenInformationItem(
            image = Icons.Default.ArrowCircleDown,
            contentDesc = stringResource(id = R.string.cd_registration_date),
            label = stringResource(id = R.string.registration_date),
            value = property.registrationDate.toString(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Info sale date
        DetailScreenInformationItem(
            image = Icons.Default.ArrowCircleUp,
            contentDesc = stringResource(id = R.string.cd_sale_date),
            label = stringResource(id = R.string.sale_date),
            value = if (property.saleDate != null) property.saleDate.toString() else "-",
        )

        Spacer(modifier = Modifier.height(16.dp))




    }
}