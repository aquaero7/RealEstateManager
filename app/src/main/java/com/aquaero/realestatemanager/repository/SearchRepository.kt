package com.aquaero.realestatemanager.repository

import com.aquaero.realestatemanager.DEFAULT_LIST_INDEX
import com.aquaero.realestatemanager.DEFAULT_RADIO_INDEX
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.utils.convertEuroToDollar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class SearchRepository {

    // Init search criteria
    var description: String? = null
    var priceMin: String? = null
    var priceMax: String? = null
    var surfaceMin: String? = null
    var surfaceMax: String? = null
    var roomsMin: String? = null
    var roomsMax: String? = null
    var bathroomsMin: String? = null
    var bathroomsMax: String? = null
    var bedroomsMin: String? = null
    var bedroomsMax: String? = null
    var typeIndex: Int = DEFAULT_LIST_INDEX
    var type: String? = null
    var agentIndex: Int = DEFAULT_LIST_INDEX
    var agent : String? = null
    var zip: String? = null
    var city: String? = null
    var state: String? = null
    var country: String? = null
    var registrationDateMin: String? = null
    var registrationDateMax: String? = null
    var saleDateMin: String? = null
    var saleDateMax: String? = null
    var salesRadioIndex: Int = DEFAULT_RADIO_INDEX
    var photosRadioIndex: Int = DEFAULT_RADIO_INDEX
    val itemPois: MutableList<Poi> = mutableListOf()
    var filteredList: MutableList<Property> = mutableListOf()

    var searchResults: MutableList<Property> = mutableListOf()
    private val _searchResultsFlow: MutableStateFlow<MutableList<Property>> = MutableStateFlow(searchResults)
    val searchResultsFlow: Flow<MutableList<Property>> = _searchResultsFlow

    var scrollToResults: Int = 0
    private val _scrollToResultsFlow: MutableStateFlow<Int> = MutableStateFlow(scrollToResults)
    val scrollToResultsFlow: Flow<Int> = _scrollToResultsFlow


    fun updateSearchResultsFlow(results: MutableList<Property>) { _searchResultsFlow.value = results }
    fun updateScrollToResultsFlow(scroll: Int) { _scrollToResultsFlow.value = scroll }
    fun updateScrollToResultsFlow(scroll: Boolean) {
        scrollToResults = if (scroll) scrollToResults + 1 else 0
        _scrollToResultsFlow.value = scrollToResults
    }


    fun convertPrice(digitalValue: Int?, currency: String): Int? {
        return digitalValue?.let {
            when (currency) {
                "€" -> convertEuroToDollar(euros = it)
                else -> it
            }
        }
    }

    fun getItemType(typeId: String, types: MutableList<Type>, stringTypes: MutableList<String>): String {
        val type = types.find { it.typeId == typeId }
        return type?.let { if (stringTypes.isNotEmpty()) stringTypes.elementAt(types.indexOf(it)) else "" } ?: ""
    }

}