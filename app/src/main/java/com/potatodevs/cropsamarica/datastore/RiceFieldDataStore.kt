package com.potatodevs.cropsamarica.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore("my_app_prefs")

class FieldDataStore(private val context: Context) {

    // Define the key
    private val SELECTED_FIELD_KEY = stringPreferencesKey("selected_field_id")

    // Expose the value as a Flow
    val selectedFieldFlow: Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[SELECTED_FIELD_KEY]
        }

    // Save the selected field
    suspend fun setSelectedField(fieldId: String) {
        context.dataStore.edit { prefs ->
            prefs[SELECTED_FIELD_KEY] = fieldId
        }
    }
}
