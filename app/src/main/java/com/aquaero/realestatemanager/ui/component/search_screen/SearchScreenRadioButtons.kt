package com.aquaero.realestatemanager.ui.component.search_screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaero.realestatemanager.R

@Composable
fun SearchScreenRadioButtons(
    radioOptions: List<String>,
    radioButtonClick: (String) -> Unit,
) {
    var selectedOptions by remember { mutableStateOf(radioOptions[2]) }
    val color = MaterialTheme.colorScheme.tertiary

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .border(width = 1.dp, color = color),
        ) {
            radioOptions.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        colors = RadioButtonColors(
                            selectedColor = color,
                            unselectedColor = color,
                            disabledSelectedColor = RadioButtonDefaults.colors().disabledSelectedColor,
                            disabledUnselectedColor = RadioButtonDefaults.colors().disabledUnselectedColor
                        ),
                        selected = (it == selectedOptions),
                        onClick = {
                            selectedOptions = it
                            radioButtonClick(it)
                        }
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 0.dp),
                        color = color,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        text = it
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SearchScreenRadioButtonsPreview() {
    Column {
        SearchScreenRadioButtons(
            radioOptions =
            listOf(stringResource(id = R.string.for_sale), stringResource(id = R.string.sold), stringResource(id = R.string.both)),
            radioButtonClick = {},
        )
        SearchScreenRadioButtons(
            radioOptions =
            listOf(stringResource(id = R.string.with_photo), stringResource(id = R.string.without_photo), stringResource(id = R.string.both)),
            radioButtonClick = {},
        )
    }
}