package com.aquaero.realestatemanager.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CurrencyStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("currency")
        private val CURRENCY_KEY = stringPreferencesKey("currency_key")
    }

    val getCurrency: Flow<String> = context.dataStore.data.map { value ->
        value[CURRENCY_KEY] ?: ""
    }

    suspend fun saveCurrency(currency: String) {
        context.dataStore.edit { value ->
            value[CURRENCY_KEY] = currency
        }
    }

}