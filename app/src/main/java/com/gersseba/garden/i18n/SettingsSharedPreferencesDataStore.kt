package com.gersseba.garden.i18n

import android.content.Context
import android.content.SharedPreferences

/**
 * Simple SettingsDataStore implementation backed by SharedPreferences.
 * This is used to persist the selected app language under key "app.language".
 */
class SettingsSharedPreferencesDataStore : SettingsDataStore {
    companion object {
        private const val PREFS_NAME = "app_settings"
        private const val KEY_LANGUAGE = "app.language"
    }

    private val prefs: SharedPreferences

    constructor(context: Context) {
        prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Constructor for tests to inject a mock SharedPreferences
    constructor(prefs: SharedPreferences) {
        this.prefs = prefs
    }

    override suspend fun getSavedLocale(): String? = prefs.getString(KEY_LANGUAGE, null)

    override suspend fun saveLocale(languageTag: String) {
        prefs.edit().putString(KEY_LANGUAGE, languageTag).apply()
    }
}


