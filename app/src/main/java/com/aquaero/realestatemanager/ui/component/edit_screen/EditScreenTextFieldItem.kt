package com.aquaero.realestatemanager.ui.component.edit_screen

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.aquaero.realestatemanager.DATE_LENGTH

@Composable
fun EditScreenTextFieldItem(
    fieldHeight: Int = 76,
    fieldMinWidth: Int = 0,
    maxLines: Int = 1,
    itemText: String?,
    labelText: String,
    placeHolderText: String,
    icon: ImageVector,
    iconCD: String,
    onValueChanged: (String) -> Unit,
    shouldBeDigitsOnly: Boolean = false,
) {
    var fieldText by remember(itemText) { mutableStateOf(itemText) }
    var isValid by remember { mutableStateOf(true) }

    fieldText?.let { it ->
        TextField(
            modifier = Modifier
                .height(fieldHeight.dp)
                .widthIn(min = fieldMinWidth.dp)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            maxLines = maxLines,
            value = it,
            onValueChange = {
                isValid = !shouldBeDigitsOnly || (it.isNotEmpty() && it.isDigitsOnly())
                if (isValid) {
                    fieldText = it
                    onValueChanged(it)
                }
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                // color = if (!isValid) Red else MaterialTheme.colorScheme.onSurface,
                // textAlign = TextAlign.Justify,
            ),
            label = {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = labelText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(0.5F),
                    text = placeHolderText,
                    fontSize = 16.sp,
                )
            },
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = icon,
                    contentDescription = iconCD,
                    tint = MaterialTheme.colorScheme.tertiary,
                )
            },
            keyboardOptions = if (shouldBeDigitsOnly) {
                KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            } else {
                KeyboardOptions.Default
            },
            isError = !isValid,
        )
        // if (!isValid) Text(text = stringResource(id = R.string.invalid_input), color = Red, fontSize = 12.sp)
    }
}
