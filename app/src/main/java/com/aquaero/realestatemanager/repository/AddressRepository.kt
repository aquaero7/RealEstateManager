package com.aquaero.realestatemanager.repository

import com.aquaero.realestatemanager.LatLngItem
import com.aquaero.realestatemanager.SM_KEY
import com.aquaero.realestatemanager.SM_MARKER_COLOR
import com.aquaero.realestatemanager.SM_SCALE
import com.aquaero.realestatemanager.SM_SIZE
import com.aquaero.realestatemanager.SM_TYPE
import com.aquaero.realestatemanager.SM_URL
import com.aquaero.realestatemanager.database.dao.AddressDao
import com.aquaero.realestatemanager.model.Address
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AddressRepository(private val addressDao: AddressDao) {


    /* Room: Database CRUD */

    suspend fun upsertAddressInRoom(address: Address): Long {
        return withContext(Dispatchers.IO) {
            addressDao.upsertAddress(address)
        }
    }

    suspend fun deleteAddressFromRoom(address: Address) {
        withContext(Dispatchers.IO) {
            addressDao.deleteAddress(address)
        }
    }

    fun getAddressFromRoom(adId: Long): Flow<Address> {
        return addressDao.getAddress(adId)
    }

    fun getAddressesFromRoom(): Flow<MutableList<Address>> {
        return addressDao.getAddresses()
    }

    fun getAddressesOrderedByIdFromRoom(): Flow<MutableList<Address>> {
        return addressDao.getAddressesOrderedById()
    }

    fun getAddressesOrderedByCityFromRoom(): Flow<MutableList<Address>> {
        return addressDao.getAddressesOrderedByCity()
    }

    /**/


    private fun addressFromId(addressId: Long, addresses: MutableList<Address>): Address? {
        return addresses.find { it.addressId == addressId }
    }

    fun address(propertyId: Long?, addresses: MutableList<Address>): Address? {
        return addresses.find { it.addressId == propertyId }
    }

    fun thumbnailUrlFromAddressId(addressId: Long, addresses: MutableList<Address>): String {
        val smMarkerAddress = addressFromId(addressId = addressId, addresses = addresses)?.toUrl() ?: ""
        return SM_URL + SM_SIZE + SM_SCALE + SM_TYPE + SM_MARKER_COLOR + smMarkerAddress + SM_KEY
    }

    fun stringAddress(addressId: Long, addresses: MutableList<Address>): String {
        return addresses.find { it.addressId == addressId }.toString()
    }

    fun stringLatLngItem(addressId: Long, addresses: MutableList<Address>, latLngItem: String): String {
        val result = when (latLngItem) {
            LatLngItem.LATITUDE.name -> addresses.find { it.addressId == addressId }?.latitude
            LatLngItem.LONGITUDE.name -> addresses.find { it.addressId == addressId }?.longitude
            else -> null
        }
        return result?.let { String.format("%.7f", it) } ?: ""
    }

}

