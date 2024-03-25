package com.aquaero.realestatemanager.ui.component.search_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.aquaero.realestatemanager.DP_CONTAINER_COLOR
import com.aquaero.realestatemanager.DP_TEXT_COLOR
import com.aquaero.realestatemanager.MAX
import com.aquaero.realestatemanager.MIN
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.ui.component.edit_screen.editScreenDatePickerColors
import com.aquaero.realestatemanager.ui.theme.Red
import com.aquaero.realestatemanager.utils.convertDateMillisToString
import com.aquaero.realestatemanager.utils.convertDateStringToMillis

@Composable
fun SearchScreenTextFieldMinMax(
    fieldFontSize: TextUnit = 16.sp,
    labelFontSize: TextUnit = 14.sp,
    iconSize: Dp = 40.dp,
    fieldHeight: Dp = 32.dp,
    labelText: String,
    icon: ImageVector,
    iconCD: String,
    onValueChange: (String, String) -> Unit,
    // For keyboard input
    shouldBeDigitsOnly: Boolean = true,                // True when the item is an input field
    // For DatePicker
    fieldsAreDates: Boolean = false,                    // True when the item is a DatePicker
) {
    var minValue by remember { mutableStateOf("") }
    var maxValue by remember { mutableStateOf("") }
    val onValidValue: (String, String) -> Unit = { textField, value ->
        when (textField) {
            MIN -> minValue = value
            MAX -> maxValue = value
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.tertiary),
    ) {
        // Row icon
        Icon(
            modifier = Modifier
                .size(iconSize)
                .padding(horizontal = 4.dp),
            imageVector = icon,
            contentDescription = iconCD,
            tint = MaterialTheme.colorScheme.tertiary,
        )
        // Row label
        Text(
            modifier = Modifier
                .defaultMinSize(minWidth = 64.dp)
                .padding(horizontal = 2.dp),
            textAlign = TextAlign.Left,
            fontSize = labelFontSize,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary,
            text = labelText,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
        ) {
            // Min label
            Text(
                modifier = Modifier
                    .weight(1F)
                    .padding(start = 2.dp),
                textAlign = TextAlign.Center,
                fontSize = labelFontSize,
                color = MaterialTheme.colorScheme.tertiary,
                text = stringResource(id = R.string.min),
            )

            // Min field
            Box(
                modifier = Modifier
                    .weight(2F)
                    .height(fieldHeight)
                    .background(color = MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextFieldMinMaxItem(
                    fieldsAreDates = fieldsAreDates,
                    fieldFontSize = fieldFontSize,
                    shouldBeDigitsOnly = shouldBeDigitsOnly,
                    fieldType = MIN,
                    maxValue = maxValue,
                    onValidValue = onValidValue,
                    onValueChange = onValueChange,
                )
            }

            // Max label
            Text(
                modifier = Modifier
                    .weight(1F)
                    .padding(start = 2.dp),
                textAlign = TextAlign.Center,
                fontSize = labelFontSize,
                color = MaterialTheme.colorScheme.tertiary,
                text = stringResource(id = R.string.max),
            )

            // Max field
            Box(
                modifier = Modifier
                    .weight(2F)
                    .height(fieldHeight)
                    .background(color = MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextFieldMinMaxItem(
                    fieldsAreDates = fieldsAreDates,
                    fieldFontSize = fieldFontSize,
                    shouldBeDigitsOnly = shouldBeDigitsOnly,
                    fieldType = MAX,
                    minValue = minValue,
                    onValidValue = onValidValue,
                    onValueChange = onValueChange,
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTextFieldMinMaxItem(
    fieldsAreDates: Boolean,
    fieldFontSize: TextUnit,
    shouldBeDigitsOnly: Boolean,
    fieldType: String,
    minValue: String = "",
    maxValue: String = "",
    onValidValue: (String, String) -> Unit,
    onValueChange: (String, String) -> Unit,
) {
    // For keyboard input
    var isValid by remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current
    // For DatePicker
    var openDpDialog by remember { mutableStateOf(false) }
    val onClick: () -> Unit = {
        // Triggered only if fields are !enabled (i.e., fieldsAreDates is true)
        if (fieldsAreDates) {
            openDpDialog = true
            Log.w("SearchScreen", "Click in $fieldType date field")
        }
    }
    // For all
    var fieldText by remember { mutableStateOf("") }
    val onSurfaceColor =  MaterialTheme.colorScheme.onSurface
    // Defines fields text color according to the correct ordering of min/max values
    fun orderCheckColor(value: String): Color {
        return if (value.isEmpty()) {
            onSurfaceColor
        } else {
            val isDisordered = if (value.isDigitsOnly()) {
                (fieldType == MIN && maxValue.isNotEmpty() && value.toInt() > maxValue.toInt())
                        || (fieldType == MAX && minValue.isNotEmpty() && value.toInt() < minValue.toInt())
            } else {
                (fieldType == MIN && maxValue.isNotEmpty() && value > maxValue)
                        || (fieldType == MAX && minValue.isNotEmpty() && value < minValue)
            }
            if (!isDisordered) onSurfaceColor else Red
        }
    }

    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                focusManager.clearFocus()
                onClick()
            },
        enabled = (!fieldsAreDates),
        textStyle = TextStyle(
            fontSize = fieldFontSize,
            textAlign = TextAlign.Center,
            color = orderCheckColor(fieldText)
        ),
        singleLine = true,
        minLines = 1,
        maxLines = 1,
        value = fieldText,
        onValueChange = {
            isValid = !shouldBeDigitsOnly || it.isEmpty() || it.isDigitsOnly()
            if (isValid) {
                fieldText = it
                onValidValue(fieldType, it)
                onValueChange(fieldType, it)
                Log.w("SearchScreen", "$fieldType = $it")
            }
        },
        keyboardOptions = if (shouldBeDigitsOnly) {
            KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        } else {
            KeyboardOptions.Default
        },
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() } ),
    )

    if (fieldsAreDates) {
        val dpState = rememberDatePickerState(
            initialSelectedDateMillis = convertDateStringToMillis(fieldText),
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
                                onValidValue(fieldType, fieldText)
                                onValueChange(fieldType, fieldText)
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


@Preview(showBackground = true)
@Composable
fun SearchScreenTextFieldMinMaxPreview() {
    SearchScreenTextFieldMinMax(
        labelText = "Label",
        icon = Icons.Default.QuestionMark,
        iconCD = "",
        onValueChange = { _, _ -> },
    )
}