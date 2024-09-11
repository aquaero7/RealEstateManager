package com.aquaero.realestatemanager.repository

import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.Loan
import com.aquaero.realestatemanager.navigateSingleTopTo
import com.aquaero.realestatemanager.utils.areDigitsOnly
import com.aquaero.realestatemanager.utils.calculateMonthlyPaymentWithInterest
import com.aquaero.realestatemanager.utils.isDecimal

class LoanRepository {

    fun calculateTerm(years: Int?, months: Int?): Int {
        return ((years ?: 0) * 12 + (months ?: 0)).coerceAtLeast(1)
    }

    fun calculateMonthlyPayment(amount: Int?, annualInterestRate: Float?, term: Int): Float {
        return calculateMonthlyPaymentWithInterest(
            amount = amount ?: 0, annualInterestRate = annualInterestRate ?: 0F, termInMonths = term
        )
    }

    fun calculateMonthlyInsurance(amount: Int?, annualInsuranceRate: Float?, term: Int): Float {
        return if (term > 1) (amount ?: 0) * (annualInsuranceRate ?: 0F) / 100 / 12 else 0F
    }

    fun calculateTotalMonthly(monthlyPayment: Float?, monthlyInsurance: Float?): Float {
        return  (monthlyPayment ?: 0F) + (monthlyInsurance ?: 0F)
    }

    fun calculateTotalLoanPayments(monthlyPayment: Float?, term: Int): Float {
        return (monthlyPayment ?: 0F) * term
    }

    fun calculateTotalInterest(totalLoanPayments: Float?, amount: Int?): Float {
        return (totalLoanPayments ?: 0F) - (amount ?: 0)
    }

    fun calculateTotalInsurance(monthlyInsurance: Float?, term: Int): Float {
        return (monthlyInsurance ?: 0F) * term
    }

    fun calculateTotalPayments(totalLoanPayments: Float?, totalInsurance: Float?): Float {
        return (totalLoanPayments ?: 0F) + (totalInsurance ?: 0F)
    }

    fun reloadScreen(navController: NavHostController) {
        navController.navigateSingleTopTo(Loan, null)
    }

    /**
     * Returns null if the value is not decimal or is an empty field.
     * Add a '0' to the value if it starts or ends with '.', before returning it
     */
    fun reformatDigitalField(value: String): String? {
        return when {
            (value.isEmpty()) -> null
            isDecimal(value) && value.first() != '.' && value.last() != '.' -> value
            value.dropLast(1).areDigitsOnly() && value.last() == '.' -> "${value}0"
            value.drop(1).areDigitsOnly() && value.first() == '.' -> "0$value"
            else -> null
        }
    }

    fun getDigitalValue(value: String?): Float? {
        return value?.let { if (isDecimal(it)) it.toFloat() else null }
    }

}