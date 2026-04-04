package com.potatodevs.cropsamarica.datastore

import android.content.Context
import android.content.res.Configuration
import androidx.core.content.edit
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Locale

class LocaleManagerImpl @Inject constructor(
    private val context: Context
) : LocaleManager {

    private val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private val languageFlow = MutableStateFlow(
        sharedPref.getString("language", "en") ?: "en"
    )

    override fun updateLocale(languageCode: String) {

        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        context.resources.updateConfiguration(
            config,
            context.resources.displayMetrics
        )

        sharedPref.edit { putString("language", languageCode) }

        languageFlow.value = languageCode
    }

    override fun getSavedLanguageCode(): Flow<String> = languageFlow
}