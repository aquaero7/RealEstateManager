package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.Detail
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.navigateToDetailEdit
import com.aquaero.realestatemanager.repository.PropertyRepository
import com.aquaero.realestatemanager.utils.ConnectionState

class DetailViewModel(
    private val propertyRepository: PropertyRepository
): ViewModel() {

    private val context: Context by lazy { ApplicationRoot.getContext() }

    fun onClickMenu(
        navController: NavHostController,
        propertyId: Comparable<*>
    ) {
        Log.w("Click on menu edit", "Screen ${Detail.label} / Property $propertyId")
        navController.navigateToDetailEdit(propertyId.toString())
    }

    fun propertyFromId(propertyId: Long): Property {
        return propertyRepository.propertyFromId(propertyId)
    }

    fun checkForConnection(connection: ConnectionState): Boolean {
        return connection === ConnectionState.Available
    }

    fun thumbnailUrl(property: Property): String {
        return propertyRepository.thumbnailUrl(property)
    }

}

