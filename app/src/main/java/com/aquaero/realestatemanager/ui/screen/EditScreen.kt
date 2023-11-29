package com.aquaero.realestatemanager.ui.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.component.edit_screen.EditScreenAppDropdownMenu

@Composable
fun EditScreen(
    pTypeSet: () -> MutableSet<Int?>,
    agentSet: () -> MutableSet<String?>,
    property: Property?,
    // onClickMenu: () -> Unit,
    onBackPressed: () -> Unit,
) {
    // TODO: To be deleted after screen implementation ---------------------------------------------
    Column {
        Row {
            Text(text = "EditScreen for ${property?.pId}")
            Spacer(modifier = Modifier.width(200.dp))
            Text(text = Locale.current.language)
        }

        Spacer(modifier = Modifier.height(40.dp))
        EditScreenAppDropdownMenu(stringResource(id = R.string.type), pTypeSet)
        Spacer(modifier = Modifier.height(40.dp))
        EditScreenAppDropdownMenu(stringResource(id = R.string.agent), agentSet)

        Spacer(modifier = Modifier.height(40.dp))

        Text(text = stringResource(R.string.flat))
        Text(text = stringResource(R.string.house))
    }
    // End TODO ------------------------------------------------------------------------------------

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
        var descriptionText by remember { mutableStateOf(property?.description) }

        descriptionText?.let { it ->
            TextField(
                modifier = Modifier
                    .height(200.dp)
                    .padding(top = 16.dp)
                    .padding(horizontal = 8.dp),
                label = {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = stringResource(R.string.description),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                value = it,
                onValueChange = { descriptionText = it },


                )
        }


    }

    BackHandler(true) {
        Log.w("TAG", "OnBackPressed")
        run(onBackPressed)
    }
}

