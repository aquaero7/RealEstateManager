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
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Property

@Composable
fun EditScreenColumn1(
    property: Property?,
    currency: String,
    pTypeSet: () -> MutableSet<Int?>,
    pTypeIndex: Int?,
    onPriceValueChange: (String) -> Unit,
    onSurfaceValueChange: (String) -> Unit,
    onDropdownMenuValueChange: (String) -> Unit,
    onNbOfRoomsValueChange: (String) -> Unit,
    onNbOfBathroomsValueChange: (String) -> Unit,
    onNbOfBedroomsValueChange: (String) -> Unit,
) {
    // Price
    EditScreenTextFieldItem(
        itemText = property?.priceInCurrency(currency)?.toString() ?: "",
        labelText = "${stringResource(R.string.price_in)} $currency",
        placeHolderText = "${stringResource(R.string.price_in)} $currency",
        icon = Icons.Default.Money,
        iconCD = stringResource(id = R.string.cd_price),
        onValueChange = onPriceValueChange,
        shouldBeDigitsOnly = true,
    )
    // Type
    EditScreenTextFieldItem(
        labelText = stringResource(R.string.type),
        placeHolderText = stringResource(R.string.type),
        icon = Icons.Default.House,
        iconCD = stringResource(id = R.string.cd_type),
        itemsSet = pTypeSet,
        index = pTypeIndex,
        onValueChange = onDropdownMenuValueChange,
    )
    // Surface
    EditScreenTextFieldItem(
        itemText = property?.surface?.toString() ?: "",
        labelText = "${stringResource(R.string.surface_in)} ${stringResource(R.string.surface_unit)}",
        placeHolderText = "${stringResource(R.string.surface_in)} ${stringResource(R.string.surface_unit)}",
        icon = Icons.Default.AspectRatio,
        iconCD = stringResource(id = R.string.cd_surface),
        onValueChange = onSurfaceValueChange,
        shouldBeDigitsOnly = true,
    )
    // Number of rooms
    EditScreenTextFieldItem(
        itemText = property?.nbOfRooms?.toString() ?: "",
        labelText = stringResource(R.string.rooms),
        placeHolderText = stringResource(R.string.rooms),
        icon = Icons.Default.OtherHouses,
        iconCD = stringResource(id = R.string.cd_rooms),
        onValueChange = onNbOfRoomsValueChange,
        shouldBeDigitsOnly = true,
    )
    // Number of bathrooms
    EditScreenTextFieldItem(
        itemText = property?.nbOfBathrooms?.toString() ?: "",
        labelText = stringResource(R.string.bathrooms),
        placeHolderText = stringResource(R.string.bathrooms),
        icon = Icons.Default.Bathtub,
        iconCD = stringResource(id = R.string.cd_bathrooms),
        onValueChange = onNbOfBathroomsValueChange,
        shouldBeDigitsOnly = true,
    )
    // Number of bedrooms
    EditScreenTextFieldItem(
        itemText = property?.nbOfBedrooms?.toString() ?: "",
        labelText = stringResource(R.string.bedrooms),
        placeHolderText = stringResource(R.string.bedrooms),
        icon = Icons.Default.Bed,
        iconCD = stringResource(id = R.string.cd_bedrooms),
        onValueChange = onNbOfBedroomsValueChange,
        shouldBeDigitsOnly = true,
    )
}