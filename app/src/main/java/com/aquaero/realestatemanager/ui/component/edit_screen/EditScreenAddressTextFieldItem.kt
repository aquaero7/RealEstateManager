package com.aquaero.realestatemanager.ui.component.edit_screen

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.utils.areDigitsOnly

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreenAddressTextFieldItem(
    minLines: Int = 1,
    maxLines: Int = 1,
    fieldFontSize: TextUnit,
    placeHolderText: String,
    itemText: String?,
    shouldBeDigitsOnly: Boolean = false,
    field: String,
    onValueChange: (String, String) -> Unit,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    var isValid by remember { mutableStateOf(true) }
    var fieldText: String? by remember(itemText) { mutableStateOf(itemText) }
    val focusManager = LocalFocusManager.current

    fieldText?.let { it ->
        BasicTextField(
            modifier = Modifier,
            enabled = enabled,
            singleLine = (maxLines == 1),
            minLines = minLines,
            maxLines = maxLines,
            value = it,
            onValueChange = {
                isValid = !shouldBeDigitsOnly || (it.isNotEmpty() && it.areDigitsOnly())
                if (isValid) {
                    fieldText = it
                    onValueChange(field, it)
                }
            },
            textStyle = TextStyle(fontSize = fieldFontSize),
            keyboardOptions = if (shouldBeDigitsOnly) {
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            } else {
                KeyboardOptions.Default
            },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() } ),
            decorationBox = { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = it,
                    innerTextField = {
                        Box(
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            innerTextField()
                        }
                    },
                    enabled = enabled,
                    singleLine = (maxLines == 1),
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    placeholder = {
                        Text(
                            modifier = Modifier.alpha(0.5F),
                            text = placeHolderText,
                            fontSize = fieldFontSize,
                        )
                    },
                    isError = !isValid,
                    contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                        top = 0.dp, bottom = 0.dp, start = 0.dp, end = 4.dp
                    ),
                )
            },
        )
    }
}
