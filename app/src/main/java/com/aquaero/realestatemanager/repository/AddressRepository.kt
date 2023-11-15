package com.aquaero.realestatemanager.repository

import com.aquaero.realestatemanager.model.Address

class AddressRepository() {

    // ...





    //
    /**
     * FAKE ADDRESSES
     */

    val fakeAddresses = listOf(
        Address(
            0,
            "3",                    //"n0000000",
            "avenue de Brehat",     //"s0000000",
            "",                         //"i0000000",
            "Villebon-sur-Yvette",        //"v0000000",
            "",                         //"d0000000",
            "91140",                  //"z0000000",
            "FR",                   //"c0000000",
            0.0,
            0.0
        ),
        Address(
            1,
            "35",    //"n1111111",
            "route de Paris", //"s1111111",
            "ZAC Les 4 Chênes", //"i1111111",
            "Pontault-Combault",  //"v1111111",
            "",  //"d1111111",
            "77340",    //"z1111111",
            "FR",   //"c1111111",
            1.1,
            1.1
        ),
        Address(
            2,
            "500",    //"n2222222",
            "Brookhaven Ave", //"s2222222",
            "", //"i2222222",
            "Atlanta",  //"v2222222",
            "GA",  //"d2222222",
            "30319",    //"z2222222",
            "US",   //"c2222222",
            2.2,
            2.2
        )
    )
    //

}

