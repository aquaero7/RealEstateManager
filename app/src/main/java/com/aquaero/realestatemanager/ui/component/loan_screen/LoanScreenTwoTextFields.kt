package com.aquaero.realestatemanager.ui.component.loan_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.aquaero.realestatemanager.CLEAR_BUTTON_SIZE
import com.aquaero.realestatemanager.LoanField
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.ui.theme.Red

@Composable
fun LoanScreenTwoTextFields(
    fieldFontSize: TextUnit = 16.sp,
    labelFontSize: TextUnit = 14.sp,
    iconSize: Dp = 40.dp,
    fieldHeight: Dp = 40.dp,
    labelText: String,
    icon: ImageVector,
    iconCD: String,
    yearsText: String?,
    monthsText: String?,
    shouldBeDigitsOnly: Boolean = true,
    onValueChange: (String, String) -> Unit,
    onClearButtonClick: (String) -> Unit,
) {
    var yearsValue by remember { mutableStateOf(yearsText) }
    var monthsValue by remember { mutableStateOf(monthsText) }
    val onValidValue: (String, String) -> Unit = { field, value ->
        when (field) {
            LoanField.YEARS.name -> yearsValue = value
            LoanField.MONTHS.name -> monthsValue = value
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

        // Row fields
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
        ) {
            // Years label
            Text(
                modifier = Modifier
                    .weight(1F)
                    .padding(start = 2.dp),
                textAlign = TextAlign.Center,
                fontSize = labelFontSize,
                color = MaterialTheme.colorScheme.tertiary,
                text = stringResource(id = R.string.years),
            )

            // Years field
            Box(
                modifier = Modifier
                    .weight(1F)
                    .height(fieldHeight)
                    .background(color = MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicLoanTwoTextFieldsItem(
                    fieldFontSize = fieldFontSize,
                    shouldBeDigitsOnly = shouldBeDigitsOnly,
                    unit = LoanField.YEARS.name,
                    fieldValue = yearsValue ?: "",
                    onValidValue = onValidValue,
                    onValueChange = onValueChange,
                    onClearButtonClick = onClearButtonClick,
                )
            }

            // Months label
            Text(
                modifier = Modifier
                    .weight(1F)
                    .padding(start = 2.dp),
                textAlign = TextAlign.Center,
                fontSize = labelFontSize,
                color = MaterialTheme.colorScheme.tertiary,
                text = stringResource(id = R.string.months),
            )

            // Months field
            Box(
                modifier = Modifier
                    .weight(1F)
                    .height(fieldHeight)
                    .background(color = MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicLoanTwoTextFieldsItem(
                    fieldFontSize = fieldFontSize,
                    shouldBeDigitsOnly = shouldBeDigitsOnly,
                    unit = LoanField.MONTHS.name,
                    fieldValue = monthsValue ?: "",
                    onValidValue = onValidValue,
                    onValueChange = onValueChange,
                    onClearButtonClick = onClearButtonClick,
                )
            }

            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicLoanTwoTextFieldsItem(
    clearButtonSize: Dp = CLEAR_BUTTON_SIZE,
    fieldFontSize: TextUnit,
    shouldBeDigitsOnly: Boolean,
    unit: String,
    fieldValue: String,
    onValidValue: (String, String) -> Unit,
    onValueChange: (String, String) -> Unit,
    onClearButtonClick: (String) -> Unit,
) {
    var isValid by remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current
    var fieldText by remember { mutableStateOf(fieldValue) }
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    Box(
        modifier = Modifier.fillMaxHeight()
    ) {
        BasicTextField(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth()
                .clickable { focusManager.clearFocus() },
            enabled = true,
            textStyle = TextStyle(
                fontSize = fieldFontSize,
                textAlign = TextAlign.Center,
                color = onSurfaceColor
            ),
            singleLine = true,
            minLines = 1,
            maxLines = 1,
            value = fieldText,
            onValueChange = {
                isValid = !shouldBeDigitsOnly || it.isEmpty() || it.isDigitsOnly()
                if (isValid) {
                    fieldText = it
                    onValidValue(unit, it)
                    onValueChange(unit, it)
                    Log.w("LoanScreen", "$unit = $it")
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
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        )

        if (fieldText.isNotEmpty()) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(clearButtonSize),
                onClick = {
                    fieldText = ""
                    onValidValue(unit, "")
                    onClearButtonClick(unit)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = stringResource(id = R.string.cd_button_clear),
                    tint = Red
                )
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun LoanScreenTwoTextFieldsPreview() {
    LoanScreenTwoTextFields(
        labelText = "Label",
        icon = Icons.Default.QuestionMark,
        iconCD = "",
        yearsText = "MIN value",
        monthsText = "MAX value",
        onValueChange = { _, _ -> },
        onClearButtonClick = {},
    )
}