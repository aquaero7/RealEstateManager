package com.aquaero.realestatemanager.repository

import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.Loan
import com.aquaero.realestatemanager.navigateSingleTopTo
import com.aquaero.realestatemanager.utils.calculateMonthlyPaymentWithInterest
import com.aquaero.realestatemanager.utils.isDecimal

class LoanRepository {

    fun calculateTerm(years: Int?, months: Int?): Int {
        return ((years ?: 0) * 12 + (months ?: 0)).coerceAtLeast(1)
    }

    fun calculateMonthlyPayment(amount: Int?, annualInterestRate: Float?, term: Int): Float? {
        return amount?.let {
            if (annualInterestRate != null && annualInterestRate != 0f && term != 1) {
                calculateMonthlyPaymentWithInterest(it, annualInterestRate, term)
            } else {
                it / term.toFloat()
            }
        }
    }

    fun calculateMonthlyInsurance(amount: Int?, annualInsuranceRate: Float?, term: Int): Float? {
        return amount?.let {
            if (term != 1) it * (annualInsuranceRate ?: 0f) / 100 / 12 else 0f
        }
    }

    fun calculateTotalMonthly(monthlyPayment: Float?, monthlyInsurance: Float?): Float? {
        return monthlyInsurance?.let { monthlyPayment?.plus(it) }
    }

    fun calculateTotalLoanPayments(monthlyPayment: Float?, term: Int): Float? {
        return monthlyPayment?.let { it * term }
    }

    fun calculateTotalInterest(totalLoanPayments: Float?, amount: Int?): Float? {
        return totalLoanPayments?.let { it - amount!! } // amount isn't null when totalLoanPayments isn't
    }

    fun calculateTotalInsurance(monthlyInsurance: Float?, term: Int): Float? {
        return monthlyInsurance?.let { it * term }
    }

    fun calculateTotalPayments(totalLoanPayments: Float?, totalInsurance: Float?): Float? {
        return totalInsurance?.let { totalLoanPayments?.plus(it) }
    }

    fun reloadScreen(navController: NavHostController) {
        navController.navigateSingleTopTo(Loan, null)
    }

    /**
     * Returns null if the value is an empty field.
     * Add a '0' to the value if it ends with '.' or ',', before returning it
     */
    fun reformatDigitalField(value: String): String? {
//        return value.ifEmpty { null }
        return if (value.isEmpty()) null else if (value.last().isDigit()) value else value + "0"
    }

    fun getDigitalValue(value: String?): Float? {
        return value?.let { if (isDecimal(it)) it.toFloat() else null }
    }

}