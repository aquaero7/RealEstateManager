package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.Loan
import com.aquaero.realestatemanager.R

class LoanViewModel(

): ViewModel() {

    private val context: Context by lazy { ApplicationRoot.getContext() }


    fun onClickMenu() {
        Log.w("Click on menu valid", "Screen ${Loan.label}")
        // TODO: Replace toast with specific action before deleting
        Toast
            .makeText(
                context, "Click on ${context.getString(R.string.valid)} on screen ${Loan.label}",
                Toast.LENGTH_SHORT
            )
            .show()
    }

}