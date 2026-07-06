package com.gersseba.garden.i18n

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * DataStore-backed implementation of SettingsDataStore.
 * Provides suspend-based non-blocking read/write methods backed by Jetpack DataStore.
 */
class SettingsDataStoreImpl private constructor(private val dataStore: DataStore<Preferences>) : SettingsDataStore {

    companion object {
        private val KEY_LANGUAGE = stringPreferencesKey("app.language")

        @Volatile
        private var INSTANCE: SettingsDataStoreImpl? = null

        /**
         * Returns a singleton instance of SettingsDataStoreImpl.
         */
        @JvmStatic
        fun getInstance(context: Context): SettingsDataStoreImpl {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SettingsDataStoreImpl(context.applicationContext.dataStore).also { INSTANCE = it }
            }
        }

        // Create from a File (useful for tests)
        @JvmStatic
        fun createForTest(file: File): SettingsDataStoreImpl {
            val ds = PreferenceDataStoreFactory.create(
                scope = CoroutineScope(Dispatchers.IO),
                produceFile = { file }
            )
            return SettingsDataStoreImpl(ds)
        }
    }

    // Java-friendly synchronous implementations that delegate to DataStore using runBlocking on IO dispatcher.
    // Calls are intended to be made off the main thread; callers should ensure not to block UI thread.
    override fun getSavedLocale(): String? = runBlocking(Dispatchers.IO) {
        try {
            dataStore.data.first()[KEY_LANGUAGE]
        } catch (e: Exception) {
            null
        }
    }

    override fun saveLocale(languageTag: String) {
        runBlocking(Dispatchers.IO) {
            dataStore.edit { prefs -> prefs[KEY_LANGUAGE] = languageTag }
        }
    }
}
