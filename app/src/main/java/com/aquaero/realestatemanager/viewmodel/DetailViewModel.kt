package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Property

class DetailViewModel(

): ViewModel() {

    private val context: Context by lazy { ApplicationRoot.getContext() }


}