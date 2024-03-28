package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.Field
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
    private var description: String? = null
    private var priceMin: Int? = null ; private var priceMax: Int? = null
    private var surfaceMin: Int? = null ; private var surfaceMax: Int? = null
    private var roomsMin: Int? = null ; private var roomsMax: Int? = null
    private var bathroomsMin: Int? = null ; private var bathroomsMax: Int? = null
    private var bedroomsMin: Int? = null ; private var bedroomsMax: Int? = null
    private var typeId: String? = null
    private var agentId: Long? = null
    private var zip: String? = null
    private var city: String? = null
    private var state: String? = null
    private var country: String? = null
    private var registrationDateMin: String? = null ; private var registrationDateMax: String? = null
    private var saleDateMin: String? = null ; private var saleDateMax: String? = null
    private var forSale: Boolean = true ; private var sold: Boolean = true
    private var withPhoto: Boolean = true ; private var withoutPhoto: Boolean = true
    private val itemPois: MutableList<Poi> = mutableListOf()

    private var _searchResults: MutableList<Property> = mutableListOf()
    private val _searchResultsFlow: MutableStateFlow<MutableList<Property>> = MutableStateFlow(_searchResults)
    val searchResultsFlow: Flow<MutableList<Property>> = _searchResultsFlow
    private var filteredList: MutableList<Property> = mutableListOf()

    init {
        // Init of _searchResults at this place is needed to display the first property added
        _searchResults = mutableListOf()
        _searchResultsFlow.value = _searchResults
    }

    private fun clearCriteria() {
        description = null
        priceMin = null ; priceMax = null
        surfaceMin = null ; surfaceMax = null
        roomsMin = null ; roomsMax = null
        bathroomsMin = null ; bathroomsMax = null
        bedroomsMin = null ; bedroomsMax = null
        typeId = null
        agentId = null
        zip = null
        city = null
        state = null
        country = null
        registrationDateMin = null ; registrationDateMax = null
        saleDateMin = null ; saleDateMax = null
        forSale = true ; sold = true
        withPhoto = true ; withoutPhoto = true
        itemPois.clear()
    }

    fun resetData() {
        clearCriteria()
        filteredList.clear()
        _searchResultsFlow.value = filteredList
    }

    fun onClickMenu(
        context: Context,
        properties: MutableList<Property>,
        addresses: MutableList<Address>,
        photos: MutableList<Photo>,
        propertyPoiJoins: MutableList<PropertyPoiJoin>,
    ) {
        _searchResults = properties.toMutableList()
        _searchResultsFlow.value = applyFilters(
            unfilteredList = _searchResults,
            addresses = addresses,
            photos = photos,
            propertyPoiJoins = propertyPoiJoins,
        )
    }

    fun onFieldValueChange(field: String, fieldType: String?, fieldValue: String, currency: String) {
        val value: String? = fieldValue.ifEmpty { null }
        val digitalValue: Int? = value?.let {
            if (field != Field.DESCRIPTION.name && it.isDigitsOnly()) it.toInt() else null
        }
        when (field) {
            Field.SURFACE.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> surfaceMin = digitalValue
                    MAX -> surfaceMax = digitalValue
                }
            }
            Field.ROOMS.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> roomsMin = digitalValue
                    MAX -> roomsMax = digitalValue
                }
            }
            Field.BATHROOMS.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> bathroomsMin = digitalValue
                    MAX -> bathroomsMax = digitalValue
                }
            }
            Field.BEDROOMS.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> bedroomsMin = digitalValue
                    MAX -> bedroomsMax = digitalValue
                }
            }
            Field.PRICE.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value $currency")
                val price: Int? = digitalValue?.let {
                    when (currency) {
                        "€" -> convertEuroToDollar(euros = it)
                        else -> it
                    }
                }
                when (fieldType) {
                    MIN -> priceMin = price
                    MAX -> priceMax = price
                }
            }
            Field.DESCRIPTION.name -> {
                Log.w("SearchViewModel", "$field = $value")
                description = value
            }
            Field.CITY.name -> {
                Log.w("SearchViewModel", "$field = $value")
                city = value
            }
            Field.STATE.name -> {
                Log.w("SearchViewModel", "$field = $value")
                state = value
            }
            Field.ZIP_CODE.name -> {
                Log.w("SearchViewModel", "$field = $value")
                zip = value
            }
            Field.COUNTRY.name -> {
                Log.w("SearchViewModel", "$field = $value")
                country = value
            }
            Field.REGISTRATION_DATE.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value")
                when (fieldType) {
                    MIN -> registrationDateMin = value
                    MAX -> registrationDateMax = value
                }
            }
            Field.SALE_DATE.name -> {
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
        types: MutableList<Type>,
        agents: MutableList<Agent>,
    ) {
        val category = value.substringBefore(delimiter = "#", missingDelimiterValue = "")
        val index = value.substringAfter(delimiter = "#", missingDelimiterValue = value).toInt()
        when (category) {
            DropdownMenuCategory.TYPE.name -> {
                typeId = types.elementAt(index).typeId
                Log.w("SearchViewModel", "TypeId = $typeId")
            }
            DropdownMenuCategory.AGENT.name -> {
                agentId = agents.elementAt(index).agentId
                Log.w("SearchViewModel", "AgentId = $agentId")
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
        when (button) {
            context.getString(R.string.for_sale) -> {
                forSale = true
                sold = false
            }
            context.getString(R.string.sold) -> {
                forSale = false
                sold = true
            }
            else -> {
                forSale = true
                sold = true
            }
        }
    }

    fun onPhotosRadioButtonClick(context: Context, button: String) {
        Log.w("SearchViewModel", "Photos selection is: $button")
        when (button) {
            context.getString(R.string.with_photo) -> {
                withPhoto = true
                withoutPhoto = false
            }
            context.getString(R.string.without_photo) -> {
                withPhoto = false
                withoutPhoto = true
            }
            else -> {
                withPhoto = true
                withoutPhoto = true
            }
        }
    }

    fun itemType(typeId: String, types: MutableList<Type>, stringTypes: MutableList<String>): String {
        val type = types.find { it.typeId == typeId }
        return type?.let { if (stringTypes.isNotEmpty()) stringTypes.elementAt(types.indexOf(it)) else "" } ?: ""
    }

    private fun applyFilters(
        unfilteredList: MutableList<Property>,
        addresses: MutableList<Address>,
        photos: MutableList<Photo>,
        propertyPoiJoins: MutableList<PropertyPoiJoin>,
    ): MutableList<Property> {
//        var filteredList: MutableList<Property> = unfilteredList
        filteredList = unfilteredList

        surfaceMin?.let { surfaceMin ->
            filteredList = filteredList.filter { property ->
                property.surface?.let { it >= surfaceMin } ?: false
            }.toMutableList()
        }
        surfaceMax?.let { surfaceMax ->
            filteredList = filteredList.filter { property ->
                property.surface?.let { it <= surfaceMax } ?: false
            }.toMutableList()
        }

        roomsMin?.let { roomsMin ->
            filteredList = filteredList.filter { property ->
                property.nbOfRooms?.let { it >= roomsMin } ?: false
            }.toMutableList()
        }
        roomsMax?.let { roomsMax ->
            filteredList = filteredList.filter { property ->
                property.nbOfRooms?.let { it <= roomsMax } ?: false
            }.toMutableList()
        }

        bathroomsMin?.let { bathroomsMin ->
            filteredList = filteredList.filter { property ->
                property.nbOfBathrooms?.let { it >= bathroomsMin } ?: false
            }.toMutableList()
        }
        bathroomsMax?.let { bathroomsMax ->
            filteredList = filteredList.filter { property ->
                property.nbOfBathrooms?.let { it <= bathroomsMax } ?: false
            }.toMutableList()
        }

        bedroomsMin?.let { bedroomsMin ->
            filteredList = filteredList.filter { property ->
                property.nbOfBedrooms?.let { it >= bedroomsMin } ?: false
            }.toMutableList()
        }
        bedroomsMax?.let { bedroomsMax ->
            filteredList = filteredList.filter { property ->
                property.nbOfBedrooms?.let { it <= bedroomsMax } ?: false
            }.toMutableList()
        }

        priceMin?.let { priceMin ->
            filteredList = filteredList.filter { property ->
                property.price?.let { it >= priceMin } ?: false
            }.toMutableList()
        }
        priceMax?.let { priceMax ->
            filteredList = filteredList.filter { property ->
                property.price?.let { it <= priceMax } ?: false
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

        typeId?.let { typeId ->
            filteredList = filteredList.filter { it.typeId == typeId }.toMutableList()
        }

        agentId?.let { agentId ->
            filteredList = filteredList.filter { it.agentId == agentId }.toMutableList()
        }

        when {
            forSale && !sold -> filteredList = filteredList.filter { it.saleDate == null }.toMutableList()
            !forSale && sold -> filteredList = filteredList.filter { it.saleDate != null }.toMutableList()
        }

        when {
            withPhoto && !withoutPhoto -> {
                filteredList = filteredList.filter { property ->
                    val itemPhotos = photoRepository.itemPhotos(property.propertyId, photos)
                    itemPhotos.isNotEmpty()
                }.toMutableList()
            }
            !withPhoto && withoutPhoto -> {
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