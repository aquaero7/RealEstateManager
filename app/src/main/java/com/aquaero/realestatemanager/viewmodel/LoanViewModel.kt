package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.EditField
import com.aquaero.realestatemanager.Loan
import com.aquaero.realestatemanager.LoanField
import com.aquaero.realestatemanager.MAX
import com.aquaero.realestatemanager.MIN
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.utils.convertEuroToDollar

class LoanViewModel(

): ViewModel() {

    // Init loan data
    var amount: Int? = null
    var years: Int? = null
    var months: Int? = null


    fun onClickMenu(context: Context) {
        // TODO

    }


    fun onFieldValueChange(field: String, unit: String?, fieldValue: String, currency: String) {
        val value: String? = fieldValue.ifEmpty { null }
        val digitalValue: Int? = value?.let {
            if (it.isDigitsOnly()) it.toInt() else null
        }
        when (field) {
            LoanField.AMOUNT.name -> {
                Log.w("LoanViewModel", "$field = $value $currency")
                amount = digitalValue?.let {
                    when (currency) {
                        "€" -> convertEuroToDollar(euros = it)
                        else -> it
                    }
                }
            }
            LoanField.TERM.name -> {
                Log.w("LoanViewModel", "$field: $unit = $value")
                when (unit) {
                    LoanField.YEARS.name -> years = digitalValue
                    LoanField.MONTHS.name -> months = digitalValue
                }
            }




        }
    }

    fun onClearButtonClick(field: String) {
        when (field) {
            LoanField.AMOUNT.name -> amount = null


        }
    }



}