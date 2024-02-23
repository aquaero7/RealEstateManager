package com.aquaero.realestatemanager.ui.component.edit_screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.Field
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Property

@Composable
fun EditScreenColumn2(
    stringAgents: MutableList<String>,
    stringAgent: String?,
    property: Property?,
    address: Address?,
    onFieldValueChange: (String, String) -> Unit,
    onDropdownMenuValueChange: (String) -> Unit,
) {
    // Location
    EditScreenAddressItem(
        labelText = stringResource(id = R.string.address),
        streetNumberPlaceHolderText = stringResource(id = R.string.street_number),
        streetNamePlaceHolderText = stringResource(id = R.string.street_name),
        addInfoPlaceHolderText = stringResource(id = R.string.add_info),
        cityPlaceHolderText = stringResource(id = R.string.city),
        statePlaceHolderText = stringResource(id = R.string.state),
        zipCodePlaceHolderText = stringResource(id = R.string.zip_code),
        countryPlaceHolderText = stringResource(id = R.string.country),
        icon = Icons.Default.LocationOn,
        iconCD = stringResource(id = R.string.cd_address),
        onFieldValueChange = onFieldValueChange,
        item = address,
    )
    // Agent
    EditScreenTextFieldItem(
        maxLines = 2,
        labelText = stringResource(id = R.string.agent),
        placeHolderText = stringResource(id = R.string.agent),
        icon = Icons.Default.Person,
        iconCD = stringResource(id = R.string.cd_agent),
        onValueChange = onDropdownMenuValueChange,
        stringItems = stringAgents,
        stringItem = stringAgent,
        dropdownMenuCategory = DropdownMenuCategory.AGENT,
    )
    // Registration date
    EditScreenTextFieldItem(
        labelText = stringResource(id = R.string.registration_date),
        placeHolderText = stringResource(id = R.string.registration_date),
        icon = Icons.Default.ArrowCircleDown,
        iconCD = stringResource(id = R.string.cd_registration_date),
        onValueChange = { onFieldValueChange(Field.REGISTRATION_DATE.name, it) },
        storedDate = property?.registrationDate ?: "",
        clearableDate = true,
    )
    // Sale date
    EditScreenTextFieldItem(
        labelText = stringResource(id = R.string.sale_date),
        placeHolderText = stringResource(id = R.string.sale_date),
        icon = Icons.Default.ArrowCircleUp,
        iconCD = stringResource(id = R.string.cd_sale_date),
        onValueChange = { onFieldValueChange(Field.SALE_DATE.name, it) },
        storedDate = property?.saleDate ?: "",
        clearableDate = true,
    )
}

