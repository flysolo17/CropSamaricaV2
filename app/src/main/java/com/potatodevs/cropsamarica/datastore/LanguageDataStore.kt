package com.potatodevs.cropsamarica.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale

private val Context.dataStore by preferencesDataStore("settings")

class LanguageDataStore(private val context: Context) {

    private val LANGUAGE_KEY = stringPreferencesKey("app_language")

    val languageFlow: Flow<String> =
        context.dataStore.data.map { prefs ->
            prefs[LANGUAGE_KEY] ?: "fil"
        }

    suspend fun setLanguage(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        context.createConfigurationContext(
            context.resources.configuration.apply {
                setLocale(locale)
            }
        )
        context.dataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = language
        }
    }
}

