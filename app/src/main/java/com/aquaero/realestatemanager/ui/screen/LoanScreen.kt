package com.aquaero.realestatemanager.ui.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale

@Composable
fun LoanScreen(
    popBackStack: () -> Unit,
) {
    val language = Locale.current.language                                  // TODO: To be deleted after screen implementation
    val region = Locale.current.region                                      // TODO: To be deleted after screen implementation
    Text(text = "LoanScreen - Language = $language - Region = $region")     // TODO: To be deleted after screen implementation

    // ...


    // To manage back nav
    BackHandler(true) {
        Log.w("OnBackPressed", "LoanScreen OnBackPressed")
        popBackStack()
    }

}