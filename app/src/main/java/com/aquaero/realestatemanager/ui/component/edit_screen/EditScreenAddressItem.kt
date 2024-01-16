package com.aquaero.realestatemanager.ui.component.edit_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.model.Address

@Composable
fun EditScreenAddressItem(
    fieldHeight: Dp = 76.dp,    // 76
    fieldWidth: Dp = 296.dp,
    itemText: Address?,
    onValueChanged: (Address) -> Unit,
) {
    Surface(
        modifier = Modifier
            .height(fieldHeight)
            .width(fieldWidth)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Row {
            // Icon


            Column {
                // Title


                // Address

            }
        }
    }

}