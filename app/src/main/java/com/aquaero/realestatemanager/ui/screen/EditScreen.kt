package com.aquaero.realestatemanager.ui.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.EditField
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.component.app.PointsOfInterest
import com.aquaero.realestatemanager.ui.component.edit_screen.EditScreenColumn1
import com.aquaero.realestatemanager.ui.component.edit_screen.EditScreenColumn2
import com.aquaero.realestatemanager.ui.component.edit_screen.EditScreenMedia
import com.aquaero.realestatemanager.ui.component.edit_screen.EditScreenTextFieldItem

@Composable
fun EditScreen(
    stringTypes: MutableList<String>,
    stringType: String?,
    stringAgents: MutableList<String>,
    stringAgent: String?,
    itemPhotos: MutableList<Photo>,
    itemPois: MutableList<Poi>,
    property: Property?,
    address: Address?,
    currency: String,
    onFieldValueChange: (String, String) -> Unit,
    onDropdownMenuValueChange: (String) -> Unit,
    onPoiClick: (String, Boolean) -> Unit,
    onShootPhotoMenuItemClick: () -> Unit,
    onSelectPhotoMenuItemClick: () -> Unit,
    buttonAddPhotoEnabled: Boolean,
    painter: Painter,
    onCancelPhotoEditionButtonClick: () -> Unit,
    onSavePhotoButtonClick: (String) -> Unit,
    onEditPhotoMenuItemClick: (Photo) -> Unit,
    onPhotoDeletionConfirmation: (Long) -> Unit,
    onBackPressed: () -> Unit,
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Description
        EditScreenTextFieldItem(
            minLines = 2,
            maxLines = 5,
            itemText = property?.description ?: "",
            labelText = stringResource(id = R.string.description),
            placeHolderText = stringResource(id = R.string.description),
            icon = Icons.Default.NoteAlt,
            iconCD = stringResource(id = R.string.cd_description),
            onValueChange = { onFieldValueChange(EditField.DESCRIPTION.name, it) },
        )

        // Columns
        Row(
            modifier = Modifier.height(IntrinsicSize.Max)
        ) {
            // Column 1
            Column(
                modifier = Modifier
                    .weight(1F)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                EditScreenColumn1(
                    stringTypes = stringTypes,
                    stringType = stringType,
                    property = property,
                    currency = currency,
                    onFieldValueChange = onFieldValueChange,
                    onDropdownMenuValueChange = onDropdownMenuValueChange,
                )
            }
            // Column 2
            Column(
                modifier = Modifier
                    .weight(1F)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                EditScreenColumn2(
                    stringAgents = stringAgents,
                    stringAgent = stringAgent,
                    property = property,
                    address = address,
                    onFieldValueChange = onFieldValueChange,
                    onDropdownMenuValueChange = onDropdownMenuValueChange,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // POIs
        PointsOfInterest(
            onPoiClick = onPoiClick,
            itemPois = itemPois,
            clickable = true,
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Media
        EditScreenMedia(
            onShootPhotoMenuItemClick = onShootPhotoMenuItemClick,
            onSelectPhotoMenuItemClick = onSelectPhotoMenuItemClick,
            buttonAddPhotoEnabled = buttonAddPhotoEnabled,
            onCancelPhotoEditionButtonClick = onCancelPhotoEditionButtonClick,
            onSavePhotoButtonClick = onSavePhotoButtonClick,
            onEditPhotoMenuItemClick = onEditPhotoMenuItemClick,
            onPhotoDeletionConfirmation = onPhotoDeletionConfirmation,
            painter = painter,
            photos = itemPhotos,
        )
    }

    // To manage back nav
    BackHandler(enabled = true) {
        Log.w("OnBackPressed", "EditScreen OnBackPressed")
        onBackPressed()
    }

}

