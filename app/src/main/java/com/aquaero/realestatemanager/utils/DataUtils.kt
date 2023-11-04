package com.aquaero.realestatemanager.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.aquaero.realestatemanager.data.fakeAgents
import com.aquaero.realestatemanager.data.fakeProperties
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Property

//TODO HERE : Report Java methods from initial project converted to functions...

fun agentsToStrings(): MutableSet<String?> {
    // val agentsSet: MutableSet<String?> = mutableSetOf()
    val agentsSet: MutableSet<String?> = mutableSetOf("Not assigned...") // Empty field for unassigned agent case
    for (agent in fakeAgents) {
        agentsSet.add(agent.toString())
    }
    return agentsSet
}

@RequiresApi(Build.VERSION_CODES.O)
fun getProperty(propertyId: Long): Property {
    lateinit var property: Property
    fakeProperties.forEach { if (it.pId == propertyId) property = it }
    return property
}

@RequiresApi(Build.VERSION_CODES.O)
fun getPropertyPictures(propertyId: Long): MutableList<Photo?> {
    val photos: MutableList<Photo?> = mutableListOf()
    getProperty(propertyId).photos?.forEach { photo ->
        photos.add(photo)
    }
    return photos
}