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
import kotlinx.coroutines.runBlocking
import java.io.File

/**
 * DataStore-backed implementation of SettingsDataStore.
 * Provides suspend-based non-blocking read/write methods backed by Jetpack DataStore.
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

    // Java-friendly synchronous implementations that delegate to DataStore using runBlocking.
    // Calls are intended to be made off the main thread; callers should ensure not to block UI thread.
    override fun getSavedLocale(): String? = runBlocking { dataStore.data.first()[KEY_LANGUAGE] }

    override fun saveLocale(languageTag: String) {
        runBlocking { dataStore.edit { prefs -> prefs[KEY_LANGUAGE] = languageTag } }
    }
}




