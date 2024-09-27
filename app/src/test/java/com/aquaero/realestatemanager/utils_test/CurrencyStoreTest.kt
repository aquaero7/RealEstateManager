package com.aquaero.realestatemanager.utils_test

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.utils.CurrencyStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Locale

//@RunWith(MockitoJUnitRunner::class)
@RunWith(RobolectricTestRunner::class)
@Config(manifest= Config.NONE)
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
            currencyStore(context).forTestingOnly_clearCurrency()
        }
    }


    @Test
    fun saveAndGetCurrencyWithSuccess() = runTest {
        // Euro
        currencyStore(context).saveCurrency(R.string.euro)
        assertEquals(R.string.euro, currencyStore(context).getCurrency.first())

        // Dollar
        currencyStore(context).saveCurrency(R.string.dollar)
        assertEquals(R.string.dollar, currencyStore(context).getCurrency.first())
    }

}