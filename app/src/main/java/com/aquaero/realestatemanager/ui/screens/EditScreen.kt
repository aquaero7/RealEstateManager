package com.aquaero.realestatemanager.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.data.agents
import com.aquaero.realestatemanager.data.pTypes
import com.aquaero.realestatemanager.ui.theme.BoxBackground

@Composable
fun EditScreen(
    propertyId: String,
    onBackPressed: () -> Unit = {},
) {
    Column {
        Row {
            Text(text = "EditScreen for $propertyId")
            Spacer(modifier = Modifier.width(200.dp))
            Text(text = Locale.current.language)
        }

        Spacer(modifier = Modifier.height(40.dp))
        AppDropdownMenu(stringResource(id = R.string.type), pTypes)
        Spacer(modifier = Modifier.height(200.dp))
        AppDropdownMenu(stringResource(id = R.string.agent), agents)

        Spacer(modifier = Modifier.height(200.dp))

        Text(text = stringResource(R.string.flat))
        Text(text = stringResource(R.string.house))



    }

    BackHandler(true) {
        Log.w("TAG", "OnBackPressed")
        run(onBackPressed)
    }
}


@Composable
fun AppDropdownMenu(label: String, itemsSet: MutableSet<String?>) {

    var expanded by remember { mutableStateOf(false)}
    var selectedIndex by remember { mutableIntStateOf(0) }

    Row {
        Spacer(modifier = Modifier.width(40.dp))
        Text(
            text = "$label :",
            modifier = Modifier
                .wrapContentWidth()
                .background(Color.White)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Surface(
            modifier = Modifier
                .width(200.dp)
                .wrapContentHeight(),
            color = BoxBackground,
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Box(
                modifier = Modifier
                    // .fillMaxSize()
                    .width(200.dp)
                    .wrapContentHeight()
                    .padding(0.dp)
                    .wrapContentSize(Alignment.Center)
            ) {
                // itemsList[selectedIndex]?.let {             // In case of a list instead of a set
                itemsSet.elementAt(selectedIndex)?.let {
                    Text(
                        it,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = { expanded = true })
                            .background(Color.White)
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(200.dp)
                        .background(BoxBackground)
                        .wrapContentSize()
                ) {
                    itemsSet.forEachIndexed { index, s ->
                        DropdownMenuItem(
                            onClick = {
                                selectedIndex = index
                                expanded = false
                            },
                            text = {
                                if (s != null) {
                                    Text(text = s)
                                }
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