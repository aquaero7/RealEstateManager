package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.repository.PropertyRepository

class ListViewModel(
    private val propertyRepository: PropertyRepository
): ViewModel() {

    private val context: Context by lazy { ApplicationRoot.getContext() }




}