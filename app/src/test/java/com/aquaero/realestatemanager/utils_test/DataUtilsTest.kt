package com.aquaero.realestatemanager.utils_test

import com.aquaero.realestatemanager.DATE_PATTERN
import com.aquaero.realestatemanager.RATE_OF_DOLLAR_IN_EURO
import com.aquaero.realestatemanager.utils.areDigitsOnly
import com.aquaero.realestatemanager.utils.calculateMonthlyPaymentWithInterest
import com.aquaero.realestatemanager.utils.convertDateMillisToString
import com.aquaero.realestatemanager.utils.convertDateStringToMillis
import com.aquaero.realestatemanager.utils.convertDollarToEuro
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

class DataUtilsTest {

    @Test
    fun convertDollarToEuroWithSuccess() {
        val inputValue: Int = 100
        val expectedResult: Int = (inputValue * RATE_OF_DOLLAR_IN_EURO).roundToInt()

        assertEquals("Wrong value in Euro", expectedResult, convertDollarToEuro(inputValue))
    }

    @Test
    fun convertEuroToDollarWithSuccess() {
        val inputValue: Int = 100
        val expectedResult: Int = (inputValue / RATE_OF_DOLLAR_IN_EURO).roundToInt()

        assertEquals("Wrong value in Dollar", expectedResult, convertEuroToDollar(inputValue))
    }

    @Test
    fun convertDateStringToMillisWithSuccess() {
        val inputValue: String = "2024-09-15"
//        val expectedResult: Long = 1726358400000
        val expectedResult =
            LocalDate.parse(inputValue, DateTimeFormatter.ofPattern(DATE_PATTERN)).atStartOfDay()
                .toInstant(ZoneOffset.UTC).toEpochMilli()

        assertEquals("Wrong date millis", expectedResult, convertDateStringToMillis(inputValue))
    }

    @Test
    fun convertDateMillisToStringWithSuccess() {
        val inputValue: Long = 1726358400000
//        val expectedResult: String = "2024-09-15"
        val expectedResult: String =
            Instant.ofEpochMilli(inputValue).atZone(ZoneId.systemDefault()).toLocalDate()
                .format(DateTimeFormatter.ofPattern(DATE_PATTERN))

        assertEquals("Wrong date string", expectedResult, convertDateMillisToString(inputValue))
    }

    @Test
    fun calculateMonthlyPaymentWithInterestWithSuccess() {
        val amountInput: Int = 100000
        val rateInput: Float = 0.5F
        val termInMonthsInput: Int = 60
//        val expectedResult: Float = 1688.0553F
        val expectedResult: Float =
            (amountInput * rateInput / 100 / 12) / (1 - (1 + rateInput / 100 / 12).pow(-termInMonthsInput))

        assertEquals(
            "Wrong value",
            expectedResult,
            calculateMonthlyPaymentWithInterest(amountInput, rateInput, termInMonthsInput)
        )
    }

    @Test
    fun addEllipsisToTextWithSuccess() {
        val maxLengthInput: Int = 14
        val text13: String = "1234567890123"
        val text14: String = "12345678901234"
        val text15: String = "123456789012345"
        val truncatedText: String = "12345678901…"

        var maxLinesInput: Int = 1

        // Case 1a : Max lines = 1 and text length = 13 (< 14)
        var fullTextInput: String = text13
        var expectedResult: String = fullTextInput
        assertEquals(
            "Wrong text result for case 1a",
            expectedResult,
            textWithEllipsis(fullTextInput, maxLengthInput, maxLinesInput)
        )

        // Case 1b : Max lines = 1 and text length = 14
        fullTextInput = text14
        expectedResult = fullTextInput
        assertEquals(
            "Wrong text result for case 1b",
            expectedResult,
            textWithEllipsis(fullTextInput, maxLengthInput, maxLinesInput)
        )

        // Case 1c : Max lines = 1 and text length = 15 (> 14)
        fullTextInput = text15
        expectedResult = truncatedText
        assertEquals(
            "Wrong text result for case 1c",
            expectedResult,
            textWithEllipsis(fullTextInput, maxLengthInput, maxLinesInput)
        )

        maxLinesInput = 2

        // Case 2a : Max lines = 2 and text length = 13 (< 14)
        fullTextInput = text13
        expectedResult = fullTextInput
        assertEquals(
            "Wrong text result for case 2a",
            expectedResult,
            textWithEllipsis(fullTextInput, maxLengthInput, maxLinesInput)
        )

        // Case 2b : Max lines = 2 and text length = 14
        fullTextInput = text14
        expectedResult = fullTextInput
        assertEquals(
            "Wrong text result for case 2b",
            expectedResult,
            textWithEllipsis(fullTextInput, maxLengthInput, maxLinesInput)
        )

        // Case 2c : Max lines = 2 and text length = 15 (> 14)
        fullTextInput = text15
        expectedResult = fullTextInput
        assertEquals(
            "Wrong text result for case 2c",
            expectedResult,
            textWithEllipsis(fullTextInput, maxLengthInput, maxLinesInput)
        )
    }

    @Test
    fun getEllipsisWithSuccess() {
        val expectedResult: String = "…"
        assertEquals("Wrong Ellipsis result", expectedResult, ellipsis())
    }

    @Test
    fun checkIfThisNumberIsDecimal() {
        val expectedDecimalNumbers = listOf("123", "12.3", "12.34", "12.")
        val unexpectedDecimalNumbers =
            listOf(".123", "-12", "1-2", "12-", "x12", "1x2", "12x", " 12", "1 2", "12 ")

        expectedDecimalNumbers.forEach {
            assertTrue("Decimal check error with $it", isDecimal(it))
        }

        unexpectedDecimalNumbers.forEach {
            assertFalse("Decimal check error with $it", isDecimal(it))
        }
    }

    @Test
    fun testIfThisStringIsOnlyDigits() {
        val expectedDigitsStrings = listOf("123")
        val unexpectedDigitsStrings =
            listOf("12.3", "12.34", "12.", ".123", "-12", "1-2", "12-", "x12", "1x2", "12x", " 12", "1 2", "12 ")

        expectedDigitsStrings.forEach {
            assertTrue("Digit check error with $it", it.areDigitsOnly())
        }

        unexpectedDigitsStrings.forEach {
            assertFalse("Digit check error with $it", it.areDigitsOnly())
        }
    }

    @Test
    fun generateProvisionalIdWithSuccess() {
        runBlocking {
            val gen1: Long = generateProvisionalId()
            delay(1000)
            val gen2: Long = generateProvisionalId()

            assertTrue("Provisional id generation error", gen1 < 0)
            assertTrue("Provisional id generation error", gen2 < 0)
            assertTrue("Provisional id generation error", gen1 != gen2)
            assertTrue("Provisional id generation error", gen1 <= -10000 && gen1 >= -99999)
            assertTrue("Provisional id generation error", gen2 <= -10000 && gen2 >= -99999)
            assertEquals("Provisional id generation error", 6, gen1.toString().length)  // Minus sign plus 5 digits
            assertEquals("Provisional id generation error", 6, gen2.toString().length)  // Minus sign plus 5 digits
        }
    }

}