package com.aquaero.realestatemanager.ui.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.component.edit_screen.EditScreenAppDropdownMenu
import com.aquaero.realestatemanager.ui.component.edit_screen.EditScreenAppDropdownMenuExample
import com.aquaero.realestatemanager.ui.component.edit_screen.EditScreenTextFieldItem
import com.aquaero.realestatemanager.utils.convertDollarToEuro

@Composable
fun EditScreen(
    pTypeSet: () -> MutableSet<Int?>,
    agentSet: () -> MutableSet<String?>,
    property: Property?,
    pTypeIndex: Int?,
    agentIndex: Int?,
    currency: String,
    onDescriptionValueChanged: (String) -> Unit,
    onPriceValueChanged: (String) -> Unit,
    onSurfaceValueChanged: (String) -> Unit,
    onDropdownMenuValueChanged: (String) -> Unit,
    onBackPressed: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                state = rememberScrollState(),
                enabled = true
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Description
        EditScreenTextFieldItem(
            fieldHeight = 150,
            fieldMinWidth = 400,
            maxLines = 5,
            itemText = property?.description,
            labelText = stringResource(R.string.description),
            placeHolderText = stringResource(R.string.description),
            icon = Icons.Default.AccessTime,
            iconCD = stringResource(id = R.string.cd_description),
            onValueChanged = onDescriptionValueChanged,
        )

        Row {

            // Column 1
            Column(
                modifier = Modifier.weight(1F),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Type
                EditScreenAppDropdownMenu(
                    icon = Icons.Default.House,
                    contentDescription = stringResource(id = R.string.cd_type),
                    label = stringResource(R.string.type),
                    itemsSet = pTypeSet,
                    index = pTypeIndex,
                    onValueChanged = onDropdownMenuValueChanged,
                )
                // Surface
                EditScreenTextFieldItem(
                    itemText = property?.surface.toString(),
                    labelText = "${stringResource(R.string.surface_in)} ${stringResource(R.string.surface_unit)}",
                    placeHolderText = "${stringResource(R.string.surface_in)} ${stringResource(R.string.surface_unit)}",
                    icon = Icons.Default.AspectRatio,
                    iconCD = stringResource(id = R.string.cd_surface),
                    onValueChanged = onSurfaceValueChanged,
                )


            }
            // Column 2
            Column(
                modifier = Modifier.weight(1F),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Price
                EditScreenTextFieldItem(
                    itemText = property?.priceInCurrency(currency).toString(),
                    labelText = "${stringResource(R.string.price_in)} $currency",
                    placeHolderText = "${stringResource(R.string.price_in)} $currency",
                    icon = Icons.Default.Money,
                    iconCD = stringResource(id = R.string.cd_price),
                    onValueChanged = onPriceValueChanged,
                )
                // Agent
                EditScreenAppDropdownMenu(
                    icon = Icons.Default.Person,
                    contentDescription = stringResource(id = R.string.cd_agent),
                    label = stringResource(R.string.agent),
                    itemsSet = agentSet,
                    index = agentIndex,
                    onValueChanged = onDropdownMenuValueChanged,
                )


            }
        }


        // TODO: To be deleted after screen implementation ---------------------------------------------
        Spacer(modifier = Modifier.height(200.dp))
        Column {
            Row {
                Text(text = "EditScreen for ${property?.pId}")
                Spacer(modifier = Modifier.width(200.dp))
                Text(text = Locale.current.language)
            }

            Spacer(modifier = Modifier.height(40.dp))
            EditScreenAppDropdownMenuExample(stringResource(id = R.string.type), pTypeSet)
            Spacer(modifier = Modifier.height(40.dp))
            EditScreenAppDropdownMenuExample(stringResource(id = R.string.agent), agentSet)

            Spacer(modifier = Modifier.height(40.dp))

            Text(text = stringResource(R.string.flat))
            Text(text = stringResource(R.string.house))
        }
        // End TODO ------------------------------------------------------------------------------------

    }

    BackHandler(true) {
        Log.w("TAG", "OnBackPressed")
        run(onBackPressed)
    }
}

