package com.aquaero.realestatemanager.ui.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.OtherHouses
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaero.realestatemanager.AppContentType
import com.aquaero.realestatemanager.DEFAULT_START_POSITION
import com.aquaero.realestatemanager.EditField
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.SEARCH_RESULT_START_POSITION
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.NO_PHOTO
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.component.app.PointsOfInterest
import com.aquaero.realestatemanager.ui.component.list_screen.PropertyCard
import com.aquaero.realestatemanager.ui.component.search_screen.SearchScreenDropdownMenu
import com.aquaero.realestatemanager.ui.component.search_screen.SearchScreenRadioButtons
import com.aquaero.realestatemanager.ui.component.search_screen.SearchScreenTextField
import com.aquaero.realestatemanager.ui.component.search_screen.SearchScreenTextFieldMinMax
import com.aquaero.realestatemanager.ui.theme.White

@Composable
fun SearchScreen(
    stringTypes: MutableList<String>,
    stringAgents: MutableList<String>,
    currency: String,
    searchResults: MutableList<Property>,
    scrollToResultsCounter: Int,
    addresses: List<Address>,
    photos: List<Photo>,
    itemType: (String) -> String,
    descriptionValue: String?,
    zipValue: String?,
    cityValue: String?,
    stateValue: String?,
    countryValue: String?,
    priceMinValue: String?,
    priceMaxValue: String?,
    surfaceMinValue: String?,
    surfaceMaxValue: String?,
    roomsMinValue: String?,
    roomsMaxValue: String?,
    bathroomsMinValue: String?,
    bathroomsMaxValue: String?,
    bedroomsMinValue: String?,
    bedroomsMaxValue: String?,
    registrationDateMinValue: String?,
    registrationDateMaxValue: String?,
    saleDateMinValue: String?,
    saleDateMaxValue: String?,
    typeValue: String,
    agentValue: String,
    salesRadioIndex: Int,
    photosRadioIndex: Int,
    itemPois: MutableList<Poi>,
    onFieldValueChange: (String, String?, String) -> Unit,
    onDropdownMenuValueChange: (String) -> Unit,
    onPoiClick: (String, Boolean) -> Unit,
    onSalesRadioButtonClick: (String) -> Unit,
    onPhotosRadioButtonClick: (String) -> Unit,
    onClearButtonClick: (String, String) -> Unit,
    onClearAllButtonClick: () -> Unit,
    popBackStack: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    var counter by remember { mutableIntStateOf(0) }    // Counter set to avoid scrolling at first screen display
    var scrollToTop by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(key1 = scrollToResultsCounter) {
        counter += 1
        if (counter > 1 ) {
            scrollState.animateScrollTo(SEARCH_RESULT_START_POSITION)
        }
    }
    LaunchedEffect(key1 = scrollToTop)  {
        if (scrollToTop) {
            scrollState.animateScrollTo(DEFAULT_START_POSITION)
            scrollToTop = false
        }
    }

    Column(
        modifier = Modifier
            .clickable { focusManager.clearFocus() } // To clear text field focus when clicking outside it.
            .fillMaxSize()
            .verticalScroll(
                state = scrollState,
                enabled = true
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        // Title
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 8.dp)
                .padding(top = 12.dp)
                .border(2.dp, color = MaterialTheme.colorScheme.tertiary),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary,
            text = stringResource(id = R.string.search_title)
        )
        // Info
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 56.dp)
                .padding(top = 4.dp, bottom = 12.dp),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface,
            minLines = 1,
            maxLines = 2,
            lineHeight = 12.sp,
            text = stringResource(id = R.string.search_info)
        )

        // Description
        SearchScreenTextField(
            leftLocationField = EditField.DESCRIPTION.name,
            leftIcon = Icons.Default.NoteAlt,
            leftIconCD = stringResource(id = R.string.cd_description),
            leftLabel = stringResource(id = R.string.description),
            leftText = descriptionValue,
            onValueChange = onFieldValueChange,
            onClearButtonClick = { onClearButtonClick("", it) },
        )
        // Price
        SearchScreenTextFieldMinMax(
            labelText = "${stringResource(id = R.string.price)} ($currency)",
            icon = Icons.Default.Money,
            iconCD = stringResource(id = R.string.cd_price),
            minText = priceMinValue,
            maxText = priceMaxValue,
            onValueChange = { fieldType, value ->
                onFieldValueChange(EditField.PRICE.name, fieldType, value)
                Log.w("SearchScreen", "Price: $fieldType value = $value")
            },
            onClearButtonClick = { onClearButtonClick(it, EditField.PRICE.name) },
        )
        // Surface
        SearchScreenTextFieldMinMax(
            labelText = "${stringResource(id = R.string.surface)} (${stringResource(id = R.string.surface_unit)})",
            icon = Icons.Default.AspectRatio,
            iconCD = stringResource(id = R.string.cd_surface),
            minText = surfaceMinValue,
            maxText = surfaceMaxValue,
            onValueChange = { fieldType, value ->
                onFieldValueChange(EditField.SURFACE.name, fieldType, value)
                Log.w("SearchScreen", "Surface: $fieldType value = $value")
            },
            onClearButtonClick = { onClearButtonClick(it, EditField.SURFACE.name) },
        )
        // Number of rooms
        SearchScreenTextFieldMinMax(
            labelText = stringResource(id = R.string.rooms),
            icon = Icons.Default.OtherHouses,
            iconCD = stringResource(id = R.string.cd_rooms),
            minText = roomsMinValue,
            maxText = roomsMaxValue,
            onValueChange = { fieldType, value ->
                onFieldValueChange(EditField.ROOMS.name, fieldType, value)
                Log.w("SearchScreen", "Rooms: $fieldType value = $value")
            },
            onClearButtonClick = { onClearButtonClick(it, EditField.ROOMS.name) },
        )
        // Number of bathrooms
        SearchScreenTextFieldMinMax(
            labelText = stringResource(id = R.string.bathrooms),
            icon = Icons.Default.Bathtub,
            iconCD = stringResource(id = R.string.cd_bathrooms),
            minText = bathroomsMinValue,
            maxText = bathroomsMaxValue,
            onValueChange = { fieldType, value ->
                onFieldValueChange(EditField.BATHROOMS.name, fieldType, value)
                Log.w("SearchScreen", "Bathrooms: $fieldType value = $value")
            },
            onClearButtonClick = { onClearButtonClick(it, EditField.BATHROOMS.name) },
        )
        // Number of bedrooms
        SearchScreenTextFieldMinMax(
            labelText = stringResource(id = R.string.bedrooms),
            icon = Icons.Default.Bed,
            iconCD = stringResource(id = R.string.cd_bedrooms),
            minText = bedroomsMinValue,
            maxText = bedroomsMaxValue,
            onValueChange = { fieldType, value ->
                onFieldValueChange(EditField.BEDROOMS.name, fieldType, value)
                Log.w("SearchScreen", "Bedrooms: $fieldType value = $value")
            },
            onClearButtonClick = { onClearButtonClick(it, EditField.BEDROOMS.name) },
        )
        // Type and agent
        SearchScreenDropdownMenu(
            stringTypes = stringTypes,
            stringAgents = stringAgents,
            typeIcon = Icons.Default.House,
            typeIconCD = stringResource(id = R.string.cd_type),
            typeText = typeValue,
            agentIcon = Icons.Default.Person,
            agentIconCD = stringResource(id = R.string.cd_agent),
            agentText = agentValue,
            onValueChange = onDropdownMenuValueChange,
            onClearButtonClick = { onClearButtonClick("", it) },
        )
        // ZIP code and city
        SearchScreenTextField(
            leftLocationField = EditField.ZIP_CODE.name,
            leftIcon = Icons.Default.LocationOn,
            leftIconCD = stringResource(id = R.string.cd_address),
            leftLabel = stringResource(id = R.string.zip_code),
            leftText = zipValue,
            rightLocationField = EditField.CITY.name,
            rightIcon = Icons.Default.LocationOn,
            rightIconCD = stringResource(id = R.string.cd_address),
            rightLabel = stringResource(id = R.string.city),
            rightText = cityValue,
            onValueChange = onFieldValueChange,
            onClearButtonClick = { onClearButtonClick("", it) },
        )
        // State and country
        SearchScreenTextField(
            leftLocationField = EditField.STATE.name,
            leftIcon = Icons.Default.LocationOn,
            leftIconCD = stringResource(id = R.string.cd_address),
            leftLabel = stringResource(id = R.string.state),
            leftText = stateValue,
            rightLocationField = EditField.COUNTRY.name,
            rightIcon = Icons.Default.LocationOn,
            rightIconCD = stringResource(id = R.string.cd_address),
            rightLabel = stringResource(id = R.string.country),
            rightText = countryValue,
            onValueChange = onFieldValueChange,
            onClearButtonClick = { onClearButtonClick("", it) },
        )
        // Registration date
        SearchScreenTextFieldMinMax(
            labelText = stringResource(id = R.string.registration_date),
            icon = Icons.Default.ArrowCircleDown,
            iconCD = stringResource(id = R.string.cd_registration_date),
            minText = registrationDateMinValue,
            maxText = registrationDateMaxValue,
            onValueChange = { fieldType, value ->
                onFieldValueChange(EditField.REGISTRATION_DATE.name, fieldType, value)
                Log.w("SearchScreen", "Registration date: $fieldType value = $value")
            },
            onClearButtonClick = { onClearButtonClick(it, EditField.REGISTRATION_DATE.name) },
            shouldBeDigitsOnly = false,
            fieldsAreDates = true,
        )
        // Sale date
        SearchScreenTextFieldMinMax(
            labelText = stringResource(id = R.string.sale_date),
            icon = Icons.Default.ArrowCircleUp,
            iconCD = stringResource(id = R.string.cd_sale_date),
            minText = saleDateMinValue,
            maxText = saleDateMaxValue,
            onValueChange = { fieldType, value ->
                onFieldValueChange(EditField.SALE_DATE.name, fieldType, value)
                Log.w("SearchScreen", "Sale date: $fieldType value = $value")
            },
            onClearButtonClick = { onClearButtonClick(it, EditField.SALE_DATE.name) },
            shouldBeDigitsOnly = false,
            fieldsAreDates = true,
        )
        // Sales status
        SearchScreenRadioButtons(
            radioOptions = listOf(
                stringResource(id = R.string.for_sale),
                stringResource(id = R.string.sold),
                stringResource(id = R.string.both)
            ),
            radioIndex = salesRadioIndex,
            radioButtonClick = onSalesRadioButtonClick,
        )
        // Photos status
        SearchScreenRadioButtons(
            radioOptions = listOf(
                stringResource(id = R.string.with_photo),
                stringResource(id = R.string.without_photo),
                stringResource(id = R.string.both)
            ),
            radioIndex = photosRadioIndex,
            radioButtonClick = onPhotosRadioButtonClick,
        )
        // Points of interest
        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .border(width = 1.dp, color = MaterialTheme.colorScheme.tertiary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PointsOfInterest(
                onPoiClick = onPoiClick,
                itemPois = itemPois,
                clickable = true,
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Clear all criteria button
        ScreenButton(
            paddingTop = 20.dp,
            paddingBottom = 10.dp,
            onClick = {
                onClearAllButtonClick()
                scrollToTop = true
            },
            imageVector = Icons.Default.Cancel,
            contentDescription = stringResource(id = R.string.cd_button_clear_all),
            text = stringResource(id = R.string.clear_all)
        )

        // Top of screen button
        ScreenButton(
            paddingTop = 10.dp,
            paddingBottom = 0.dp,
            onClick = { scrollToTop = true },
            imageVector = Icons.Default.ArrowUpward,
            contentDescription = stringResource(id = R.string.cd_button_to_top),
            text = stringResource(id = R.string.to_top)
        )

        /* List of results */

        // Title
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 8.dp)
                .padding(top = 20.dp)
                .border(2.dp, color = MaterialTheme.colorScheme.tertiary),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary,
            text = stringResource(id = R.string.search_results)
        )

        // List
        Column(
            modifier = Modifier
                .height(680.dp)
                .padding(top = 20.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = LazyListState(firstVisibleItemIndex = 0),
            ) {
                items(items = searchResults) { propertyItem ->
                    val photo = photos.find { it.propertyId == propertyItem.propertyId }
                    val phId = photo?.photoId ?: NO_PHOTO.photoId
                    val phUri = photo?.uri ?: NO_PHOTO.uri
                    val pId = propertyItem.propertyId
                    val pType = itemType(propertyItem.typeId)
                    val pCity = addresses.find { it.addressId == propertyItem.addressId }?.city ?: ""
                    val pPriceFormatted = propertyItem.priceFormattedInCurrency(currency)
                    val isSold = propertyItem.saleDate != null

                    PropertyCard(
                        contentType = AppContentType.LIST_OR_DETAIL,
                        pId = pId,
                        pType = pType,
                        pCity = pCity,
                        phId = phId,
                        phUri = phUri,
                        pPriceFormatted = pPriceFormatted,
                        isSold = isSold,
                        selected = false,
                        unselectedByDefaultDisplay = false,
                        onSelection = {  },
                        onPropertyClick = {  },
                    )
                }
            }
        }

    }

    // To manage back nav
    BackHandler(true) {
        Log.w("OnBackPressed", "SearchScreen OnBackPressed")
        popBackStack()
    }

}

@Composable
fun ScreenButton(
    paddingTop: Dp,
    paddingBottom: Dp,
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String,
    text: String,
) {
    Button(
        modifier = Modifier.padding(top = paddingTop, bottom = paddingBottom),
        contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
        colors = ButtonDefaults.buttonColors().copy(containerColor = MaterialTheme.colorScheme.secondary),
        onClick = onClick
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = White,
        )
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            color = White,
            fontWeight = FontWeight.Bold,
            text = text
        )
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = White,
        )
    }
}
