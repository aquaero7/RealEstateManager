package com.aquaero.realestatemanager

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
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
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.component.map_screen.MapScreenNoMap
import com.aquaero.realestatemanager.ui.screen.DetailScreen
import com.aquaero.realestatemanager.ui.screen.EditScreen
import com.aquaero.realestatemanager.ui.screen.ListAndDetailScreen
import com.aquaero.realestatemanager.ui.screen.LoanScreen
import com.aquaero.realestatemanager.ui.screen.LocationPermissionsScreen
import com.aquaero.realestatemanager.ui.screen.MapScreen
import com.aquaero.realestatemanager.ui.screen.SearchScreen
import com.aquaero.realestatemanager.utils.AppContentType
import com.aquaero.realestatemanager.utils.MyLocationSource
import com.aquaero.realestatemanager.utils.connectivityState
import com.aquaero.realestatemanager.viewmodel.AppViewModel
import com.aquaero.realestatemanager.viewmodel.DetailViewModel
import com.aquaero.realestatemanager.viewmodel.EditViewModel
import com.aquaero.realestatemanager.viewmodel.ListViewModel
import com.aquaero.realestatemanager.viewmodel.LoanViewModel
import com.aquaero.realestatemanager.viewmodel.MapViewModel
import com.aquaero.realestatemanager.viewmodel.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow

@SuppressLint("NewApi")
@Composable
fun AppNavHost(
    modifier: Modifier,
    contentType: AppContentType,
    navController: NavHostController,
    properties: List<Property>,
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
            (navBackStackEntry.arguments!!.getString(propertyKey)
                ?: properties[0].pId.toString()).also {

                val property = listViewModel.propertyFromId(it.toLong())
                val thumbnailUrl = listViewModel.thumbnailUrl(property)
                val onPropertyClick: (Long) -> Unit = { propertyId ->
                    navController.navigateToDetail(propertyId.toString(), contentType)
                }
                val connection by connectivityState()
                val internetAvailable = listViewModel.checkForConnection(connection = connection)
                val onFabClick = { navController.navigateToDetailEdit("-1") }
                val onBackPressed: () -> Unit = { navController.popBackStack() }

                ListAndDetailScreen(
                    items = properties,
                    thumbnailUrl = thumbnailUrl,
                    contentType = contentType,
                    onPropertyClick = onPropertyClick,
                    property = property,
                    currency = currency,
                    stringAgent = appViewModel.agentFromId(property.agentId).toString(),
                    internetAvailable = internetAvailable,
                    onFabClick = onFabClick,
                    onBackPressed = onBackPressed,
                )
            }
        }

        composable(route = GeolocMap.route) {
            // Get network connection availability
            val connection by connectivityState()
            val internetAvailable = mapViewModel.checkForConnection(connection = connection)
            // Get permission grants
            var locationPermissionsGranted by remember {
                mutableStateOf(mapViewModel.areLocPermsGranted())
            }

            if (internetAvailable) {
                if (locationPermissionsGranted) {
                    var showMap by remember { mutableStateOf(false) }
                    var currentLocation by remember { mutableStateOf(DEFAULT_LOCATION) }
                    val locationFlow = callbackFlow {
                        while (true) {
                            mapViewModel.getCurrentLocation {
                                currentLocation = it
                                showMap = true
                                trySend(it)
                            }
                            delay(1)
                        }
                    }
                    val locationState = locationFlow.collectAsState(initial = currentLocation)
                    val locationSource = MyLocationSource()
                    MapScreen(
                        showMap = showMap,
                        properties = properties,
                        locationState = locationState,
                        locationSource = locationSource,
                    )
                } else {
                    val onOpenAppSettings = { mapViewModel.openAppSettings() }
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
            val propertyId = navBackStackEntry.arguments!!.getString(propertyKey)!!
            val property = detailViewModel.propertyFromId(propertyId.toLong())
            val thumbnailUrl = detailViewModel.thumbnailUrl(property)
            val connection by connectivityState()
            val internetAvailable = detailViewModel.checkForConnection(connection = connection)
            val onBackPressed: () -> Unit = { navController.popBackStack() }

            DetailScreen(
                property = property,
                thumbnailUrl = thumbnailUrl,
                stringAgent = appViewModel.agentFromId(property.agentId).toString(),
                currency = currency,
                internetAvailable = internetAvailable,
                onBackPressed = onBackPressed,
            )
        }

        composable(
            route = EditDetail.routeWithArgs,
            arguments = EditDetail.arguments
        ) { navBackStackEntry ->
            val propertyId = navBackStackEntry.arguments!!.getString(propertyKey)
            val property: Property? = if (propertyId != "-1") {
                // Edition mode
                editViewModel.propertyFromId(propertyId!!.toLong())
            } else {
                // Creation mode
                null
            }
            val pTypeSet = { editViewModel.pTypesSet }
            val agentSet = editViewModel.agentsSet
            val pTypeIndex = property?.let {
                editViewModel.mutableSetIndex(
                    run(pTypeSet) as MutableSet<Any?>,
                    stringResource(it.pType)
                )
            }
            val agentIndex = property?.let {
                editViewModel.mutableSetIndex(
                    run(agentSet) as MutableSet<Any?>,
                    editViewModel.agentFromId(it.agentId).toString()
                )
            }
            val onDescriptionValueChange: (String) -> Unit = {
                editViewModel.onDescriptionValueChange(propertyId, it)
            }
            val onPriceValueChange: (String) -> Unit = {
                editViewModel.onPriceValueChange(propertyId, it, currency)
            }
            val onSurfaceValueChange: (String) -> Unit = {
                editViewModel.onSurfaceValueChange(propertyId, it)
            }
            val onDropdownMenuValueChange: (String) -> Unit = {
                editViewModel.onDropdownMenuValueChange(propertyId, it)
            }
            val onNbOfRoomsValueChange: (String) -> Unit = {
                editViewModel.onNbOfRoomsValueChange(propertyId, it)
            }
            val onNbOfBathroomsValueChange: (String) -> Unit = {
                editViewModel.onNbOfBathroomsValueChange(propertyId, it)
            }
            val onNbOfBedroomsValueChange: (String) -> Unit = {
                editViewModel.onNbOfBedroomsValueChange(propertyId, it)
            }


            val onLocationValuesChange: (Address) -> Unit = {
                editViewModel.onLocationValuesChange(propertyId, it)
            }
            val onStreetNumberValueChange: (String) -> Unit = {
                editViewModel.onStreetNumberValueChange(propertyId, it)
            }
            val onStreetNameValueChange: (String) -> Unit = {
                editViewModel.onStreetNameValueChange(propertyId, it)
            }
            val onAddInfoValueChange: (String) -> Unit = {
                editViewModel.onAddInfoValueChange(propertyId, it)
            }
            val onCityValueChange: (String) -> Unit = {
                editViewModel.onCityValueChange(propertyId, it)
            }
            val onStateValueChange: (String) -> Unit = {
                editViewModel.onStateValueChange(propertyId, it)
            }
            val onZipCodeValueChange: (String) -> Unit = {
                editViewModel.onZipCodeValueChange(propertyId, it)
            }
            val onCountryValueChange: (String) -> Unit = {
                editViewModel.onCountryValueChange(propertyId, it)
            }


            val onRegistrationDateValueChange: (String) -> Unit = {
                editViewModel.onRegistrationDateValueChange(propertyId, it)
            }
            val onSaleDateValueChange: (String) -> Unit = {
                editViewModel.onSaleDateValueChange(propertyId, it)
            }


            /**
             * Photo shooting and picking
             */
            var buttonSavePhotoEnabled by remember { mutableStateOf(false) }
            var photoToAddUri by remember { mutableStateOf(Uri.EMPTY) }
            val painterResource = painterResource(id = R.drawable.baseline_add_a_photo_black_24)
            var painter by remember(photoToAddUri) { mutableStateOf(painterResource) }

            /**
             * Photo shooting
             */
            val cameraUri: Uri = editViewModel.getPhotoUri()
            var capturedImageUri by remember { mutableStateOf(Uri.EMPTY) }
            val cameraLauncher =
                rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
                    capturedImageUri = cameraUri
                }
            val permissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) {
                if (it) {
                    // Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                    cameraLauncher.launch(cameraUri)
                } else {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
            val onShootPhotoMenuItemClick: () -> Unit = {
                editViewModel.onShootPhotoMenuItemClick(
                    uri = cameraUri,
                    cameraLauncher = cameraLauncher,
                    permissionLauncher = permissionLauncher,
                )
            }

            /**
             * Photo picking
             */
            var pickerUri by remember { mutableStateOf(Uri.EMPTY) }
            val pickerLauncher =
                rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                    //When the user has selected a photo, its URI is returned here
                    if (uri != null) {
                        pickerUri = uri
                    }
                }
            val onSelectPhotoMenuItemClick: () -> Unit = {
                editViewModel.onSelectPhotoMenuItemClick(pickerLauncher = pickerLauncher)
            }

            /**
             * Photo shooting and picking
             */
            // if (capturedImageUri.path?.isNotEmpty() == true) {
            if (capturedImageUri != Uri.EMPTY) {
                photoToAddUri = capturedImageUri
                capturedImageUri = Uri.EMPTY
            }
            // if (pickerUri.path?.isNotEmpty() == true) {
            if (pickerUri != Uri.EMPTY) {
                photoToAddUri = pickerUri
                pickerUri = Uri.EMPTY
            }
            //
            if (photoToAddUri != Uri.EMPTY) {
                painter = rememberAsyncImagePainter(model = photoToAddUri)
                /*  // or...
                painter = rememberAsyncImagePainter(
                    ImageRequest
                        .Builder(context)
                        .data(data = photoToAddUri)
                        .build()
                )
                */
                buttonSavePhotoEnabled = true
            } else {
                painter = painterResource
                buttonSavePhotoEnabled = false
            }
            val onSavePhotoButtonClick: (String) -> Unit = {
                editViewModel.onSavePhotoButtonClick(propertyId.toLong(), photoToAddUri, it)
                photoToAddUri = Uri.EMPTY
            }
            val onEditPhotoMenuItemClick: (Photo) -> Unit = { photo ->
                photoToAddUri = photo.phUri
                buttonSavePhotoEnabled = true
            }
            val onPhotoDeletionConfirmation: (Long) -> Unit = { photoId ->
                editViewModel.onPhotoDeletionConfirmation(photoId, propertyId.toLong())
            }









            val onBackPressed: () -> Unit = { navController.popBackStack() }

            EditScreen(
                pTypeSet = pTypeSet,
                agentSet = agentSet,
                property = property,
                pTypeIndex = pTypeIndex,
                agentIndex = agentIndex,
                currency = currency,
                onDescriptionValueChange = onDescriptionValueChange,
                onPriceValueChange = onPriceValueChange,
                onSurfaceValueChange = onSurfaceValueChange,
                onDropdownMenuValueChange = onDropdownMenuValueChange,
                onNbOfRoomsValueChange = onNbOfRoomsValueChange,
                onNbOfBathroomsValueChange = onNbOfBathroomsValueChange,
                onNbOfBedroomsValueChange = onNbOfBedroomsValueChange,

                onLocationValuesChange = onLocationValuesChange,
                onStreetNumberValueChange = onStreetNumberValueChange,
                onStreetNameValueChange = onStreetNameValueChange,
                onAddInfoValueChange = onAddInfoValueChange,
                onCityValueChange = onCityValueChange,
                onStateValueChange = onStateValueChange,
                onZipCodeValueChange = onZipCodeValueChange,
                onCountryValueChange = onCountryValueChange,

                onRegistrationDateValueChange = onRegistrationDateValueChange,
                onSaleDateValueChange = onSaleDateValueChange,

                onShootPhotoMenuItemClick = onShootPhotoMenuItemClick,
                onSelectPhotoMenuItemClick = onSelectPhotoMenuItemClick,
                buttonAddPhotoEnabled = buttonSavePhotoEnabled,
                painter = painter,
                onSavePhotoButtonClick = onSavePhotoButtonClick,
                onEditPhotoMenuItemClick = onEditPhotoMenuItemClick,
                onPhotoDeletionConfirmation = onPhotoDeletionConfirmation,
                onBackPressed = onBackPressed,
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(destination: AppDestination, propertyId: String) {
    val route =
        if (destination == ListAndDetail) "${destination.route}/${propertyId}" else destination.route
    this.navigate(route) {
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) { saveState = false }
        launchSingleTop = true
        restoreState = false
    }
}

fun NavHostController.navigateToDetail(propertyId: String, contentType: AppContentType) {
    if (contentType == AppContentType.SCREEN_ONLY) {
        this.navigate("${Detail.route}/$propertyId")
    } else {
        this.navigateSingleTopTo(ListAndDetail, propertyId)
    }
}

fun NavHostController.navigateToDetailEdit(propertyId: String) {
    this.navigate("${EditDetail.route}/$propertyId")
}


