package com.gersseba.garden.i18n

/**
 * Minimal interface representing a persisted settings store for locale.
 * Implementations may use DataStore, SharedPreferences, or test fakes.
 *
 * Exposes suspend functions to avoid blocking callers.
 */
interface SettingsDataStore {
    /**
     * Returns the saved locale as a BCP-47 language tag or null if not set.
     */
    suspend fun getSavedLocale(): String?

    /**
     * Persists the locale as a BCP-47 language tag.
     */
    suspend fun saveLocale(languageTag: String)
}


