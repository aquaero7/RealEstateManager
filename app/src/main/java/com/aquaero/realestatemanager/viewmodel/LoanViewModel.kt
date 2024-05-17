package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.EditField
import com.aquaero.realestatemanager.Loan
import com.aquaero.realestatemanager.LoanField
import com.aquaero.realestatemanager.MAX
import com.aquaero.realestatemanager.MIN
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.navigateSingleTopTo
import com.aquaero.realestatemanager.utils.convertEuroToDollar

class LoanViewModel(

): ViewModel() {

    // Init loan data
    var amount: Int? = null
    var years: Int? = null
    var months: Int? = null
    var annualInterestRate: Float? = null
    var annualInsuranceRate: Float? = null
    var monthlyPrincipal: Float? = null
    var monthlyInterest: Float? = null
    var monthlyInsurance: Float? = null
    var totalMonthly: Float? = null
    var totalPayments: Float? = null
    var totalInterest: Float? = null
    var totalInsurance: Float? = null


    fun onClickMenu(navController: NavHostController, context: Context) {
        // TODO

        monthlyPrincipal = 123456.78f
        monthlyInterest = 123456.78f
        monthlyInsurance = 123456.78f
        totalMonthly = 123456.78f
        totalPayments = 123456.78f
        totalInterest = 123456.78f
        totalInsurance = 123456.78f

        navController.navigateSingleTopTo(Loan, null)

    }


    fun onFieldValueChange(field: String, unit: String?, fieldValue: String, currency: String) {
        var value: String? = fieldValue.ifEmpty { null }
        // Adds a '0' to the value if it ends with '.' or ','
        value?.let { if (!it[it.length.minus(1)].isDigit()) value += "0" }
        Log.w("LoanViewmodel", "Value $value ends with : ${value?.length?.minus(1)?.let { value[it] }}")

        val digitalValue: Float? = value?.let {
//            if (it.isDigitsOnly()) it.toInt() else null
            if (it.isDigitsOnly()) it.toFloat() else null
            /*
            when (field) {
                LoanField.AMOUNT.name, LoanField.TERM.name -> if (it.isDigitsOnly()) it.toInt() else null
                LoanField.ANNUAL_INTEREST_RATE.name -> if (it.isDigitsOnly()) it.toFloat() else null
                else -> null
            }
            */
        }
        when (field) {
            LoanField.AMOUNT.name -> {
                Log.w("LoanViewModel", "$field = $value $currency")
                amount = digitalValue?.let {
                    when (currency) {
                        "€" -> convertEuroToDollar(euros = it.toInt())
//                        "€" -> convertEuroToDollar(euros = it as Int)
                        else -> it.toInt()
//                        else -> it as Int
                    }
                }
            }
            LoanField.TERM.name -> {
                Log.w("LoanViewModel", "$field: $unit = $value")
                when (unit) {
                    LoanField.YEARS.name -> years = digitalValue?.toInt()
//                    LoanField.YEARS.name -> years = digitalValue as Int
                    LoanField.MONTHS.name -> months = digitalValue?.toInt()
//                    LoanField.MONTHS.name -> months = digitalValue as Int
                }
            }
            LoanField.ANNUAL_INTEREST_RATE.name -> annualInterestRate = digitalValue
//            LoanField.ANNUAL_INTEREST_RATE.name -> annualInterestRate = digitalValue as Float
            LoanField.ANNUAL_INSURANCE_RATE.name -> annualInsuranceRate = digitalValue
        }
    }

    fun onClearButtonClick(field: String, unit: String?) {
        when (field) {
            LoanField.AMOUNT.name -> amount = null
            LoanField.TERM.name -> {
                when (unit) {
                    LoanField.YEARS.name -> years = null
                    LoanField.MONTHS.name -> months = null
                }
            }
            LoanField.ANNUAL_INTEREST_RATE.name -> annualInterestRate = null
            LoanField.ANNUAL_INSURANCE_RATE.name -> annualInsuranceRate = null

        }
    }

    fun clearAllFields() {
        amount = null
        years = null
        months = null
        annualInterestRate = null
        annualInsuranceRate = null
        monthlyPrincipal = null
        monthlyInterest = null
        monthlyInsurance = null
        totalMonthly = null
        totalPayments = null
        totalInterest = null
        totalInsurance = null
    }

}