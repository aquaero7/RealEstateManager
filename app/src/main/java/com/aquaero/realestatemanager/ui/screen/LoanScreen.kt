package com.aquaero.realestatemanager.ui.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarViewMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaero.realestatemanager.LoanField
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.ui.component.loan_screen.LoanScreenResultText
import com.aquaero.realestatemanager.ui.component.loan_screen.LoanScreenTextField
import com.aquaero.realestatemanager.ui.component.loan_screen.LoanScreenTwoTextFields
import com.aquaero.realestatemanager.ui.theme.White

@Composable
fun LoanScreen(
    currency: String,
    amountValue: String?,
    yearsValue: String?,
    monthsValue: String?,
    annualInterestRateValue: String?,
    annualInsuranceRateValue: String?,
    monthlyPaymentValue: String?,
    monthlyInsuranceValue: String?,
    totalMonthlyValue: String?,
    totalPaymentsValue: String?,
    totalInterestValue: String?,
    totalInsuranceValue: String?,
    onFieldValueChange: (String, String?, String) -> Unit,
    onClearButtonClick: (String, String?) -> Unit,
    onClearAllButtonClick: () -> Unit,
    popBackStack: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .clickable { focusManager.clearFocus() } // To clear text field focus when clicking outside it.
            .fillMaxSize()
            .verticalScroll(
                state = scrollState,
                enabled = true
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
//        verticalArrangement = Arrangement.SpaceBetween,
//        verticalArrangement = Arrangement.Top,
    ) {
        // Title
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 8.dp)
                .padding(top = 12.dp)
                .border(2.dp, color = MaterialTheme.colorScheme.tertiary),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary,
            text = stringResource(id = R.string.loan_title)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Amount
        LoanScreenTextField(
            labelText = "${stringResource(id = R.string.loan_amount)} ($currency)",
            icon = Icons.Default.Money,
            iconCD = stringResource(id = R.string.cd_loan_amount),
            text = amountValue,
            shouldBeDecimal = false,
            onValueChange = {
                onFieldValueChange(LoanField.AMOUNT.name, null, it)
                Log.w("LoanScreen", "Amount value = $it")
            },
            onClearButtonClick = { onClearButtonClick(LoanField.AMOUNT.name, null) },
        )

        // Term
        LoanScreenTwoTextFields(
            labelText = stringResource(id = R.string.loan_term),
            icon = Icons.Default.Timer,
            iconCD = stringResource(id = R.string.cd_loan_term),
            yearsText = yearsValue,
            monthsText = monthsValue,
            onValueChange = { unit, value ->
                onFieldValueChange(LoanField.TERM.name, unit, value)
                Log.w("LoanScreen", "Term: $unit value = $value")
            },
            onClearButtonClick = { unit ->
                onClearButtonClick(LoanField.TERM.name, unit)
            },
        )

        // Annual Interest Rate
        LoanScreenTextField(
            labelText = "${stringResource(id = R.string.annual_interest_rate)} (%)",
            icon = Icons.Default.Percent,
            iconCD = stringResource(id = R.string.cd_annual_interest_rate),
            text = annualInterestRateValue,
            shouldBeDecimal = true,
            onValueChange = {
                onFieldValueChange(LoanField.ANNUAL_INTEREST_RATE.name, null, it)
                Log.w("LoanScreen", "Annual Interest Rate = $it")
            },
            onClearButtonClick = { onClearButtonClick(LoanField.ANNUAL_INTEREST_RATE.name, null) },
        )

        // Annual Insurance Rate
        LoanScreenTextField(
            labelText = "${stringResource(id = R.string.annual_insurance_rate)} (%)",
            icon = Icons.Default.Percent,
            iconCD = stringResource(id = R.string.cd_annual_insurance_rate),
            text = annualInsuranceRateValue,
            shouldBeDecimal = true,
            onValueChange = {
                onFieldValueChange(LoanField.ANNUAL_INSURANCE_RATE.name, null, it)
                Log.w("LoanScreen", "Annual Insurance Rate = $it")
            },
            onClearButtonClick = { onClearButtonClick(LoanField.ANNUAL_INSURANCE_RATE.name, null) },
        )

        // Clear all button
        Button(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
            colors = ButtonDefaults.buttonColors().copy(containerColor = MaterialTheme.colorScheme.secondary),
            onClick = onClearAllButtonClick
        ) {
            Icon(
                imageVector = Icons.Default.Cancel,
                contentDescription = stringResource(id = R.string.cd_button_clear_all),
                tint = White,
            )
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                color = White,
                fontWeight = FontWeight.Bold,
                text = stringResource(id = R.string.clear)
            )
            Icon(
                imageVector = Icons.Default.Cancel,
                contentDescription = stringResource(id = R.string.cd_button_clear_all),
                tint = White,
            )
        }

        // Monthly results
        Column {
            // Monthly Payment
            LoanScreenResultText(
                labelText = "${stringResource(id = R.string.monthly_payment)} ($currency) : ",
                icon = Icons.Default.CalendarMonth,
                iconCD = stringResource(id = R.string.cd_loan_calculation_result),
                resultText = monthlyPaymentValue ?: ""
            )
            // Monthly Insurance Payment
            LoanScreenResultText(
                labelText = "${stringResource(id = R.string.monthly_insurance)} ($currency) : ",
                icon = Icons.Default.CalendarMonth,
                iconCD = stringResource(id = R.string.cd_loan_calculation_result),
                resultText = monthlyInsuranceValue ?: ""
            )
            // Total Monthly Payment
            LoanScreenResultText(
                labelText = "${stringResource(id = R.string.total_monthly)} ($currency) : ",
                icon = Icons.Default.CalendarMonth,
                iconCD = stringResource(id = R.string.cd_loan_calculation_result),
                resultText = totalMonthlyValue ?: ""
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Total results
        Column {
            // Total Payments
            LoanScreenResultText(
                labelText = "${stringResource(id = R.string.total_payments)} ($currency) : ",
                icon = ImageVector.vectorResource(R.drawable.sigma),
                iconCD = stringResource(id = R.string.cd_loan_total_result),
                resultText = totalPaymentsValue ?: ""
            )
            // Total Interest
            LoanScreenResultText(
                labelText = "${stringResource(id = R.string.total_interest)} ($currency) : ",
                icon = ImageVector.vectorResource(R.drawable.sigma),
                iconCD = stringResource(id = R.string.cd_loan_total_result),
                resultText = totalInterestValue ?: ""
            )
            // Total Insurance
            LoanScreenResultText(
                labelText = "${stringResource(id = R.string.total_insurance)} ($currency) : ",
                icon = ImageVector.vectorResource(R.drawable.sigma),
                iconCD = stringResource(id = R.string.cd_loan_total_result),
                resultText = totalInsuranceValue ?: ""
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }


    // To manage back nav
    BackHandler(true) {
        Log.w("OnBackPressed", "LoanScreen OnBackPressed")
        popBackStack()
    }

}