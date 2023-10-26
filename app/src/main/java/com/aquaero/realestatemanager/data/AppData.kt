package com.aquaero.realestatemanager.data

import com.aquaero.realestatemanager.agentsToStrings

val pTypes: MutableSet<String?> = mutableSetOf(
    "", "FLAT", "HOUSE", "DUPLEX", "PENTHOUSE", "LOFT", "MANOR", "CASTLE", "HOSTEL"
)

val poi: MutableSet<String?> = mutableSetOf(
    "HOSPITAL", "SCHOOL", "RESTAURANT", "SHOP", "RAILWAY STATION", "CAR PARK",
)

val agents: MutableSet<String?> = agentsToStrings()


