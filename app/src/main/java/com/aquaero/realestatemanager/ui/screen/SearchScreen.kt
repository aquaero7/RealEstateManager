package com.aquaero.realestatemanager.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchScreen(
    onButton1Click: () -> Unit,    // TODO: To be deleted after screen implementation
    onButton2Click: () -> Unit,     // TODO: To be deleted after screen implementation
) {







    // TODO: To be deleted after screen implementation
    Column {
        Text(text = "SearchScreen")
        Row {
            Button(onClick = onButton1Click) {
                Text(text = "Button 1")
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button(onClick = onButton2Click) {
                Text(text = "Button 2")
            }
        }
    }
    //
}