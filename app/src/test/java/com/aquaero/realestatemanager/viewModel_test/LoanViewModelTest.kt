package com.aquaero.realestatemanager.viewModel_test

import android.util.Log
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.LoanField
import com.aquaero.realestatemanager.repository.LoanRepository
import com.aquaero.realestatemanager.viewmodel.LoanViewModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.Mockito.any
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.KArgumentCaptor
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doCallRealMethod
import org.mockito.kotlin.reset
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
//@RunWith(RobolectricTestRunner::class)
class LoanViewModelTest {
    private lateinit var loanRepository: LoanRepository
    private lateinit var navController: NavHostController
    private lateinit var viewmodel: LoanViewModel

    private lateinit var intArgumentCaptor: KArgumentCaptor<Int>
    private lateinit var floatArgumentCaptor: KArgumentCaptor<Float>
    private lateinit var stringArgumentCaptor: KArgumentCaptor<String?>

    private lateinit var currency: String
    private lateinit var euro: String
    private lateinit var dollar: String

    private var amountInDollar: Int? = null
    private var amountInEuro: Int? = null
    private var years: Int? = null
    private var months: Int? = null
    private var term: Int = 1
    private var annualInterestRate: Float? = null
    private var annualInsuranceRate: Float? = null
    private var monthlyPaymentInDollar: Float? = null
    private var monthlyPaymentInEuro: Float? = null
    private var monthlyInsuranceInDollar: Float? = null
    private var monthlyInsuranceInEuro: Float? = null
    private var totalMonthlyInDollar: Float? = null
    private var totalMonthlyInEuro: Float? = null
    private var totalPaymentsInDollar: Float? = null
    private var totalPaymentsInEuro: Float? = null
    private var totalLoanPaymentsInDollar: Float? = 0F
    private var totalLoanPaymentsInEuro: Float? = 0F
    private var totalInterestInDollar: Float? = null
    private var totalInterestInEuro: Float? = null
    private var totalInsuranceInDollar: Float? = null
    private var totalInsuranceInEuro: Float? = null
    private  lateinit var defaultCurrency: String

    // Created to avoid class "Log" error when tests are run with coverage
    private lateinit var logMock: MockedStatic<Log>


    @Before
    fun setup() {
        // Initialize logMock
        logMock = Mockito.mockStatic(Log::class.java)

        loanRepository = mock(LoanRepository::class.java)
        navController = mock(NavHostController::class.java)

        defaultCurrency = "$"
        euro = "€"
        dollar = "$"
        currency = ""

        viewmodel = LoanViewModel(loanRepository)

        intArgumentCaptor = argumentCaptor()
        floatArgumentCaptor = argumentCaptor()
        stringArgumentCaptor = argumentCaptor()

        initTestData()
        initViewModelInputs(defaultCurrency = true)
    }

    @After
    fun teardown() {
        // Close logMock
        logMock.close()
    }
    private fun initTestData() {
        currency = defaultCurrency
        amountInDollar = 12000
        amountInEuro = 9744
        years = 1
        months = 6
        term = 18
        annualInterestRate = 10F
        annualInsuranceRate = 1F
        monthlyPaymentInDollar = 720.68567F
        monthlyPaymentInEuro = 585.1968F
        monthlyInsuranceInDollar = 10F
        monthlyInsuranceInEuro = 8.12F
        totalMonthlyInDollar = 730.69F
        totalMonthlyInEuro = 593.32F
        totalPaymentsInDollar = 13152.34F
        totalPaymentsInEuro = 10679.70F
        totalLoanPaymentsInDollar = 12972.342F
        totalLoanPaymentsInEuro = 10533.542F
        totalInterestInDollar = 972.34F
        totalInterestInEuro = 789.54F
        totalInsuranceInDollar = 180F
        totalInsuranceInEuro = 146.16F
    }

    private fun initViewModelInputs(defaultCurrency: Boolean, resetInputCurrency: Boolean = true) {
        if (resetInputCurrency) viewmodel.inputCurrency = dollar
        viewmodel.amount = if (defaultCurrency) amountInDollar else amountInEuro
        viewmodel.years = years
        viewmodel.months = months
        viewmodel.annualInterestRate = annualInterestRate
        viewmodel.annualInsuranceRate = annualInsuranceRate

        doCallRealMethod().`when`(loanRepository).calculateTerm(any(), any())
        doCallRealMethod().`when`(loanRepository).calculateMonthlyPayment(any(), any(), anyInt())
        doCallRealMethod().`when`(loanRepository).calculateMonthlyInsurance(any(), any(), anyInt())
        doCallRealMethod().`when`(loanRepository).calculateTotalMonthly(any(), any())
        doCallRealMethod().`when`(loanRepository).calculateTotalLoanPayments(any(), anyInt())
        doCallRealMethod().`when`(loanRepository).calculateTotalInterest(any(), any())
        doCallRealMethod().`when`(loanRepository).calculateTotalInsurance(any(), anyInt())
        doCallRealMethod().`when`(loanRepository).calculateTotalPayments(any(), any())

        doCallRealMethod().`when`(loanRepository).reformatDigitalField(anyString())
        doCallRealMethod().`when`(loanRepository).getDigitalValue(any())
    }

    private fun simulatePreviousResults() {
        viewmodel.monthlyPayment = 1F
        viewmodel.monthlyInsurance = 1F
        viewmodel.totalMonthly = 1F
        viewmodel.totalPayments = 1F
        viewmodel.totalInterest = 1F
        viewmodel.totalInsurance = 1F
    }

    private fun launchFieldValueChangeTest(field: String, fieldValue: String = "10", unit: String? = null) {
        reset(loanRepository)
        initViewModelInputs(defaultCurrency = true)

        currency = euro
        val value: String? = when (fieldValue) {
            "" -> null
            "10" -> "10"
            "10." -> "10.0"
            else -> fieldValue
        }
        val digitalValue: Float? = if (fieldValue.isEmpty()) null else 10.0F

        // Before function call
        assertEquals(amountInDollar, viewmodel.amount)
        assertEquals(dollar, viewmodel.inputCurrency)
        assertEquals(years, viewmodel.years)
        assertEquals(months, viewmodel.months)
        assertEquals(annualInterestRate, viewmodel.annualInterestRate)
        assertEquals(annualInsuranceRate, viewmodel.annualInsuranceRate)

        simulatePreviousResults()

        // Call function
        viewmodel.onFieldValueChange(field, unit, fieldValue, currency)

        // Assertions
        verify(loanRepository).reformatDigitalField(fieldValue)
        verify(loanRepository).getDigitalValue(value)
        when (field) {
            LoanField.AMOUNT.name -> {
                assertEquals(digitalValue?.toInt(), viewmodel.amount)
                assertEquals(currency, viewmodel.inputCurrency)
            }
            LoanField.TERM.name -> {
                when (unit) {
                    null -> {
                        assertEquals(years, viewmodel.years)
                        assertEquals(months, viewmodel.months)
                    }
                    LoanField.YEARS.name -> { assertEquals(digitalValue?.toInt(), viewmodel.years) }
                    LoanField.MONTHS.name -> { assertEquals(digitalValue?.toInt(), viewmodel.months) }
                }
            }
            LoanField.ANNUAL_INTEREST_RATE.name -> { assertEquals(digitalValue, viewmodel.annualInterestRate) }
            LoanField.ANNUAL_INSURANCE_RATE.name -> { assertEquals(digitalValue, viewmodel.annualInsuranceRate) }
        }

        // Verify clearResults()
        assertNull(viewmodel.monthlyPayment)
        assertNull(viewmodel.monthlyInsurance)
        assertNull(viewmodel.totalMonthly)
        assertNull(viewmodel.totalPayments)
        assertNull(viewmodel.totalInterest)
        assertNull(viewmodel.totalInsurance)
    }

    private fun launchClearButtonClickTest(field: String, unit: String? = null) {
        reset(loanRepository)
        initViewModelInputs(defaultCurrency = true)

        // Before function call
        assertNotNull(viewmodel.amount)
        assertNotNull(viewmodel.years)
        assertNotNull(viewmodel.months)
        assertNotNull(viewmodel.annualInterestRate)
        assertNotNull(viewmodel.annualInsuranceRate)

        simulatePreviousResults()

        // Call function
        viewmodel.onClearButtonClick(field, unit)

        // Assertions
        when (field) {
            LoanField.AMOUNT.name -> { assertNull(viewmodel.amount) }
            LoanField.TERM.name -> {
                when (unit) {
                    null -> {
                        assertNotNull(viewmodel.years)
                        assertNotNull(viewmodel.months)
                    }
                    LoanField.YEARS.name -> { assertNull(viewmodel.years) }
                    LoanField.MONTHS.name -> { assertNull(viewmodel.months) }
                }
            }
            LoanField.ANNUAL_INTEREST_RATE.name -> { assertNull(viewmodel.annualInterestRate) }
            LoanField.ANNUAL_INSURANCE_RATE.name -> { assertNull(viewmodel.annualInsuranceRate) }
        }

        // Verify clearResults()
        assertNull(viewmodel.monthlyPayment)
        assertNull(viewmodel.monthlyInsurance)
        assertNull(viewmodel.totalMonthly)
        assertNull(viewmodel.totalPayments)
        assertNull(viewmodel.totalInterest)
        assertNull(viewmodel.totalInsurance)
    }


    @Test
    fun testOnClickMenu() {
        //  Test with inputCurrency = "$" (default because loanDataRepository called for the first time or reset)
        //  and currency in $
        reset(loanRepository)   // Not mandatory for this first call
        initViewModelInputs(defaultCurrency = true)   // Not mandatory for this first call
        currency = dollar

        // Call function
        viewmodel.onClickMenu(navController, currency)

        // Assertions
        verify(loanRepository).calculateTerm(years, months)
        assertEquals(amountInDollar, viewmodel.amount)
        verify(loanRepository).calculateMonthlyPayment(amountInDollar, annualInterestRate, term)
        verify(loanRepository).calculateMonthlyInsurance(amountInDollar, annualInsuranceRate, term)
        verify(loanRepository).calculateTotalMonthly(monthlyPaymentInDollar, monthlyInsuranceInDollar)
        verify(loanRepository).calculateTotalLoanPayments(monthlyPaymentInDollar, term)
        verify(loanRepository).calculateTotalInterest(totalLoanPaymentsInDollar, amountInDollar)
        verify(loanRepository).calculateTotalInsurance(monthlyInsuranceInDollar, term)
        verify(loanRepository).calculateTotalPayments(totalLoanPaymentsInDollar, totalInsuranceInDollar)
        verify(loanRepository).reloadScreen(navController)


        // Test with inputCurrency = "$" (default because loanDataRepository reset)
        // and currency in €
        reset(loanRepository)
        initViewModelInputs(defaultCurrency = true)
        currency = euro

        // Call function
        viewmodel.onClickMenu(navController, currency)

        // Assertions
        verify(loanRepository).calculateTerm(years, months)
        assertEquals(amountInEuro, viewmodel.amount)
        verify(loanRepository).calculateMonthlyPayment(amountInEuro, annualInterestRate, term)
        verify(loanRepository).calculateMonthlyInsurance(amountInEuro, annualInsuranceRate, term)
        verify(loanRepository).calculateTotalMonthly(monthlyPaymentInEuro, monthlyInsuranceInEuro)
        verify(loanRepository).calculateTotalLoanPayments(monthlyPaymentInEuro, term)
        verify(loanRepository).calculateTotalInterest(totalLoanPaymentsInEuro, amountInEuro)
        verify(loanRepository).calculateTotalInsurance(monthlyInsuranceInEuro, term)
        verify(loanRepository).calculateTotalPayments(totalLoanPaymentsInEuro, totalInsuranceInEuro)
        verify(loanRepository).reloadScreen(navController)


        // Test with inputCurrency = "€" (loanDataRepository not reset after previous test)
        // and currency in $
        initViewModelInputs(defaultCurrency = false, resetInputCurrency = false)
        currency = dollar

        // Call function
        viewmodel.onClickMenu(navController, currency)

        // Assertions
        verify(loanRepository, times(2)).calculateTerm(years, months)
        assertEquals(amountInDollar, viewmodel.amount)
        verify(loanRepository).calculateMonthlyPayment(amountInDollar, annualInterestRate, term)
        verify(loanRepository).calculateMonthlyInsurance(amountInDollar, annualInsuranceRate, term)
        verify(loanRepository).calculateTotalMonthly(monthlyPaymentInDollar, monthlyInsuranceInDollar)
        verify(loanRepository).calculateTotalLoanPayments(monthlyPaymentInDollar, term)
        verify(loanRepository).calculateTotalInterest(totalLoanPaymentsInDollar, amountInDollar)
        verify(loanRepository).calculateTotalInsurance(monthlyInsuranceInDollar, term)
        verify(loanRepository).calculateTotalPayments(totalLoanPaymentsInDollar, totalInsuranceInDollar)
        verify(loanRepository, times(2)).reloadScreen(navController)
    }

    /**
     * Also testing clearResults()
     */
    @Test
    fun testOnFieldValueChange() {
        launchFieldValueChangeTest(field = LoanField.AMOUNT.name)
        launchFieldValueChangeTest(field = LoanField.TERM.name)
        launchFieldValueChangeTest(field = LoanField.TERM.name, unit = LoanField.YEARS.name)
        launchFieldValueChangeTest(field = LoanField.TERM.name, unit = LoanField.MONTHS.name)
        launchFieldValueChangeTest(field = LoanField.ANNUAL_INTEREST_RATE.name)
        launchFieldValueChangeTest(field = LoanField.ANNUAL_INSURANCE_RATE.name)
        launchFieldValueChangeTest(field = LoanField.ANNUAL_INSURANCE_RATE.name, fieldValue = "10.")
        launchFieldValueChangeTest(field = LoanField.ANNUAL_INSURANCE_RATE.name, fieldValue = "")
    }

    /**
     * Also testing clearResults()
     */
    @Test
    fun testOnClearButtonClick() {
        launchClearButtonClickTest(field = LoanField.AMOUNT.name)
        launchClearButtonClickTest(field = LoanField.TERM.name)
        launchClearButtonClickTest(field = LoanField.TERM.name, unit = LoanField.YEARS.name)
        launchClearButtonClickTest(field = LoanField.TERM.name, unit = LoanField.MONTHS.name)
        launchClearButtonClickTest(field = LoanField.ANNUAL_INTEREST_RATE.name)
        launchClearButtonClickTest(field = LoanField.ANNUAL_INSURANCE_RATE.name)
    }

    /**
     * Also testing clearAllFields(), clearInput() and clearResults()
     */
    @Test
    fun testOnClearAllButtonClick() {
        // Before function call
        assertNotNull(viewmodel.amount)
        assertNotNull(viewmodel.years)
        assertNotNull(viewmodel.months)
        assertNotNull(viewmodel.annualInterestRate)
        assertNotNull(viewmodel.annualInsuranceRate)

        simulatePreviousResults()

        // Call function
        viewmodel.onClearAllButtonClick(navController)

        // Assertions
        // Verify clearInput()
        assertNull(viewmodel.amount)
        assertNull(viewmodel.years)
        assertNull(viewmodel.months)
        assertNull(viewmodel.annualInterestRate)
        assertNull(viewmodel.annualInsuranceRate)
        // Verify clearResults()
        assertNull(viewmodel.monthlyPayment)
        assertNull(viewmodel.monthlyInsurance)
        assertNull(viewmodel.totalMonthly)
        assertNull(viewmodel.totalPayments)
        assertNull(viewmodel.totalInterest)
        assertNull(viewmodel.totalInsurance)

        verify(loanRepository).reloadScreen(navController)
    }
}