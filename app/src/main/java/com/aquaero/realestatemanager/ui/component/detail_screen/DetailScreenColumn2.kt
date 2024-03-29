package com.aquaero.realestatemanager.ui.component.detail_screen

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.aquaero.realestatemanager.NULL_PROPERTY_ID
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Property

@SuppressLint("NewApi")
@Composable
fun DetailScreenColumn2(
    property: Property?,
    stringAddress: String,
    stringAgent: String,
    currency: String,
) {
    // Info price
    DetailScreenInformationItem(
        image = Icons.Default.Money,
        contentDesc = stringResource(id = R.string.cd_price),
        label = stringResource(R.string.price),
        value = property?.priceFormattedInCurrency(currency) ?: "",
    )
    // Info location
    DetailScreenInformationItem(
        maxLines = 9,
        image = Icons.Default.LocationOn,
        contentDesc = stringResource(id = R.string.cd_address),
        label = stringResource(id = R.string.address),
        value = stringAddress,
    )
    // Info agent
    DetailScreenInformationItem(
        maxLines = 2,
        image = Icons.Default.Person,
        contentDesc = stringResource(id = R.string.cd_agent),
        label = stringResource(id = R.string.agent),
        value = if (property != null && property.propertyId != NULL_PROPERTY_ID) stringAgent else "",
    )
    // Info registration date
    DetailScreenInformationItem(
        image = Icons.Default.ArrowCircleDown,
        contentDesc = stringResource(id = R.string.cd_registration_date),
        label = stringResource(id = R.string.registration_date),
        value = property?.registrationDate ?: "",
    )
    // Info sale date
    DetailScreenInformationItem(
        image = Icons.Default.ArrowCircleUp,
        contentDesc = stringResource(id = R.string.cd_sale_date),
        label = stringResource(id = R.string.sale_date),
        value = property?.saleDate ?: "",
    )
}