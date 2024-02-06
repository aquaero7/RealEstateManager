package com.aquaero.realestatemanager.ui.component.edit_screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.OtherHouses
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.Type

@Composable
fun EditScreenColumn1(
    stringTypes: MutableList<String>,
    stringType: String?,
    property: Property?,
    currency: String,
    onPriceValueChange: (String) -> Unit,
    onSurfaceValueChange: (String) -> Unit,
    onDropdownMenuValueChange: (String) -> Unit,
    onNbOfRoomsValueChange: (String) -> Unit,
    onNbOfBathroomsValueChange: (String) -> Unit,
    onNbOfBedroomsValueChange: (String) -> Unit,
) {
    // Price
    EditScreenTextFieldItem(
        labelText = "${stringResource(id = R.string.price_in)} $currency",
        placeHolderText = "${stringResource(id = R.string.price_in)} $currency",
        icon = Icons.Default.Money,
        iconCD = stringResource(id = R.string.cd_price),
        onValueChange = onPriceValueChange,
        itemText = property?.priceInCurrency(currency)?.toString() ?: "",
        shouldBeDigitsOnly = true,
    )
    // Type
    EditScreenTextFieldItem(
        labelText = stringResource(id = R.string.type),
        placeHolderText = stringResource(id = R.string.type),
        icon = Icons.Default.House,
        iconCD = stringResource(id = R.string.cd_type),
        onValueChange = onDropdownMenuValueChange,
        stringItems = stringTypes,
        stringItem = stringType,
        dropdownMenuCategory = DropdownMenuCategory.TYPE,
    )
    // Surface
    EditScreenTextFieldItem(
        labelText = "${stringResource(id = R.string.surface_in)} ${stringResource(id = R.string.surface_unit)}",
        placeHolderText = "${stringResource(id = R.string.surface_in)} ${stringResource(id = R.string.surface_unit)}",
        icon = Icons.Default.AspectRatio,
        iconCD = stringResource(id = R.string.cd_surface),
        onValueChange = onSurfaceValueChange,
        itemText = property?.surface?.toString() ?: "",
        shouldBeDigitsOnly = true,
    )
    // Number of rooms
    EditScreenTextFieldItem(
        labelText = stringResource(id = R.string.rooms),
        placeHolderText = stringResource(id = R.string.rooms),
        icon = Icons.Default.OtherHouses,
        iconCD = stringResource(id = R.string.cd_rooms),
        onValueChange = onNbOfRoomsValueChange,
        itemText = property?.nbOfRooms?.toString() ?: "",
        shouldBeDigitsOnly = true,
    )
    // Number of bathrooms
    EditScreenTextFieldItem(
        labelText = stringResource(id = R.string.bathrooms),
        placeHolderText = stringResource(id = R.string.bathrooms),
        icon = Icons.Default.Bathtub,
        iconCD = stringResource(id = R.string.cd_bathrooms),
        onValueChange = onNbOfBathroomsValueChange,
        itemText = property?.nbOfBathrooms?.toString() ?: "",
        shouldBeDigitsOnly = true,
    )
    // Number of bedrooms
    EditScreenTextFieldItem(
        labelText = stringResource(id = R.string.bedrooms),
        placeHolderText = stringResource(id = R.string.bedrooms),
        icon = Icons.Default.Bed,
        iconCD = stringResource(id = R.string.cd_bedrooms),
        onValueChange = onNbOfBedroomsValueChange,
        itemText = property?.nbOfBedrooms?.toString() ?: "",
        shouldBeDigitsOnly = true,
    )
}