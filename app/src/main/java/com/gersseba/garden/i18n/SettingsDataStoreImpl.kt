package com.gersseba.garden.i18n

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.first
import java.io.File

/**
 * DataStore-backed implementation of SettingsDataStore.
 * Provides a small Java-friendly synchronous API by using runBlocking for reads/writes.
 */
class SettingsDataStoreImpl private constructor(private val dataStore: DataStore<Preferences>) : SettingsDataStore {

    companion object {
        private val KEY_LANGUAGE = stringPreferencesKey("app.language")

        // Create from Android Context
        @JvmStatic
        fun create(context: Context): SettingsDataStoreImpl {
            val ds = PreferenceDataStoreFactory.create(
                scope = CoroutineScope(Dispatchers.IO),
                produceFile = { context.preferencesDataStoreFile("app_settings.preferences_pb") }
            )
            return SettingsDataStoreImpl(ds)
        }

        // Create from a File (useful for tests)
        @JvmStatic
        fun create(file: File): SettingsDataStoreImpl {
            val ds = PreferenceDataStoreFactory.create(
                scope = CoroutineScope(Dispatchers.IO),
                produceFile = { file }
            )
            return SettingsDataStoreImpl(ds)
        }
    }

    // Non-blocking suspend implementations
    override suspend fun getSavedLocale(): String? = dataStore.data.first()[KEY_LANGUAGE]

    override suspend fun saveLocale(languageTag: String) {
        dataStore.edit { prefs -> prefs[KEY_LANGUAGE] = languageTag }
    }
}


