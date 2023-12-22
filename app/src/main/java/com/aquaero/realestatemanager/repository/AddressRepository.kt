package com.aquaero.realestatemanager.repository

import android.content.Context
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.model.Address
import com.google.android.gms.maps.model.LatLng

class AddressRepository() {

    private val context: Context by lazy { ApplicationRoot.getContext() }

    // ...





    //
    /**
     * FAKE ADDRESSES
     */

    val fakeAddresses = listOf(
        Address(
            addressId = 0,
            streetNumber = "3",                    //"n0000000",
            streetName = "avenue de Brehat",     //"s0000000",
            addInfo = "",                         //"i0000000",
            city = "Villebon-sur-Yvette",        //"v0000000",
            state = "",                         //"d0000000",
            zipCode = "91140",                  //"z0000000",
            country = "FR",                   //"c0000000",
            latLng = LatLng(48.6860854, 2.2201107)
        ),
        Address(
            addressId = 1,
            streetNumber = "35",    //"n1111111",
            streetName = "route de Paris", //"s1111111",
            addInfo = "ZAC Les 4 Chênes", //"i1111111",
            city = "Pontault-Combault",  //"v1111111",
            state = "",  //"d1111111",
            zipCode = "77340",    //"z1111111",
            country = "FR",   //"c1111111",
            latLng = LatLng(48.7765790,2.5906768)
        ),
        Address(
            addressId = 2,
            streetNumber = "500",    //"n2222222",
            streetName = "Brookhaven Ave", //"s2222222",
            addInfo = "", //"i2222222",
            city = "Atlanta",  //"v2222222",
            state = "GA",  //"d2222222",
            zipCode = "30319",    //"z2222222",
            country = "US",   //"c2222222",
            latLng = LatLng(33.8725435,-84.3370041)
        )
    )
    //

}

