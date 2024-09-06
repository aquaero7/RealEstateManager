package com.aquaero.realestatemanager.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.aquaero.realestatemanager.DEFAULT_LIST_INDEX
import com.aquaero.realestatemanager.DEFAULT_RADIO_INDEX
import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.EditField
import com.aquaero.realestatemanager.MAX
import com.aquaero.realestatemanager.MIN
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.RadioButtonCategory
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

    /* SEARCH RESULTS FLOW */
    val searchResultsFlow: Flow<MutableList<Property>> = searchRepository.searchResultsFlow
    private fun clearSearchResults() { searchRepository.clearSearchResults() }
    private fun updateSearchResults(results: MutableList<Property>) { searchRepository.updateSearchResults(results) }
    private fun updateSearchResultsFlow() { searchRepository.updateSearchResultsFlow() }

    /* SCROLL TO RESULTS FLOW */
    val scrollToResultsFlow: Flow<Int> = searchRepository.scrollToResultsFlow
    private fun updateScrollToResultsFlow(scroll: Boolean) { searchRepository.updateScrollToResultsFlow(scroll) }

    /* GETTERS */
    fun getDescription(): String? { return searchRepository.getDescription() }
    fun getZip(): String? { return searchRepository.getZip() }
    fun getCity(): String? { return searchRepository.getCity() }
    fun getState(): String? { return searchRepository.getState() }
    fun getCountry(): String? { return searchRepository.getCountry() }
    fun getPriceMin(): String? { return searchRepository.getPriceMin() }
    fun getPriceMax(): String? { return searchRepository.getPriceMax() }
    fun getSurfaceMin(): String? { return searchRepository.getSurfaceMin() }
    fun getSurfaceMax(): String? { return searchRepository.getSurfaceMax() }
    fun getRoomsMin(): String? { return searchRepository.getRoomsMin() }
    fun getRoomsMax(): String? { return searchRepository.getRoomsMax() }
    fun getBathroomsMin(): String? { return searchRepository.getBathroomsMin() }
    fun getBathroomsMax(): String? { return searchRepository.getBathroomsMax() }
    fun getBedroomsMin(): String? { return searchRepository.getBedroomsMin() }
    fun getBedroomsMax(): String? { return searchRepository.getBedroomsMax() }
    fun getRegistrationDateMin(): String? { return searchRepository.getRegistrationDateMin() }
    fun getRegistrationDateMax(): String? { return searchRepository.getRegistrationDateMax() }
    fun getSaleDateMin(): String? { return searchRepository.getSaleDateMin() }
    fun getSaleDateMax(): String? { return searchRepository.getSaleDateMax() }
    fun getType(): String? { return searchRepository.getType() }
    fun getAgent(): String? { return searchRepository.getAgent() }
    fun getSalesRadioIndex(): Int { return searchRepository.getSalesRadioIndex() }
    fun getPhotosRadioIndex(): Int { return searchRepository.getPhotosRadioIndex() }
    fun getItemPois(): MutableList<Poi> { return searchRepository.getItemPois() }

    init {
        // Init of _searchResults at this place is needed to display the first property added
        clearSearchResults()
        updateSearchResultsFlow()
        // Init scroll to results counter
        resetScrollToResults()
    }

    fun resetScrollToResults() { updateScrollToResultsFlow(scroll = false) }

    fun resetData() {
        clearCriteria()
        clearSearchResults()
        updateSearchResultsFlow()
    }

    private fun clearCriteria() { searchRepository.clearCriteria() }

    fun onClearButtonClick(field: String, bound: String? = null) {
        when (field) {
            EditField.DESCRIPTION.name -> searchRepository.setDescription(null)
            EditField.PRICE.name -> when (bound) {
                MIN -> searchRepository.setPriceMin(null)
                MAX -> searchRepository.setPriceMax(null)
            }
            EditField.SURFACE.name -> when (bound) {
                MIN -> searchRepository.setSurfaceMin(null)
                MAX -> searchRepository.setSurfaceMax(null)
            }
            EditField.ROOMS.name -> when (bound) {
                MIN -> searchRepository.setRoomsMin(null)
                MAX -> searchRepository.setRoomsMax(null)
            }
            EditField.BATHROOMS.name -> when (bound) {
                MIN -> searchRepository.setBathroomsMin(null)
                MAX -> searchRepository.setBathroomsMax(null)
            }
            EditField.BEDROOMS.name -> when (bound) {
                MIN -> searchRepository.setBedroomsMin(null)
                MAX -> searchRepository.setBedroomsMax(null)
            }
            DropdownMenuCategory.TYPE.name -> {
                searchRepository.setDropdownMenuCategory(
                    category = DropdownMenuCategory.TYPE.name, index = DEFAULT_LIST_INDEX, value = null
                )
            }
            DropdownMenuCategory.AGENT.name -> {
                searchRepository.setDropdownMenuCategory(
                    category = DropdownMenuCategory.AGENT.name, index = DEFAULT_LIST_INDEX, value = null
                )
            }
            EditField.ZIP_CODE.name -> searchRepository.setZip(null)
            EditField.CITY.name -> searchRepository.setCity(null)
            EditField.STATE.name -> searchRepository.setState(null)
            EditField.COUNTRY.name -> searchRepository.setCountry(null)
            EditField.REGISTRATION_DATE.name -> when (bound) {
                MIN -> searchRepository.setRegistrationDateMin(null)
                MAX -> searchRepository.setRegistrationDateMax(null)
            }
            EditField.SALE_DATE.name -> when (bound) {
                MIN -> searchRepository.setSaleDateMin(null)
                MAX -> searchRepository.setSaleDateMax(null)
            }
        }
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
        updateSearchResults(
            searchRepository.applyFilters(
                unfilteredList = properties,
                addresses = addresses,
                types = types,
                agents = agents,
                photos = photos,
                propertyPoiJoins = propertyPoiJoins,
                currency = currency,
                addressRepository = addressRepository,
                photoRepository = photoRepository,
            )
        )

        updateSearchResultsFlow()
        updateScrollToResultsFlow(scroll = true)
    }

    fun onFieldValueChange(
        field: String,
        bound: String?,
        fieldValue: String,
        currency: String
    ) {
        val value: String? = fieldValue.ifEmpty { null }
        val digitalValue: Int? = value?.let {
            if (field != EditField.DESCRIPTION.name && it.areDigitsOnly()) it.toInt() else null
        }
        when (field) {
            EditField.SURFACE.name -> {
                Log.w("SearchViewModel", "$field: $bound = $value")
                when (bound) {
                    MIN -> searchRepository.setSurfaceMin(digitalValue?.toString())
                    MAX -> searchRepository.setSurfaceMax(digitalValue?.toString())
                }
            }
            EditField.ROOMS.name -> {
                Log.w("SearchViewModel", "$field: $bound = $value")
                when (bound) {
                    MIN -> searchRepository.setRoomsMin(digitalValue?.toString())
                    MAX -> searchRepository.setRoomsMax(digitalValue?.toString())
                }
            }
            EditField.BATHROOMS.name -> {
                Log.w("SearchViewModel", "$field: $bound = $value")
                when (bound) {
                    MIN -> searchRepository.setBathroomsMin(digitalValue?.toString())
                    MAX -> searchRepository.setBathroomsMax(digitalValue?.toString())
                }
            }
            EditField.BEDROOMS.name -> {
                Log.w("SearchViewModel", "$field: $bound = $value")
                when (bound) {
                    MIN -> searchRepository.setBedroomsMin(digitalValue?.toString())
                    MAX -> searchRepository.setBedroomsMax(digitalValue?.toString())
                }
            }
            EditField.PRICE.name -> {
                Log.w("SearchViewModel", "$field: $bound = $value $currency")
                when (bound) {
                    MIN -> searchRepository.setPriceMin(digitalValue?.toString())
                    MAX -> searchRepository.setPriceMax(digitalValue?.toString())
                }
            }
            EditField.DESCRIPTION.name -> {
                Log.w("SearchViewModel", "$field = $value")
                searchRepository.setDescription(value)
            }
            EditField.CITY.name -> {
                Log.w("SearchViewModel", "$field = $value")
                searchRepository.setCity(value)
            }
            EditField.STATE.name -> {
                Log.w("SearchViewModel", "$field = $value")
                searchRepository.setState(value)
            }
            EditField.ZIP_CODE.name -> {
                Log.w("SearchViewModel", "$field = $value")
                searchRepository.setZip(value)
            }
            EditField.COUNTRY.name -> {
                Log.w("SearchViewModel", "$field = $value")
                searchRepository.setCountry(value)
            }
            EditField.REGISTRATION_DATE.name -> {
                Log.w("SearchViewModel", "$field: $bound = $value")
                when (bound) {
                    MIN -> searchRepository.setRegistrationDateMin(value)
                    MAX -> searchRepository.setRegistrationDateMax(value)
                }
            }
            EditField.SALE_DATE.name -> {
                Log.w("SearchViewModel", "$field: $bound = $value")
                when (bound) {
                    MIN -> searchRepository.setSaleDateMin(value)
                    MAX -> searchRepository.setSaleDateMax(value)
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
                Log.w("SearchViewModel", "typeIndex = $index")
                searchRepository.setDropdownMenuCategory(
                    DropdownMenuCategory.TYPE.name, index = index, value = stringTypes.elementAt(index)
                )
            }
            DropdownMenuCategory.AGENT.name -> {
                Log.w("SearchViewModel", "agentIndex = $index")
                searchRepository.setDropdownMenuCategory(
                    DropdownMenuCategory.AGENT.name, index = index, value = stringAgents.elementAt(index)
                )
            }
        }
    }

    fun onPoiClick(poiItem: String, isSelected: Boolean) {
        if (isSelected) searchRepository.updateItemPois(
            trueAddFalseRemove = true, poi = Poi(poiId = poiItem)
        )
        else searchRepository.updateItemPois(
            trueAddFalseRemove = false, poi = Poi(poiId = poiItem)
        )

        val itemPois = searchRepository.getItemPois()
        Log.w("SearchViewModel", "itemPois contains ${itemPois.size} items")
        itemPois.forEach { Log.w("SearchViewModel", it.poiId) }
    }

    fun onRadioButtonClick(category: String, button: Int) {
        when (category) {
            RadioButtonCategory.SALES.name -> {
                Log.w("SearchViewModel", "Sales selection is: $button")
                searchRepository.setSalesRadioIndex(
                    when (button) {
                        R.string.for_sale -> 0
                        R.string.sold -> 1
                        else -> DEFAULT_RADIO_INDEX
                    }
                )
            }
            RadioButtonCategory.PHOTOS.name -> {
                Log.w("SearchViewModel", "Photos selection is: $button")
                searchRepository.setPhotosRadioIndex(
                    when (button) {
                        R.string.with_photo -> 0
                        R.string.without_photo -> 1
                        else -> DEFAULT_RADIO_INDEX
                    }
                )
            }
        }
    }

    fun getItemType(typeId: String, types: MutableList<Type>, stringTypes: MutableList<String>): String {
        return searchRepository.getItemType(typeId, types, stringTypes)
    }

}