package com.aquaero.realestatemanager.ui.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.OtherHouses
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaero.realestatemanager.AppContentType
import com.aquaero.realestatemanager.Field
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.NO_PHOTO
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.component.app.PointsOfInterest
import com.aquaero.realestatemanager.ui.component.list_screen.PropertyCard
import com.aquaero.realestatemanager.ui.component.search_screen.SearchScreenDropdownMenu
import com.aquaero.realestatemanager.ui.component.search_screen.SearchScreenRadioButtons
import com.aquaero.realestatemanager.ui.component.search_screen.SearchScreenTextField
import com.aquaero.realestatemanager.ui.component.search_screen.SearchScreenTextFieldMinMax

@Composable
fun SearchScreen(
    stringTypes: MutableList<String>,
    stringAgents: MutableList<String>,
    currency: String,
    searchResults: MutableList<Property>,
    addresses: List<Address>,
    photos: List<Photo>,
    itemType: (String) -> String,
    onFieldValueChange: (String, String?, String) -> Unit,
    onDropdownMenuValueChange: (String) -> Unit,
    onPoiClick: (String, Boolean) -> Unit,
    onSalesRadioButtonClick: (String) -> Unit,
    onPhotosRadioButtonClick: (String) -> Unit,
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
            leftLocationField = Field.DESCRIPTION.name,
            leftIcon = Icons.Default.NoteAlt,
            leftIconCD = stringResource(id = R.string.cd_description),
            leftLabel = stringResource(id = R.string.description),
            onValueChange = onFieldValueChange
        )
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
        // Type and agent
        SearchScreenDropdownMenu(
            stringTypes = stringTypes,
            stringAgents = stringAgents,
            typeIcon = Icons.Default.House,
            typeIconCD = stringResource(id = R.string.cd_type),
            agentIcon = Icons.Default.Person,
            agentIconCD = stringResource(id = R.string.cd_agent),
            onValueChange = onDropdownMenuValueChange,
        )
        // ZIP code and city
        SearchScreenTextField(
            leftLocationField = Field.ZIP_CODE.name,
            leftIcon = Icons.Default.LocationOn,
            leftIconCD = stringResource(id = R.string.cd_address),
            leftLabel = stringResource(id = R.string.zip_code),
            rightLocationField = Field.CITY.name,
            rightIcon = Icons.Default.LocationOn,
            rightIconCD = stringResource(id = R.string.cd_address),
            rightLabel = stringResource(id = R.string.city),
            onValueChange = onFieldValueChange
        )
        // State and country
        SearchScreenTextField(
            leftLocationField = Field.STATE.name,
            leftIcon = Icons.Default.LocationOn,
            leftIconCD = stringResource(id = R.string.cd_address),
            leftLabel = stringResource(id = R.string.state),
            rightLocationField = Field.COUNTRY.name,
            rightIcon = Icons.Default.LocationOn,
            rightIconCD = stringResource(id = R.string.cd_address),
            rightLabel = stringResource(id = R.string.country),
            onValueChange = onFieldValueChange
        )
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
        // Sales status
        SearchScreenRadioButtons(
            radioOptions = listOf(
                stringResource(id = R.string.for_sale),
                stringResource(id = R.string.sold),
                stringResource(id = R.string.both)
            ),
            radioButtonClick = onSalesRadioButtonClick,
        )
        // Photos status
        SearchScreenRadioButtons(
            radioOptions = listOf(
                stringResource(id = R.string.with_photo),
                stringResource(id = R.string.without_photo),
                stringResource(id = R.string.both)
            ),
            radioButtonClick = onPhotosRadioButtonClick,
        )
        // Points of interest
        PointsOfInterest(
            onPoiClick = onPoiClick,
            itemPois = mutableListOf(),
            clickable = true,
        )

        Spacer(modifier = Modifier.height(12.dp))

        // List of results

        // Title
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 8.dp)
                .padding(top = 32.dp)
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
                    /*
                    val selected by remember(propertyItem, property) {
                        mutableStateOf(
                            selectedId == propertyItem.propertyId ||
                                    property?.propertyId.toString() == propertyItem.propertyId.toString()
                        )
                    }
                    val unselectedByDefaultDisplay by remember(propertyItem, property) {
                        mutableStateOf(property?.propertyId.toString() != propertyItem.propertyId.toString())
                    }
                    val onSelection by remember(propertyItem) {
                        mutableStateOf({ selectedId = propertyItem.propertyId } )
                    }
                    */

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