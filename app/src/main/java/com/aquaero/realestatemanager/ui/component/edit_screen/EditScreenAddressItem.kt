package com.aquaero.realestatemanager.ui.component.edit_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaero.realestatemanager.Field
import com.aquaero.realestatemanager.model.Address

@Composable
fun EditScreenAddressItem(
    fieldFontSize: TextUnit = 14.sp,
    labelFontSize: TextUnit = 14.sp,
    iconSize: Dp = 40.dp,
    labelText: String,
    streetNumberPlaceHolderText: String,
    streetNamePlaceHolderText: String,
    addInfoPlaceHolderText: String,
    cityPlaceHolderText: String,
    statePlaceHolderText: String,
    zipCodePlaceHolderText: String,
    countryPlaceHolderText: String,
    icon: ImageVector,
    iconCD: String,
    onFieldValueChange: (String, String) -> Unit,
    item: Address?,
) {
    Surface(
        modifier = Modifier
            .height(intrinsicSize = IntrinsicSize.Max)
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Row {
            // Icon
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 4.dp, horizontal = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    imageVector = icon,
                    contentDescription = iconCD,
                    tint = MaterialTheme.colorScheme.tertiary,
                )
            }

            Column(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp)
            ) {
                // Label
                Text(
                    text = labelText,
                    fontSize = labelFontSize,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Address
                Column(
                    modifier = Modifier
                        // .height(72.dp)       // To use instead of wrapContentHeight to enable scroll
                        .wrapContentHeight()    // To use instead of height to display all address fields
                        .fillMaxWidth()
                        .verticalScroll(state = rememberScrollState(), enabled = true)
                ) {
                    item.let {
                        // Street Number
                        EditScreenAddressTextFieldItem(
                            fieldFontSize = fieldFontSize,
                            placeHolderText = streetNumberPlaceHolderText,
                            field = Field.STREET_NUMBER.name,
                            onValueChange = onFieldValueChange,
                            itemText = it?.streetNumber ?: "",
                        )
                        // Street name
                        EditScreenAddressTextFieldItem(
                            fieldFontSize = fieldFontSize,
                            placeHolderText = streetNamePlaceHolderText,
                            field = Field.STREET_NAME.name,
                            onValueChange = onFieldValueChange,
                            itemText = it?.streetName ?: "",
                        )
                        // Additional information
                        EditScreenAddressTextFieldItem(
                            fieldFontSize = fieldFontSize,
                            placeHolderText = addInfoPlaceHolderText,
                            field = Field.ADD_INFO.name,
                            onValueChange = onFieldValueChange,
                            itemText = it?.addInfo ?: "",
                        )
                        // City
                        EditScreenAddressTextFieldItem(
                            fieldFontSize = fieldFontSize,
                            placeHolderText = cityPlaceHolderText,
                            field = Field.CITY.name,
                            onValueChange = onFieldValueChange,
                            itemText = it?.city ?: "",
                        )
                        // State
                        EditScreenAddressTextFieldItem(
                            fieldFontSize = fieldFontSize,
                            placeHolderText = statePlaceHolderText,
                            field = Field.STATE.name,
                            onValueChange = onFieldValueChange,
                            itemText = it?.state ?: "",
                        )
                        // ZIP code
                        EditScreenAddressTextFieldItem(
                            fieldFontSize = fieldFontSize,
                            placeHolderText = zipCodePlaceHolderText,
                            field = Field.ZIP_CODE.name,
                            onValueChange = onFieldValueChange,
                            itemText = it?.zipCode ?: "",
                        )
                        // Country
                        EditScreenAddressTextFieldItem(
                            fieldFontSize = fieldFontSize,
                            placeHolderText = countryPlaceHolderText,
                            field = Field.COUNTRY.name,
                            onValueChange = onFieldValueChange,
                            itemText = it?.country ?: "",
                        )
                    }
                }
            }
        }
    }
}