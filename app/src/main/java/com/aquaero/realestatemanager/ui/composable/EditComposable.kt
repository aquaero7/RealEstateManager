package com.aquaero.realestatemanager.ui.composable

import android.content.Context
import android.net.Uri
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
    val property: Property? = editViewModel.property(propertyId = propertyId, properties = properties)
    val (stringType, stringAgent, address) = editViewModel.itemData(
        property = property,
        types = types,
        stringTypes = stringTypes,
        agents = agents,
        stringAgents = stringAgents,
        addresses = addresses
    )
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

    /*
     * Photo shooting and picking
     */
    var buttonSavePhotoEnabled by remember { mutableStateOf(false) }
    var photoToAddUri by remember { mutableStateOf(Uri.EMPTY) }
    val painterResource = painterResource(id = R.drawable.baseline_add_a_photo_black_24)
    var painter by remember { mutableStateOf(painterResource) }

    /*
     * Photo shooting
     */
    val cameraUri: Uri = editViewModel.getPhotoUri(context = context)
    var capturedImageUri by remember { mutableStateOf(Uri.EMPTY) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { capturedImageUri = cameraUri }
    val camPermLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        editViewModel.onResponseToCamPermRequest(
            isGranted = it, cameraLauncher = cameraLauncher, cameraUri = cameraUri, context = context
        )
    }
    val onShootPhotoMenuItemClick: () -> Unit = {
        editViewModel.onShootPhotoMenuItemClick(
            context = context,
            uri = cameraUri,
            cameraLauncher = cameraLauncher,
            camPermLauncher = camPermLauncher,
        )
    }

    /*
     * Photo picking
     */
    var pickerUri by remember { mutableStateOf(Uri.EMPTY) }
    val pickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
            // When the user selects a photo, its URI is returned here
            uri -> if (uri != null) {
                pickerUri = editViewModel.saveToInternalStorage(context = context, uri = uri)
            }
        }
    val onSelectPhotoMenuItemClick: () -> Unit = {
        editViewModel.onSelectPhotoMenuItemClick(pickerLauncher = pickerLauncher)
    }

    /*
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
    val onPhotoDeletionConfirmation: (Long) -> Unit = {
        editViewModel.onPhotoDeletionConfirmation(propertyId = propertyId, photoId = it)
    }

    val onBackPressed: () -> Unit = {
        editViewModel.clearCache()
        popBackStack()
    }

    // Connexion for address latLng update
    val connection by connectivityState()
    editViewModel.connexionStatus(connection = connection)

    // Cache data
    val firstCompositionFlag = editViewModel.firstCompositionFlag
    LaunchedEffect(key1 = Unit) {
        // Embedded into LaunchedEffect to avoid recompositions after screen exit
        // (validation or back press), that reruns initCache and resets firstCompositionFlag to false.
        if (firstCompositionFlag) {
            editViewModel.initCache(
                context = context,
                property = property,
                stringType = stringType,
                stringAgent = stringAgent,
                address = address,
                itemPhotos = itemPhotos,
                itemPois = itemPois,
            )
        }
    }
    val cacheItemPhotos: MutableList<Photo> by editViewModel.cacheItemPhotosFlow.collectAsState(initial = mutableListOf())
    val (cacheProperty, cacheAddress, cacheItemPois) = editViewModel.cacheData()
    val (cacheStringType, cacheStringAgent) = editViewModel.itemData(
        property = cacheProperty, types = types, stringTypes = stringTypes, agents = agents, stringAgents = stringAgents
    )

    EditScreen(
        stringTypes = stringTypes,
        stringType = cacheStringType,
        stringAgents = stringAgents,
        stringAgent = cacheStringAgent,
        itemPhotos = cacheItemPhotos,
        itemPois = cacheItemPois,
        property = cacheProperty,
        address = cacheAddress,
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