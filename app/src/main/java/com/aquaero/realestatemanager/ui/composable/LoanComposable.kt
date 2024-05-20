package com.aquaero.realestatemanager.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
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
        loanViewModel.onClearAllButtonClick(navController)
    }

    // Inputs
    val amountValue by remember { mutableStateOf(loanViewModel.amount?.toString()) }
    val yearsValue by remember { mutableStateOf(loanViewModel.years?.toString()) }
    val monthsValue by remember { mutableStateOf(loanViewModel.months?.toString()) }
    val annualInterestRateValue by remember { mutableStateOf(loanViewModel.annualInterestRate?.toString()) }
    val annualInsuranceRateValue by remember { mutableStateOf(loanViewModel.annualInsuranceRate?.toString()) }

    // Results
    var monthlyPaymentValue by remember { mutableStateOf(loanViewModel.monthlyPayment?.let { String.format("%.2f", it) }) }
    var monthlyInsuranceValue by remember { mutableStateOf(loanViewModel.monthlyInsurance?.let { String.format("%.2f", it) }) }
    var totalMonthlyValue by remember { mutableStateOf(loanViewModel.totalMonthly?.let { String.format("%.2f", it) }) }
    var totalPaymentsValue by remember { mutableStateOf(loanViewModel.totalPayments?.let { String.format("%.2f", it) }) }
    var totalInterestValue by remember { mutableStateOf(loanViewModel.totalInterest?.let { String.format("%.2f", it) }) }
    var totalInsuranceValue by remember { mutableStateOf(loanViewModel.totalInsurance?.let { String.format("%.2f", it) }) }

    // Refresh results display
    val refreshResultsDisplay by loanViewModel.refreshResultsDisplay.collectAsState()
    LaunchedEffect(key1 = refreshResultsDisplay) {
        monthlyPaymentValue = loanViewModel.monthlyPayment?.let { String.format("%.2f", it) }
        monthlyInsuranceValue = loanViewModel.monthlyInsurance?.let { String.format("%.2f", it) }
        totalMonthlyValue = loanViewModel.totalMonthly?.let { String.format("%.2f", it) }
        totalPaymentsValue = loanViewModel.totalPayments?.let { String.format("%.2f", it) }
        totalInterestValue = loanViewModel.totalInterest?.let { String.format("%.2f", it) }
        totalInsuranceValue = loanViewModel.totalInsurance?.let { String.format("%.2f", it) }
    }


    LoanScreen(
        currency = currency,
        // Inputs
        amountValue = amountValue,
        yearsValue = yearsValue,
        monthsValue = monthsValue,
        annualInterestRateValue = annualInterestRateValue,
        annualInsuranceRateValue = annualInsuranceRateValue,
        // Results
        monthlyPaymentValue = monthlyPaymentValue,
        monthlyInsuranceValue = monthlyInsuranceValue,
        totalMonthlyValue = totalMonthlyValue,
        totalPaymentsValue = totalPaymentsValue,
        totalInterestValue = totalInterestValue,
        totalInsuranceValue = totalInsuranceValue,
        // Actions
        onFieldValueChange = onFieldValueChange,
        onClearButtonClick = onClearButtonClick,
        onClearAllButtonClick = onClearAllButtonClick,
        popBackStack = popBackStack,
    )

}