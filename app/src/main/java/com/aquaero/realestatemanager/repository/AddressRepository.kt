package com.aquaero.realestatemanager.repository

import android.content.Context
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.SM_KEY
import com.aquaero.realestatemanager.SM_MARKER_COLOR
import com.aquaero.realestatemanager.SM_SCALE
import com.aquaero.realestatemanager.SM_SIZE
import com.aquaero.realestatemanager.SM_TYPE
import com.aquaero.realestatemanager.SM_URL
import com.aquaero.realestatemanager.database.dao.AddressDao
import com.aquaero.realestatemanager.model.Address
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AddressRepository(private val addressDao: AddressDao) {

    private val context: Context by lazy { ApplicationRoot.getContext() }


    /** Room: Database CRUD **/

    suspend fun upsertAddressInRoom(address: Address) {
        withContext(Dispatchers.IO) {
            addressDao.upsertAddress(address)
        }
    }

    suspend fun deleteAddressFromRoom(address: Address) {
        withContext(Dispatchers.IO) {
            addressDao.deleteAddress(address)
        }
    }

    /*suspend*/ fun getAddressFromRoom(adId: Long): Flow<Address> {
        /*
        return withContext(Dispatchers.IO) {
            addressDao.getAddress(adId)
        }
        */
        return addressDao.getAddress(adId)
    }

    /*suspend*/ fun getAddressesFromRoom(): Flow<MutableList<Address>> {
        /*
        return withContext(Dispatchers.IO) {
            addressDao.getAddresses()
        }
        */
        return addressDao.getAddresses()
    }

    /*suspend*/ fun getAddressesOrderedByIdFromRoom(): Flow<MutableList<Address>> {
        /*
        return withContext(Dispatchers.IO) {
            addressDao.getAddressesOrderedById()
        }
        */
        return addressDao.getAddressesOrderedById()
    }

    /*suspend*/ fun getAddressesOrderedByCityFromRoom(): Flow<MutableList<Address>> {
        /*
        return withContext(Dispatchers.IO) {
            addressDao.getAddressesOrderedByCity()
        }
        */
        return addressDao.getAddressesOrderedByCity()
    }

    /***/


    fun addressFromId(addresses: MutableList<Address>, addressId: Long): Address {
//        return fakeAddresses.find { it.addressId == addressId }!!
        return addresses.first { it.addressId == addressId }
    }

    fun thumbnailUrlFromAddressId(addresses: MutableList<Address>, addressId: Long): String {
        val smMarkerAddress = addressFromId(addresses, addressId).toUrl()
        return SM_URL + SM_SIZE + SM_SCALE + SM_TYPE + SM_MARKER_COLOR + smMarkerAddress + SM_KEY
    }

    fun stringAddress(addresses: MutableList<Address>, addressId: Long): String {
        return addresses.find { it.addressId == addressId }.toString()
    }

    fun stringLatitude(addresses: MutableList<Address>, addressId: Long): String {
        return addresses.find { it.addressId == addressId }?.latitude?.toString() ?: ""
    }

    fun stringLongitude(addresses: MutableList<Address>, addressId: Long): String {
        return addresses.find { it.addressId == addressId }?.longitude?.toString() ?: ""
    }


    /**
     * FAKE ADDRESSES
     */

    val fakeAddresses = listOf(
        Address(
            addressId = -1,
            streetNumber = "3",                    //"n1111111",
            streetName = "avenue de Brehat",       //"s1111111",
            addInfo = "",                          //"i1111111",
            city = "Villebon-sur-Yvette",          //"v1111111",
            state = "",                            //"d1111111",
            zipCode = "91140",                     //"z1111111",
            country = "FR",                        //"c1111111",
            latitude = 48.6860854,
            longitude = 2.2201107,
//            latLng = LatLng(48.6860854, 2.2201107)
        ),
        Address(
            addressId = -2,
            streetNumber = "35",                  //"n2222222",
            streetName = "route de Paris",        //"s2222222",
            addInfo = "ZAC Les 4 Chênes",         //"i2222222",
            city = "Pontault-Combault",           //"v2222222",
            state = "",                           //"d2222222",
            zipCode = "77340",                    //"z2222222",
            country = "FR",                       //"c2222222",
            latitude = 48.7765790,
            longitude = 2.5906768,
//            latLng = LatLng(48.7765790,2.5906768)
        ),
        Address(
            addressId = -3,
            streetNumber = "500",                //"n3333333",
            streetName = "Brookhaven Ave",       //"s3333333",
            addInfo = "",                        //"i3333333",
            city = "Atlanta",                    //"v3333333",
            state = "GA",                        //"d3333333",
            zipCode = "30319",                   //"z3333333",
            country = "US",                      //"c3333333",
            latitude = 33.8725435,
            longitude = -84.3370041,
//            latLng = LatLng(33.8725435,-84.3370041)
        )
    )
    //

}

