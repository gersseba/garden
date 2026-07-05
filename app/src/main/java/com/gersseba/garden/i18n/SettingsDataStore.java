package com.gersseba.garden.i18n;

/**
 * Minimal interface representing a persisted settings store for locale.
 * Implementations may use DataStore, SharedPreferences, or test fakes.
 */
public interface SettingsDataStore {
    /**
     * Returns the saved locale as a BCP-47 language tag or null if not set.
     */
    String getSavedLocale();

    /**
     * Persists the locale as a BCP-47 language tag.
     */
    void saveLocale(String languageTag);
}
