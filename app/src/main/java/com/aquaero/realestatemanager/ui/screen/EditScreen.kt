package com.aquaero.realestatemanager.ui.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.NO_PHOTO
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.component.edit_screen.EditScreenColumn1
import com.aquaero.realestatemanager.ui.component.edit_screen.EditScreenColumn2
import com.aquaero.realestatemanager.ui.component.edit_screen.EditScreenMedia
import com.aquaero.realestatemanager.ui.component.edit_screen.EditScreenTextFieldItem

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
    onNbOfRoomsValueChanged: (String) -> Unit,
    onNbOfBathroomsValueChanged: (String) -> Unit,
    onNbOfBedroomsValueChanged: (String) -> Unit,
    onRegistrationDateValueChanged: (String) -> Unit,
    onSaleDateValueChanged: (String) -> Unit,

    onShootPhotoMenuItemClick: () -> Unit,
    onSelectPhotoMenuItemClick: () -> Unit,
    buttonAddPhotoEnabled: Boolean,
    painter: Painter,
    onSavePhotoButtonClick: (String) -> Unit,
    onEditPhotoMenuItemClick: (Photo) -> Unit,
    onPhotoDeletionConfirmation: (Long) -> Unit,
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
            fieldHeight = 150.dp,
            fieldMinWidth = 400.dp,
            maxLines = 5,
            itemText = property?.description,
            labelText = stringResource(R.string.description),
            placeHolderText = stringResource(R.string.description),
            icon = Icons.Default.AccessTime,
            iconCD = stringResource(id = R.string.cd_description),
            onValueChanged = onDescriptionValueChanged,
        )

        // Columns
        Row {

            // Column 1
            Column(
                modifier = Modifier
                    .weight(1F)
                    .height(380.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                EditScreenColumn1(
                    property = property,
                    pTypeSet = pTypeSet,
                    pTypeIndex = pTypeIndex,
                    onSurfaceValueChanged = onSurfaceValueChanged,
                    onDropdownMenuValueChanged = onDropdownMenuValueChanged,
                    onNbOfRoomsValueChanged = onNbOfRoomsValueChanged,
                    onNbOfBathroomsValueChanged = onNbOfBathroomsValueChanged,
                    onNbOfBedroomsValueChanged = onNbOfBedroomsValueChanged,
                )
            }

            // Column 2
            Column(
                modifier = Modifier
                    .weight(1F)
                    .height(380.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                EditScreenColumn2(
                    property = property,
                    currency = currency,
                    agentSet = agentSet,
                    agentIndex = agentIndex,
                    onPriceValueChanged = onPriceValueChanged,
                    onDropdownMenuValueChanged = onDropdownMenuValueChanged,
                    onRegistrationDateValueChanged = onRegistrationDateValueChanged,
                    onSaleDateValueChanged = onSaleDateValueChanged,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Media
        EditScreenMedia(
            photos = property?.photos ?: mutableListOf(NO_PHOTO),
            onShootPhotoMenuItemClick = onShootPhotoMenuItemClick,
            onSelectPhotoMenuItemClick = onSelectPhotoMenuItemClick,
            buttonAddPhotoEnabled = buttonAddPhotoEnabled,
            painter = painter,
            onSavePhotoButtonClick = onSavePhotoButtonClick,
            onEditPhotoMenuItemClick = onEditPhotoMenuItemClick,
            onPhotoDeletionConfirmation = onPhotoDeletionConfirmation,
        )



    }

    BackHandler(true) {
        Log.w("TAG", "OnBackPressed")
        run(onBackPressed)
    }
}

