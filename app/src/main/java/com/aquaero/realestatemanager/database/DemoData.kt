package com.aquaero.realestatemanager.database

import com.aquaero.realestatemanager.CACHE_LONG_ID_VALUE
import com.aquaero.realestatemanager.NULL_PROPERTY_ID
import com.aquaero.realestatemanager.UNASSIGNED_ID
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.NO_PHOTO
import com.aquaero.realestatemanager.model.PoiEnum
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.model.TypeEnum

val ADDRESS_PREPOPULATION_DATA = listOf(
    Address(
        addressId = 1001,
        streetNumber = "3",                    //"n1111111",
        streetName = "avenue de Brehat",       //"s1111111",
        addInfo = "",                          //"i1111111",
        city = "Villebon-sur-Yvette",          //"v1111111",
        state = "",                            //"d1111111",
        zipCode = "91140",                     //"z1111111",
        country = "FR",                        //"c1111111",
        latitude = 48.6860854,
        longitude = 2.2201107,
    ),
    Address(
        addressId = 1002,
        streetNumber = "35",                   //"n2222222",
        streetName = "route de Paris",         //"s2222222",
        addInfo = "ZAC Les 4 Chênes",          //"i2222222",
        city = "Pontault-Combault",            //"v2222222",
        state = "",                            //"d2222222",
        zipCode = "77340",                     //"z2222222",
        country = "FR",                        //"c2222222",
        latitude = 48.7765790,
        longitude = 2.5906768,
    ),
    Address(
        addressId = 1003,
        streetNumber = "500",                  //"n3333333",
        streetName = "Brookhaven Ave",         //"s3333333",
        addInfo = "",                          //"i3333333",
        city = "Atlanta",                      //"v3333333",
        state = "GA",                          //"d3333333",
        zipCode = "30319",                     //"z3333333",
        country = "US",                        //"c3333333",
        latitude = 33.8725435,
        longitude = -84.3370041,
    ),
)

val PROPERTY_PREPOPULATION_DATA = listOf(
    Property(
        propertyId = 0L,   // NULL_PROPERTY_ID,
        typeId = TypeEnum.UNASSIGNED.key,
        addressId = null,
        price = null,
        description = null,
        surface = null,
        nbOfRooms = null,
        nbOfBathrooms = null,
        nbOfBedrooms = null,
        registrationDate = null,
        saleDate = null,
        agentId = UNASSIGNED_ID
    ),
)

val PHOTO_PREPOPULATION_DATA = listOf(NO_PHOTO)

val PROPERTY_POI_JOIN_PREPOPULATION_DATA = listOf(
    PropertyPoiJoin(1, PoiEnum.RESTAURANT.key),
    PropertyPoiJoin(1, PoiEnum.RAILWAY_STATION.key),
)