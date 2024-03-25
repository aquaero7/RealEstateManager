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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.utils.textWithEllipsis

@Composable
fun SearchScreenDropdownMenu(
    stringTypes: MutableList<String>,
    stringAgents: MutableList<String>,
    fieldFontSize: TextUnit = 16.sp,
    labelFontSize: TextUnit = 14.sp,
    iconSize: Dp = 40.dp,
    fieldHeight: Dp = 40.dp,
    typeIcon: ImageVector,
    typeIconCD: String,
    agentIcon: ImageVector,
    agentIconCD: String,
    onValueChange: (String) -> Unit,
    ) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        // Type
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1F)
                .border(width = 1.dp, color = MaterialTheme.colorScheme.tertiary),
        ) {
            // Type icon
            Icon(
                modifier = Modifier
                    .size(iconSize)
                    .padding(horizontal = 4.dp),
                imageVector = typeIcon,
                contentDescription = typeIconCD,
                tint = MaterialTheme.colorScheme.tertiary,
            )
            // Type label
            Text(
                modifier = Modifier.padding(end = 2.dp),
                textAlign = TextAlign.Left,
                fontSize = labelFontSize,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary,
                text = stringResource(id = R.string.type),
            )
            // Type field
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
                    BasicDropdownMenuItem(
                        dropdownMenuCategory = DropdownMenuCategory.TYPE,
                        stringItems = stringTypes,
                        fieldFontSize = fieldFontSize,
                        onValueChange = onValueChange,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(4.dp))

        // Agent
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1F)
                .border(width = 1.dp, color = MaterialTheme.colorScheme.tertiary),
        ) {
            // Agent icon
            Icon(
                modifier = Modifier
                    .size(iconSize)
                    .padding(horizontal = 4.dp),
                imageVector = agentIcon,
                contentDescription = agentIconCD,
                tint = MaterialTheme.colorScheme.tertiary,
            )
            // Agent label
            Text(
                modifier = Modifier.padding(end = 2.dp),
                textAlign = TextAlign.Left,
                fontSize = labelFontSize,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary,
                text = stringResource(id = R.string.agent),
            )
            // Agent field
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
                    BasicDropdownMenuItem(
                        dropdownMenuCategory = DropdownMenuCategory.AGENT,
                        stringItems = stringAgents,
                        fieldFontSize = fieldFontSize,
                        onValueChange = onValueChange,
                    )
                }
            }
        }
    }
}


@Composable
fun BasicDropdownMenuItem(
    dropdownMenuCategory: DropdownMenuCategory,
    stringItems: MutableList<String>,
    fieldFontSize: TextUnit,
    onValueChange: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    var selectedItem by remember { mutableStateOf("") }
    var fieldText by remember(selectedItem) {
        mutableStateOf(textWithEllipsis(fullText = selectedItem, maxLength = 14, maxLines = 2))
    }
    val onClick: () -> Unit = { expanded = true }

    BasicTextField(
        modifier = Modifier.clickable {
            focusManager.clearFocus()
            onClick()
        },
        enabled = false,
        textStyle = TextStyle(
            fontSize = fieldFontSize,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        ),
        singleLine = false,
        minLines = 1,
        maxLines = 2,
        value = fieldText,
        onValueChange = {
            fieldText = it
            onValueChange(it)
            Log.w("SearchScreen", "New value for ${dropdownMenuCategory.name} = $it")
        },
    )

    // List
    DropdownMenu(
        expanded = expanded,
        offset = DpOffset(x = 32.dp, y = (-16).dp),
        onDismissRequest = { expanded = false },
        modifier = Modifier
            .width(160.dp)
            .wrapContentSize()
    ) {
        stringItems.forEachIndexed { index, s ->
            DropdownMenuItem(
                text = { Text(text = s, fontSize = fieldFontSize) },
                onClick = {
                    selectedIndex = index
                    selectedItem = s
                    onValueChange("${dropdownMenuCategory.name}#$index")
                    expanded = false
                },
            )
            HorizontalDivider(thickness = 1.dp)
        }
    }
}