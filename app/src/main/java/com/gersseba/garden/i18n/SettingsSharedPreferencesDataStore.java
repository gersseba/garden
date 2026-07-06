package com.gersseba.garden.i18n;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Simple SettingsDataStore implementation backed by SharedPreferences.
 * Persists the selected app language under key "app.language".
 */
public class SettingsSharedPreferencesDataStore implements SettingsDataStore {
	private static final String PREFS_NAME = "app_settings";
	private static final String KEY_LANGUAGE = "app.language";

	private final SharedPreferences prefs;

	public SettingsSharedPreferencesDataStore(Context context) {
		this(context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE));
	}

	// Constructor for tests to inject a mock SharedPreferences
	public SettingsSharedPreferencesDataStore(SharedPreferences prefs) {
		this.prefs = prefs;
	}

	@Override
	public String getSavedLocale() {
		return prefs.getString(KEY_LANGUAGE, null);
	}

	@Override
	public void saveLocale(String languageTag) {
		// apply() is asynchronous; avoids blocking the calling thread
		prefs.edit().putString(KEY_LANGUAGE, languageTag).apply();
	}
}
