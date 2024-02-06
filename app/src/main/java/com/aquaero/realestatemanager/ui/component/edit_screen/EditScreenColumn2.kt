package com.aquaero.realestatemanager.ui.component.edit_screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Property

@Composable
fun EditScreenColumn2(
    stringAgents: MutableList<String>,
    stringAgent: String?,
    property: Property?,
    addresses: List<Address>,
    onStreetNumberValueChange: (String) -> Unit,
    onStreetNameValueChange: (String) -> Unit,
    onAddInfoValueChange: (String) -> Unit,
    onCityValueChange: (String) -> Unit,
    onStateValueChange: (String) -> Unit,
    onZipCodeValueChange: (String) -> Unit,
    onCountryValueChange: (String) -> Unit,
    onDropdownMenuValueChange: (String) -> Unit,
    onRegistrationDateValueChange: (String) -> Unit,
    onSaleDateValueChange: (String) -> Unit,
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
        onStreetNumberValueChange = onStreetNumberValueChange,
        onStreetNameValueChange = onStreetNameValueChange,
        onAddInfoValueChange = onAddInfoValueChange,
        onCityValueChange = onCityValueChange,
        onStateValueChange = onStateValueChange,
        onZipCodeValueChange = onZipCodeValueChange,
        onCountryValueChange = onCountryValueChange,
        item = addresses.find { it.addressId == property?.addressId },
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
        onValueChange = onRegistrationDateValueChange,
        storedDate = property?.registrationDate ?: "",
        clearableDate = true,
    )
    // Sale date
    EditScreenTextFieldItem(
        labelText = stringResource(id = R.string.sale_date),
        placeHolderText = stringResource(id = R.string.sale_date),
        icon = Icons.Default.ArrowCircleUp,
        iconCD = stringResource(id = R.string.cd_sale_date),
        onValueChange = onSaleDateValueChange,
        storedDate = property?.saleDate ?: "",
        clearableDate = true,
    )
}

