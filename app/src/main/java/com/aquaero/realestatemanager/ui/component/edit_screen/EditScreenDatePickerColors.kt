package com.aquaero.realestatemanager.ui.component.edit_screen

import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import com.aquaero.realestatemanager.DP_CONTAINER_COLOR
import com.aquaero.realestatemanager.DP_CONTENT_COLOR
import com.aquaero.realestatemanager.DP_HEADERS_COLOR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun editScreenDatePickerColors(): DatePickerColors {
    return DatePickerColors(
        // Setting of DatePickerDefaults.colors (material3)
        titleContentColor = DP_HEADERS_COLOR,
        headlineContentColor = DP_HEADERS_COLOR,
        weekdayContentColor = DP_HEADERS_COLOR,
        subheadContentColor = DP_HEADERS_COLOR,
        containerColor = DP_CONTAINER_COLOR,
        selectedDayContentColor = DP_CONTENT_COLOR,
        selectedYearContentColor = DP_CONTENT_COLOR,
        dayContentColor = DP_CONTENT_COLOR,
        yearContentColor = DP_CONTENT_COLOR,
        todayContentColor = DP_CONTENT_COLOR,
        todayDateBorderColor = DP_CONTENT_COLOR,
        currentYearContentColor = DP_CONTENT_COLOR,
        selectedYearContainerColor = DatePickerDefaults.colors().selectedYearContainerColor,
        disabledDayContentColor = DatePickerDefaults.colors().disabledDayContentColor,
        disabledSelectedDayContentColor = DatePickerDefaults.colors().disabledSelectedDayContentColor,
        selectedDayContainerColor = DatePickerDefaults.colors().selectedDayContainerColor,
        disabledSelectedDayContainerColor = DatePickerDefaults.colors().disabledSelectedDayContainerColor,
        dayInSelectionRangeContentColor = DatePickerDefaults.colors().dayInSelectionRangeContentColor,
        dayInSelectionRangeContainerColor = DatePickerDefaults.colors().dayInSelectionRangeContainerColor,
        // Setting of DatePickerColors (material3-adaptive)
        dateTextFieldColors = TextFieldDefaults.colors(),
        dividerColor = DP_CONTENT_COLOR,
        navigationContentColor = DP_CONTENT_COLOR,
        disabledSelectedYearContainerColor = DatePickerDefaults.colors().disabledSelectedYearContainerColor,
        disabledSelectedYearContentColor = DatePickerDefaults.colors().disabledSelectedYearContentColor,
        disabledYearContentColor = DatePickerDefaults.colors().disabledYearContentColor,
    )
}
