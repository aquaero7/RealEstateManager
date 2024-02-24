package com.aquaero.realestatemanager

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.CACHE_PROPERTY
import com.aquaero.realestatemanager.model.NO_PHOTO
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.ui.component.map_screen.MapScreenNoMap
import com.aquaero.realestatemanager.ui.screen.DetailScreen
import com.aquaero.realestatemanager.ui.screen.EditScreen
import com.aquaero.realestatemanager.ui.screen.ListAndDetailScreen
import com.aquaero.realestatemanager.ui.screen.LoanScreen
import com.aquaero.realestatemanager.ui.screen.LocationPermissionsScreen
import com.aquaero.realestatemanager.ui.screen.MapScreen
import com.aquaero.realestatemanager.ui.screen.SearchScreen
import com.aquaero.realestatemanager.utils.connectivityState
import com.aquaero.realestatemanager.viewmodel.AppViewModel
import com.aquaero.realestatemanager.viewmodel.DetailViewModel
import com.aquaero.realestatemanager.viewmodel.EditViewModel
import com.aquaero.realestatemanager.viewmodel.ListViewModel
import com.aquaero.realestatemanager.viewmodel.LoanViewModel
import com.aquaero.realestatemanager.viewmodel.MapViewModel
import com.aquaero.realestatemanager.viewmodel.SearchViewModel
import kotlinx.coroutines.flow.StateFlow

@SuppressLint("NewApi")
@Composable
fun AppNavHost(
    modifier: Modifier,
    context: Context,
    contentType: AppContentType,
    navController: NavHostController,
    properties: MutableList<Property>,
    addresses: MutableList<Address>,
    photos: MutableList<Photo>,
    agents: MutableList<Agent>,
    types: MutableList<Type>,
    pois: MutableList<Poi>,
    propertyPoiJoins: MutableList<PropertyPoiJoin>,
    stringTypes: MutableList<String>,
    stringAgents: MutableList<String>,
    appViewModel: AppViewModel,
    listViewModel: ListViewModel,
    detailViewModel: DetailViewModel,
    editViewModel: EditViewModel,
    mapViewModel: MapViewModel,
    searchViewModel: SearchViewModel,
    loanViewModel: LoanViewModel,
    currency: String,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ListAndDetail.routeWithArgs,
    ) {

        composable(
            route = ListAndDetail.routeWithArgs, arguments = ListAndDetail.arguments
        ) { navBackStackEntry ->
            (navBackStackEntry.arguments?.getString(propertyKey)).also {

                /** For both list and detail screens **/
                val propertyId = it?.let {
                    navBackStackEntry.arguments!!.getString(propertyKey)!!.toLong()
                }
                val property = it?.let {
                    if (it.toLong() != NEW_ITEM_ID && properties.isNotEmpty()) listViewModel.propertyFromId(
                        propertyId = it.toLong(),
                        properties = properties
                    ) else null
                }

                /** For list screen only **/
                val itemType: (String) -> String = { typeId ->
                    listViewModel.itemType(typeId, types, stringTypes)
                }
                val onPropertyClick: (Long) -> Unit = { propId ->
                    navController.navigateToDetail(propertyId = propId.toString(), contentType = contentType)
                }
                val onFabClick = {
                    navController.navigateToDetailEdit(propertyId = NEW_ITEM_ID.toString())
                }

                /** For detail screen only **/
                val itemPhotos =
                    it?.let { listViewModel.itemPhotos(propertyId = propertyId!!, photos = photos)
                    } ?: mutableListOf(NO_PHOTO)
                val itemPois =
                    it?.let {
                        listViewModel.itemPois(
                            propertyId = propertyId!!,
                            propertyPoiJoins = propertyPoiJoins,
                            pois = pois,
                        )
                    } ?: mutableListOf()
                val stringType = property?.let {
                    listViewModel.stringType(typeId = property.typeId, types = types, stringTypes = stringTypes)
                } ?: ""
                val stringAgent = property?.let {
                    listViewModel.stringAgent(agentId = property.agentId, agents = agents, stringAgents = stringAgents)
                } ?: ""
                val stringAddress = property?.addressId?.let { addressId ->
                    listViewModel.stringAddress(addressId = addressId, addresses = addresses)
                } ?: ""
                fun stringLatLngItem(latLngItem: String): String {
                    return property?.addressId?.let { addressId ->
                        listViewModel.stringLatLngItem(
                            addressId = addressId,
                            addresses = addresses,
                            latLngItem = latLngItem,
                            context = context
                        )
                    } ?: ""
                }
                val thumbnailUrl = property?.addressId?.let { addressId ->
                    if (addresses.isNotEmpty()) listViewModel.thumbnailUrl(addressId = addressId, addresses = addresses) else ""
                } ?: ""
                val connection by connectivityState()
                val internetAvailable = listViewModel.checkForConnection(connection = connection)
                val networkAvailable by remember(internetAvailable) { mutableStateOf(internetAvailable) }
                val onBackPressed: () -> Unit = { navController.popBackStack() }

                ListAndDetailScreen(
                    // For both list and detail screens
                    property = property,
                    currency = currency,
                    // For list screen only
                    contentType = contentType,
                    items = properties,
                    addresses = addresses,
                    photos = photos,
                    itemType = itemType,
                    onPropertyClick = onPropertyClick,
                    onFabClick = onFabClick,
                    // For detail screen only
                    itemPhotos = itemPhotos,
                    itemPois = itemPois,
                    stringType = stringType,
                    stringAgent = stringAgent,
                    stringAddress = stringAddress,
                    stringLatitude = stringLatLngItem(LatLngItem.LATITUDE.name),
                    stringLongitude = stringLatLngItem(LatLngItem.LONGITUDE.name),
                    thumbnailUrl = thumbnailUrl,
                    networkAvailable = networkAvailable,
                    onBackPressed = onBackPressed,
                )
            }
        }

        composable(route = GeolocMap.route) {
            // Get network connection availability
            val connection by connectivityState()
            val internetAvailable by remember(connection) {
                mutableStateOf(mapViewModel.checkForConnection(connection = connection))
            }
            val networkAvailable by remember(internetAvailable) { mutableStateOf(internetAvailable) }
            // Get permission grants
            var locationPermissionsGranted by remember { mutableStateOf(mapViewModel.areLocPermsGranted()) }

            val startLocationUpdates: () -> Unit = { mapViewModel.startLocationUpdates() }
            val stopLocationUpdates: () -> Unit = { mapViewModel.stopLocationUpdates() }
            val getLocationUpdates: () -> StateFlow<Location?> = { mapViewModel.getLocationUpdates() }

            if (networkAvailable) {
                if (locationPermissionsGranted) {
                    MapScreen(
                        properties = properties,
                        addresses = addresses,
                        startLocationUpdates = startLocationUpdates,
                        stopLocationUpdates = stopLocationUpdates,
                        getLocationUpdates = getLocationUpdates,
                    )
                } else {
                    val onOpenAppSettings = { mapViewModel.openAppSettings(context = context) }
                    val onPermissionsGranted = { locationPermissionsGranted = true }

                    LocationPermissionsScreen(
                        onOpenAppSettings = onOpenAppSettings,
                        onPermissionsGranted = onPermissionsGranted,
                    )
                }
            } else {
                // No network
                MapScreenNoMap(stringResource(id = R.string.network_unavailable))
            }
        }

        composable(route = SearchCriteria.route) {
            val onButton1Click = { navController.navigateToDetail("1", contentType) }
            val onButton2Click = { navController.navigateToDetail("2", contentType) }

            SearchScreen(
                onButton1Click = onButton1Click,
                onButton2Click = onButton2Click,

                )
        }

        composable(route = Loan.route) {
            LoanScreen()
        }

        composable(
            route = Detail.routeWithArgs,
            arguments = Detail.arguments
        ) { navBackStackEntry ->
            val propertyId = navBackStackEntry.arguments!!.getString(propertyKey)!!.toLong()
            val property = if (propertyId != NEW_ITEM_ID && properties.isNotEmpty()) {
                detailViewModel.propertyFromId(propertyId = propertyId, properties = properties) ?: properties[0]
            } else if (properties.isNotEmpty()) {
                properties[0]
            } else {
                CACHE_PROPERTY
            }
            val itemPhotos = detailViewModel.itemPhotos(propertyId = propertyId, photos = photos)
            val itemPois = detailViewModel.itemPois(
                propertyId = propertyId, propertyPoiJoins = propertyPoiJoins, pois = pois,
            )
            val stringType = detailViewModel.stringType(
                typeId = property.typeId, types = types, stringTypes = stringTypes,
            )
            val stringAgent = detailViewModel.stringAgent(
                agentId = property.agentId, agents = agents, stringAgents = stringAgents,
            )
            val stringAddress = property.addressId?.let {
                detailViewModel.stringAddress(addressId = it, addresses = addresses)
            } ?: ""
            fun stringLatLngItem(latLngItem: String): String {
                return property.addressId?.let {
                    detailViewModel.stringLatLngItem(
                        addressId = it,
                        addresses = addresses,
                        latLngItem = latLngItem,
                        context = context
                    )
                } ?: ""
            }
            val thumbnailUrl = property.addressId?.let {
                if (addresses.isNotEmpty()) detailViewModel.thumbnailUrl(addressId = it, addresses = addresses) else ""
            } ?: ""
            val connection by connectivityState()
            val internetAvailable = detailViewModel.checkForConnection(connection = connection)
            val networkAvailable by remember(internetAvailable) { mutableStateOf(internetAvailable) }
            val onBackPressed: () -> Unit = { navController.popBackStack() }

            DetailScreen(
                property = property,
                currency = currency,
                itemPhotos = itemPhotos,
                itemPois = itemPois,
                stringType = stringType,
                stringAgent = stringAgent,
                stringAddress = stringAddress,
                stringLatitude = stringLatLngItem(LatLngItem.LATITUDE.name),
                stringLongitude = stringLatLngItem(LatLngItem.LONGITUDE.name),
                thumbnailUrl = thumbnailUrl,
                networkAvailable = networkAvailable,
                onBackPressed = onBackPressed,
            )
        }

        composable(
            route = EditDetail.routeWithArgs,
            arguments = EditDetail.arguments
        ) { navBackStackEntry ->
            val propertyId = navBackStackEntry.arguments!!.getString(propertyKey)!!.toLong()
            val property: Property? = if (propertyId != NEW_ITEM_ID && properties.isNotEmpty()) {
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
                navController.popBackStack()
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
    }
}

fun NavHostController.navigateSingleTopTo(destination: AppDestination, propertyId: String?) {
    val route =
        if (destination == ListAndDetail) "${destination.route}/${propertyId}" else destination.route
    this.navigate(route = route) {
        popUpTo(id = this@navigateSingleTopTo.graph.findStartDestination().id) { saveState = false }
        launchSingleTop = true
        restoreState = false
    }
}

fun NavHostController.navigateToDetail(propertyId: String, contentType: AppContentType) {
    if (contentType == AppContentType.SCREEN_ONLY) {
        this.navigate(route = "${Detail.route}/$propertyId")
    } else {
        this.navigateSingleTopTo(destination = ListAndDetail, propertyId = propertyId)
    }
}

fun NavHostController.navigateToDetailEdit(propertyId: String) {
    this.navigate(route = "${EditDetail.route}/$propertyId")
}


