package com.gersseba.garden.i18n;

/**
 * Java-friendly settings store interface for the app language.
 * Implementations should avoid blocking the main thread for writes.
 */
public interface SettingsDataStore {
	/**
	 * Returns the saved locale as a BCP-47 language tag or null if not set.
	 * Implementations should return quickly (SharedPreferences.getString is acceptable).
	 */
	String getSavedLocale();

	/**
	 * Persists the locale as a BCP-47 language tag. Implementations should use
	 * an asynchronous apply() or off-main-thread write so callers are not blocked.
	 */
	void saveLocale(String languageTag);
}
