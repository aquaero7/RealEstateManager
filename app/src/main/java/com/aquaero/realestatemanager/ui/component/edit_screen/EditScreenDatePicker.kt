package com.aquaero.realestatemanager.ui.component.edit_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.ui.theme.PurpleGrey40
import com.aquaero.realestatemanager.ui.theme.PurpleGrey80
import com.aquaero.realestatemanager.ui.theme.Red
import com.aquaero.realestatemanager.ui.theme.White
import com.aquaero.realestatemanager.utils.convertDateMillisToString
import com.aquaero.realestatemanager.utils.convertDateStringToMillis

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreenDatePicker(
    storedDate: String,
    labelText: String,
    icon: ImageVector,
    iconCD: String,
    onValueChanged: (String) -> Unit,
    clearableDate: Boolean = false
) {
    var dateValue by remember { mutableStateOf(storedDate) }
    val dpState = rememberDatePickerState(
        initialSelectedDateMillis = convertDateStringToMillis(dateValue),
        initialDisplayMode = DisplayMode.Picker
    )
    var openDpDialog by remember { mutableStateOf(false) }

    val lineColor = MaterialTheme.colorScheme.onBackground
    val headersColor = White
    val contentColor = PurpleGrey80
    val containerColor = PurpleGrey40
    val datePickerColors = DatePickerColors(
        // DatePickerDefaults.colors (material3)
        titleContentColor = headersColor,
        headlineContentColor = headersColor,
        weekdayContentColor = headersColor,
        subheadContentColor = headersColor,
        containerColor = containerColor,
        selectedDayContentColor = contentColor,
        selectedYearContentColor = contentColor,
        dayContentColor = contentColor,
        yearContentColor = contentColor,
        todayContentColor = contentColor,
        todayDateBorderColor = contentColor,
        currentYearContentColor = contentColor,
        selectedYearContainerColor = DatePickerDefaults.colors().selectedYearContainerColor,
        disabledDayContentColor = DatePickerDefaults.colors().disabledDayContentColor,
        disabledSelectedDayContentColor = DatePickerDefaults.colors().disabledSelectedDayContentColor,
        selectedDayContainerColor = DatePickerDefaults.colors().selectedDayContainerColor,
        disabledSelectedDayContainerColor = DatePickerDefaults.colors().disabledSelectedDayContainerColor,
        dayInSelectionRangeContentColor = DatePickerDefaults.colors().dayInSelectionRangeContentColor,
        dayInSelectionRangeContainerColor = DatePickerDefaults.colors().dayInSelectionRangeContainerColor,
        // DatePickerColors (material3-adaptive)
        dateTextFieldColors = TextFieldDefaults.colors(),
        dividerColor = contentColor,
        navigationContentColor = contentColor,
        disabledSelectedYearContainerColor = DatePickerDefaults.colors().disabledSelectedYearContainerColor,
        disabledSelectedYearContentColor = DatePickerDefaults.colors().disabledSelectedYearContentColor,
        disabledYearContentColor = DatePickerDefaults.colors().disabledYearContentColor,
    )

    if (openDpDialog) {
        DatePickerDialog(
            shape = DatePickerDefaults.shape,
            colors = DatePickerDefaults.colors(
                containerColor = containerColor,
            ),
            onDismissRequest = { openDpDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (dpState.selectedDateMillis != null) {
                            dateValue = convertDateMillisToString(dpState.selectedDateMillis!!)
                            onValueChanged(dateValue)
                        }
                        openDpDialog = false
                    },
                ) {
                    Text(
                        text = stringResource(id = R.string.ok),
                        color = White,
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { openDpDialog = false },
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        color = White
                    )
                }
            },
        ) {
            DatePicker(
                state = dpState,
                colors = datePickerColors,
            )
        }
    }

    Button(
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
            .height(76.dp)
            .width(296.dp)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RectangleShape,
        contentPadding = PaddingValues(all = 0.dp),
        onClick = {
            openDpDialog = true
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Icon
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .weight(if (clearableDate) 1.3F else 1F),
                imageVector = icon,
                contentDescription = iconCD,
                tint = MaterialTheme.colorScheme.tertiary,
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(3.5F),
            ) {
                // Label
                Text(
                    text = labelText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Field
                Text(
                    text = dateValue,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
                    // modifier = Modifier.fillMaxWidth(),
                )
            }

            if (clearableDate) {
                FloatingActionButton(
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.Bottom)
                        .padding(bottom = 8.dp),
                    shape = ButtonDefaults.shape,
                    onClick = {
                        dateValue = ""
                        onValueChanged(dateValue)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = stringResource(id = R.string.cd_button_cancel),
                        tint = Red,
                    )
                }
            }
        }
    }
}