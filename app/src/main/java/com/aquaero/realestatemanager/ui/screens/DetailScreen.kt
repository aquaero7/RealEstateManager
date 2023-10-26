package com.aquaero.realestatemanager.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DetailScreen(
    propertyId: String?,
    onEditButtonClick: () -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    Column {
        Text(text = "DetailScreen  for $propertyId")
        Button(
            onClick = onEditButtonClick
        ) {
            Text(text = "EditScreen")
        }
        BackHandler(true) {
            Log.w("TAG", "OnBackPressed")
            run(onBackPressed)
        }
    }

}