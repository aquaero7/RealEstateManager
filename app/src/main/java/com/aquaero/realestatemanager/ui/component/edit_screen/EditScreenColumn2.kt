package com.aquaero.realestatemanager.ui.component.edit_screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Property

@Composable
fun EditScreenColumn2(
    property: Property?,
    addresses: List<Address>,
    agentSet: () -> MutableSet<String?>,
    agentIndex: Int?,
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
        labelText = stringResource(R.string.address),
        streetNumberPlaceHolderText = stringResource(R.string.street_number),
        streetNamePlaceHolderText = stringResource(R.string.street_name),
        addInfoPlaceHolderText = stringResource(R.string.add_info),
        cityPlaceHolderText = stringResource(R.string.city),
        statePlaceHolderText = stringResource(R.string.state),
        zipCodePlaceHolderText = stringResource(R.string.zip_code),
        countryPlaceHolderText = stringResource(R.string.country),
        icon = Icons.Default.LocationOn,
        iconCD = stringResource(id = R.string.cd_address),
        // item = property?.addressId,
        item = addresses.find { it.addressId == property?.addressId },
        onStreetNumberValueChange = onStreetNumberValueChange,
        onStreetNameValueChange = onStreetNameValueChange,
        onAddInfoValueChange = onAddInfoValueChange,
        onCityValueChange = onCityValueChange,
        onStateValueChange = onStateValueChange,
        onZipCodeValueChange = onZipCodeValueChange,
        onCountryValueChange = onCountryValueChange,
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
        onValueChange = onDropdownMenuValueChange,
    )
    // Registration date
    EditScreenTextFieldItem(
        storedDate = if (property?.registrationDate != null) property.registrationDate.toString() else "",
        labelText = stringResource(R.string.registration_date),
        placeHolderText = stringResource(R.string.registration_date),
        icon = Icons.Default.ArrowCircleDown,
        iconCD = stringResource(id = R.string.cd_registration_date),
        onValueChange = onRegistrationDateValueChange,
        clearableDate = true,
    )
    // Sale date
    EditScreenTextFieldItem(
        storedDate = if (property?.saleDate != null) property.saleDate.toString() else "",
        labelText = stringResource(R.string.sale_date),
        placeHolderText = stringResource(R.string.sale_date),
        icon = Icons.Default.ArrowCircleUp,
        iconCD = stringResource(id = R.string.cd_sale_date),
        onValueChange = onSaleDateValueChange,
        clearableDate = true,
    )
}

