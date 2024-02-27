package com.aquaero.realestatemanager.ui.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.ui.theme.White

@Composable
fun SearchScreen(
    onButton1Click: () -> Unit,    // TODO: To be deleted after screen implementation
    onButton2Click: () -> Unit,     // TODO: To be deleted after screen implementation
    popBackStack: () -> Unit,
) {
    // ...


    // TODO: To be deleted after screen implementation
    Column {
        Text(text = "SearchScreen")
        Row {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = White,
                ),
                onClick = onButton1Click,
            ) {
                Text(text = "Button 1")
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = White,
                ),
                onClick = onButton2Click,
            ) {
                Text(text = "Button 2")
            }
        }
    }
    //

    // To manage back nav
    BackHandler(true) {
        Log.w("OnBackPressed", "SearchScreen OnBackPressed")
        popBackStack()
    }

}