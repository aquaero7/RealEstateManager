package com.aquaero.realestatemanager.ui.composable

import androidx.compose.runtime.Composable
import com.aquaero.realestatemanager.ui.screen.LoanScreen
import com.aquaero.realestatemanager.viewmodel.LoanViewModel

@Composable
fun LoanComposable(
    loanViewModel: LoanViewModel,
    currency: String,
    popBackStack: () -> Unit,
) {
    val onFieldValueChange: (String, String?, String) -> Unit = { field, unit, value ->
        loanViewModel.onFieldValueChange(field = field, unit = unit, fieldValue = value, currency = currency)
    }
    val onClearButtonClick: (String) -> Unit = { field ->
        loanViewModel.onClearButtonClick(field)
    }

    LoanScreen(
        currency = currency,
        amountValue = loanViewModel.amount?.toString(),
        yearsValue = loanViewModel.years?.toString(),
        monthsValue = loanViewModel.months?.toString(),
        onFieldValueChange = onFieldValueChange,
        onClearButtonClick = onClearButtonClick,

        popBackStack = popBackStack,
    )

}