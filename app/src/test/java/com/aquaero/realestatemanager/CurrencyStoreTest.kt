package com.aquaero.realestatemanager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.aquaero.realestatemanager.utils.CurrencyStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
class CurrencyStoreTest {

    // Use of a real application context given by Robolectric
    private val context: Context = ApplicationProvider.getApplicationContext()

    private fun currencyStore(context: Context) = CurrencyStore(context)


    @Before
    fun setup() {
        // Setup locale
        val locale = Locale("fr", "FR")
        Locale.setDefault(locale)
    }

    @After
    fun tearDown() {
        runTest {
            currencyStore(context).clearCurrency()
        }
    }


    @Test
    fun saveAndGetCurrencyWithSuccess() = runTest {
        // Euro
        currencyStore(context).saveCurrency("€")
        assertEquals("€", currencyStore(context).getCurrency.first())

        // Dollar
        currencyStore(context).saveCurrency("$")
        assertEquals("$", currencyStore(context).getCurrency.first())
    }

    @Test
    fun clearCurrencyWithSuccess() {
        runTest {
            // Save and check "dollar" as selected currency
            currencyStore(context).saveCurrency("$")
            assertEquals("$", currencyStore(context).getCurrency.first())

            // Clear the selected currency
            currencyStore(context).clearCurrency()

            // Check that the currency is the default currency (€) according to Locale set in test setup
            assertEquals("€", currencyStore(context).getCurrency.first())
        }
    }

    @Test
    fun getDefaultCurrencyWithSuccess() {
        runTest {
            // Clear the selected currency
            currencyStore(context).clearCurrency()

            // Verify the default currency for France
            var locale = Locale("fr", "FR")
            Locale.setDefault(locale)
            assertEquals("€", currencyStore(context).getCurrency.first())

            // Verify the default currency for USA
            locale = Locale("en", "US")
            Locale.setDefault(locale)
            assertEquals("$", currencyStore(context).getCurrency.first())
        }
    }

}