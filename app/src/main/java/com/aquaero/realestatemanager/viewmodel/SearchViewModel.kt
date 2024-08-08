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
import com.aquaero.realestatemanager.repository.SearchDataRepository
import com.aquaero.realestatemanager.utils.convertEuroToDollar
import kotlinx.coroutines.flow.Flow

class SearchViewModel(
    private val addressRepository: AddressRepository,
    private val photoRepository: PhotoRepository,
    private val searchDataRepository: SearchDataRepository,
) : ViewModel() {
    // Init search criteria
    var description: String? = searchDataRepository.description
    var priceMin: String? = searchDataRepository.priceMin
    var priceMax: String? = searchDataRepository.priceMax
    var surfaceMin: String? = searchDataRepository.surfaceMin
    var surfaceMax: String? = searchDataRepository.surfaceMax
    var roomsMin: String? = searchDataRepository.roomsMin
    var roomsMax: String? = searchDataRepository.roomsMax
    var bathroomsMin: String? = searchDataRepository.bathroomsMin
    var bathroomsMax: String? = searchDataRepository.bathroomsMax
    var bedroomsMin: String? = searchDataRepository.bedroomsMin
    var bedroomsMax: String? = searchDataRepository.bedroomsMax
    var type: String? = searchDataRepository.type
    var agent: String? = searchDataRepository.agent
    var zip: String? = searchDataRepository.zip
    var city: String? = searchDataRepository.city
    var state: String? = searchDataRepository.state
    var country: String? = searchDataRepository.country
    var registrationDateMin: String? = searchDataRepository.registrationDateMin
    var registrationDateMax: String? = searchDataRepository.registrationDateMax
    var saleDateMin: String? = searchDataRepository.saleDateMin
    var saleDateMax: String? = searchDataRepository.saleDateMax
    var salesRadioIndex: Int = searchDataRepository.salesRadioIndex
    var photosRadioIndex: Int = searchDataRepository.photosRadioIndex
    val itemPois: MutableList<Poi> = searchDataRepository.itemPois

    val searchResultsFlow: Flow<MutableList<Property>> = searchDataRepository.searchResultsFlow
    val scrollToResultsFlow: Flow<Int> = searchDataRepository.scrollToResultsFlow

    init {
        // Init of _searchResults at this place is needed to display the first property added
        searchDataRepository.searchResults.clear()
        searchDataRepository.updateSearchResultsFlow(searchDataRepository.searchResults)
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

        searchDataRepository.salesRadioIndex = DEFAULT_RADIO_INDEX
        searchDataRepository.photosRadioIndex = DEFAULT_RADIO_INDEX
        searchDataRepository.itemPois.clear()
    }

    fun onClearButtonClick(bound: String, field: String) {
        when (field) {
            EditField.DESCRIPTION.name -> searchDataRepository.description = null
            EditField.PRICE.name -> when (bound) {
                MIN -> searchDataRepository.priceMin = null
                MAX -> searchDataRepository.priceMax = null
            }
            EditField.SURFACE.name -> when (bound) {
                MIN -> searchDataRepository.surfaceMin = null
                MAX -> searchDataRepository.surfaceMax = null
            }
            EditField.ROOMS.name -> when (bound) {
                MIN -> searchDataRepository.roomsMin = null
                MAX -> searchDataRepository.roomsMax = null
            }
            EditField.BATHROOMS.name -> when (bound) {
                MIN -> searchDataRepository.bathroomsMin = null
                MAX -> searchDataRepository.bathroomsMax = null
            }
            EditField.BEDROOMS.name -> when (bound) {
                MIN -> searchDataRepository.bedroomsMin = null
                MAX -> searchDataRepository.bedroomsMax = null
            }
            DropdownMenuCategory.TYPE.name -> {
                searchDataRepository.typeIndex = DEFAULT_LIST_INDEX
                searchDataRepository.type = null
            }
            DropdownMenuCategory.AGENT.name -> {
                searchDataRepository.agentIndex = DEFAULT_LIST_INDEX
                searchDataRepository.agent = null
            }
            EditField.ZIP_CODE.name -> searchDataRepository.zip = null
            EditField.CITY.name -> searchDataRepository.city = null
            EditField.STATE.name -> searchDataRepository.state = null
            EditField.COUNTRY.name -> searchDataRepository.country = null
            EditField.REGISTRATION_DATE.name -> when (bound) {
                MIN -> searchDataRepository.registrationDateMin = null
                MAX -> searchDataRepository.registrationDateMax = null
            }
            EditField.SALE_DATE.name -> when (bound) {
                MIN -> searchDataRepository.saleDateMin = null
                MAX -> searchDataRepository.saleDateMax = null
            }
        }
    }

    fun resetData() {
        clearCriteria()
        searchDataRepository.filteredList.clear()
        searchDataRepository.updateSearchResultsFlow(searchDataRepository.filteredList)
    }

    fun resetScrollToResults() {
        searchDataRepository.scrollToResults = 0
        searchDataRepository.updateScrollToResultsFlow(0)
    }

    fun onClickMenu(
        properties: MutableList<Property>,
        addresses: MutableList<Address>,
        types: MutableList<Type>,
        agents: MutableList<Agent>,
        photos: MutableList<Photo>,
        propertyPoiJoins: MutableList<PropertyPoiJoin>,
    ) {
        searchDataRepository.searchResults = properties.toMutableList()

        val results = applyFilters(
            unfilteredList = searchDataRepository.searchResults,
            addresses = addresses,
            types = types,
            agents = agents,
            photos = photos,
            propertyPoiJoins = propertyPoiJoins,
        )
        searchDataRepository.updateSearchResultsFlow(results)

        searchDataRepository.scrollToResults +=1
        searchDataRepository.updateScrollToResultsFlow(searchDataRepository.scrollToResults)
        Log.w("SearchViewModel", "Click on menu valid ${searchDataRepository.scrollToResults} times")
    }

    fun onFieldValueChange(
        field: String,
        fieldType: String?,
        fieldValue: String,
        currency: String
    ) {
        val value: String? = fieldValue.ifEmpty { null }
        val digitalValue: Int? = value?.let {
            if (field != EditField.DESCRIPTION.name && it.isDigitsOnly()) it.toInt() else null
        }
        when (field) {
            EditField.SURFACE.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> searchDataRepository.surfaceMin = digitalValue?.toString()
                    MAX -> searchDataRepository.surfaceMax = digitalValue?.toString()
                }
            }
            EditField.ROOMS.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> searchDataRepository.roomsMin = digitalValue?.toString()
                    MAX -> searchDataRepository.roomsMax = digitalValue?.toString()
                }
            }
            EditField.BATHROOMS.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> searchDataRepository.bathroomsMin = digitalValue?.toString()
                    MAX -> searchDataRepository.bathroomsMax = digitalValue?.toString()
                }
            }
            EditField.BEDROOMS.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> searchDataRepository.bedroomsMin = digitalValue?.toString()
                    MAX -> searchDataRepository.bedroomsMax = digitalValue?.toString()
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
                    MIN -> searchDataRepository.priceMin = price?.toString()
                    MAX -> searchDataRepository.priceMax = price?.toString()
                }
            }
            EditField.DESCRIPTION.name -> {
                Log.w("SearchViewModel", "$field = $value")
                searchDataRepository.description = value
            }
            EditField.CITY.name -> {
                Log.w("SearchViewModel", "$field = $value")
                searchDataRepository.city = value
            }
            EditField.STATE.name -> {
                Log.w("SearchViewModel", "$field = $value")
                searchDataRepository.state = value
            }
            EditField.ZIP_CODE.name -> {
                Log.w("SearchViewModel", "$field = $value")
                searchDataRepository.zip = value
            }
            EditField.COUNTRY.name -> {
                Log.w("SearchViewModel", "$field = $value")
                searchDataRepository.country = value
            }
            EditField.REGISTRATION_DATE.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> searchDataRepository.registrationDateMin = value
                    MAX -> searchDataRepository.registrationDateMax = value
                }
            }
            EditField.SALE_DATE.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> searchDataRepository.saleDateMin = value
                    MAX -> searchDataRepository.saleDateMax = value
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
                searchDataRepository.typeIndex = index
                searchDataRepository.type = stringTypes.elementAt(index)
                Log.w("SearchViewModel", "typeIndex = $index")
            }
            DropdownMenuCategory.AGENT.name -> {
                searchDataRepository.agentIndex = index
                searchDataRepository.agent = stringAgents.elementAt(index)
                Log.w("SearchViewModel", "agentIndex = $index")
            }
        }
    }

    fun onPoiClick(poiItem: String, isSelected: Boolean) {
        if (isSelected) searchDataRepository.itemPois.add(Poi(poiId = poiItem))
        else searchDataRepository.itemPois.remove(Poi(poiId = poiItem))

        val itemPois = searchDataRepository.itemPois
        Log.w("SearchViewModel", "itemPois contains ${itemPois.size} items")
        itemPois.forEach { Log.w("SearchViewModel", it.poiId) }
    }

    fun onSalesRadioButtonClick(context: Context, button: String) {
        Log.w("SearchViewModel", "Sales selection is: $button")
        searchDataRepository.salesRadioIndex = when (button) {
            context.getString(R.string.for_sale) -> 0
            context.getString(R.string.sold) -> 1
            else -> DEFAULT_RADIO_INDEX
        }
    }

    fun onPhotosRadioButtonClick(context: Context, button: String) {
        Log.w("SearchViewModel", "Photos selection is: $button")
        searchDataRepository.photosRadioIndex = when (button) {
            context.getString(R.string.with_photo) -> 0
            context.getString(R.string.without_photo) -> 1
            else -> DEFAULT_RADIO_INDEX
        }
    }

    fun itemType(
        typeId: String,
        types: MutableList<Type>,
        stringTypes: MutableList<String>
    ): String {
        val type = types.find { it.typeId == typeId }
        return type?.let { if (stringTypes.isNotEmpty()) stringTypes.elementAt(types.indexOf(it)) else "" }
            ?: ""
    }

    private fun applyFilters(
        unfilteredList: MutableList<Property>,
        addresses: MutableList<Address>,
        types: MutableList<Type>,
        agents: MutableList<Agent>,
        photos: MutableList<Photo>,
        propertyPoiJoins: MutableList<PropertyPoiJoin>,
    ): MutableList<Property> {
        var filteredList: MutableList<Property> = unfilteredList

        searchDataRepository.surfaceMin?.let { surfaceMin ->
            filteredList = filteredList.filter { property ->
                property.surface?.let { it >= surfaceMin.toInt() } ?: false
            }.toMutableList()
        }
        searchDataRepository.surfaceMax?.let { surfaceMax ->
            filteredList = filteredList.filter { property ->
                property.surface?.let { it <= surfaceMax.toInt() } ?: false
            }.toMutableList()
        }
        searchDataRepository.roomsMin?.let { roomsMin ->
            filteredList = filteredList.filter { property ->
                property.nbOfRooms?.let { it >= roomsMin.toInt() } ?: false
            }.toMutableList()
        }
        searchDataRepository.roomsMax?.let { roomsMax ->
            filteredList = filteredList.filter { property ->
                property.nbOfRooms?.let { it <= roomsMax.toInt() } ?: false
            }.toMutableList()
        }
        searchDataRepository.bathroomsMin?.let { bathroomsMin ->
            filteredList = filteredList.filter { property ->
                property.nbOfBathrooms?.let { it >= bathroomsMin.toInt() } ?: false
            }.toMutableList()
        }
        searchDataRepository.bathroomsMax?.let { bathroomsMax ->
            filteredList = filteredList.filter { property ->
                property.nbOfBathrooms?.let { it <= bathroomsMax.toInt() } ?: false
            }.toMutableList()
        }
        searchDataRepository.bedroomsMin?.let { bedroomsMin ->
            filteredList = filteredList.filter { property ->
                property.nbOfBedrooms?.let { it >= bedroomsMin.toInt() } ?: false
            }.toMutableList()
        }
        searchDataRepository.bedroomsMax?.let { bedroomsMax ->
            filteredList = filteredList.filter { property ->
                property.nbOfBedrooms?.let { it <= bedroomsMax.toInt() } ?: false
            }.toMutableList()
        }
        searchDataRepository.priceMin?.let { priceMin ->
            filteredList = filteredList.filter { property ->
                property.price?.let { it >= priceMin.toInt() } ?: false
            }.toMutableList()
        }
        searchDataRepository.priceMax?.let { priceMax ->
            filteredList = filteredList.filter { property ->
                property.price?.let { it <= priceMax.toInt() } ?: false
            }.toMutableList()
        }
        searchDataRepository.description?.let { description ->
            filteredList =
                filteredList.filter { property -> property.description!!.contains(description) }
                    .toMutableList()
        }
        searchDataRepository.zip?.let { zip ->
            filteredList =
                filteredList.filter { property ->
                    val address =
                        property.addressId?.let { addressRepository.addressFromId(it, addresses) }
                    address?.let { it.zipCode == zip } ?: false
                }.toMutableList()
        }
        searchDataRepository.city?.let { city ->
            filteredList =
                filteredList.filter { property ->
                    val address =
                        property.addressId?.let { addressRepository.addressFromId(it, addresses) }
                    address?.let { it.city == city } ?: false
                }.toMutableList()
        }
        searchDataRepository.state?.let { state ->
            filteredList =
                filteredList.filter { property ->
                    val address =
                        property.addressId?.let { addressRepository.addressFromId(it, addresses) }
                    address?.let { it.state == state } ?: false
                }.toMutableList()
        }
        searchDataRepository.country?.let { country ->
            filteredList =
                filteredList.filter { property ->
                    val address =
                        property.addressId?.let { addressRepository.addressFromId(it, addresses) }
                    address?.let { it.country == country } ?: false
                }.toMutableList()
        }
        searchDataRepository.registrationDateMin?.let { registrationDateMin ->
            filteredList = filteredList.filter { property ->
                property.registrationDate?.let { it >= registrationDateMin } ?: false
            }.toMutableList()
        }
        searchDataRepository.registrationDateMax?.let { registrationDateMax ->
            filteredList = filteredList.filter { property ->
                property.registrationDate?.let { it <= registrationDateMax } ?: false
            }.toMutableList()
        }
        searchDataRepository.saleDateMin?.let { saleDateMin ->
            filteredList = filteredList.filter { property ->
                property.saleDate?.let { it >= saleDateMin } ?: false
            }.toMutableList()
        }
        searchDataRepository.saleDateMax?.let { saleDateMax ->
            filteredList = filteredList.filter { property ->
                property.saleDate?.let { it <= saleDateMax } ?: false
            }.toMutableList()
        }
        searchDataRepository.type?.let {
            val typeId = types.elementAt(searchDataRepository.typeIndex).typeId
            filteredList = filteredList.filter { it.typeId == typeId }.toMutableList()
        }
        searchDataRepository.agent?.let {
            val agentId = agents.elementAt(searchDataRepository.agentIndex).agentId
            filteredList = filteredList.filter { it.agentId == agentId }.toMutableList()
        }
        when (searchDataRepository.salesRadioIndex) {
            0 -> filteredList = filteredList.filter { it.saleDate == null }.toMutableList()
            1 -> filteredList = filteredList.filter { it.saleDate != null }.toMutableList()
        }
        when (searchDataRepository.photosRadioIndex) {
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
        val itemPois = searchDataRepository.itemPois
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