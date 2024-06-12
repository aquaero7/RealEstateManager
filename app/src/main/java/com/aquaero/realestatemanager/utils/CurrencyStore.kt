package com.aquaero.realestatemanager.utils

import android.content.Context
import androidx.compose.ui.text.intl.Locale
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.Region
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CurrencyStore(private val context: Context) {

    private val defaultCurrency =
        if (Locale.current.region == Region.FR.name) context.getString(R.string.euro)
        else context.getString(R.string.dollar)

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("currency")
        private val CURRENCY_KEY = stringPreferencesKey("currency_key")
    }

    val getCurrency: Flow<String> = context.dataStore.data.map { value ->
        value[CURRENCY_KEY] ?: defaultCurrency
    }

    suspend fun saveCurrency(currency: String) {
        context.dataStore.edit { value ->
            value[CURRENCY_KEY] = currency
        }
    }

    suspend fun clearCurrency() {
        context.dataStore.edit { value ->
            value.remove(CURRENCY_KEY)
        }
    }

}