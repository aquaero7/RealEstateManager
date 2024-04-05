package com.aquaero.realestatemanager.ui.component.search_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaero.realestatemanager.CLEAR_BUTTON_SIZE
import com.aquaero.realestatemanager.Field
import com.aquaero.realestatemanager.MAX
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.ui.theme.Red

@Composable
fun SearchScreenTextField(
    fieldFontSize: TextUnit = 16.sp,
    labelFontSize: TextUnit = 14.sp,
    iconSize: Dp = 40.dp,
    fieldHeight: Dp = 40.dp,
    leftLocationField: String,
    leftIcon: ImageVector,
    leftIconCD: String,
    leftLabel: String,
    leftText: String?,
    rightLocationField: String? = null,
    rightIcon: ImageVector? = null,
    rightIconCD: String? = null,
    rightLabel: String? = null,
    rightText: String? = null,
    onValueChange: (String, String?, String) -> Unit,
    onClearButtonClick: (String) -> Unit,
) {
    val leftValue by remember { mutableStateOf(leftText) }
    val rightValue by remember { mutableStateOf(rightText) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        // Left item
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1F)
                .wrapContentHeight()
                .border(width = 1.dp, color = MaterialTheme.colorScheme.tertiary),
        ) {
            // Left icon
            Icon(
                modifier = Modifier
                    .size(iconSize)
                    .padding(horizontal = 4.dp),
                imageVector = leftIcon,
                contentDescription = leftIconCD,
                tint = MaterialTheme.colorScheme.tertiary,
            )
            // Left label
            Text(
                modifier = Modifier.padding(end = 2.dp),
                textAlign = TextAlign.Left,
                fontSize = labelFontSize,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary,
                text = leftLabel,
            )
            // Left field
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
                    BasicTextFieldItem(
                        field = leftLocationField,
                        fieldFontSize = fieldFontSize,
                        fieldValue = leftValue,
                        onValueChange = onValueChange,
                        onClearButtonClick = onClearButtonClick,
                    )
                }
            }
        }

        rightLocationField?.let {
            Spacer(modifier = Modifier.width(4.dp))

            // Right item
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1F)
                    .wrapContentHeight()
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.tertiary),
            ) {
                // Right icon
                Icon(
                    modifier = Modifier
                        .size(iconSize)
                        .padding(horizontal = 4.dp),
                    imageVector = rightIcon ?: Icons.Default.QuestionMark,
                    contentDescription = rightIconCD,
                    tint = MaterialTheme.colorScheme.tertiary,
                )
                // Right label
                Text(
                    modifier = Modifier.padding(end = 2.dp),
                    textAlign = TextAlign.Left,
                    fontSize = labelFontSize,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                    text = rightLabel ?: "",
                )
                // Right field
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
                        BasicTextFieldItem(
                            field = rightLocationField,
                            fieldFontSize = fieldFontSize,
                            fieldValue = rightValue,
                            onValueChange = onValueChange,
                            onClearButtonClick = onClearButtonClick,
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun BasicTextFieldItem(
    clearButtonSize: Dp = CLEAR_BUTTON_SIZE,
    field: String,
    fieldFontSize: TextUnit,
    fieldValue: String?,
    onValueChange: (String, String?, String) -> Unit,
    onClearButtonClick: (String) -> Unit,
) {
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
                fieldText = it
                onValueChange(field, null, it)
                Log.w("SearchScreen", "$field = $it")
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
                    onClearButtonClick(field)
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
fun SearchScreenTextFieldPreview() {
    SearchScreenTextField(
        leftLocationField = Field.DESCRIPTION.name,
        leftIcon = Icons.Default.QuestionMark,
        leftIconCD = "",
        leftLabel = stringResource(id = R.string.description),
        leftText = "Left value",
        //
        rightLocationField = Field.DESCRIPTION.name,
        rightIcon = Icons.Default.QuestionMark,
        rightIconCD = "",
        rightLabel = stringResource(id = R.string.description),
        rightText = "Right value",
        //
        onValueChange = { _, _, _ -> },
        onClearButtonClick = {}
    )
}