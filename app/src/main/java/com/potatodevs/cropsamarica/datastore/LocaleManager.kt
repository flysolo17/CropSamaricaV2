package com.potatodevs.cropsamarica.datastore

import kotlinx.coroutines.flow.Flow

interface LocaleManager {
    fun updateLocale(languageCode: String)
    fun getSavedLanguageCode(): Flow<String>

}
