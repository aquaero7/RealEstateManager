package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import android.util.Log
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
import com.aquaero.realestatemanager.repository.SearchRepository
import com.aquaero.realestatemanager.utils.areDigitsOnly
import kotlinx.coroutines.flow.Flow

class SearchViewModel(
    private val addressRepository: AddressRepository,
    private val photoRepository: PhotoRepository,
    private val searchRepository: SearchRepository,
) : ViewModel() {

    val searchResultsFlow: Flow<MutableList<Property>> = searchRepository.searchResultsFlow
    val scrollToResultsFlow: Flow<Int> = searchRepository.scrollToResultsFlow

    init {
        // Init of _searchResults at this place is needed to display the first property added
        searchRepository.searchResults.clear()
        searchRepository.updateSearchResultsFlow(searchRepository.searchResults)
        // Init scroll to results counter
        resetScrollToResults()
    }

    /* GETTERS */
    fun getScrollToResults(): Int { return searchRepository.scrollToResults }
    fun getDescription(): String? { return searchRepository.description }
    fun getZip(): String? { return searchRepository.zip }
    fun getCity(): String? { return searchRepository.city }
    fun getState(): String? { return searchRepository.state }
    fun getCountry(): String? { return searchRepository.country }
    fun getPriceMin(): String? { return searchRepository.priceMin }
    fun getPriceMax(): String? { return searchRepository.priceMax }
    fun getSurfaceMin(): String? { return searchRepository.surfaceMin }
    fun getSurfaceMax(): String? { return searchRepository.surfaceMax }
    fun getRoomsMin(): String? { return searchRepository.roomsMin }
    fun getRoomsMax(): String? { return searchRepository.roomsMax }
    fun getBathroomsMin(): String? { return searchRepository.bathroomsMin }
    fun getBathroomsMax(): String? { return searchRepository.bathroomsMax }
    fun getBedroomsMin(): String? { return searchRepository.bedroomsMin }
    fun getBedroomsMax(): String? { return searchRepository.bedroomsMax }
    fun getRegistrationDateMin(): String? { return searchRepository.registrationDateMin }
    fun getRegistrationDateMax(): String? { return searchRepository.registrationDateMax }
    fun getSaleDateMin(): String? { return searchRepository.saleDateMin }
    fun getSaleDateMax(): String? { return searchRepository.saleDateMax }
    fun getType(): String? { return searchRepository.type }
    fun getAgent(): String? { return searchRepository.agent }
    fun getSalesRadioIndex(): Int { return searchRepository.salesRadioIndex }
    fun getPhotosRadioIndex(): Int { return searchRepository.photosRadioIndex }
    fun getItemPois(): List<Poi> { return searchRepository.itemPois.toList() }

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

        searchRepository.salesRadioIndex = DEFAULT_RADIO_INDEX
        searchRepository.photosRadioIndex = DEFAULT_RADIO_INDEX
        searchRepository.itemPois.clear()
    }

    fun onClearButtonClick(bound: String, field: String) {
        when (field) {
            EditField.DESCRIPTION.name -> searchRepository.description = null
            EditField.PRICE.name -> when (bound) {
                MIN -> searchRepository.priceMin = null
                MAX -> searchRepository.priceMax = null
            }
            EditField.SURFACE.name -> when (bound) {
                MIN -> searchRepository.surfaceMin = null
                MAX -> searchRepository.surfaceMax = null
            }
            EditField.ROOMS.name -> when (bound) {
                MIN -> searchRepository.roomsMin = null
                MAX -> searchRepository.roomsMax = null
            }
            EditField.BATHROOMS.name -> when (bound) {
                MIN -> searchRepository.bathroomsMin = null
                MAX -> searchRepository.bathroomsMax = null
            }
            EditField.BEDROOMS.name -> when (bound) {
                MIN -> searchRepository.bedroomsMin = null
                MAX -> searchRepository.bedroomsMax = null
            }
            DropdownMenuCategory.TYPE.name -> {
                searchRepository.typeIndex = DEFAULT_LIST_INDEX
                searchRepository.type = null
            }
            DropdownMenuCategory.AGENT.name -> {
                searchRepository.agentIndex = DEFAULT_LIST_INDEX
                searchRepository.agent = null
            }
            EditField.ZIP_CODE.name -> searchRepository.zip = null
            EditField.CITY.name -> searchRepository.city = null
            EditField.STATE.name -> searchRepository.state = null
            EditField.COUNTRY.name -> searchRepository.country = null
            EditField.REGISTRATION_DATE.name -> when (bound) {
                MIN -> searchRepository.registrationDateMin = null
                MAX -> searchRepository.registrationDateMax = null
            }
            EditField.SALE_DATE.name -> when (bound) {
                MIN -> searchRepository.saleDateMin = null
                MAX -> searchRepository.saleDateMax = null
            }
        }
    }

    fun resetData() {
        clearCriteria()
        searchRepository.filteredList.clear()
        searchRepository.updateSearchResultsFlow(searchRepository.filteredList)
    }

    fun resetScrollToResults() {
        searchRepository.scrollToResults = 0
        searchRepository.updateScrollToResultsFlow(0)
    }

    fun onClickMenu(
        properties: MutableList<Property>,
        addresses: MutableList<Address>,
        types: MutableList<Type>,
        agents: MutableList<Agent>,
        photos: MutableList<Photo>,
        propertyPoiJoins: MutableList<PropertyPoiJoin>,
        currency: String,
    ) {
        searchRepository.searchResults = properties.toMutableList()

        val results = applyFilters(
            unfilteredList = searchRepository.searchResults,
            addresses = addresses,
            types = types,
            agents = agents,
            photos = photos,
            propertyPoiJoins = propertyPoiJoins,
            currency = currency,
        )
        searchRepository.updateSearchResultsFlow(results)

        searchRepository.scrollToResults +=1
        searchRepository.updateScrollToResultsFlow(searchRepository.scrollToResults)
        Log.w("SearchViewModel", "Click on menu valid ${searchRepository.scrollToResults} times")
    }

    fun onFieldValueChange(
        field: String,
        fieldType: String?,
        fieldValue: String,
        currency: String
    ) {
        val value: String? = fieldValue.ifEmpty { null }
        val digitalValue: Int? = value?.let {
            if (field != EditField.DESCRIPTION.name && it.areDigitsOnly()) it.toInt() else null
        }
        when (field) {
            EditField.SURFACE.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> searchRepository.surfaceMin = digitalValue?.toString()
                    MAX -> searchRepository.surfaceMax = digitalValue?.toString()
                }
            }
            EditField.ROOMS.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> searchRepository.roomsMin = digitalValue?.toString()
                    MAX -> searchRepository.roomsMax = digitalValue?.toString()
                }
            }
            EditField.BATHROOMS.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> searchRepository.bathroomsMin = digitalValue?.toString()
                    MAX -> searchRepository.bathroomsMax = digitalValue?.toString()
                }
            }
            EditField.BEDROOMS.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> searchRepository.bedroomsMin = digitalValue?.toString()
                    MAX -> searchRepository.bedroomsMax = digitalValue?.toString()
                }
            }
            EditField.PRICE.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value $currency")
                when (fieldType) {
                    MIN -> searchRepository.priceMin = digitalValue?.toString()
                    MAX -> searchRepository.priceMax = digitalValue?.toString()
                }
            }
            EditField.DESCRIPTION.name -> {
                Log.w("SearchViewModel", "$field = $value")
                searchRepository.description = value
            }
            EditField.CITY.name -> {
                Log.w("SearchViewModel", "$field = $value")
                searchRepository.city = value
            }
            EditField.STATE.name -> {
                Log.w("SearchViewModel", "$field = $value")
                searchRepository.state = value
            }
            EditField.ZIP_CODE.name -> {
                Log.w("SearchViewModel", "$field = $value")
                searchRepository.zip = value
            }
            EditField.COUNTRY.name -> {
                Log.w("SearchViewModel", "$field = $value")
                searchRepository.country = value
            }
            EditField.REGISTRATION_DATE.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> searchRepository.registrationDateMin = value
                    MAX -> searchRepository.registrationDateMax = value
                }
            }
            EditField.SALE_DATE.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> searchRepository.saleDateMin = value
                    MAX -> searchRepository.saleDateMax = value
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
                searchRepository.typeIndex = index
                searchRepository.type = stringTypes.elementAt(index)
                Log.w("SearchViewModel", "typeIndex = $index")
            }
            DropdownMenuCategory.AGENT.name -> {
                searchRepository.agentIndex = index
                searchRepository.agent = stringAgents.elementAt(index)
                Log.w("SearchViewModel", "agentIndex = $index")
            }
        }
    }

    fun onPoiClick(poiItem: String, isSelected: Boolean) {
        if (isSelected) searchRepository.itemPois.add(Poi(poiId = poiItem))
        else searchRepository.itemPois.remove(Poi(poiId = poiItem))

        val itemPois = searchRepository.itemPois
        Log.w("SearchViewModel", "itemPois contains ${itemPois.size} items")
        itemPois.forEach { Log.w("SearchViewModel", it.poiId) }
    }

    fun onSalesRadioButtonClick(context: Context, button: String) {
        Log.w("SearchViewModel", "Sales selection is: $button")
        searchRepository.salesRadioIndex = when (button) {
            context.getString(R.string.for_sale) -> 0
            context.getString(R.string.sold) -> 1
            else -> DEFAULT_RADIO_INDEX
        }
    }

    fun onPhotosRadioButtonClick(context: Context, button: String) {
        Log.w("SearchViewModel", "Photos selection is: $button")
        searchRepository.photosRadioIndex = when (button) {
            context.getString(R.string.with_photo) -> 0
            context.getString(R.string.without_photo) -> 1
            else -> DEFAULT_RADIO_INDEX
        }
    }

    fun getItemType(typeId: String, types: MutableList<Type>, stringTypes: MutableList<String>): String {
        return searchRepository.getItemType(typeId, types, stringTypes)
    }

    private fun applyFilters(
        unfilteredList: MutableList<Property>,
        addresses: MutableList<Address>,
        types: MutableList<Type>,
        agents: MutableList<Agent>,
        photos: MutableList<Photo>,
        propertyPoiJoins: MutableList<PropertyPoiJoin>,
        currency: String,
    ): MutableList<Property> {
        var filteredList: MutableList<Property> = unfilteredList

        searchRepository.surfaceMin?.let { surfaceMin ->
            filteredList = filteredList.filter { property ->
                property.surface?.let { it >= surfaceMin.toInt() } ?: false
            }.toMutableList()
        }
        searchRepository.surfaceMax?.let { surfaceMax ->
            filteredList = filteredList.filter { property ->
                property.surface?.let { it <= surfaceMax.toInt() } ?: false
            }.toMutableList()
        }
        searchRepository.roomsMin?.let { roomsMin ->
            filteredList = filteredList.filter { property ->
                property.nbOfRooms?.let { it >= roomsMin.toInt() } ?: false
            }.toMutableList()
        }
        searchRepository.roomsMax?.let { roomsMax ->
            filteredList = filteredList.filter { property ->
                property.nbOfRooms?.let { it <= roomsMax.toInt() } ?: false
            }.toMutableList()
        }
        searchRepository.bathroomsMin?.let { bathroomsMin ->
            filteredList = filteredList.filter { property ->
                property.nbOfBathrooms?.let { it >= bathroomsMin.toInt() } ?: false
            }.toMutableList()
        }
        searchRepository.bathroomsMax?.let { bathroomsMax ->
            filteredList = filteredList.filter { property ->
                property.nbOfBathrooms?.let { it <= bathroomsMax.toInt() } ?: false
            }.toMutableList()
        }
        searchRepository.bedroomsMin?.let { bedroomsMin ->
            filteredList = filteredList.filter { property ->
                property.nbOfBedrooms?.let { it >= bedroomsMin.toInt() } ?: false
            }.toMutableList()
        }
        searchRepository.bedroomsMax?.let { bedroomsMax ->
            filteredList = filteredList.filter { property ->
                property.nbOfBedrooms?.let { it <= bedroomsMax.toInt() } ?: false
            }.toMutableList()
        }
        searchRepository.priceMin?.let { value ->
            val priceMin = searchRepository.convertPrice(value.toInt(), currency).toString()
            filteredList = filteredList.filter { property ->
                property.price?.let { it >= priceMin.toInt() } ?: false
            }.toMutableList()
        }
        searchRepository.priceMax?.let { value ->
            val priceMax = searchRepository.convertPrice(value.toInt(), currency).toString()
            filteredList = filteredList.filter { property ->
                property.price?.let { it <= priceMax.toInt() } ?: false
            }.toMutableList()
        }
        searchRepository.description?.let { description ->
            filteredList =
                filteredList.filter { property -> property.description!!.contains(description) }
                    .toMutableList()
        }
        searchRepository.zip?.let { zip ->
            filteredList =
                filteredList.filter { property ->
                    val address =
                        property.addressId?.let { addressRepository.addressFromId(it, addresses) }
                    address?.let { it.zipCode == zip } ?: false
                }.toMutableList()
        }
        searchRepository.city?.let { city ->
            filteredList =
                filteredList.filter { property ->
                    val address =
                        property.addressId?.let { addressRepository.addressFromId(it, addresses) }
                    address?.let { it.city == city } ?: false
                }.toMutableList()
        }
        searchRepository.state?.let { state ->
            filteredList =
                filteredList.filter { property ->
                    val address =
                        property.addressId?.let { addressRepository.addressFromId(it, addresses) }
                    address?.let { it.state == state } ?: false
                }.toMutableList()
        }
        searchRepository.country?.let { country ->
            filteredList =
                filteredList.filter { property ->
                    val address =
                        property.addressId?.let { addressRepository.addressFromId(it, addresses) }
                    address?.let { it.country == country } ?: false
                }.toMutableList()
        }
        searchRepository.registrationDateMin?.let { registrationDateMin ->
            filteredList = filteredList.filter { property ->
                property.registrationDate?.let { it >= registrationDateMin } ?: false
            }.toMutableList()
        }
        searchRepository.registrationDateMax?.let { registrationDateMax ->
            filteredList = filteredList.filter { property ->
                property.registrationDate?.let { it <= registrationDateMax } ?: false
            }.toMutableList()
        }
        searchRepository.saleDateMin?.let { saleDateMin ->
            filteredList = filteredList.filter { property ->
                property.saleDate?.let { it >= saleDateMin } ?: false
            }.toMutableList()
        }
        searchRepository.saleDateMax?.let { saleDateMax ->
            filteredList = filteredList.filter { property ->
                property.saleDate?.let { it <= saleDateMax } ?: false
            }.toMutableList()
        }
        searchRepository.type?.let {
            val typeId = types.elementAt(searchRepository.typeIndex).typeId
            filteredList = filteredList.filter { it.typeId == typeId }.toMutableList()
        }
        searchRepository.agent?.let {
            val agentId = agents.elementAt(searchRepository.agentIndex).agentId
            filteredList = filteredList.filter { it.agentId == agentId }.toMutableList()
        }
        when (searchRepository.salesRadioIndex) {
            0 -> filteredList = filteredList.filter { it.saleDate == null }.toMutableList()
            1 -> filteredList = filteredList.filter { it.saleDate != null }.toMutableList()
        }
        when (searchRepository.photosRadioIndex) {
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
        val itemPois = searchRepository.itemPois
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