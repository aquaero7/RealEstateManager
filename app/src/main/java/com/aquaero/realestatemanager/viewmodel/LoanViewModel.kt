package com.aquaero.realestatemanager.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.Loan
import com.aquaero.realestatemanager.LoanField
import com.aquaero.realestatemanager.navigateSingleTopTo
import com.aquaero.realestatemanager.utils.calculateMonthlyPaymentWithInterest
import com.aquaero.realestatemanager.utils.convertDollarToEuro
import com.aquaero.realestatemanager.utils.convertEuroToDollar
import com.aquaero.realestatemanager.utils.isDecimal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoanViewModel(

): ViewModel() {

    // Init loan data
    var amount: Int? = null
    var years: Int? = null
    var months: Int? = null
    var annualInterestRate: Float? = null
    var annualInsuranceRate: Float? = null
    var monthlyPayment: Float? = null
    var monthlyInsurance: Float? = null
    var totalMonthly: Float? = null
    var totalPayments: Float? = null
    var totalInterest: Float? = null
    var totalInsurance: Float? = null

    private var inputCurrency: String = "$"

    private val _refreshResultsDisplay = MutableStateFlow(false)
    val refreshResultsDisplay: StateFlow<Boolean> = _refreshResultsDisplay.asStateFlow()


    fun onClickMenu(navController: NavHostController, currency: String) {
        val term: Int = ((years ?: 0) * 12 + (months ?: 0)).coerceAtLeast(1)

        if (currency == "$" && inputCurrency == "€") amount = convertEuroToDollar(euros = amount)
        if (currency == "€" && inputCurrency == "$") amount = convertDollarToEuro(dollars = amount)
        inputCurrency = currency

        monthlyPayment = amount?.let {
            if (annualInterestRate != null && annualInterestRate != 0f && term != 1) {
//                (it * annualInterestRate!! / 100 / 12) / (1 - (1 + annualInterestRate!! / 100 / 12).pow(-term))   // TODO: To be deleted
                calculateMonthlyPaymentWithInterest(it, annualInterestRate!!, term)
            } else {
                it / term.toFloat()
            }
        }
        monthlyInsurance = amount?.let {
            if (term != 1) it * (annualInsuranceRate ?: 0f) / 100 / 12 else 0f
        }
        totalMonthly = monthlyInsurance?.let { monthlyPayment?.plus(it) }
        val totalLoanPayments: Float? = monthlyPayment?.let { it * term }
        totalInterest = totalLoanPayments?.let { it - amount!! }
        totalInsurance = monthlyInsurance?.let { it * term }
        totalPayments = totalInsurance?.let { ti -> totalLoanPayments?.plus(ti) }

        // Reload screen to refresh results display
        navController.navigateSingleTopTo(Loan, null)
    }


    fun onFieldValueChange(field: String, unit: String?, fieldValue: String, currency: String) {
        var value: String? = fieldValue.ifEmpty { null }

        // Adds a '0' to the value if it ends with '.' or ','
        value?.let { if (!it[it.length.minus(1)].isDigit()) value += "0" }
        Log.w("LoanViewmodel", "Value $value ends with : ${value?.length?.minus(1)?.let { value[it] }}")

        val digitalValue: Float? = value?.let { if (isDecimal(it)) it.toFloat() else null }
        when (field) {
            LoanField.AMOUNT.name -> {
                Log.w("LoanViewModel", "$field = $value $currency")
                amount = digitalValue?.toInt()
                inputCurrency = currency
            }
            LoanField.TERM.name -> {
                Log.w("LoanViewModel", "$field: $unit = $value")
                when (unit) {
                    LoanField.YEARS.name -> years = digitalValue?.toInt()
                    LoanField.MONTHS.name -> months = digitalValue?.toInt()
                }
            }
            LoanField.ANNUAL_INTEREST_RATE.name -> annualInterestRate = digitalValue
            LoanField.ANNUAL_INSURANCE_RATE.name -> annualInsuranceRate = digitalValue
        }

        // Results are now invalid and new input has to be validated
        if (monthlyPayment != null) {
            clearResults()
            // Refresh results display
            _refreshResultsDisplay.value = !refreshResultsDisplay.value
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

        // Results are now invalid and must be cleared until the new input is validated
        if (monthlyPayment != null) {
            clearResults()
            // Refresh results display
            _refreshResultsDisplay.value = !refreshResultsDisplay.value
        }
    }

    fun onClearAllButtonClick(navController: NavHostController) {
        clearAllFields()
        // Reload screen to refresh results display
        navController.navigateSingleTopTo(destination = Loan, null)
    }

    private fun clearAllFields() {
        clearInput()
        clearResults()
    }

    private fun clearInput() {
        amount = null
        years = null
        months = null
        annualInterestRate = null
        annualInsuranceRate = null
    }

    private fun clearResults() {
        monthlyPayment = null
        monthlyInsurance = null
        totalMonthly = null
        totalPayments = null
        totalInterest = null
        totalInsurance = null
    }

}