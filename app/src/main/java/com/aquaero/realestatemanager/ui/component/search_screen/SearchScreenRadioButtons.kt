package com.aquaero.realestatemanager.ui.component.search_screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
    salesRadioOptions: List<String>,
    photosRadioOptions: List<String>,
    onSalesRadioButtonClick: (String) -> Unit,
    onPhotosRadioButtonClick: (String) -> Unit,
) {
    var salesSelectedOptions by remember { mutableStateOf(salesRadioOptions[0]) }
    var photosSelectedOptions by remember { mutableStateOf(photosRadioOptions[0]) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        // Sales radio buttons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1F)
                .wrapContentHeight()
                .border(width = 1.dp, color = MaterialTheme.colorScheme.tertiary),
        ) {
            salesRadioOptions.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = (it == salesSelectedOptions),
                        onClick = {
                            salesSelectedOptions = it
                            onSalesRadioButtonClick(it)
                        }
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 0.dp),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        text = it
                    )
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
        }

        // Photos radio buttons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1F)
                .wrapContentHeight()
                .border(width = 1.dp, color = MaterialTheme.colorScheme.tertiary),
        ) {
            photosRadioOptions.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = (it == photosSelectedOptions),
                        onClick = {
                            photosSelectedOptions = it
                            onPhotosRadioButtonClick(it)
                        }
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 0.dp),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        text = it
                    )
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SearchScreenRadioButtonsPreview() {
    SearchScreenRadioButtons(
        salesRadioOptions =
        listOf(stringResource(id = R.string.for_sale), stringResource(id = R.string.sold)),
        photosRadioOptions =
        listOf(stringResource(id = R.string.with_photo), stringResource(id = R.string.without_photo)),
        onSalesRadioButtonClick = {},
        onPhotosRadioButtonClick = {},
    )
}