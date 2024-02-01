package com.aquaero.realestatemanager.ui.screen

import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.NO_PHOTO
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.ui.component.app.PointsOfInterest
import com.aquaero.realestatemanager.ui.component.edit_screen.EditScreenColumn1
import com.aquaero.realestatemanager.ui.component.edit_screen.EditScreenColumn2
import com.aquaero.realestatemanager.ui.component.edit_screen.EditScreenMedia
import com.aquaero.realestatemanager.ui.component.edit_screen.EditScreenTextFieldItem

@Composable
fun EditScreen(
    types: MutableList<Type>,
    stringTypes: MutableList<String>,
    stringType: String?,
    agents: MutableList<Agent>,
    stringAgents: MutableList<String>,
    stringAgent: String?,
    itemPhotos: MutableList<Photo>,
    itemPois: MutableList<Poi>,
//    pTypeSet: () -> MutableSet<Int>,
//    agentSet: () -> MutableSet<String?>,
    property: Property?,
    addresses: List<Address>,
//    pTypeIndex: Int,
//    agentIndex: Int?,
    currency: String,
    onDescriptionValueChange: (String) -> Unit,
    onPriceValueChange: (String) -> Unit,
    onSurfaceValueChange: (String) -> Unit,
    onDropdownMenuValueChange: (String) -> Unit,
    onNbOfRoomsValueChange: (String) -> Unit,
    onNbOfBathroomsValueChange: (String) -> Unit,
    onNbOfBedroomsValueChange: (String) -> Unit,
    onStreetNumberValueChange: (String) -> Unit,
    onStreetNameValueChange: (String) -> Unit,
    onAddInfoValueChange: (String) -> Unit,
    onCityValueChange: (String) -> Unit,
    onStateValueChange: (String) -> Unit,
    onZipCodeValueChange: (String) -> Unit,
    onCountryValueChange: (String) -> Unit,
    onRegistrationDateValueChange: (String) -> Unit,
    onSaleDateValueChange: (String) -> Unit,
    onHospitalClick: (Boolean) -> Unit,
    onSchoolClick: (Boolean) -> Unit,
    onRestaurantClick: (Boolean) -> Unit,
    onShopClick: (Boolean) -> Unit,
    onRailwayStationClick: (Boolean) -> Unit,
    onCarParkClick: (Boolean) -> Unit,
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
            minLines = 2,
            maxLines = 5,
            itemText = property?.description ?: "",
            labelText = stringResource(R.string.description),
            placeHolderText = stringResource(R.string.description),
            icon = Icons.Default.AccessTime,
            iconCD = stringResource(id = R.string.cd_description),
            onValueChange = onDescriptionValueChange,
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
                    types = types,
                    stringTypes = stringTypes,
                    stringType = stringType,
                    property = property,
                    currency = currency,
//                    pTypeSet = pTypeSet,
//                    pTypeIndex = pTypeIndex,
                    onPriceValueChange = onPriceValueChange,
                    onSurfaceValueChange = onSurfaceValueChange,
                    onDropdownMenuValueChange = onDropdownMenuValueChange,
                    onNbOfRoomsValueChange = onNbOfRoomsValueChange,
                    onNbOfBathroomsValueChange = onNbOfBathroomsValueChange,
                    onNbOfBedroomsValueChange = onNbOfBedroomsValueChange,
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
                    agents = agents,
                    stringAgents = stringAgents,
                    stringAgent = stringAgent,
                    property = property,
                    addresses = addresses,
//                    agentSet = agentSet,
//                    agentIndex = agentIndex,
                    onStreetNumberValueChange = onStreetNumberValueChange,
                    onStreetNameValueChange = onStreetNameValueChange,
                    onAddInfoValueChange = onAddInfoValueChange,
                    onCityValueChange = onCityValueChange,
                    onStateValueChange = onStateValueChange,
                    onZipCodeValueChange = onZipCodeValueChange,
                    onCountryValueChange = onCountryValueChange,
                    onDropdownMenuValueChange = onDropdownMenuValueChange,
                    onRegistrationDateValueChange = onRegistrationDateValueChange,
                    onSaleDateValueChange = onSaleDateValueChange,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // POIs
        // property?.let { DetailScreenPoi(selectedPoi = it.pPoi) }
        PointsOfInterest(
            onHospitalClick = onHospitalClick,
            onSchoolClick = onSchoolClick,
            onRestaurantClick = onRestaurantClick,
            onShopClick = onShopClick,
            onRailwayStationClick = onRailwayStationClick,
            onCarParkClick = onCarParkClick,
//            itemPois = (property?.poi ?: mutableListOf()),
            itemPois = itemPois,
            clickable = true,
        )


        Spacer(modifier = Modifier.height(32.dp))

        // Media
        EditScreenMedia(
            onShootPhotoMenuItemClick = onShootPhotoMenuItemClick,
            onSelectPhotoMenuItemClick = onSelectPhotoMenuItemClick,
            buttonAddPhotoEnabled = buttonAddPhotoEnabled,
            onSavePhotoButtonClick = onSavePhotoButtonClick,
            onEditPhotoMenuItemClick = onEditPhotoMenuItemClick,
            onPhotoDeletionConfirmation = onPhotoDeletionConfirmation,
            painter = painter,
//            photos = property?.photos ?: mutableListOf(NO_PHOTO),
            photos = itemPhotos,
        )

    }

    BackHandler(true) {
        Log.w("TAG", "OnBackPressed")
        run(onBackPressed)
    }
}

