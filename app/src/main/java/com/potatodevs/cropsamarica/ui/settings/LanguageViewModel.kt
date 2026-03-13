package com.potatodevs.cropsamarica.ui.settings

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potatodevs.cropsamarica.datastore.LanguageDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val languageDataStore: LanguageDataStore
) : ViewModel() {

    val language: StateFlow<String> = languageDataStore.languageFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "en"
        )

    fun changeLanguage(code: String) {
        viewModelScope.launch {
            languageDataStore.setLanguage(code)
            applyLocale(code)

        }
    }

    fun applyLocale(code: String) {
        val localeList = LocaleListCompat.forLanguageTags(code)
        AppCompatDelegate.setApplicationLocales(localeList)

        Log.d("LANG_DEBUG", "Applied locale: $code")
        Log.d("LANG_DEBUG", "AppCompat locales: ${AppCompatDelegate.getApplicationLocales().toLanguageTags()}")
        Log.d("LANG_DEBUG", "System Locale.getDefault(): ${Locale.getDefault().toLanguageTag()}")
    }
}