package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.SearchCriteria

class SearchViewModel(

): ViewModel() {

    private val context: Context by lazy { ApplicationRoot.getContext() }


    fun onClickMenu() {
        Log.w("Click on menu valid", "Screen ${SearchCriteria.label}")
        // TODO: Replace toast with specific action
        Toast
            .makeText(
                context, "Click on ${context.getString(R.string.valid)} on screen ${SearchCriteria.label}",
                Toast.LENGTH_SHORT
            )
            .show()
    }

}