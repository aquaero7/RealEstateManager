package com.aquaero.realestatemanager.ui.composable

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import com.aquaero.realestatemanager.NULL_PROPERTY_ID
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.ui.screen.EditScreen
import com.aquaero.realestatemanager.utils.connectivityState
import com.aquaero.realestatemanager.viewmodel.EditViewModel

@Composable
fun EditComposable(
    propertyId: Long,
    context: Context,
    editViewModel: EditViewModel,
    currency: String,
    properties: MutableList<Property>,
    types: MutableList<Type>,
    stringTypes: MutableList<String>,
    agents: MutableList<Agent>,
    stringAgents:MutableList<String>,
    addresses: MutableList<Address>,
    photos: MutableList<Photo>,
    pois: MutableList<Poi>,
    propertyPoiJoins: MutableList<PropertyPoiJoin>,
    popBackStack: () -> Unit,
) {
    val property: Property? = if (propertyId != NULL_PROPERTY_ID && properties.isNotEmpty()) {
        // Edition mode
        editViewModel.propertyFromId(propertyId = propertyId, properties = properties)
    } else {
        // Creation mode
        null
    }
    val stringType = property?.let {
        editViewModel.stringType(typeId = it.typeId, types = types, stringTypes = stringTypes)
    }
    val stringAgent = property?.let {
        editViewModel.stringAgent(agentId = it.agentId, agents = agents, stringAgents = stringAgents)
    }
    val address = property?.let {
        editViewModel.address(property.addressId, addresses)
    }
    val itemPhotos = editViewModel.itemPhotos(propertyId = propertyId, photos = photos)
    val itemPois = editViewModel.itemPois(
        propertyId = propertyId, propertyPoiJoins = propertyPoiJoins, pois = pois
    )
    val onDropdownMenuValueChange: (String) -> Unit = {
        editViewModel.onDropdownMenuValueChange(value = it, types = types, agents = agents)
    }
    val onFieldValueChange: (String, String) -> Unit = { field, value ->
        editViewModel.onFieldValueChange(field = field, value = value, currency = currency)
    }
    val onPoiClick: (String, Boolean) -> Unit = { poiItem, isSelected ->
        editViewModel.onPoiClick(poiItem = poiItem, isSelected = isSelected)
    }

    /**
     * Photo shooting and picking
     */
    var buttonSavePhotoEnabled by remember { mutableStateOf(false) }
    var photoToAddUri by remember { mutableStateOf(Uri.EMPTY) }
    val painterResource = painterResource(id = R.drawable.baseline_add_a_photo_black_24)
    var painter by remember { mutableStateOf(painterResource) }

    /**
     * Photo shooting
     */
    val cameraUri: Uri = editViewModel.getPhotoUri(context = context)
    var capturedImageUri by remember { mutableStateOf(Uri.EMPTY) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { capturedImageUri = cameraUri }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            cameraLauncher.launch(cameraUri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
    val onShootPhotoMenuItemClick: () -> Unit = {
        editViewModel.onShootPhotoMenuItemClick(
            context = context, uri = cameraUri, cameraLauncher = cameraLauncher, permissionLauncher = permissionLauncher,
        )
    }

    /**
     * Photo picking
     */
    var pickerUri by remember { mutableStateOf(Uri.EMPTY) }
    val pickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) {
            //When the user selects a photo, its URI is returned here
                uri -> if (uri != null) { pickerUri = uri }
        }
    val onSelectPhotoMenuItemClick: () -> Unit = {
        editViewModel.onSelectPhotoMenuItemClick(pickerLauncher = pickerLauncher)
    }

    /**
     * Photo shooting and picking
     */
    val uris = editViewModel.checkUris(
        capturedImageUri = capturedImageUri,
        pickerUri = pickerUri,
        photoToAddUri = photoToAddUri
    )
    capturedImageUri = uris.first
    pickerUri = uris.second
    photoToAddUri = uris.third
    if (photoToAddUri != Uri.EMPTY) {
        painter = rememberAsyncImagePainter(model = photoToAddUri)
        buttonSavePhotoEnabled = true
    } else {
        painter = painterResource
        buttonSavePhotoEnabled = false
    }
    val onCancelPhotoEditionButtonClick: () -> Unit =
        { photoToAddUri = editViewModel.onCancelPhotoEditionButtonClick() }
    val onSavePhotoButtonClick: (String) -> Unit = {
        photoToAddUri =
            editViewModel.onSavePhotoButtonClick(uri = photoToAddUri, label = it.ifBlank { null })
    }
    val onEditPhotoMenuItemClick: (Photo) -> Unit = {
        val editResult = editViewModel.onEditPhotoMenuItemClick(photo = it)
        photoToAddUri = editResult.first
        buttonSavePhotoEnabled = editResult.second
    }
    val onPhotoDeletionConfirmation: (Long) -> Unit = { photoId ->
        editViewModel.onPhotoDeletionConfirmation(propertyId = propertyId, photoId = photoId)
    }

    val onBackPressed: () -> Unit = {
        editViewModel.clearCache()
        popBackStack()
    }

    // Connexion for address latLng update
    val connection by connectivityState()
    editViewModel.connexionStatus(connection = connection)

    // Cache data
    val (isCacheInitialized, setCacheInitialized) = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit) {
        if (!isCacheInitialized) {
            editViewModel.initCache(
                context = context,
                property = property,
                stringType = stringType,
                stringAgent = stringAgent,
                address = address,
                itemPhotos = itemPhotos,
                itemPois = itemPois,
            )
            setCacheInitialized(true)
        }
    }
    val cacheItemPhotos: MutableList<Photo> by editViewModel.cacheItemPhotosFlow.collectAsState(initial = mutableListOf())

    EditScreen(
        stringTypes = stringTypes,
        stringType = stringType,
        stringAgents = stringAgents,
        stringAgent = stringAgent,
        itemPhotos = cacheItemPhotos,
        itemPois = itemPois,
        property = property,
        address = address,
        currency = currency,
        onFieldValueChange = onFieldValueChange,
        onDropdownMenuValueChange = onDropdownMenuValueChange,
        onPoiClick = onPoiClick,
        onShootPhotoMenuItemClick = onShootPhotoMenuItemClick,
        onSelectPhotoMenuItemClick = onSelectPhotoMenuItemClick,
        buttonAddPhotoEnabled = buttonSavePhotoEnabled,
        painter = painter,
        onCancelPhotoEditionButtonClick = onCancelPhotoEditionButtonClick,
        onSavePhotoButtonClick = onSavePhotoButtonClick,
        onEditPhotoMenuItemClick = onEditPhotoMenuItemClick,
        onPhotoDeletionConfirmation = onPhotoDeletionConfirmation,
        onBackPressed = onBackPressed,
    )

}