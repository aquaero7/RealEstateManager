package com.aquaero.realestatemanager.repository_test

import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.Loan
import com.aquaero.realestatemanager.repository.LoanRepository
import com.aquaero.realestatemanager.utils.calculateMonthlyPaymentWithInterest
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.argThat
import kotlin.math.pow

@RunWith(MockitoJUnitRunner::class)
//@RunWith(RobolectricTestRunner::class)
/**
 * Testing LoanRepository
 */
class LoanRepositoryTest {
    private lateinit var repository: LoanRepository

    @Before
    fun setup() {
        repository = LoanRepository()
    }

    @After
    fun teardown() {}

    private fun launchTestsForPaymentCalculation(
        amount: Int?,
        rate: Float?,
        term: Int,
        expectedResult: Float,
        counter: Int = 1,
    ) {
        // Function under test
        val result =
            repository.calculateMonthlyPayment(amount = amount, annualInterestRate = rate, term = term)

        // Verifications and assertions
        assertEquals(expectedResult, result)
        verify(exactly = counter) {
            calculateMonthlyPaymentWithInterest(
                amount = amount ?: 0,
                annualInterestRate = rate ?: 0F,
                termInMonths = term
            )
        }
    }


    @Test
    fun testCalculateTerm() {
        assertEquals(1, repository.calculateTerm(years = null, months = null))
        assertEquals(1, repository.calculateTerm(years = 0, months = 0))
        assertEquals(6, repository.calculateTerm(years = 0, months = 6))
        assertEquals(24, repository.calculateTerm(years = 2, months = 0))
        assertEquals(30, repository.calculateTerm(years = 2, months = 6))
    }

    @Test
    fun testCalculateMonthlyPayment() {
        // Using MockK to mock high level function in DataUtils
        mockkStatic("com.aquaero.realestatemanager.utils.DataUtilsKt")
        // every { calculateMonthlyPaymentWithInterest(any(), any(), any()) } answers { callOriginal() }

        launchTestsForPaymentCalculation(amount = null, rate = null, term = 0, expectedResult = 0F)
        launchTestsForPaymentCalculation(amount = null, rate = null, term = 1, expectedResult = 0F)
        launchTestsForPaymentCalculation(amount = null, rate = null, term = 2, expectedResult = 0F)
        launchTestsForPaymentCalculation(amount = null, rate = 0.5F, term = 2, expectedResult = 0F)
        launchTestsForPaymentCalculation(amount = 100000, rate = null, term = 2, expectedResult = 50000F)
        launchTestsForPaymentCalculation(
            amount = 100000, rate = 0F, term = 2, expectedResult = 50000F, counter = 2
        )
        launchTestsForPaymentCalculation(amount = 100000, rate = 0.5F, term = 0, expectedResult = 100000F)
        launchTestsForPaymentCalculation(amount = 100000, rate = 0.5F, term = 1, expectedResult = 100000F)
        val expectedResult: Float = (100000 * 0.5F / 100 / 12) / (1 - (1 + 0.5F / 100 / 12).pow(-60))
        launchTestsForPaymentCalculation(
            amount = 100000, rate = 0.5F, term = 60, expectedResult = expectedResult
        )

        // Cleaning MockK static mocks
        unmockkAll()
    }

    @Test
    fun testCalculateMonthlyInsurance() {
        var result =
            repository.calculateMonthlyInsurance(amount = null, annualInsuranceRate = null, term = 0)
        assertEquals(0F, result)

        result =
            repository.calculateMonthlyInsurance(amount = null, annualInsuranceRate = null, term = 1)
        assertEquals(0F, result)

        result =
            repository.calculateMonthlyInsurance(amount = null, annualInsuranceRate = null, term = 2)
        assertEquals(0F, result)

        result =
            repository.calculateMonthlyInsurance(amount = null, annualInsuranceRate = 12F, term = 2)
        assertEquals(0F, result)

        result =
            repository.calculateMonthlyInsurance(amount = 100000, annualInsuranceRate = null, term = 2)
        assertEquals(0F, result)

        result =
            repository.calculateMonthlyInsurance(amount = 100000, annualInsuranceRate = 0F, term = 2)
        assertEquals(0F, result)

        result =
            repository.calculateMonthlyInsurance(amount = 100000, annualInsuranceRate = 12F, term = 0)
        assertEquals(0F, result)

        result =
            repository.calculateMonthlyInsurance(amount = 100000, annualInsuranceRate = 12F, term = 1)
        assertEquals(0F, result)

        result =
            repository.calculateMonthlyInsurance(amount = 100000, annualInsuranceRate = 12F, term = 60)
        assertEquals(1000F, result)
    }

    @Test
    fun testCalculateTotalMonthly() {
        var result = repository.calculateTotalMonthly(monthlyPayment = null, monthlyInsurance = null)
        assertEquals(0F, result)

        result = repository.calculateTotalMonthly(monthlyPayment = null, monthlyInsurance = 1F)
        assertEquals(1F, result)

        result = repository.calculateTotalMonthly(monthlyPayment = 100F, monthlyInsurance = null)
        assertEquals(100F, result)

        result = repository.calculateTotalMonthly(monthlyPayment = 100F, monthlyInsurance = 1F)
        assertEquals(101F, result)
    }

    @Test
    fun testCalculateTotalLoanPayments() {
        var result = repository.calculateTotalLoanPayments(monthlyPayment = null, term = 0)
        assertEquals(0F, result)

        result = repository.calculateTotalLoanPayments(monthlyPayment = null, term = 2)
        assertEquals(0F, result)

        result = repository.calculateTotalLoanPayments(monthlyPayment = 100F, term = 0)
        assertEquals(0F, result)

        result = repository.calculateTotalLoanPayments(monthlyPayment = 100F, term = 2)
        assertEquals(200F, result)
    }

    @Test
    fun testCalculateTotalInterest() {
        var result = repository.calculateTotalInterest(totalLoanPayments = null, amount = null)
        assertEquals(0F, result)

        result = repository.calculateTotalInterest(totalLoanPayments = null, amount = 100000)
        assertEquals(-100000F, result)

        result = repository.calculateTotalInterest(totalLoanPayments = 150000F, amount = null)
        assertEquals(150000F, result)

        result = repository.calculateTotalInterest(totalLoanPayments = 150000F, amount = 100000)
        assertEquals(50000F, result)
    }

    @Test
    fun testCalculateTotalInsurance() {
        var result = repository.calculateTotalInsurance(monthlyInsurance = null, term = 0)
        assertEquals(0F, result)

        result = repository.calculateTotalInsurance(monthlyInsurance = null, term = 2)
        assertEquals(0F, result)

        result = repository.calculateTotalInsurance(monthlyInsurance = 10F, term = 0)
        assertEquals(0F, result)

        result = repository.calculateTotalInsurance(monthlyInsurance = 10F, term = 2)
        assertEquals(20F, result)
    }

    @Test
    fun testCalculateTotalPayments() {
        var result = repository.calculateTotalPayments(totalLoanPayments = null, totalInsurance = null)
        assertEquals(0F, result)

        result = repository.calculateTotalPayments(totalLoanPayments = null, totalInsurance = 1000F)
        assertEquals(1000F, result)

        result = repository.calculateTotalPayments(totalLoanPayments = 150000F, totalInsurance = null)
        assertEquals(150000F, result)

        result = repository.calculateTotalPayments(totalLoanPayments = 150000F, totalInsurance = 1000F)
        assertEquals(151000F, result)
    }

    @Test
    fun testReloadScreen() {
        val navController: NavHostController = mock(NavHostController::class.java)
        // Function under test
        repository.reloadScreen(navController = navController)
        // Verification
        // (NavHostController.navigate() should be called through NavHostController.navigateSingleTopTo())
        verify(navController).navigate(route = argThat { this == Loan.route }, builder = argThat { true })
    }

    @Test
    fun testReformatDigitalField() {
        assertEquals(null, repository.reformatDigitalField(""))
        assertEquals("77", repository.reformatDigitalField("77"))
        assertEquals("77.0", repository.reformatDigitalField("77."))
        assertEquals("0.77", repository.reformatDigitalField(".77"))
        assertEquals("7.7", repository.reformatDigitalField("7.7"))
        assertEquals(null, repository.reformatDigitalField("7x7"))
        assertEquals(null, repository.reformatDigitalField("7 7"))
        assertEquals(null, repository.reformatDigitalField("7-7"))
    }

    @Test
    fun testGetDigitalValue() {
        assertEquals(null, repository.getDigitalValue(null))
        assertEquals(123F, repository.getDigitalValue("123"))
        assertEquals(12.34F, repository.getDigitalValue("12.34"))
        assertEquals(0.123F, repository.getDigitalValue(".123"))
        assertEquals(123.0F, repository.getDigitalValue("123."))

        assertEquals(null, repository.getDigitalValue("1.2.3"))
        assertEquals(null, repository.getDigitalValue(".12.3"))
        assertEquals(null, repository.getDigitalValue("1.23."))
        assertEquals(null, repository.getDigitalValue(".123."))

        assertEquals(null, repository.getDigitalValue("12 34"))
        assertEquals(null, repository.getDigitalValue("12-34"))
        assertEquals(null, repository.getDigitalValue("12x34"))
    }

}