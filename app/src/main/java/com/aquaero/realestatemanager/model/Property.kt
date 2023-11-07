package com.aquaero.realestatemanager.model

import java.time.LocalDate

data class Property(
    val pId: Long,
    val pType: String,
    val pAddress: Address,
    val pPrice: Int,
    val description: String?,
    val surface: Int,
    val nbOfRooms: Int,
    val nbOfBathrooms: Int,
    val nbOfBedrooms: Int,
    val photos: List<Photo>?,
    val registrationDate: LocalDate,
    val saleDate: LocalDate?,
    // val statusSold: Boolean,
    val pPoi: List<String>,
    val agent: Agent
) {



}