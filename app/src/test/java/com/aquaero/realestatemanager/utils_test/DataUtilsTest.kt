package com.aquaero.realestatemanager.utils_test

import androidx.compose.ui.unit.Density
import com.aquaero.realestatemanager.DATE_PATTERN
import com.aquaero.realestatemanager.RATE_OF_DOLLAR_IN_EURO
import com.aquaero.realestatemanager.utils.areDigitsOnly
import com.aquaero.realestatemanager.utils.calculateMonthlyPaymentWithInterest
import com.aquaero.realestatemanager.utils.convertDateMillisToString
import com.aquaero.realestatemanager.utils.convertDateStringToMillis
import com.aquaero.realestatemanager.utils.convertDollarToEuro
import com.aquaero.realestatemanager.utils.convertDpToPxInt
import com.aquaero.realestatemanager.utils.convertEuroToDollar
import com.aquaero.realestatemanager.utils.ellipsis
import com.aquaero.realestatemanager.utils.generateProvisionalId
import com.aquaero.realestatemanager.utils.isDecimal
import com.aquaero.realestatemanager.utils.textWithEllipsis
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.reflect.KFunction

class DataUtilsTest {
    @Test
    fun convertDpToPxIntWithSuccess() {
        val densityValue = 2.0F
        val fontScaleValue = 1.0F
        val computedDensityValue = Density(density = densityValue, fontScale = fontScaleValue)

        val inputValueInt = 800
        val expectedResultInt = (inputValueInt * densityValue).toInt()

        assertEquals(expectedResultInt, convertDpToPxInt(inputValueInt, computedDensityValue))
    }

    @Test
    fun convertDollarToEuroWithSuccess() {
        val inputValue = 100
        val expectedResult: Int = (inputValue * RATE_OF_DOLLAR_IN_EURO).roundToInt()

        assertEquals(expectedResult, convertDollarToEuro(inputValue))
        assertEquals(null, convertDollarToEuro(null))
    }

    @Test
    fun convertEuroToDollarWithSuccess() {
        val inputValue = 100
        val expectedResult: Int = (inputValue / RATE_OF_DOLLAR_IN_EURO).roundToInt()

        assertEquals(expectedResult, convertEuroToDollar(inputValue))
        assertEquals(null, convertEuroToDollar(null))
    }

    @Test
    fun convertDateStringToMillisWithSuccess() {
        var inputValue = "2024-09-15"
//        val expectedResult: Long = 1726358400000
        var expectedResult =
            LocalDate.parse(inputValue, DateTimeFormatter.ofPattern(DATE_PATTERN))
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli()
        assertEquals(expectedResult, convertDateStringToMillis(inputValue))

        inputValue = ""
        expectedResult =
            LocalDate.parse(LocalDate.now().toString(), DateTimeFormatter.ofPattern(DATE_PATTERN))
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli()
        assertEquals(expectedResult, convertDateStringToMillis(inputValue))

        inputValue = "wrongDateFormat"
        expectedResult =
            LocalDate.parse(LocalDate.now().toString(), DateTimeFormatter.ofPattern(DATE_PATTERN))
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli()
        assertEquals(expectedResult, convertDateStringToMillis(inputValue))
    }

    @Test
    fun convertDateMillisToStringWithSuccess() {
        val inputValue = 1726358400000
//        val expectedResult: String = "2024-09-15"
        val expectedResult: String =
            Instant.ofEpochMilli(inputValue).atZone(ZoneId.systemDefault()).toLocalDate()
                .format(DateTimeFormatter.ofPattern(DATE_PATTERN))

        assertEquals(expectedResult, convertDateMillisToString(inputValue))
    }

    @Test
    fun calculateMonthlyPaymentWithInterestWithSuccess() {
        val function: KFunction<*> = ::calculateMonthlyPaymentWithInterest
        // Function arguments are amount/rate/termInMonths
        assertEquals(0F, function.call(0, 0F, 0))
        assertEquals(0F, function.call(0, 0.5F, 2))
        assertEquals(100000F, function.call(100000, 0F, 0))
        assertEquals(100000F, function.call(100000, 0.5F, 0))
        assertEquals(100000F, function.call(100000, 0.5F, 1))
        assertEquals(50000F, function.call(100000, 0F, 2))
//        val expectedResult: Float = 1688.0553F
        val expectedResult: Float = (100000 * 0.5F / 100 / 12) / (1 - (1 + 0.5F / 100 / 12).pow(-60))
        assertEquals(expectedResult, function.call(100000, 0.5F, 60))
    }

    @Test
    fun addEllipsisToTextWithSuccess() {
        val maxLengthInput = 14
        val text13 = "1234567890123"
        val text14 = "12345678901234"
        val text15 = "123456789012345"
        val truncatedText = "12345678901…"

        var maxLinesInput = 1

        // Case 1a : Max lines = 1 and text length = 13 (< 14)
        var fullTextInput: String = text13
        var expectedResult: String = fullTextInput
        assertEquals(expectedResult, textWithEllipsis(fullTextInput, maxLengthInput, maxLinesInput))

        // Case 1b : Max lines = 1 and text length = 14
        fullTextInput = text14
        expectedResult = fullTextInput
        assertEquals(expectedResult, textWithEllipsis(fullTextInput, maxLengthInput, maxLinesInput))

        // Case 1c : Max lines = 1 and text length = 15 (> 14)
        fullTextInput = text15
        expectedResult = truncatedText
        assertEquals(expectedResult, textWithEllipsis(fullTextInput, maxLengthInput, maxLinesInput))

        maxLinesInput = 2

        // Case 2a : Max lines = 2 and text length = 13 (< 14)
        fullTextInput = text13
        expectedResult = fullTextInput
        assertEquals(expectedResult, textWithEllipsis(fullTextInput, maxLengthInput, maxLinesInput))

        // Case 2b : Max lines = 2 and text length = 14
        fullTextInput = text14
        expectedResult = fullTextInput
        assertEquals(expectedResult, textWithEllipsis(fullTextInput, maxLengthInput, maxLinesInput))

        // Case 2c : Max lines = 2 and text length = 15 (> 14)
        fullTextInput = text15
        expectedResult = fullTextInput
        assertEquals(expectedResult, textWithEllipsis(fullTextInput, maxLengthInput, maxLinesInput))
    }

    @Test
    fun getEllipsisWithSuccess() {
        val expectedResult = "…"
        assertEquals(expectedResult, ellipsis())
    }

    @Test
    fun checkIfThisNumberIsDecimal() {
        val expectedDecimalNumbers = listOf("123", "12.3", "1.23", "12.34", "123.", ".123")
        val unexpectedDecimalNumbers =
            listOf(
                ".12.3", "1.2.3", "1.23.", ".123.",
                "-12", "1-2", "12-", "x12", "1x2", "12x", " 12", "1 2", "12 "
            )

        expectedDecimalNumbers.forEach { assertTrue(isDecimal(it)) }
        unexpectedDecimalNumbers.forEach { assertFalse(isDecimal(it)) }
    }

    @Test
    fun testIfThisStringIsOnlyDigits() {
        val expectedDigitsStrings = listOf("123")
        val unexpectedDigitsStrings =
            listOf(
                "12.3", "12.34", "12.", ".123",
                "-12", "1-2", "12-", "x12", "1x2", "12x", " 12", "1 2", "12 "
            )

        expectedDigitsStrings.forEach { assertTrue(it.areDigitsOnly()) }
        unexpectedDigitsStrings.forEach { assertFalse(it.areDigitsOnly()) }
    }

    @Test
    fun generateProvisionalIdWithSuccess() {
        runBlocking {
            val gen1: Long = generateProvisionalId()
            delay(1000)
            val gen2: Long = generateProvisionalId()

            val stringGen1 = gen1.toString()
            val stringGen2 = gen2.toString()

            // Verify if two IDs are different when generated in the same thread with a minimum delay of 1s
            assertTrue(gen1 != gen2)
            // Verify if the generated ID is negative and not more than 6 chars long (minus sign + 5 digits)
            assertTrue(gen1 < 0 && gen1 >= -99999)
            assertTrue(gen2 < 0 && gen2 >= -99999)
            assertTrue(stringGen1.length in 2..6)
            assertTrue(stringGen2.length in 2..6)
            // Verify if the first char of the generated ID is the minus sign
            assertEquals('-', stringGen1[0])
            assertEquals('-', stringGen2[0])
        }
    }

}