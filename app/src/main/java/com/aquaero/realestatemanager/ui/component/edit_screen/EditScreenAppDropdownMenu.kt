package com.aquaero.realestatemanager.ui.component.edit_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaero.realestatemanager.ui.theme.BoxBackgroundColor
import com.aquaero.realestatemanager.ui.theme.BoxTextColor

@Composable
fun EditScreenAppDropdownMenu(
    fieldHeight: Int = 76,
    fieldMinWidth: Int = 0,
    fieldMaxWidth: Int = 296,
    fieldFontSize: Int = 16,
    maxLines: Int = 1,
    icon: ImageVector,
    contentDescription: String,
    label: String,
    itemsSet: () -> MutableSet<*>,
    index: Int?,
    onValueChanged: (String) -> Unit,
    ) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(index ?: 0) }

    val lineColor = MaterialTheme.colorScheme.onBackground

    Surface(
        modifier = Modifier
            .drawWithContent {
                drawContent()
                drawLine(
                    color = lineColor,
                    start = Offset(20F, size.height - 8F),
                    end = Offset(size.width - 20F, size.height - 8F),
                    strokeWidth = 2F
                )
            }
            .height(fieldHeight.dp)
            .widthIn(min = fieldMinWidth.dp, max = fieldMaxWidth.dp)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Icon
            Icon(
                modifier = Modifier.size(40.dp),
                imageVector = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.tertiary,
            )

            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                // Label
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Menu
                Box {
                    // Field
                    // itemsList()[selectedIndex]?.let {    // In case of a list instead of a set
                    itemsSet().elementAt(selectedIndex)?.let {
                        Text(
                            text = if (it is String) it else stringResource(id = it as Int),
                            textAlign = TextAlign.Start,
                            fontSize = fieldFontSize.sp,
                            maxLines = maxLines,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = { expanded = true })
                            // .background(Color.White)
                        )
                    }
                    // List
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .width(160.dp)
                            .wrapContentSize()
                            // .background(BoxBackgroundColor)
                    ) {
                        itemsSet().forEachIndexed { index, s ->
                            val textDisplayed = if (s is String) s else stringResource(id = s as Int)
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = textDisplayed,
                                        // color = BoxTextColor,
                                        fontSize = 16.sp,
                                    )
                                },
                                onClick = {
                                    selectedIndex = index
                                    onValueChanged("$index#$textDisplayed")
                                    expanded = false
                                },
                            )
                            Divider(
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }
        }
    }
}

// TODO : To be deleted ----------------------------------------------------------------------------
@Composable
fun EditScreenAppDropdownMenuExample(label: String, itemsSet: () -> MutableSet<*>) {

    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.width(240.dp),
        horizontalAlignment = Alignment.End,
    ) {
        Row(
            modifier = Modifier.wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$label :",
                modifier = Modifier.wrapContentWidth(),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                modifier = Modifier
                    .width(200.dp)
                    .wrapContentHeight()
                    .border(1.dp, MaterialTheme.colorScheme.onSurface),             // ?
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface)    // ?
            ) {
                Box(
                    modifier = Modifier
                        // .fillMaxSize()
                        .width(200.dp)
                        .wrapContentHeight()
                        .padding(vertical = 4.dp, horizontal = 2.dp)
                        .wrapContentSize(Alignment.Center)
                ) {
                    // itemsList()[selectedIndex]?.let {    // In case of a list instead of a set
                    itemsSet().elementAt(selectedIndex)?.let {
                        Text(
                            text = if (it is String) it else stringResource(id = it as Int),
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = { expanded = true })
                            // .background(Color.White)
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .width(200.dp)
                            .background(BoxBackgroundColor)
                            .wrapContentSize()
                    ) {
                        itemsSet().forEachIndexed { index, s ->
                            DropdownMenuItem(
                                text = {
                                    if (s != null) {
                                        Text(
                                            text = if (s is String) s else stringResource(id = s as Int),
                                            color = BoxTextColor,
                                        )
                                    }
                                },
                                onClick = {
                                    selectedIndex = index
                                    expanded = false
                                },
                            )
                            Divider(
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }
        }
    }
    // TODO : End deletion  ------------------------------------------------------------------------


}