package com.gersseba.garden.i18n;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

/**
 * SharedPreferences-backed implementation of SettingsDataStore.
 */
public class SettingsDataStoreImpl implements SettingsDataStore {

    private static final String PREF_NAME = "settings";
    private static final String KEY_LANGUAGE = "app.language";

    private static volatile SettingsDataStoreImpl INSTANCE;
    private final SharedPreferences prefs;

    @VisibleForTesting
    SettingsDataStoreImpl(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    private SettingsDataStoreImpl(Context context) {
        this(context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE));
    }

    public static SettingsDataStoreImpl getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (SettingsDataStoreImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SettingsDataStoreImpl(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static SettingsDataStoreImpl createForTest(@NonNull Context context) {
        return new SettingsDataStoreImpl(context);
    }

    @Override
    @Nullable
    public String getSavedLocale() {
        return prefs.getString(KEY_LANGUAGE, null);
    }

    @Override
    public void saveLocale(@NonNull String languageTag) {
        prefs.edit().putString(KEY_LANGUAGE, languageTag).apply();
    }
}
