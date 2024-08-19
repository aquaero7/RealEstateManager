package com.aquaero.realestatemanager.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.LoanField
import com.aquaero.realestatemanager.repository.LoanRepository
import com.aquaero.realestatemanager.utils.convertDollarToEuro
import com.aquaero.realestatemanager.utils.convertEuroToDollar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoanViewModel(
    private val loanRepository: LoanRepository
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

    var inputCurrency: String = "$"

    private val _refreshResultsDisplay = MutableStateFlow(false)
    val refreshResultsDisplay: StateFlow<Boolean> = _refreshResultsDisplay.asStateFlow()


    fun onClickMenu(navController: NavHostController, currency: String) {
        val term = loanRepository.calculateTerm(years = years, months = months)

        if (currency == "$" && inputCurrency == "€") amount = convertEuroToDollar(euros = amount)
        if (currency == "€" && inputCurrency == "$") amount = convertDollarToEuro(dollars = amount)
        inputCurrency = currency

        monthlyPayment = loanRepository.calculateMonthlyPayment(
            amount = amount,
            annualInterestRate = annualInterestRate,
            term = term
        )

        monthlyInsurance = loanRepository.calculateMonthlyInsurance(
            amount = amount,
            annualInsuranceRate = annualInsuranceRate,
            term = term
        )

        totalMonthly = loanRepository.calculateTotalMonthly(
            monthlyPayment = monthlyPayment,
            monthlyInsurance = monthlyInsurance
        )

        val totalLoanPayments = loanRepository.calculateTotalLoanPayments(
            monthlyPayment = monthlyPayment,
            term = term
        )

        totalInterest = loanRepository.calculateTotalInterest(
            totalLoanPayments = totalLoanPayments,
            amount = amount
        )

        totalInsurance = loanRepository.calculateTotalInsurance(
            monthlyInsurance = monthlyInsurance,
            term = term
        )

        totalPayments = loanRepository.calculateTotalPayments(
            totalLoanPayments = totalLoanPayments,
            totalInsurance = totalInsurance
        )

        // Reload screen to refresh results display
        loanRepository.reloadScreen(navController = navController)
    }


    fun onFieldValueChange(field: String, unit: String?, fieldValue: String, currency: String) {
        val value: String? = loanRepository.reformatDigitalField(fieldValue)
        Log.w(
            "LoanViewmodel",
            if (fieldValue.isNotEmpty()) "FieldValue $fieldValue ends with : ${fieldValue.last()}"
            else "FieldValue is empty"
        )
        Log.w(
            "LoanViewmodel",
            value?.let { "Value $it ends with : ${it.last()}" } ?: "Value is null"
        )

//        val digitalValue: Float? = value?.let { if (isDecimal(it)) it.toFloat() else null }
        val digitalValue: Float? = loanRepository.getDigitalValue(value)

        when (field) {
            LoanField.AMOUNT.name -> {
                Log.w("LoanViewModel", value?.let { "$field = $value $currency" } ?: "$field = null")
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
        loanRepository.reloadScreen(navController = navController)
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
