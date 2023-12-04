package com.aquaero.realestatemanager.ui.component.edit_screen

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
) {
    var fieldText by remember(itemText) { mutableStateOf(itemText) }

    fieldText?.let { it ->
        TextField(
            modifier = Modifier
                .height(fieldHeight.dp)
                .widthIn(min = fieldMinWidth.dp)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            maxLines = maxLines,
            value = it,
            onValueChange = {
                fieldText = it
                onValueChanged(it)
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                // textAlign = TextAlign.Justify,
            ),
            label = {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = labelText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
            },
            placeholder = {
                Text(
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
            }
        )
    }
}