package com.gersseba.garden.i18n;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Locale management service.
 * Reads initial value from SettingsDataStore and exposes it via LiveData.
 */
public class LocaleManager {
    private static volatile LocaleManager INSTANCE;

    private static final ExecutorService DEFAULT_EXECUTOR = Executors.newSingleThreadExecutor();

    private final SettingsDataStore settings;
    private final ExecutorService executor;
    private final MutableLiveData<Locale> _currentLocale = new MutableLiveData<>();
    private volatile Locale currentLocaleValue;
    private final Set<Locale> supportedLocales;

    public LocaleManager(@NonNull SettingsDataStore settings) {
        this(settings, null, DEFAULT_EXECUTOR);
    }

    @VisibleForTesting
    public LocaleManager(@NonNull SettingsDataStore settings, @Nullable CountDownLatch initLatch, @NonNull ExecutorService executor) {
        this.settings = settings;
        this.executor = executor;
        Set<Locale> locales = new HashSet<>();
        locales.add(Locale.ENGLISH);
        locales.add(Locale.GERMAN);
        this.supportedLocales = Collections.unmodifiableSet(locales);

        executor.execute(() -> {
            try {
                String saved = settings.getSavedLocale();
                Locale locale;
                if (saved == null || saved.isEmpty()) {
                    locale = Locale.getDefault();
                } else {
                    locale = Locale.forLanguageTag(saved);
                }
                currentLocaleValue = locale;
                _currentLocale.postValue(locale);
            } finally {
                if (initLatch != null) {
                    initLatch.countDown();
                }
            }
        });
    }

    public static LocaleManager getInstance(@NonNull SettingsDataStore settings) {
        if (INSTANCE == null) {
            synchronized (LocaleManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocaleManager(settings, null, DEFAULT_EXECUTOR);
                }
            }
        }
        return INSTANCE;
    }

    /** LiveData exposing the current locale. Observe on main thread. */
    @NonNull
    public LiveData<Locale> currentLocale() {
        return _currentLocale;
    }

    /** Sets a new locale and persists it without blocking the caller. */
    public void setLocale(@Nullable Locale locale) {
        if (locale == null) return;
        executor.execute(() -> {
            settings.saveLocale(locale.toLanguageTag());
            currentLocaleValue = locale;
            _currentLocale.postValue(locale);
        });
    }

    /** Returns the current locale synchronously (may be null until loaded). */
    @Nullable
    public Locale getCurrentLocale() {
        return currentLocaleValue != null ? currentLocaleValue : _currentLocale.getValue();
    }

    /** Returns whether the provided locale is supported by the app. */
    public boolean isLocaleSupported(@Nullable Locale locale) {
        return locale != null && supportedLocales.contains(locale);
    }
}

