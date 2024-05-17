package com.aquaero.realestatemanager.ui.composable

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.Loan
import com.aquaero.realestatemanager.navigateSingleTopTo
import com.aquaero.realestatemanager.ui.screen.LoanScreen
import com.aquaero.realestatemanager.viewmodel.LoanViewModel

@Composable
fun LoanComposable(
    navController: NavHostController,
    loanViewModel: LoanViewModel,
    currency: String,
    popBackStack: () -> Unit,
) {
    val onFieldValueChange: (String, String?, String) -> Unit = { field, unit, value ->
        loanViewModel.onFieldValueChange(field = field, unit = unit, fieldValue = value, currency = currency)
    }
    val onClearButtonClick: (String, String?) -> Unit = { field, unit ->
        loanViewModel.onClearButtonClick(field, unit)
    }
    val onClearAllButtonClick: () -> Unit = {
        loanViewModel.clearAllFields()
        navController.navigateSingleTopTo(destination = Loan, null)
        Log.w("LoanComposable", "Click on clear all button")
    }

    var monthlyPrincipalValue by remember { mutableStateOf(loanViewModel.monthlyPrincipal) }
    var monthlyInterestValue by remember { mutableStateOf(loanViewModel.monthlyInterest) }
    var monthlyInsuranceValue by remember { mutableStateOf(loanViewModel.monthlyInsurance) }
    var totalMonthlyValue by remember { mutableStateOf(loanViewModel.totalMonthly) }
    var totalPaymentsValue by remember { mutableStateOf(loanViewModel.totalPayments) }
    var totalInterestValue by remember { mutableStateOf(loanViewModel.totalInterest) }
    var totalInsuranceValue by remember { mutableStateOf(loanViewModel.totalInsurance) }

    LaunchedEffect(key1 = Unit) {
        monthlyPrincipalValue = loanViewModel.monthlyPrincipal
        monthlyInterestValue = loanViewModel.monthlyInterest
        monthlyInsuranceValue = loanViewModel.monthlyInsurance
        totalMonthlyValue = loanViewModel.totalMonthly
        totalPaymentsValue = loanViewModel.totalPayments
        totalInterestValue = loanViewModel.totalInterest
        totalInsuranceValue = loanViewModel.totalInsurance
    }

    LoanScreen(
        currency = currency,
        amountValue = loanViewModel.amount?.toString(),
        yearsValue = loanViewModel.years?.toString(),
        monthsValue = loanViewModel.months?.toString(),
        annualInterestRateValue = loanViewModel.annualInterestRate?.toString(),
        annualInsuranceRateValue = loanViewModel.annualInsuranceRate?.toString(),
        monthlyPrincipalValue = monthlyPrincipalValue?.toString(),
        monthlyInterestValue = monthlyInterestValue?.toString(),
        monthlyInsuranceValue = monthlyInsuranceValue?.toString(),
        totalMonthlyValue = totalMonthlyValue?.toString(),
        totalPaymentsValue = totalPaymentsValue?.toString(),
        totalInterestValue = totalInterestValue?.toString(),
        totalInsuranceValue = totalInsuranceValue?.toString(),
        onFieldValueChange = onFieldValueChange,
        onClearButtonClick = onClearButtonClick,
        onClearAllButtonClick = onClearAllButtonClick,
        popBackStack = popBackStack,
    )

}