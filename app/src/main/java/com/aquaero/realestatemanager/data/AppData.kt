package com.aquaero.realestatemanager.data

import com.aquaero.realestatemanager.utils.agentsToStrings

val pTypes: MutableSet<String?> = mutableSetOf(
    "", "Flat", "House", "Duplex", "Penthouse", "Loft", "Manor", "Castle", "Hostel"
)

val poi: MutableSet<String?> = mutableSetOf(
    "Hospital", "School", "Restaurant", "Shop", "Railway station", "Car park",
)

val agents: MutableSet<String?> = agentsToStrings()


