package com.aquaero.realestatemanager.ui.component.edit_screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Property

@Composable
fun EditScreenColumn2(
    property: Property?,
    currency: String,
    agentSet: () -> MutableSet<String?>,
    agentIndex: Int?,
    onPriceValueChanged: (String) -> Unit,
    onDropdownMenuValueChanged: (String) -> Unit,
    onRegistrationDateValueChanged: (String) -> Unit,
    onSaleDateValueChanged: (String) -> Unit,
) {
    // Price
    EditScreenTextFieldItem(
        itemText = property?.priceInCurrency(currency).toString(),
        labelText = "${stringResource(R.string.price_in)} $currency",
        placeHolderText = "${stringResource(R.string.price_in)} $currency",
        icon = Icons.Default.Money,
        iconCD = stringResource(id = R.string.cd_price),
        onValueChanged = onPriceValueChanged,
        shouldBeDigitsOnly = true,
    )
    // Location
    EditScreenAddressItem(
        itemText = property?.pAddress,
        onValueChanged = { /* TODO */ }
    )
    // Agent
    EditScreenTextFieldItem(
        maxLines = 2,
        labelText = stringResource(R.string.agent),
        placeHolderText = stringResource(R.string.agent),
        icon = Icons.Default.Person,
        iconCD = stringResource(id = R.string.cd_agent),
        itemsSet = agentSet,
        index = agentIndex,
        onValueChanged = onDropdownMenuValueChanged,
    )
    // Registration date
    EditScreenTextFieldItem(
        storedDate = if (property?.registrationDate != null) property.registrationDate.toString() else "",
        labelText = stringResource(R.string.registration_date),
        placeHolderText = stringResource(R.string.registration_date),
        icon = Icons.Default.ArrowCircleDown,
        iconCD = stringResource(id = R.string.cd_registration_date),
        onValueChanged = onRegistrationDateValueChanged,
    )
    // Sale date
    EditScreenTextFieldItem(
        storedDate = if (property?.saleDate != null) property.saleDate.toString() else "",
        labelText = stringResource(R.string.sale_date),
        placeHolderText = stringResource(R.string.sale_date),
        icon = Icons.Default.ArrowCircleUp,
        iconCD = stringResource(id = R.string.cd_sale_date),
        onValueChanged = onSaleDateValueChanged,
        clearableDate = true,
    )

}

