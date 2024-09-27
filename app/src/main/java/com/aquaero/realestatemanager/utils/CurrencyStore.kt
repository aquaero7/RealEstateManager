package com.aquaero.realestatemanager.utils

import android.content.Context
import androidx.compose.ui.text.intl.Locale
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.Region
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CurrencyStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("currency")
        private val CURRENCY_KEY = intPreferencesKey("currency_key")
    }

    private val defaultCurrency: Int =
        if (Locale.current.region == Region.FR.name) R.string.euro
        else R.string.dollar

    val getCurrency: Flow<Int> = context.dataStore.data.map { value ->
        value[CURRENCY_KEY] ?: defaultCurrency
    }

    suspend fun saveCurrency(currency: Int) {
        context.dataStore.edit { value ->
            value[CURRENCY_KEY] = currency
        }
    }

    /**
     * For testing only
     * Returns the repository private Int variable: defaultCurrency
     */
    @ForTestingOnly
    @Suppress("PropertyName")
    val forTestingOnly_getDefaultCurrency: Int = defaultCurrency

    /**
     * For testing only
     * Remove the preferences stored with the key: CURRENCY_KEY
     */
    @ForTestingOnly
    @Suppress("FunctionName")
    suspend fun forTestingOnly_clearCurrency() {
        context.dataStore.edit { preferences ->
            preferences.remove(CURRENCY_KEY)
        }
    }

}