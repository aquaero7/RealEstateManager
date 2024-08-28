package com.aquaero.realestatemanager.repository

import android.util.Log
import com.aquaero.realestatemanager.DEFAULT_LIST_INDEX
import com.aquaero.realestatemanager.DEFAULT_RADIO_INDEX
import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.MAX
import com.aquaero.realestatemanager.MIN
import com.aquaero.realestatemanager.SearchField
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.utils.convertEuroToDollar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.reflect.KMutableProperty1

class SearchRepository {

    // Init search criteria
    private var description: String? = null
    private var priceMin: String? = null
    private var priceMax: String? = null
    private var surfaceMin: String? = null
    private var surfaceMax: String? = null
    private var roomsMin: String? = null
    private var roomsMax: String? = null
    private var bathroomsMin: String? = null
    private var bathroomsMax: String? = null
    private var bedroomsMin: String? = null
    private var bedroomsMax: String? = null
    private var typeIndex: Int = DEFAULT_LIST_INDEX
    private var type: String? = null
    private var agentIndex: Int = DEFAULT_LIST_INDEX
    private var agent : String? = null
    private var zip: String? = null
    private var city: String? = null
    private var state: String? = null
    private var country: String? = null
    private var registrationDateMin: String? = null
    private var registrationDateMax: String? = null
    private var saleDateMin: String? = null
    private var saleDateMax: String? = null
    private var salesRadioIndex: Int = DEFAULT_RADIO_INDEX
    private var photosRadioIndex: Int = DEFAULT_RADIO_INDEX
    private val itemPois: MutableList<Poi> = mutableListOf()
    private var filteredList: MutableList<Property> = mutableListOf()

    /* SEARCH RESULTS FLOW */       // TODO: Case 2: Only if the flow is handled in the repository

    private var searchResults: MutableList<Property> = mutableListOf()
    private val _searchResultsFlow: MutableStateFlow<MutableList<Property>> = MutableStateFlow(searchResults)
    val searchResultsFlow: Flow<MutableList<Property>> = _searchResultsFlow

    fun clearSearchResults() { searchResults = mutableListOf() }
    fun updateSearchResults(results: MutableList<Property>) { searchResults = results }
    fun updateSearchResultsFlow() {
        _searchResultsFlow.value = searchResults
        Log.w("SearchRepository", "Results list contains ${searchResults.size} items")
    }


    /* SCROLL TO RESULTS FLOW */        // TODO: Case 2: Only if the flow is handled in the repository

    private var scrollToResults: Int = 0
    private val _scrollToResultsFlow: MutableStateFlow<Int> = MutableStateFlow(scrollToResults)
    val scrollToResultsFlow: Flow<Int> = _scrollToResultsFlow

    fun updateScrollToResultsFlow(scroll: Boolean) {
        scrollToResults = if (scroll) scrollToResults + 1 else 0
        _scrollToResultsFlow.value = scrollToResults
        Log.w("SearchRepository", "Click on menu valid ${scrollToResults} times")
    }

    /**
     * For test only
     * Returns the repository private MutableStateFlow variable: _scrollToResultsFlow
     */
    fun getScrollToResultsStateFlow(): MutableStateFlow<Int> { return _scrollToResultsFlow }
    /**
     * For test only
     * Returns the repository private Int variable: typeIndex
     */
    fun getTypeIndex(): Int { return typeIndex }
    /**
     * For test only
     * Returns the repository private Int variable: agentIndex
     */
    fun getAgentIndex(): Int { return agentIndex }


    /* GETTERS */
    fun getDescription(): String? { return description }
    fun getPriceMin(): String? { return priceMin }
    fun getPriceMax(): String? { return priceMax }
    fun getSurfaceMin(): String? { return surfaceMin }
    fun getSurfaceMax(): String? { return surfaceMax }
    fun getRoomsMin(): String? { return roomsMin }
    fun getRoomsMax(): String? { return roomsMax }
    fun getBathroomsMin(): String? { return bathroomsMin }
    fun getBathroomsMax(): String? { return bathroomsMax }
    fun getBedroomsMin(): String? { return bedroomsMin }
    fun getBedroomsMax(): String? { return bedroomsMax }
    fun getType(): String? { return type }
    fun getAgent(): String? { return agent }
    fun getZip(): String? { return zip }
    fun getCity(): String? { return city }
    fun getState(): String? { return state }
    fun getCountry(): String? { return country }
    fun getRegistrationDateMin(): String? { return registrationDateMin }
    fun getRegistrationDateMax(): String? { return registrationDateMax }
    fun getSaleDateMin(): String? { return saleDateMin }
    fun getSaleDateMax(): String? { return saleDateMax }
    fun getSalesRadioIndex(): Int { return salesRadioIndex }
    fun getPhotosRadioIndex(): Int { return photosRadioIndex }
    fun getItemPois(): MutableList<Poi> { return itemPois }

    /* SETTERS */
    fun setDescription(value: String?) { description = value }
    fun setPriceMin(value: String?) { priceMin = value }
    fun setPriceMax(value: String?) { priceMax = value }
    fun setSurfaceMin(value: String?) { surfaceMin = value }
    fun setSurfaceMax(value: String?) { surfaceMax = value }
    fun setRoomsMin(value: String?) { roomsMin = value }
    fun setRoomsMax(value: String?) { roomsMax = value }
    fun setBathroomsMin(value: String?) { bathroomsMin = value }
    fun setBathroomsMax(value: String?) { bathroomsMax = value }
    fun setBedroomsMin(value: String?) { bedroomsMin = value }
    fun setBedroomsMax(value: String?) { bedroomsMax = value }
    fun setZip(value: String?) { zip = value }
    fun setCity(value: String?) { city = value }
    fun setState(value: String?) { state = value }
    fun setCountry(value: String?) { country = value }
    fun setRegistrationDateMin(value: String?) { registrationDateMin = value }
    fun setRegistrationDateMax(value: String?) { registrationDateMax = value }
    fun setSaleDateMin(value: String?) { saleDateMin = value }
    fun setSaleDateMax(value: String?) { saleDateMax = value }
    fun setSalesRadioIndex(value: Int) { salesRadioIndex = value }
    fun setPhotosRadioIndex(value: Int) { photosRadioIndex = value }


    fun getItemType(typeId: String, types: MutableList<Type>, stringTypes: MutableList<String>): String {
        val type = types.find { it.typeId == typeId }
        return type?.let { if (stringTypes.isNotEmpty()) stringTypes.elementAt(types.indexOf(it)) else "" } ?: ""
    }

    fun clearItemPois() { itemPois.clear() }

    /**
     * Adds poi if trueAddFalseRemove is true.
     * Removes poi if trueAddFalseRemove is false.
     */
    fun updateItemPois(trueAddFalseRemove: Boolean, poi: Poi) {
        if (trueAddFalseRemove) itemPois.add(poi) else itemPois.remove(poi)
    }

    fun setDropdownMenuCategory(category: String, index: Int, value: String?) {
        when (category) {
            DropdownMenuCategory.TYPE.name -> {
                typeIndex = index
                type = value
            }
            DropdownMenuCategory.AGENT.name -> {
                agentIndex = index
                agent = value
            }
        }
    }

    fun convertPrice(digitalValue: Int?, currency: String): Int? {
        return digitalValue?.let {
            when (currency) {
                "€" -> convertEuroToDollar(euros = it)
                else -> it
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun applyFilters(
        unfilteredList: MutableList<Property>,
        addresses: MutableList<Address>,
        types: MutableList<Type>,
        agents: MutableList<Agent>,
        photos: MutableList<Photo>,
        propertyPoiJoins: MutableList<PropertyPoiJoin>,
        currency: String,
        addressRepository: AddressRepository,
        photoRepository: PhotoRepository,
    ): MutableList<Property> {
        filteredList = unfilteredList

        surfaceMin?.let { surfaceMin ->
            filteredList = filterList(
                SearchField.SURFACE.name, Property::surface as KMutableProperty1<Any, Any?>,
                bound = MIN, fieldValue = surfaceMin
            )
        }
        surfaceMax?.let { surfaceMax ->
            filteredList = filterList(
                SearchField.SURFACE.name, Property::surface as KMutableProperty1<Any, Any?>,
                bound = MAX, fieldValue = surfaceMax
            )
        }
        roomsMin?.let { roomsMin ->
            filteredList = filterList(
                SearchField.ROOMS.name, Property::nbOfRooms as KMutableProperty1<Any, Any?>,
                bound = MIN, fieldValue = roomsMin
            )
        }
        roomsMax?.let { roomsMax ->
            filteredList = filterList(
                SearchField.ROOMS.name, Property::nbOfRooms as KMutableProperty1<Any, Any?>,
                bound = MAX, fieldValue = roomsMax
            )
        }
        bathroomsMin?.let { bathroomsMin ->
            filteredList = filterList(
                SearchField.BATHROOMS.name, Property::nbOfBathrooms as KMutableProperty1<Any, Any?>,
                bound = MIN, fieldValue = bathroomsMin
            )
        }
        bathroomsMax?.let { bathroomsMax ->
            filteredList = filterList(
                SearchField.BATHROOMS.name, Property::nbOfBathrooms as KMutableProperty1<Any, Any?>,
                bound = MAX, fieldValue = bathroomsMax
            )
        }
        bedroomsMin?.let { bedroomsMin ->
            filteredList = filterList(
                SearchField.BEDROOMS.name, Property::nbOfBedrooms as KMutableProperty1<Any, Any?>,
                bound = MIN, fieldValue = bedroomsMin
            )
        }
        bedroomsMax?.let { bedroomsMax ->
            filteredList = filterList(
                SearchField.BEDROOMS.name, Property::nbOfBedrooms as KMutableProperty1<Any, Any?>,
                bound = MAX, fieldValue = bedroomsMax
            )
        }
        priceMin?.let { value ->
            val priceMin = convertPrice(value.toInt(), currency).toString()
            filteredList = filterList(
                SearchField.PRICE.name, Property::price as KMutableProperty1<Any, Any?>,
                bound = MIN, fieldValue = priceMin
            )
        }
        priceMax?.let { value ->
            val priceMax = convertPrice(value.toInt(), currency).toString()
            filteredList = filterList(
                SearchField.PRICE.name, Property::price as KMutableProperty1<Any, Any?>,
                bound = MAX, fieldValue = priceMax
            )
        }
        description?.let { description ->
            filteredList = filterList(
                SearchField.DESCRIPTION.name, Property::description as KMutableProperty1<Any, Any?>,
                fieldValue = description
            )
        }
        zip?.let { zip ->
            filteredList = filterList(
                SearchField.ZIP_CODE.name, Address::zipCode as KMutableProperty1<Any, Any?>,
                fieldValue = zip, addresses = addresses, addressRepository = addressRepository
            )
        }
        city?.let { city ->
            filteredList = filterList(
                SearchField.CITY.name, Address::city as KMutableProperty1<Any, Any?>,
                fieldValue = city, addresses = addresses, addressRepository = addressRepository
            )
        }
        state?.let { state ->
            filteredList = filterList(
                SearchField.STATE.name, Address::state as KMutableProperty1<Any, Any?>,
                fieldValue = state, addresses = addresses, addressRepository = addressRepository
            )
        }
        country?.let { country ->
            filteredList = filterList(
                SearchField.COUNTRY.name, Address::country as KMutableProperty1<Any, Any?>,
                fieldValue = country, addresses = addresses, addressRepository = addressRepository
            )
        }
        registrationDateMin?.let { registrationDateMin ->
            filteredList = filterList(
                SearchField.REGISTRATION_DATE.name, Property::registrationDate as KMutableProperty1<Any, Any?>,
                bound = MIN, fieldValue = registrationDateMin
            )
        }
        registrationDateMax?.let { registrationDateMax ->
            filteredList = filterList(
                SearchField.REGISTRATION_DATE.name, Property::registrationDate as KMutableProperty1<Any, Any?>,
                bound = MAX, fieldValue = registrationDateMax
            )
        }
        saleDateMin?.let { saleDateMin ->
            filteredList = filterList(
                SearchField.SALE_DATE.name, Property::saleDate as KMutableProperty1<Any, Any?>,
                bound = MIN, fieldValue = saleDateMin
            )
        }
        saleDateMax?.let { saleDateMax ->
            filteredList = filterList(
                SearchField.SALE_DATE.name, Property::saleDate as KMutableProperty1<Any, Any?>,
                bound = MAX, fieldValue = saleDateMax
            )
        }
        type?.let {
            val typeId = types.elementAt(typeIndex).typeId
            filteredList = filterList(
                SearchField.TYPE.name, Property::typeId as KMutableProperty1<Any, Any?>,
                fieldValue = typeId
            )
        }
        agent?.let {
            val agentId = agents.elementAt(agentIndex).agentId
            filteredList = filterList(
                SearchField.AGENT.name, Property::agentId as KMutableProperty1<Any, Any?>,
                fieldValue = agentId
            )
        }
        filteredList = filterList(
            SearchField.SALES_STATUS.name, Property::saleDate as KMutableProperty1<Any, Any?>,
            fieldValue = salesRadioIndex
        )
        filteredList = filterList(
            SearchField.PHOTOS_STATUS.name, Property::propertyId as KMutableProperty1<Any, Any?>,
            fieldValue = photosRadioIndex, photos = photos, photoRepository = photoRepository,
        )
        if (itemPois.isNotEmpty()) {
            filteredList = filterList(
                SearchField.POIS.name, Property::propertyId as KMutableProperty1<Any, Any?>,
                propertyPoiJoins = propertyPoiJoins
            )
        }

        return filteredList
    }

    fun filterList(
        field: String,
        getter: KMutableProperty1<Any, Any?>,
        bound: String? = null,
        fieldValue: Any? = null,
        addresses: MutableList<Address>? = null,
        addressRepository: AddressRepository? = null,
        photos: MutableList<Photo>? = null,
        photoRepository: PhotoRepository? = null,
        propertyPoiJoins: MutableList<PropertyPoiJoin>? = null
    ): MutableList<Property> {
        return when (field) {
            SearchField.DESCRIPTION.name -> {
                filteredList.filter { property ->
                    val value = getter.get(property)
                    if (fieldValue != null) {
                        value?.let { (it as String).contains(fieldValue as String, ignoreCase = true) } ?: false
                    } else false
                }.toMutableList()
            }

            SearchField.TYPE.name, SearchField.AGENT.name -> {
                filteredList.filter { property ->
                    val value = getter.get(property)
                    when (getter) {
                        Property::typeId -> value?.let { it == fieldValue } ?: false
                        Property::agentId -> value?.let { it == fieldValue } ?: false
                        else -> { false }
                    }
                }.toMutableList()
            }

            SearchField.SALES_STATUS.name -> {
                when (fieldValue) {
                    0 -> filteredList.filter { property -> getter.get(property) == null }
                        .toMutableList()
                    1 -> filteredList.filter { property -> getter.get(property) != null }
                        .toMutableList()
                    else -> { filteredList }
                }
            }

            SearchField.PHOTOS_STATUS.name -> {
                filteredList.filter { property ->
                    val value = getter.get(property)
                    if (photoRepository != null && photos != null) {
                        value?.let {
                            val itemPhotos = photoRepository.itemPhotos(value as Long, photos)
                            when (fieldValue) {
                                0 -> itemPhotos.isNotEmpty()
                                1 -> itemPhotos.isEmpty()
                                else -> { true }
                            }
                        } ?: false
                    } else false
                }.toMutableList()
            }

            SearchField.POIS.name -> {
                filteredList.filter { property ->
                    val value = getter.get(property)
                    value?.let {
                        if (propertyPoiJoins != null) {
                            itemPois.all { itemPoi ->
                                propertyPoiJoins.any { propertyPoiJoin ->
                                    propertyPoiJoin.propertyId == value && propertyPoiJoin.poiId == itemPoi.poiId
                                }
                            }
                        } else false
                    } ?: false
                }.toMutableList()
            }

            SearchField.ZIP_CODE.name, SearchField.CITY.name,
            SearchField.STATE.name, SearchField.COUNTRY.name -> {
                filteredList.filter { property ->
                    if (addressRepository != null && addresses != null) {
                        val address =
                            property.addressId?.let { addressRepository.addressFromId(it, addresses) }
                        val value = address?.let { getter.get(it) }
                        value?.let { (it as String).contains(fieldValue as String, ignoreCase = true) } ?: false
                    } else false
                }.toMutableList()
            }

            SearchField.REGISTRATION_DATE.name, SearchField.SALE_DATE.name -> {
                filteredList.filter { property ->
                    val value = getter.get(property)
                    when {
                        bound != null && fieldValue != null && bound.contains(MIN, ignoreCase = true) ->
                            value?.let { it as String >= fieldValue as String } ?: false
                        bound != null && fieldValue != null && bound.contains(MAX, ignoreCase = true) ->
                            value?.let { it as String <= fieldValue as String } ?: false
                        else -> { false }
                    }
                }.toMutableList()
            }

            else -> {
                filteredList.filter { property ->
                    val value = getter.get(property)
                    when {
                        bound != null && fieldValue != null && bound.contains(MIN, ignoreCase = true) ->
                            value?.let { it as Int >= (fieldValue as String).toInt() } ?: false
                        bound != null && fieldValue != null && bound.contains(MAX, ignoreCase = true) ->
                            value?.let { it as Int <= (fieldValue as String).toInt() } ?: false
                        else -> { false }
                    }
                }.toMutableList()
            }
        }
    }

}