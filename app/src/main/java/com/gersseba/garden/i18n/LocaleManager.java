package com.gersseba.garden.i18n;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service that manages the current locale for the app.
 * <p>
 * Public API is intentionally small: observe {@link #currentLocale()} and use
 * {@link #setLocale(Locale)} to change. Initial value is read from the provided
 * {@link SettingsDataStore}; if absent, falls back to {@link Locale#getDefault()}.
 */
public class LocaleManager {
    private final SettingsDataStore settings;
    private final MutableLiveData<Locale> currentLocale = new MutableLiveData<>();
    // keep a synchronous copy for tests and callers without Android Looper
    private volatile Locale currentLocaleValue;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // Define supported locales for the application.
    private final Set<Locale> supportedLocales = new HashSet<>(Arrays.asList(
            Locale.ENGLISH,
            Locale.GERMAN
    ));

    public LocaleManager(SettingsDataStore settingsDataStore) {
        this.settings = settingsDataStore;
        // load initial value asynchronously to avoid blocking callers
        executor.execute(() -> {
            String saved = settings.getSavedLocale();
            Locale locale = saved == null || saved.isEmpty() ? Locale.getDefault() : Locale.forLanguageTag(saved);
            currentLocaleValue = locale;
            currentLocale.postValue(locale);
        });
    }

    /**
     * LiveData exposing the current locale. Observe on main thread.
     */
    public LiveData<Locale> currentLocale() {
        return currentLocale;
    }

    /**
     * Sets a new locale and persists it.
     */
    public void setLocale(Locale locale) {
        if (locale == null) return;
        synchronized (this) {
            settings.saveLocale(locale.toLanguageTag());
            currentLocaleValue = locale;
            // use postValue so observers on main thread are updated in Android; tests read value via getCurrentLocale()
            currentLocale.postValue(locale);
        }
    }

    /**
     * Returns the current locale synchronously (may be null until loaded).
     */
    public Locale getCurrentLocale() {
        Locale v = currentLocaleValue;
        if (v != null) return v;
        return currentLocale.getValue();
    }

    /**
     * Returns whether the provided locale is supported by the app.
     */
    public boolean isLocaleSupported(Locale locale) {
        if (locale == null) return false;
        return supportedLocales.contains(locale);
    }
}
