package com.aquaero.realestatemanager.ui.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.OtherHouses
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.aquaero.realestatemanager.Field
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.ui.component.search_screen.SearchScreenTextFieldMinMax

@Composable
fun SearchScreen(
    currency: String,
    onFieldValueChange: (String, String, String) -> Unit,
    popBackStack: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .clickable { focusManager.clearFocus() } // To clear text field focus when clicking outside it.
            .fillMaxSize()
            .verticalScroll(
                state = rememberScrollState(),
                enabled = true
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        // Price
        SearchScreenTextFieldMinMax(
            labelText = "${stringResource(id = R.string.price)} ($currency)",
            icon = Icons.Default.Money,
            iconCD = stringResource(id = R.string.cd_price),
            onValueChange = { fieldType, value ->
                onFieldValueChange(Field.PRICE.name, fieldType, value)
                Log.w("SearchScreen", "Price: $fieldType value = $value")
            },
        )
        // Surface
        SearchScreenTextFieldMinMax(
            labelText = "${stringResource(id = R.string.surface)} (${stringResource(id = R.string.surface_unit)})",
            icon = Icons.Default.AspectRatio,
            iconCD = stringResource(id = R.string.cd_surface),
            onValueChange = { fieldType, value ->
                onFieldValueChange(Field.SURFACE.name, fieldType, value)
                Log.w("SearchScreen", "Surface: $fieldType value = $value")
            },
        )
        // Number of rooms
        SearchScreenTextFieldMinMax(
            labelText = stringResource(id = R.string.rooms),
            icon = Icons.Default.OtherHouses,
            iconCD = stringResource(id = R.string.cd_rooms),
            onValueChange = { fieldType, value ->
                onFieldValueChange(Field.ROOMS.name, fieldType, value)
                Log.w("SearchScreen", "Rooms: $fieldType value = $value")
            },
        )
        // Number of bathrooms
        SearchScreenTextFieldMinMax(
            labelText = stringResource(id = R.string.bathrooms),
            icon = Icons.Default.Bathtub,
            iconCD = stringResource(id = R.string.cd_bathrooms),
            onValueChange = { fieldType, value ->
                onFieldValueChange(Field.BATHROOMS.name, fieldType, value)
                Log.w("SearchScreen", "Bathrooms: $fieldType value = $value")
            },
        )
        // Number of bedrooms
        SearchScreenTextFieldMinMax(
            labelText = stringResource(id = R.string.bedrooms),
            icon = Icons.Default.Bed,
            iconCD = stringResource(id = R.string.cd_bedrooms),
            onValueChange = { fieldType, value ->
                onFieldValueChange(Field.BEDROOMS.name, fieldType, value)
                Log.w("SearchScreen", "Bedrooms: $fieldType value = $value")
            },
        )
        // Type





        // Registration date
        SearchScreenTextFieldMinMax(
            labelText = stringResource(id = R.string.registration_date),
            icon = Icons.Default.ArrowCircleDown,
            iconCD = stringResource(id = R.string.cd_registration_date),
            onValueChange = { fieldType, value ->
                onFieldValueChange(Field.REGISTRATION_DATE.name, fieldType, value)
                Log.w("SearchScreen", "Registration date: $fieldType value = $value")
            },
            shouldBeDigitsOnly = false,
            fieldsAreDates = true,
        )
        // Sale date
        SearchScreenTextFieldMinMax(
            labelText = stringResource(id = R.string.sale_date),
            icon = Icons.Default.ArrowCircleUp,
            iconCD = stringResource(id = R.string.cd_sale_date),
            onValueChange = { fieldType, value ->
                onFieldValueChange(Field.SALE_DATE.name, fieldType, value)
                Log.w("SearchScreen", "Sale date: $fieldType value = $value")
            },
            shouldBeDigitsOnly = false,
            fieldsAreDates = true,
        )


    }

    // To manage back nav
    BackHandler(true) {
        Log.w("OnBackPressed", "SearchScreen OnBackPressed")
        popBackStack()
    }

}