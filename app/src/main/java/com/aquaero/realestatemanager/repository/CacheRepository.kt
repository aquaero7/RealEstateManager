package com.aquaero.realestatemanager.repository

import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.EditField
import com.aquaero.realestatemanager.NonEditField
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.CACHE_ADDRESS
import com.aquaero.realestatemanager.model.CACHE_PROPERTY
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class CacheRepository {

    private var cacheProperty: Property = CACHE_PROPERTY.copy()
    private var initialAddress: Address? = null
    private var cacheAddress: Address = CACHE_ADDRESS.copy()
    private var cacheStringType: String = ""
    private var cacheStringAgent: String = ""
    private var initialItemPois: MutableList<Poi> = mutableListOf()
    private var cacheItemPois: MutableList<Poi> = mutableListOf()
    private var initialItemPhotos: MutableList<Photo> = mutableListOf()
    private var _cacheItemPhotos: MutableList<Photo> = mutableListOf()
    private val _cacheItemPhotosFlow: MutableStateFlow<MutableList<Photo>> = MutableStateFlow(_cacheItemPhotos)
    val cacheItemPhotosFlow: Flow<MutableList<Photo>> = _cacheItemPhotosFlow


    fun initCache(
        unassigned: String,
        property: Property?,
        stringType: String?,
        stringAgent: String?,
        address: Address?,
        itemPhotos: MutableList<Photo>,
        itemPois: MutableList<Poi>
    ) {
        cacheProperty = property?.copy() ?: CACHE_PROPERTY.copy()
        initialAddress = address
        cacheAddress = address?.copy() ?: CACHE_ADDRESS.copy()
        cacheStringType = stringType ?: unassigned
        cacheStringAgent = stringAgent ?: unassigned
        initialItemPois = itemPois.toMutableList()
        cacheItemPois = itemPois.toMutableList()
        initialItemPhotos = itemPhotos
        _cacheItemPhotos = itemPhotos.toMutableList()
        _cacheItemPhotosFlow.value = _cacheItemPhotos
    }

    fun clearCache(unassigned: String) {
        cacheProperty = CACHE_PROPERTY.copy()
        initialAddress = null
        cacheAddress = CACHE_ADDRESS.copy()
        cacheStringType = unassigned
        cacheStringAgent = unassigned
        initialItemPois = mutableListOf()
        cacheItemPois = mutableListOf()
        initialItemPhotos = mutableListOf()
        _cacheItemPhotos = mutableListOf()
        _cacheItemPhotosFlow.value = _cacheItemPhotos
    }

    // Getter methods
    fun getCacheProperty() = cacheProperty
    fun getInitialAddress() = initialAddress
    fun getCacheAddress() = cacheAddress
    fun getInitialItemPois() = initialItemPois
    fun getCacheItemPois() = cacheItemPois
    fun getInitialItemPhotos() = initialItemPhotos
    fun getCacheItemPhotos() = _cacheItemPhotos


    fun cacheData(): Triple<Property, Address, MutableList<Poi>> {
        return Triple(cacheProperty, cacheAddress, cacheItemPois)
    }

    fun updateCacheAddress(lat: Double?, lng: Double?) {
        cacheAddress.latitude = lat
        cacheAddress.longitude = lng
    }

    fun updateCacheAddress(addressId: Long) {
        cacheProperty.addressId = addressId
    }

    fun updateCacheItemPois(poiItem: String, isSelected: Boolean) {
        if (isSelected) cacheItemPois.add(Poi(poiId = poiItem)) else cacheItemPois.remove(Poi(poiId = poiItem))
    }

    fun updateCacheItemPhotos(itemPhotos: MutableList<Photo>) {
        _cacheItemPhotos = itemPhotos
        _cacheItemPhotosFlow.value = _cacheItemPhotos
    }

    fun updateCacheItemPhotos(photoIndex: Int, photoToUpdate: Photo) {
        _cacheItemPhotos[photoIndex] = photoToUpdate
        _cacheItemPhotosFlow.value = _cacheItemPhotos
    }

    fun updateCacheItemPhotos(photoToAdd: Photo?, photoToRemove: Photo?) {
        photoToAdd?.let { _cacheItemPhotos.add(photoToAdd) }
        photoToRemove?.let { _cacheItemPhotos.remove(photoToRemove) }
        _cacheItemPhotosFlow.value = _cacheItemPhotos
    }

    fun updateCacheItemPhotos(propertyId: Long) {
        _cacheItemPhotos.forEach { it.propertyId = propertyId }
    }

    fun updateCachePropertyItem(item: String, value: Any?) {
        when (item) {
            DropdownMenuCategory.TYPE.name -> cacheProperty.typeId = value as String
            DropdownMenuCategory.AGENT.name -> cacheProperty.agentId = value as Long

            EditField.PRICE.name -> cacheProperty.price = value as Int?

            EditField.SURFACE.name -> cacheProperty.surface = value as Int?
            EditField.ROOMS.name -> cacheProperty.nbOfRooms = value as Int?
            EditField.BATHROOMS.name -> cacheProperty.nbOfBathrooms = value as Int?
            EditField.BEDROOMS.name -> cacheProperty.nbOfBedrooms = value as Int?
            EditField.DESCRIPTION.name -> cacheProperty.description = value as String
            EditField.REGISTRATION_DATE.name -> cacheProperty.registrationDate = value as String
            EditField.SALE_DATE.name -> cacheProperty.saleDate = value as String

            NonEditField.ADDRESS_ID.name -> cacheProperty.addressId = value as Long?
            NonEditField.PROPERTY_ID.name -> cacheProperty.propertyId = value as Long
        }
    }

    fun updateCacheAddressItem(item: String, value: String) {
        when (item) {
            EditField.STREET_NUMBER.name -> cacheAddress.streetNumber = value
            EditField.STREET_NAME.name -> cacheAddress.streetName = value
            EditField.ADD_INFO.name -> cacheAddress.addInfo = value
            EditField.CITY.name -> cacheAddress.city = value
            EditField.STATE.name -> cacheAddress.state = value
            EditField.ZIP_CODE.name -> cacheAddress.zipCode = value
            EditField.COUNTRY.name -> cacheAddress.country = value
        }
    }

}