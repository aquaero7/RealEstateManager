package com.aquaero.realestatemanager.ui.component.loan_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.QuestionMark
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
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.ui.theme.Red
import com.aquaero.realestatemanager.ui.theme.White
import com.aquaero.realestatemanager.utils.isDecimal

@Composable
fun LoanScreenTextField(
    fieldFontSize: TextUnit = 16.sp,
    labelFontSize: TextUnit = 14.sp,
    iconSize: Dp = 40.dp,
    fieldHeight: Dp = 40.dp,
    labelText: String,
    icon: ImageVector,
    iconCD: String,
    text: String?,
    shouldBeDigitsOnly: Boolean = true,
    shouldBeDecimal: Boolean,
    onValueChange: (String) -> Unit,
    onClearButtonClick: () -> Unit,
) {
    val value by remember { mutableStateOf(text) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1F)
                .wrapContentHeight()
                .border(width = 1.dp, color = MaterialTheme.colorScheme.tertiary),
        ) {
            // Icon
            Icon(
                modifier = Modifier
                    .size(iconSize)
                    .padding(horizontal = 4.dp),
                imageVector = icon,
                contentDescription = iconCD,
                tint = MaterialTheme.colorScheme.tertiary,
            )
            // Label
            Text(
                modifier = Modifier.padding(end = 2.dp),
                textAlign = TextAlign.Left,
                fontSize = labelFontSize,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary,
                text = labelText,
            )
            // Field
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
            ) {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(fieldHeight)
                        .padding(horizontal = 4.dp)
                        .background(color = MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    BasicLoanTextFieldItem(
                        shouldBeDigitsOnly = shouldBeDigitsOnly,
                        shouldBeDecimal = shouldBeDecimal,
                        fieldFontSize = fieldFontSize,
                        fieldValue = value,
                        onValueChange = onValueChange,
                        onClearButtonClick = onClearButtonClick,
                    )
                }
            }
        }
    }

}


@Composable
fun BasicLoanTextFieldItem(
    clearButtonSize: Dp = CLEAR_BUTTON_SIZE,
    shouldBeDigitsOnly: Boolean,
    shouldBeDecimal: Boolean,
    fieldFontSize: TextUnit,
    fieldValue: String?,
    onValueChange: (String) -> Unit,
    onClearButtonClick: () -> Unit,
) {
    var isValid by remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current
    var fieldText by remember { mutableStateOf(fieldValue) }

    Box(
        modifier = Modifier.fillMaxHeight(),
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
                color = MaterialTheme.colorScheme.onSurface
            ),
            singleLine = true,
            minLines = 1,
            maxLines = 1,
            value = fieldText ?: "",
            onValueChange = {
                isValid =
                    !shouldBeDigitsOnly || it.isEmpty() || if (shouldBeDecimal) isDecimal(it) else it.isDigitsOnly()
                if (isValid) {
                    fieldText = it
                    onValueChange(it)
                    Log.w("LoanScreen", "field = $it")
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

        if (!fieldText.isNullOrEmpty()) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(clearButtonSize),
                onClick = {
                    fieldText = ""
                    onClearButtonClick()
                }
            ) {
                Icon(
                    modifier = Modifier.background(color = White),
                    tint = Red,
                    imageVector = Icons.Default.Cancel,
                    contentDescription = stringResource(id = R.string.cd_button_clear),
                )
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun LoanScreenTextFieldPreview() {
    LoanScreenTextField(
        labelText = "Label",
        icon = Icons.Default.QuestionMark,
        iconCD = "",
        text = "Value",
        shouldBeDecimal = true,
        onValueChange = {},
        onClearButtonClick = {}
    )
}