package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.aquaero.realestatemanager.DEFAULT_LIST_INDEX
import com.aquaero.realestatemanager.DEFAULT_RADIO_INDEX
import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.EditField
import com.aquaero.realestatemanager.MAX
import com.aquaero.realestatemanager.MIN
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.repository.AddressRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.utils.convertEuroToDollar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class SearchViewModel(
    private val addressRepository: AddressRepository,
    private val photoRepository: PhotoRepository,
): ViewModel() {

    // Init search criteria
    var description: String? = null
    var priceMin: String? = null ; var priceMax: String? = null
    var surfaceMin: String? = null ; var surfaceMax: String? = null
    var roomsMin: String? = null ; var roomsMax: String? = null
    var bathroomsMin: String? = null ; var bathroomsMax: String? = null
    var bedroomsMin: String? = null ; var bedroomsMax: String? = null
    private var typeIndex: Int = DEFAULT_LIST_INDEX
    var type: String? = null
    private var agentIndex: Int = DEFAULT_LIST_INDEX
    var agent : String? = null
    var zip: String? = null
    var city: String? = null
    var state: String? = null
    var country: String? = null
    var registrationDateMin: String? = null ; var registrationDateMax: String? = null
    var saleDateMin: String? = null ; var saleDateMax: String? = null
    var salesRadioIndex: Int = DEFAULT_RADIO_INDEX
    var photosRadioIndex: Int = DEFAULT_RADIO_INDEX
    val itemPois: MutableList<Poi> = mutableListOf()
    private var filteredList: MutableList<Property> = mutableListOf()

    private var _searchResults: MutableList<Property> = mutableListOf()
    private val _searchResultsFlow: MutableStateFlow<MutableList<Property>> = MutableStateFlow(_searchResults)
    val searchResultsFlow: Flow<MutableList<Property>> = _searchResultsFlow

    private var _scrollToResults: Int = 0
    private val _scrollToResultsFlow: MutableStateFlow<Int> = MutableStateFlow(_scrollToResults)
    val scrollToResultsFlow: Flow<Int> = _scrollToResultsFlow

    init {
        // Init of _searchResults at this place is needed to display the first property added
        _searchResults = mutableListOf()
        _searchResultsFlow.value = _searchResults
        // Init scroll to results counter
        resetScrollToResults()
    }

    private fun clearCriteria() {
        onClearButtonClick("", EditField.DESCRIPTION.name)
        onClearButtonClick(MIN, EditField.PRICE.name)
        onClearButtonClick(MAX, EditField.PRICE.name)
        onClearButtonClick(MIN, EditField.SURFACE.name)
        onClearButtonClick(MAX, EditField.SURFACE.name)
        onClearButtonClick(MIN, EditField.ROOMS.name)
        onClearButtonClick(MAX, EditField.ROOMS.name)
        onClearButtonClick(MIN, EditField.BATHROOMS.name)
        onClearButtonClick(MAX, EditField.BATHROOMS.name)
        onClearButtonClick(MIN, EditField.BEDROOMS.name)
        onClearButtonClick(MAX, EditField.BEDROOMS.name)
        onClearButtonClick("", DropdownMenuCategory.TYPE.name)
        onClearButtonClick("", DropdownMenuCategory.AGENT.name)
        onClearButtonClick("", EditField.ZIP_CODE.name)
        onClearButtonClick("", EditField.CITY.name)
        onClearButtonClick("", EditField.STATE.name)
        onClearButtonClick("", EditField.COUNTRY.name)
        onClearButtonClick(MIN, EditField.REGISTRATION_DATE.name)
        onClearButtonClick(MAX, EditField.REGISTRATION_DATE.name)
        onClearButtonClick(MIN, EditField.SALE_DATE.name)
        onClearButtonClick(MAX, EditField.SALE_DATE.name)

        salesRadioIndex = DEFAULT_RADIO_INDEX
        photosRadioIndex = DEFAULT_RADIO_INDEX
        itemPois.clear()
    }

    fun onClearButtonClick(bound: String, field: String) {
        when (field) {
            EditField.DESCRIPTION.name -> description = null
            EditField.PRICE.name -> when (bound) {
                MIN -> priceMin = null
                MAX -> priceMax = null
            }
            EditField.SURFACE.name -> when (bound) {
                MIN -> surfaceMin = null
                MAX -> surfaceMax = null
            }
            EditField.ROOMS.name -> when (bound) {
                MIN -> roomsMin = null
                MAX -> roomsMax = null
            }
            EditField.BATHROOMS.name -> when (bound) {
                MIN -> bathroomsMin = null
                MAX -> bathroomsMax = null
            }
            EditField.BEDROOMS.name -> when (bound) {
                MIN -> bedroomsMin = null
                MAX -> bedroomsMax = null
            }
            DropdownMenuCategory.TYPE.name -> {
                typeIndex = DEFAULT_LIST_INDEX
                type = null
            }
            DropdownMenuCategory.AGENT.name -> {
                agentIndex = DEFAULT_LIST_INDEX
                agent = null
            }
            EditField.ZIP_CODE.name -> zip = null
            EditField.CITY.name -> city = null
            EditField.STATE.name -> state = null
            EditField.COUNTRY.name -> country = null
            EditField.REGISTRATION_DATE.name -> when (bound) {
                MIN -> registrationDateMin = null
                MAX -> registrationDateMax = null
            }
            EditField.SALE_DATE.name -> when (bound) {
                MIN -> saleDateMin = null
                MAX -> saleDateMax = null
            }
        }
    }

    fun resetData() {
        clearCriteria()
        filteredList.clear()
        _searchResultsFlow.value = filteredList
    }

    fun resetScrollToResults() {
        _scrollToResults = 0
        _scrollToResultsFlow.value = _scrollToResults
    }

    fun onClickMenu(
        properties: MutableList<Property>,
        addresses: MutableList<Address>,
        types: MutableList<Type>,
        agents: MutableList<Agent>,
        photos: MutableList<Photo>,
        propertyPoiJoins: MutableList<PropertyPoiJoin>,
    ) {
        _searchResults = properties.toMutableList()
        _searchResultsFlow.value = applyFilters(
            unfilteredList = _searchResults,
            addresses = addresses,
            types = types,
            agents = agents,
            photos = photos,
            propertyPoiJoins = propertyPoiJoins,
        )

        _scrollToResults += 1
        _scrollToResultsFlow.value = _scrollToResults
        Log.w("SearchViewModel", "Click on menu valid $_scrollToResults times")
    }

    fun onFieldValueChange(field: String, fieldType: String?, fieldValue: String, currency: String) {
        val value: String? = fieldValue.ifEmpty { null }
        val digitalValue: Int? = value?.let {
            if (field != EditField.DESCRIPTION.name && it.isDigitsOnly()) it.toInt() else null
        }
        when (field) {
            EditField.SURFACE.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> surfaceMin = digitalValue?.toString()
                    MAX -> surfaceMax = digitalValue?.toString()
                }
            }
            EditField.ROOMS.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> roomsMin = digitalValue?.toString()
                    MAX -> roomsMax = digitalValue?.toString()
                }
            }
            EditField.BATHROOMS.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> bathroomsMin = digitalValue?.toString()
                    MAX -> bathroomsMax = digitalValue?.toString()
                }
            }
            EditField.BEDROOMS.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> bedroomsMin = digitalValue?.toString()
                    MAX -> bedroomsMax = digitalValue?.toString()
                }
            }
            EditField.PRICE.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value $currency")
                val price: Int? = digitalValue?.let {
                    when (currency) {
                        "€" -> convertEuroToDollar(euros = it)
                        else -> it
                    }
                }
                when (fieldType) {
                    MIN -> priceMin = price?.toString()
                    MAX -> priceMax = price?.toString()
                }
            }
            EditField.DESCRIPTION.name -> {
                Log.w("SearchViewModel", "$field = $value")
                description = value
            }
            EditField.CITY.name -> {
                Log.w("SearchViewModel", "$field = $value")
                city = value
            }
            EditField.STATE.name -> {
                Log.w("SearchViewModel", "$field = $value")
                state = value
            }
            EditField.ZIP_CODE.name -> {
                Log.w("SearchViewModel", "$field = $value")
                zip = value
            }
            EditField.COUNTRY.name -> {
                Log.w("SearchViewModel", "$field = $value")
                country = value
            }
            EditField.REGISTRATION_DATE.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> registrationDateMin = value
                    MAX -> registrationDateMax = value
                }
            }
            EditField.SALE_DATE.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> saleDateMin = value
                    MAX -> saleDateMax = value
                }
            }
        }
    }

    fun onDropdownMenuValueChange(
        value: String,
        stringTypes: MutableList<String>,
        stringAgents: MutableList<String>,
    ) {
        val category = value.substringBefore(delimiter = "#", missingDelimiterValue = "")
        val index = value.substringAfter(delimiter = "#", missingDelimiterValue = value).toInt()
        when (category) {
            DropdownMenuCategory.TYPE.name -> {
                typeIndex = index
                type = stringTypes.elementAt(index)
                Log.w("SearchViewModel", "typeIndex = $typeIndex")
            }
            DropdownMenuCategory.AGENT.name -> {
                agentIndex = index
                agent = stringAgents.elementAt(index)
                Log.w("SearchViewModel", "agentIndex = $agentIndex")
            }
        }
    }

    fun onPoiClick(poiItem: String, isSelected: Boolean) {
        if (isSelected) itemPois.add(Poi(poiId = poiItem)) else itemPois.remove(Poi(poiId = poiItem))

        Log.w("SearchViewModel", "itemPois contains ${itemPois.size} items")
        itemPois.forEach() { Log.w("SearchViewModel", it.poiId) }
    }

    fun onSalesRadioButtonClick(context: Context, button: String) {
        Log.w("SearchViewModel", "Sales selection is: $button")
        salesRadioIndex = when (button) {
            context.getString(R.string.for_sale) -> 0
            context.getString(R.string.sold) -> 1
            else -> DEFAULT_RADIO_INDEX
        }
    }

    fun onPhotosRadioButtonClick(context: Context, button: String) {
        Log.w("SearchViewModel", "Photos selection is: $button")
        photosRadioIndex = when (button) {
            context.getString(R.string.with_photo) -> 0
            context.getString(R.string.without_photo) -> 1
            else -> DEFAULT_RADIO_INDEX
        }
    }

    fun itemType(typeId: String, types: MutableList<Type>, stringTypes: MutableList<String>): String {
        val type = types.find { it.typeId == typeId }
        return type?.let { if (stringTypes.isNotEmpty()) stringTypes.elementAt(types.indexOf(it)) else "" } ?: ""
    }

    private fun applyFilters(
        unfilteredList: MutableList<Property>,
        addresses: MutableList<Address>,
        types: MutableList<Type>,
        agents: MutableList<Agent>,
        photos: MutableList<Photo>,
        propertyPoiJoins: MutableList<PropertyPoiJoin>,
    ): MutableList<Property> {
        filteredList = unfilteredList

        surfaceMin?.let { surfaceMin ->
            filteredList = filteredList.filter { property ->
                property.surface?.let { it >= surfaceMin.toInt() } ?: false
            }.toMutableList()
        }
        surfaceMax?.let { surfaceMax ->
            filteredList = filteredList.filter { property ->
                property.surface?.let { it <= surfaceMax.toInt() } ?: false
            }.toMutableList()
        }

        roomsMin?.let { roomsMin ->
            filteredList = filteredList.filter { property ->
                property.nbOfRooms?.let { it >= roomsMin.toInt() } ?: false
            }.toMutableList()
        }
        roomsMax?.let { roomsMax ->
            filteredList = filteredList.filter { property ->
                property.nbOfRooms?.let { it <= roomsMax.toInt() } ?: false
            }.toMutableList()
        }

        bathroomsMin?.let { bathroomsMin ->
            filteredList = filteredList.filter { property ->
                property.nbOfBathrooms?.let { it >= bathroomsMin.toInt() } ?: false
            }.toMutableList()
        }
        bathroomsMax?.let { bathroomsMax ->
            filteredList = filteredList.filter { property ->
                property.nbOfBathrooms?.let { it <= bathroomsMax.toInt() } ?: false
            }.toMutableList()
        }

        bedroomsMin?.let { bedroomsMin ->
            filteredList = filteredList.filter { property ->
                property.nbOfBedrooms?.let { it >= bedroomsMin.toInt() } ?: false
            }.toMutableList()
        }
        bedroomsMax?.let { bedroomsMax ->
            filteredList = filteredList.filter { property ->
                property.nbOfBedrooms?.let { it <= bedroomsMax.toInt() } ?: false
            }.toMutableList()
        }

        priceMin?.let { priceMin ->
            filteredList = filteredList.filter { property ->
                property.price?.let { it >= priceMin.toInt() } ?: false
            }.toMutableList()
        }
        priceMax?.let { priceMax ->
            filteredList = filteredList.filter { property ->
                property.price?.let { it <= priceMax.toInt() } ?: false
            }.toMutableList()
        }

        description?.let { description ->
            filteredList =
                filteredList.filter { property -> property.description!!.contains(description) }
                    .toMutableList()
        }

        zip?.let { zip ->
            filteredList =
                filteredList.filter { property ->
                    val address =
                        property.addressId?.let { addressRepository.addressFromId(it, addresses) }
                    address?.let { it.zipCode == zip } ?: false
                }.toMutableList()
        }

        city?.let { city ->
            filteredList =
                filteredList.filter { property ->
                    val address =
                        property.addressId?.let { addressRepository.addressFromId(it, addresses) }
                    address?.let { it.city == city } ?: false
                }.toMutableList()
        }

        state?.let { state ->
            filteredList =
                filteredList.filter { property ->
                    val address =
                        property.addressId?.let { addressRepository.addressFromId(it, addresses) }
                    address?.let { it.state == state } ?: false
                }.toMutableList()
        }

        country?.let { country ->
            filteredList =
                filteredList.filter { property ->
                    val address =
                        property.addressId?.let { addressRepository.addressFromId(it, addresses) }
                    address?.let { it.country == country } ?: false
                }.toMutableList()
        }

        registrationDateMin?.let { registrationDateMin ->
            filteredList = filteredList.filter { property ->
                property.registrationDate?.let { it >= registrationDateMin } ?: false
            }.toMutableList()
        }
        registrationDateMax?.let { registrationDateMax ->
            filteredList = filteredList.filter { property ->
                property.registrationDate?.let { it <= registrationDateMax } ?: false
            }.toMutableList()
        }

        saleDateMin?.let { saleDateMin ->
            filteredList = filteredList.filter { property ->
                property.saleDate?.let { it >= saleDateMin } ?: false
            }.toMutableList()
        }
        saleDateMax?.let { saleDateMax ->
            filteredList = filteredList.filter { property ->
                property.saleDate?.let { it <= saleDateMax } ?: false
            }.toMutableList()
        }

        type?.let {
            val typeId = types.elementAt(typeIndex).typeId
            filteredList = filteredList.filter { it.typeId == typeId }.toMutableList()
        }

        agent?.let {
            val agentId = agents.elementAt(agentIndex).agentId
            filteredList = filteredList.filter { it.agentId == agentId }.toMutableList()
        }

        when (salesRadioIndex) {
            0 -> filteredList = filteredList.filter { it.saleDate == null }.toMutableList()
            1 -> filteredList = filteredList.filter { it.saleDate != null }.toMutableList()
        }

        when (photosRadioIndex) {
            0 -> {
                filteredList = filteredList.filter { property ->
                    val itemPhotos = photoRepository.itemPhotos(property.propertyId, photos)
                    itemPhotos.isNotEmpty()
                }.toMutableList()
            }
            1 -> {
                filteredList = filteredList.filter { property ->
                    val itemPhotos = photoRepository.itemPhotos(property.propertyId, photos)
                    itemPhotos.isEmpty()
                }.toMutableList()
            }
        }

        if (itemPois.isNotEmpty()) {
            filteredList = filteredList.filter { property ->
                itemPois.all { itemPoi ->
                    propertyPoiJoins.any {
                        it.propertyId == property.propertyId && it.poiId == itemPoi.poiId
                    }
                }
            }.toMutableList()
        }

        return filteredList
    }

}