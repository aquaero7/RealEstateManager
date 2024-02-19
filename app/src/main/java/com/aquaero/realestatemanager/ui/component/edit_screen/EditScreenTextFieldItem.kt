package com.aquaero.realestatemanager.ui.component.edit_screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.aquaero.realestatemanager.DP_CONTAINER_COLOR
import com.aquaero.realestatemanager.DP_TEXT_COLOR
import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.ui.theme.Red
import com.aquaero.realestatemanager.utils.convertDateMillisToString
import com.aquaero.realestatemanager.utils.convertDateStringToMillis
import com.aquaero.realestatemanager.utils.textWithEllipsis

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreenTextFieldItem(
    minLines: Int = 1,
    maxLines: Int = 1,
    fieldFontSize: TextUnit = 16.sp,
    labelFontSize: TextUnit = 14.sp,
    iconSize: Dp = 40.dp,
    labelText: String,
    placeHolderText: String,
    icon: ImageVector,
    iconCD: String,
    onValueChange: (String) -> Unit,
    // For keyboard input
    itemText: String? = null,                           // Not null when the item is an input field
    shouldBeDigitsOnly: Boolean = false,
    // For DropDownMenu
    stringItems: MutableList<String>? = null,           // Not null when the item is a DropdownMenu
    stringItem: String? = null,
    dropdownMenuCategory: DropdownMenuCategory? = null, // Not null when the item is a DropdownMenu
    // For DatePicker
    storedDate: String? = null,                         // Not null when the item is a DatePicker
    clearableDate: Boolean = false,
) {
    // For keyboard input
    var isValid by remember { mutableStateOf(true) }

    // For DropdownMenu
    val isDropdownMenu = dropdownMenuCategory != null
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(stringItems?.indexOf(stringItem) ?: 0) }
    val unassigned = stringResource(id = R.string._unassigned_)
    var selectedItem by remember { mutableStateOf(stringItem ?: unassigned) }
    /*  // TODO: To be deleted
    var selectedItem by remember {
        if (stringItem != null && stringItem != CACHE_AGENT.toString() && stringItem != CACHE_TYPE.typeId) {
            mutableStateOf(stringItem)
        } else {
            mutableStateOf(unassigned)
        }
    }
    */

    // For DatePicker
    val isDatePicker = storedDate != null
    var openDpDialog by remember { mutableStateOf(false) }

    // For DropdownMenu and DatePicker
    val onClick: () -> Unit = {
        // Triggered only if field is !enabled (i.e., if dropdownMenuCategory or storedDate is not null)
        if (isDropdownMenu) {
            expanded = true
            Log.w("EditScreen", "Click in list field")
        }
        if (isDatePicker) {
            openDpDialog = true
            Log.w("EditScreen", "Click in date field")
        }
    }

    // For all
    var fieldText: String? by remember(itemText, selectedItem, storedDate) {
        if (isDropdownMenu) {
            mutableStateOf(
                textWithEllipsis(fullText = selectedItem, maxLength = 14, maxLines = maxLines)
            )
        } else if (isDatePicker) {
            mutableStateOf(storedDate)
        } else {
            mutableStateOf(itemText)
        }
        // At least one of the 3 items (itemText, selectedItem, storedDate) is not null.
        // Thus, fieldText is never null.
    }
    val trailingIcon: @Composable (() -> Unit)? = if (clearableDate && !fieldText.isNullOrBlank()) {
        {
            IconButton(
                modifier = Modifier
                    .size(24.dp)
                    .offset(y = 10.dp),
                onClick = {
                    fieldText = ""
                    onValueChange(fieldText!!)
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = stringResource(id = R.string.cd_button_cancel),
                    tint = Red,
                )
            }
        }
    } else null

    Box {
        fieldText?.let { it ->
            TextField(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .clickable(onClick = onClick),
                enabled = (dropdownMenuCategory == null && storedDate == null),
                // Field must be !enabled to allow a click to launch a specific action.
                // In that case, text color must be restored from disabled to normal when filed is !enabled.
                colors = TextFieldDefaults.colors(disabledTextColor = MaterialTheme.colorScheme.onSurface),
                singleLine = (maxLines == 1),
                minLines = minLines,
                maxLines = maxLines,
                value = it,
                onValueChange = {
                    isValid = !shouldBeDigitsOnly || it.isEmpty() || it.isDigitsOnly()
                    if (isValid) {
                        fieldText = it
                        onValueChange(it)
                    }
                },
                textStyle = TextStyle(fontSize = fieldFontSize),
                label = {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = labelText,
                        fontSize = labelFontSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                },
                placeholder = {
                    Text(
                        modifier = Modifier.alpha(0.5F),
                        text = placeHolderText,
                        fontSize = fieldFontSize,
                    )
                },
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        imageVector = icon,
                        contentDescription = iconCD,
                        tint = MaterialTheme.colorScheme.tertiary,
                    )
                },
                trailingIcon = trailingIcon,
                keyboardOptions = if (shouldBeDigitsOnly) {
                    KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                } else {
                    KeyboardOptions.Default
                },
                isError = !isValid,
            )
        }

        if (isDropdownMenu) {
            // List
            DropdownMenu(
                expanded = expanded,
                offset = DpOffset(x = 32.dp, y = (-16).dp),
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(160.dp)
                    .wrapContentSize()
            ) {
                // 'stringItems' is not null when the item is a DropdownMenu
                stringItems!!.forEachIndexed { index, s ->
                    DropdownMenuItem(
                        text = { Text(text = s, fontSize = fieldFontSize) },
                        onClick = {
                            selectedIndex = index
                            selectedItem = s
                            onValueChange("${dropdownMenuCategory!!.name}#$index")
                            expanded = false
                        },
                    )
                    HorizontalDivider(thickness = 1.dp)
                }
            }
        }
    }

    if (isDatePicker) {
        val dpState = rememberDatePickerState(
            initialSelectedDateMillis = convertDateStringToMillis(fieldText!!),
            initialDisplayMode = DisplayMode.Picker
        )

        if (openDpDialog) {
            DatePickerDialog(
                shape = DatePickerDefaults.shape,
                colors = DatePickerDefaults.colors(containerColor = DP_CONTAINER_COLOR,),
                onDismissRequest = { openDpDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (dpState.selectedDateMillis != null) {
                                fieldText = convertDateMillisToString(dpState.selectedDateMillis!!)
                                onValueChange(fieldText!!)
                            }
                            openDpDialog = false
                        },
                    ) {
                        Text(
                            text = stringResource(id = R.string.ok),
                            color = DP_TEXT_COLOR,
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { openDpDialog = false },
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            color = DP_TEXT_COLOR,
                        )
                    }
                },
            ) {
                DatePicker(
                    state = dpState,
                    colors = editScreenDatePickerColors(),
                )
            }
        }
    }

}

